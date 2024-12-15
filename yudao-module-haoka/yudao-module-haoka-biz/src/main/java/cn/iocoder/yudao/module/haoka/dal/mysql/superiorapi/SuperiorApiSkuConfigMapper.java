package cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;

/**
 * 上游API接口SKU要求配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SuperiorApiSkuConfigMapper extends BaseMapperX<SuperiorApiSkuConfigDO> {

    default PageResult<SuperiorApiSkuConfigDO> selectPage(SuperiorApiSkuConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorApiSkuConfigDO>()
                .eqIfPresent(SuperiorApiSkuConfigDO::getHaokaSuperiorApiId, reqVO.getHaokaSuperiorApiId())
                .likeIfPresent(SuperiorApiSkuConfigDO::getCode, reqVO.getCode())
                .likeIfPresent(SuperiorApiSkuConfigDO::getName, reqVO.getName())
                .eqIfPresent(SuperiorApiSkuConfigDO::getRequired, reqVO.getRequired())
                .eqIfPresent(SuperiorApiSkuConfigDO::getRemarks, reqVO.getRemarks())
                .eqIfPresent(SuperiorApiSkuConfigDO::getInputType, reqVO.getInputType())
                .eqIfPresent(SuperiorApiSkuConfigDO::getInputSelectValues, reqVO.getInputSelectValues())
                .eqIfPresent(SuperiorApiSkuConfigDO::getDeptId, reqVO.getDeptId())
                .betweenIfPresent(SuperiorApiSkuConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SuperiorApiSkuConfigDO::getId));
    }

    default void deleteByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        delete(new LambdaQueryWrapperX<SuperiorApiSkuConfigDO>()
                .eqIfPresent(SuperiorApiSkuConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId)
                .orderByDesc(SuperiorApiSkuConfigDO::getId));
    }

    default PageResult<SuperiorApiSkuConfigDO> selectPage(PageParam pageReqVO, Long haokaSuperiorApiId) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<SuperiorApiSkuConfigDO>()
                .eqIfPresent(SuperiorApiSkuConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId)
                .orderByDesc(SuperiorApiSkuConfigDO::getId));
    }
}
