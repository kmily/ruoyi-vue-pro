package cn.iocoder.yudao.module.yr.service.sys.dict;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.yr.api.dict.QgDictDataRespDTO;
import cn.iocoder.yudao.module.yr.common.vo.UpdateStatusVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataPageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataUpdateReqVO;
import cn.iocoder.yudao.module.yr.convert.sys.dict.QgDictDataConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictDataDO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictTypeDO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;
import cn.iocoder.yudao.module.yr.dal.mysql.sys.dict.QgDictDataMapper;

import cn.iocoder.yudao.module.yr.mq.producer.dict.QgDictDataProducer;
import cn.iocoder.yudao.module.yr.service.sys.dictTree.QgDictTreeService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.yr.enums.ErrorCodeConstants.*;

/**
 * 字典数据 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Slf4j
public class QgDictDataServiceImpl implements QgDictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<QgDictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(QgDictDataDO::getDictType)
            .thenComparingInt(QgDictDataDO::getSort);

    @Resource
    private QgDictTypeService qgDictTypeService;

    @Resource
    private QgDictTreeService qgDictTreeService;

    @Resource
    private QgDictDataMapper qgDictDataMapper;


    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 业务字典缓存
     * key：字典值ID {@link QgDictDataDO#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @SuppressWarnings("FieldCanBeLocal")
    private volatile Map<Long, String> qgdictDataCache;

    /**
     * 字典数据缓存，第二个 key 使用 value
     *
     * key1：字典类型 dictType
     * key2：字典值 id
     */
    private ImmutableTable<String, String, QgDictDataDO> idDictDataCache;

    /**
     * 缓存业务字典值的最大更新时间，用于后续的增量轮询，判断是否有更新
     */
    private volatile LocalDateTime maxUpdateTime;

    @Resource
    private QgDictDataProducer dictDataProducer;

    @Resource
    @Lazy // 注入自己，所以延迟加载
    private QgDictDataService self;

    @Override
    @PostConstruct
    @TenantIgnore // 初始化缓存，无需租户过滤
    public synchronized void initLocalCache() {
        // 获取业务字典值列表，如果有更新
        List<QgDictDataDO> dictDataList = loadDeptIfUpdate(maxUpdateTime);
        if (CollUtil.isEmpty(dictDataList)) {
            return;
        }

        // 构建缓存
        ImmutableMap.Builder<Long,String> builder = ImmutableMap.builder();
        dictDataList.forEach(qgDictDataDO -> {
            builder.put(qgDictDataDO.getId(), qgDictDataDO.getLabel());
        });
        // 设置缓存
        qgdictDataCache = builder.build();
        maxUpdateTime = CollectionUtils.getMaxValue(dictDataList, QgDictDataDO::getUpdateTime);
        log.info("[initLocalCache][初始化 QgDictData 数量为 {}]", dictDataList.size());
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        self.initLocalCache();
    }

    /**
     * 如果业务字典值发生变化，从数据库中获取最新的全量业务字典值。
     * 如果未发生变化，则返回空
     *
     * @param maxUpdateTime 当前业务字典值的最大更新时间
     * @return 业务字典值列表
     */
    protected List<QgDictDataDO> loadDeptIfUpdate(LocalDateTime maxUpdateTime) {
        // 第一步，判断是否要更新。
        if (maxUpdateTime == null) { // 如果更新时间为空，说明 DB 一定有新数据
            log.info("[loadQgDictDataIfUpdate][首次加载全量业务字典值]");
        } else { // 判断数据库中是否有更新的业务字典值
            if (qgDictDataMapper.selectCountByUpdateTimeGt(maxUpdateTime) == 0) {
                return null;
            }
            log.info("[loadQgDictDataIfUpdate][增量加载全量业务字典值]");
        }
        // 第二步，如果有更新，则从数据库加载所有部门
        return qgDictDataMapper.selectList();
    }




    @Override
    public List<QgDictDataDO> getDictDatas() {
        List<QgDictDataDO> list = qgDictDataMapper.selectList();
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<QgDictDataDO> getDictDataPage(QgDictDataPageReqVO reqVO) {
        return qgDictDataMapper.selectPage(reqVO);
    }

    @Override
    public List<QgDictDataDO> getDictDatas(QgDictDataExportReqVO reqVO) {
        List<QgDictDataDO> list = qgDictDataMapper.selectList(reqVO);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public QgDictDataDO getDictData(Long id) {
        return qgDictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(QgDictDataCreateReqVO reqVO) {
        // 校验正确性
        checkCreateOrUpdate(null, reqVO.getValue(), reqVO.getTreeId());

        // 插入字典类型
        QgDictDataDO dictData = QgDictDataConvert.INSTANCE.convert(reqVO);
        qgDictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(QgDictDataUpdateReqVO reqVO) {
        // 校验正确性
        checkCreateOrUpdate(reqVO.getId(), reqVO.getValue(), reqVO.getTreeId());

        // 更新字典类型
        QgDictDataDO updateObj = QgDictDataConvert.INSTANCE.convert(reqVO);
        qgDictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        checkDictDataExists(id);

        // 删除字典数据
        qgDictDataMapper.deleteById(id);
    }

    @Override
    public long countByDictType(String dictType) {
        return qgDictDataMapper.selectCountByDictType(dictType);
    }


    private void checkCreateOrUpdate(Long id, String value, Long treeId) {
        // 校验自己存在
        checkDictDataExists(id);
        // 校验字典类型有效
        checkTreeIdValid(treeId);
        // 校验字典数据的值的唯一性
        checkDictDataValueUnique(id, treeId, value);
    }

    @VisibleForTesting
    public void checkDictDataValueUnique(Long id, String dictType, String value) {
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(QG_DICT_DATA_VALUE_DUPLICATE);
        }
        QgDictDataDO dictData = qgDictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        if (!dictData.getId().equals(id)) {
            throw exception(QG_DICT_DATA_VALUE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public void checkDictDataValueUnique(Long id, Long treeId, String value) {
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            return;
        }
        QgDictDataDO dictData = qgDictDataMapper.selectByTreeIdAndValue(treeId, value);
        if (dictData == null) {
            return;
        }
        if (!dictData.getId().equals(id)) {
            throw exception(QG_DICT_DATA_VALUE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public void checkDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        QgDictDataDO dictData = qgDictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(QG_DICT_DATA_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    public void checkDictTypeValid(String type) {
        QgDictTypeDO dictType = qgDictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(QG_DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(QG_DICT_TYPE_NOT_ENABLE);
        }
    }

    @VisibleForTesting
    public void checkTreeIdValid(Long treeId) {
        if (treeId == null) {
            throw exception(QgDict_TREE_NOT_EXISTS);
        }
        QgDictTreeDO qgDictTree = qgDictTreeService.getQgDictTree(treeId);
        if (qgDictTree==null) {
            throw exception(QgDict_TREE_NOT_EXISTS);
        }

    }

    @Override
    public void validDictDatas(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, QgDictDataDO> dictDataMap = CollectionUtils.convertMap(
                qgDictDataMapper.selectByDictTypeAndValues(dictType, values), QgDictDataDO::getValue);
        // 校验
        values.forEach(value -> {
            QgDictDataDO dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(QG_DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(QG_DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public QgDictDataDO getDictData(String dictType, String value) {
        return qgDictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public QgDictDataDO parseDictData(String dictType, String label) {
        return qgDictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }
    @Override
    public List<QgDictDataRespDTO> listDictDatasFromCache(String type) {
        return QgDictDataConvert.INSTANCE.convertList03(idDictDataCache.row(type).values());
    }
    @Override
    public void updateStatus(UpdateStatusVO updateStatusVO) {
        // 校验存在
        this.checkDictDataExists(updateStatusVO.getId());
        // 更新
        QgDictDataDO updateObj = QgDictDataConvert.INSTANCE.convert(updateStatusVO);
        qgDictDataMapper.updateById(updateObj);
    }
}
