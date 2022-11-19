package cn.iocoder.yudao.module.yr.service.sys.dictTree;

import cn.hutool.core.collection.CollUtil;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.*;
import cn.iocoder.yudao.module.yr.convert.sys.dictTree.QgDictTreeConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;
import cn.iocoder.yudao.module.yr.dal.mysql.sys.dictTree.QgDictTreeMapper;
import cn.iocoder.yudao.module.yr.mq.producer.dictTree.QgDictTreeProducer;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.yr.enums.ErrorCodeConstants.QgDict_TREE_NOT_EXISTS;
import static cn.iocoder.yudao.module.yr.enums.QgDictTreeConstants.*;


/**
 * 业务字典分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
@Validated
public class QgDictTreeServiceImpl implements QgDictTreeService {
    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 部门缓存
     * key：部门编号 {@link QgDictTreeDO#getId()}
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @SuppressWarnings("FieldCanBeLocal")
    private volatile Map<Long, QgDictTreeDO> QgDictTreeCache;
    /**
     * 父部门缓存
     * key：部门编号 {@link QgDictTreeDO#getParentId()}
     * value: 直接子部门列表
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Multimap<Long, QgDictTreeDO> parentQgDictTreeCache;
    /**
     * 缓存部门的最大更新时间，用于后续的增量轮询，判断是否有更新
     */
    private volatile LocalDateTime maxUpdateTime;


    @Resource
    private QgDictTreeMapper qgDictTreeMapper;

    @Resource
    private DictDataApi dictDataApi;

    @Resource
    private QgDictTreeProducer qgDictTreeProducer;

    @Resource
    @Lazy // 注入自己，所以延迟加载
    private QgDictTreeService self;

    @Override
    @PostConstruct
    @TenantIgnore // 初始化缓存，无需租户过滤
    public synchronized void initLocalCache() {
        // 获取部门列表，如果有更新
        List<QgDictTreeDO> QgDictTreeList = loadQgDictTreeIfUpdate(maxUpdateTime);
        if (CollUtil.isEmpty(QgDictTreeList)) {
            return;
        }

        // 构建缓存
        ImmutableMap.Builder<Long, QgDictTreeDO> builder = ImmutableMap.builder();
        ImmutableMultimap.Builder<Long, QgDictTreeDO> parentBuilder = ImmutableMultimap.builder();
        QgDictTreeList.forEach(QgDictTreeDO -> {
            builder.put(QgDictTreeDO.getId(), QgDictTreeDO);
            parentBuilder.put(QgDictTreeDO.getParentId(), QgDictTreeDO);
        });
        // 设置缓存
        QgDictTreeCache = builder.build();
        parentQgDictTreeCache = parentBuilder.build();
        maxUpdateTime = CollectionUtils.getMaxValue(QgDictTreeList, QgDictTreeDO::getUpdateTime);
        log.info("[initLocalCache][初始化 业务字典分类 数量为 {}]", QgDictTreeList.size());
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        self.initLocalCache();
    }

    /**
     * 如果部门发生变化，从数据库中获取最新的全量部门。
     * 如果未发生变化，则返回空
     *
     * @param maxUpdateTime 当前部门的最大更新时间
     * @return 部门列表
     */
    protected List<QgDictTreeDO> loadQgDictTreeIfUpdate(LocalDateTime maxUpdateTime) {
        // 第一步，判断是否要更新。
        if (maxUpdateTime == null) { // 如果更新时间为空，说明 DB 一定有新数据
            log.info("[loadQgDictTreeIfUpdate][首次加载全量业务字典分类]");
        } else { // 判断数据库中是否有更新的业务字典分类
            if (qgDictTreeMapper.selectExistsByUpdateTimeAfter(maxUpdateTime) == null) {
                return null;
            }
            log.info("[loadQgDictTreeIfUpdate][增量加载全量业务字典分类]");
        }
        // 第二步，如果有更新，则从数据库加载所有业务字典分类
        return qgDictTreeMapper.selectList();
    }

    @Override
    public Long createQgDictTree(QgDictTreeCreateReqVO createReqVO) {

        // 插入
        QgDictTreeDO QgDictTree = QgDictTreeConvert.INSTANCE.convert(createReqVO);
//        QgDictTree.setLevel(createReqVO.getLevel()+TREE_STEP_LEVEL);
        // 插入
        qgDictTreeMapper.insert(QgDictTree);
        // 发送刷新消息
        qgDictTreeProducer.sendQgDictTreeRefreshMessage();
        // 返回
        return QgDictTree.getId();
    }

    @Override
    public void updateQgDictTree(QgDictTreeUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateQgDictTreeExists(updateReqVO.getId());
        // 更新
        QgDictTreeDO updateObj = QgDictTreeConvert.INSTANCE.convert(updateReqVO);
        qgDictTreeMapper.updateById(updateObj);
        // 发送刷新消息
        qgDictTreeProducer.sendQgDictTreeRefreshMessage();
    }

    @Override
    public void deleteQgDictTree(Long id) {
        // 校验存在
        this.validateQgDictTreeExists(id);
        // 删除
        qgDictTreeMapper.deleteById(id);
        // 发送刷新消息
        qgDictTreeProducer.sendQgDictTreeRefreshMessage();
    }

    private void validateQgDictTreeExists(Long id) {
        if (qgDictTreeMapper.selectById(id) == null) {
            throw exception(QgDict_TREE_NOT_EXISTS);
        }
    }

    @Override
    public QgDictTreeDO getQgDictTree(Long id) {
        return qgDictTreeMapper.selectById(id);
    }

    @Override
    public List<QgDictTreeDO> getQgDictTreeList(Collection<Long> ids) {
        return qgDictTreeMapper.selectBatchIds(ids);
    }

    @Override
    public List<QgDictTreeDO> getByParentIdFromCache(Long parentId, boolean recursive) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        List<QgDictTreeDO> result = new ArrayList<>(); // TODO 芋艿：待优化，新增缓存，避免每次遍历的计算
        // 递归，简单粗暴
        this.getByParentIdFromCache(result, parentId,
                recursive ? Integer.MAX_VALUE : 1, // 如果递归获取，则无限；否则，只递归 1 次
                parentQgDictTreeCache);
        return result;
    }

    @Override
    public List<QgDictTreeDO> getQgDictTreeListByType(Long parentId) {
        if(parentId==0l){
            List<QgDictTreeDO> list =qgDictTreeMapper.selectListByType(null,parentId);
            if(!list.isEmpty()){
                parentId=list.get(0).getId();
            }
        }
        List<QgDictTreeDO> result =qgDictTreeMapper.selectListByType(null,parentId);
        return result;
    }

    /**
     * 递归获取所有的子部门，添加到 result 结果
     *
     * @param result               结果
     * @param parentId             父编号
     * @param recursiveCount       递归次数
     * @param parentQgDictTreeMap 父部门 Map，使用缓存，避免变化
     */
    private void getByParentIdFromCache(List<QgDictTreeDO> result, Long parentId, int recursiveCount,
                                        Multimap<Long, QgDictTreeDO> parentQgDictTreeMap) {
        // 递归次数为 0，结束！
        if (recursiveCount == 0) {
            return;
        }
        // 获得子部门
        Collection<QgDictTreeDO> depts = parentQgDictTreeMap.get(parentId);
        if (CollUtil.isEmpty(depts)) {
            return;
        }
        result.addAll(depts);
        // 继续递归
        depts.forEach(dept -> getByParentIdFromCache(result, dept.getId(),
                recursiveCount - 1, parentQgDictTreeMap));
    }

    @Override
    public PageResult<QgDictTreeDO> getQgDictTreePage(QgDictTreePageReqVO pageReqVO) {
        return qgDictTreeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<QgDictTreeDO> getQgDictTreeList(QgDictTreeExportReqVO exportReqVO) {
        return qgDictTreeMapper.selectList(exportReqVO);
    }

    @Override
    public void initTop() {
            //如果没有业务字典分类数据，创建一个顶级的根节点
            QgDictTreeDO createDO = new QgDictTreeDO();
            createDO.setLevel(TREE_STEP_LEVEL);
            createDO.setParentId(TREE_TOP_PARENTID);
            createDO.setName(TREE_TOP_NAME);
            createDO.setIsRead(IS_READ_TRUE);
//            createDO.setType(0);
            qgDictTreeMapper.insert(createDO);

    }

    @Override
    public List<QgDictTreeDO> getListTop(QgDictTreeKindTopVO ReqVO) {
        List<QgDictTreeDO> list = qgDictTreeMapper.selectListTop(ReqVO);
        if(list.isEmpty()){
            initTop();
            list = qgDictTreeMapper.selectListTop(ReqVO);
        }
        return list;
    }

    /**
     * 获得业务字典分类条件：查询指定节点的业务字典分类子编号们，包括自身
     *
     * @param QgDictTreeId 部门编号
     * @return 业务字典分类编号集合
     */
    @Override
    public Set<Long> getCondition(Long QgDictTreeId) {
        if (QgDictTreeId == null) {
            return Collections.emptySet();
        }
        Set<Long> QgDictTreeIds = CollectionUtils.convertSet(self.getByParentIdFromCache(
                QgDictTreeId, true), QgDictTreeDO::getId);
        QgDictTreeIds.add(QgDictTreeId); // 包括自身
        return QgDictTreeIds;
    }

    @Override
    public List<QgDictTreeDO> getSimpleQgDictTrees(Collection<Long> ids) {
        return qgDictTreeMapper.selectBatchIds(ids);
    }

    @Override
    public String getTreeNamesFromCache(Collection<Long> ids) {
        List<String> names=new ArrayList<>();
        ids.stream().forEach(t->{
            QgDictTreeDO treeDO = QgDictTreeCache.get(t);
            if(treeDO!=null){
                names.add(treeDO.getName());
            }

        });
        return names.stream().collect(Collectors.joining(","));
    }

    @Override
    public String getSignTreeNamesFromCache(Long id) {

        QgDictTreeDO treeDO = QgDictTreeCache.get(id);
        if(treeDO!=null){
            return treeDO.getName();
        }
        return "";
    }
}
