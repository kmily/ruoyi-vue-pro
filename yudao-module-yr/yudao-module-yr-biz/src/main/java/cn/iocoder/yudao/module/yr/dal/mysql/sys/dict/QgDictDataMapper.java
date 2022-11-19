package cn.iocoder.yudao.module.yr.dal.mysql.sys.dict;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataPageReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Mapper
public interface QgDictDataMapper extends BaseMapperX<QgDictDataDO> {

    default QgDictDataDO selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(new LambdaQueryWrapper<QgDictDataDO>().eq(QgDictDataDO::getDictType, dictType)
                .eq(QgDictDataDO::getValue, value));
    }

    default QgDictDataDO selectByTreeIdAndValue(Long treeId, String value) {
        return selectOne(new LambdaQueryWrapper<QgDictDataDO>().eq(QgDictDataDO::getTreeId, treeId)
                .eq(QgDictDataDO::getValue, value));
    }

    default QgDictDataDO selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(new LambdaQueryWrapper<QgDictDataDO>().eq(QgDictDataDO::getDictType, dictType)
                .eq(QgDictDataDO::getLabel, label));
    }

    default List<QgDictDataDO> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<QgDictDataDO>().eq(QgDictDataDO::getDictType, dictType)
                .in(QgDictDataDO::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(QgDictDataDO::getDictType, dictType);
    }

    default PageResult<QgDictDataDO> selectPage(QgDictDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QgDictDataDO>()
                .likeIfPresent(QgDictDataDO::getLabel, reqVO.getLabel())
                .likeIfPresent(QgDictDataDO::getDictType, reqVO.getDictType())
                .eqIfPresent(QgDictDataDO::getStatus, reqVO.getStatus())
                .orderByDesc(Arrays.asList(QgDictDataDO::getDictType, QgDictDataDO::getSort)));
    }

    default List<QgDictDataDO> selectList(QgDictDataExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<QgDictDataDO>().likeIfPresent(QgDictDataDO::getLabel, reqVO.getLabel())
                .likeIfPresent(QgDictDataDO::getDictType, reqVO.getDictType())
                .eqIfPresent(QgDictDataDO::getStatus, reqVO.getStatus()));
    }

    @Select("SELECT COUNT(*) FROM qg_sys_dict_data WHERE update_time > #{maxUpdateTime}")
    Long selectCountByUpdateTimeGt(LocalDateTime maxUpdateTime);

}
