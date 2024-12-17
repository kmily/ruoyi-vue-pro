package cn.iocoder.yudao.module.haoka.dal.mysql.superiorproductconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo.*;

/**
 * 产品对接上游配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SuperiorProductConfigMapper extends BaseMapperX<SuperiorProductConfigDO> {

    default PageResult<SuperiorProductConfigDO> selectPage(SuperiorProductConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorProductConfigDO>()
                .eqIfPresent(SuperiorProductConfigDO::getHaokaSuperiorApiId, reqVO.getHaokaSuperiorApiId())
                .eqIfPresent(SuperiorProductConfigDO::getHaokaProductId, reqVO.getHaokaProductId())
                .likeIfPresent(SuperiorProductConfigDO::getSuperiorCode, reqVO.getSuperiorCode())
                .eqIfPresent(SuperiorProductConfigDO::getIsConfined, reqVO.getIsConfined())
                .eqIfPresent(SuperiorProductConfigDO::getConfig, reqVO.getConfig())
                .eqIfPresent(SuperiorProductConfigDO::getRemarks, reqVO.getRemarks())
                .betweenIfPresent(SuperiorProductConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SuperiorProductConfigDO::getId));
    }

    default PageResult<SuperiorProductConfigDO> selectPageByHaokaSuperiorApiId(PageParam reqVO, Long haokaSuperiorApiId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorProductConfigDO>()
                .eq(SuperiorProductConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId)
                .orderByDesc(SuperiorProductConfigDO::getId));
    }

    default void deleteByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        delete(SuperiorProductConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId);
    }


    default PageResult<SuperiorProductConfigDO> selectPageByHaokaProductId(PageParam reqVO, Long haokaProductId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorProductConfigDO>()
                .eq(SuperiorProductConfigDO::getHaokaProductId, haokaProductId)
                .orderByDesc(SuperiorProductConfigDO::getId));
    }

    default void deleteByHaokaProductId(Long haokaProductId) {
        delete(SuperiorProductConfigDO::getHaokaProductId, haokaProductId);
    }
}
