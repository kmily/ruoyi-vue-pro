package cn.iocoder.yudao.module.yr.service.sys.dict;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypePageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeUpdateReqVO;
import cn.iocoder.yudao.module.yr.convert.sys.dict.QgDictTypeConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictTypeDO;
import cn.iocoder.yudao.module.yr.dal.mysql.sys.dict.QgDictTypeMapper;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.yr.enums.ErrorCodeConstants.*;

/**
 * 字典类型 Service 实现类
 *
 * @author alex
 */
@Service
public class QgDictTypeServiceImpl implements QgDictTypeService {

    @Resource
    private QgDictDataService dictDataService;

    @Resource
    private QgDictTypeMapper qgDictTypeMapper;

    @Override
    public PageResult<QgDictTypeDO> getDictTypePage(QgDictTypePageReqVO reqVO) {
        return qgDictTypeMapper.selectPage(reqVO);
    }

    @Override
    public List<QgDictTypeDO> getDictTypeList(QgDictTypeExportReqVO reqVO) {
        return qgDictTypeMapper.selectList(reqVO);
    }

    @Override
    public QgDictTypeDO getDictType(Long id) {
        return qgDictTypeMapper.selectById(id);
    }

    @Override
    public QgDictTypeDO getDictType(String type) {
        return qgDictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(QgDictTypeCreateReqVO reqVO) {
        // 校验正确性
        checkCreateOrUpdate(null, reqVO.getName(), reqVO.getType());
        // 插入字典类型
        QgDictTypeDO dictType = QgDictTypeConvert.INSTANCE.convert(reqVO);
        qgDictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(QgDictTypeUpdateReqVO reqVO) {
        // 校验正确性
        checkCreateOrUpdate(reqVO.getId(), reqVO.getName(), null);
        // 更新字典类型
        QgDictTypeDO updateObj = QgDictTypeConvert.INSTANCE.convert(reqVO);
        qgDictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        QgDictTypeDO dictType = checkDictTypeExists(id);
        // 校验是否有字典数据
        if (dictDataService.countByDictType(dictType.getType()) > 0) {
            throw exception(QG_DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        qgDictTypeMapper.deleteById(id);
    }

    @Override
    public List<QgDictTypeDO> getDictTypeList() {
        return qgDictTypeMapper.selectList();
    }

    private void checkCreateOrUpdate(Long id, String name, String type) {
        // 校验自己存在
        checkDictTypeExists(id);
        // 校验字典类型的名字的唯一性
        checkDictTypeNameUnique(id, name);
        // 校验字典类型的类型的唯一性
        checkDictTypeUnique(id, type);
    }

    @VisibleForTesting
    public void checkDictTypeNameUnique(Long id, String name) {
        QgDictTypeDO dictType = qgDictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(QG_DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(QG_DICT_TYPE_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    public void checkDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        QgDictTypeDO dictType = qgDictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(QG_DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(QG_DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public QgDictTypeDO checkDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        QgDictTypeDO dictType = qgDictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(QG_DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
