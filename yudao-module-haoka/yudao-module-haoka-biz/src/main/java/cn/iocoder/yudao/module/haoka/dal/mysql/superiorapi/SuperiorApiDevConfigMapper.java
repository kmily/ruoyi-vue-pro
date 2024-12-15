package cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;

/**
 * 上游API接口开发配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SuperiorApiDevConfigMapper extends BaseMapperX<SuperiorApiDevConfigDO> {

    default PageResult<SuperiorApiDevConfigDO> selectPage(SuperiorApiDevConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorApiDevConfigDO>()
                .eqIfPresent(SuperiorApiDevConfigDO::getHaokaSuperiorApiId, reqVO.getHaokaSuperiorApiId())
                .eqIfPresent(SuperiorApiDevConfigDO::getCode, reqVO.getCode())
                .likeIfPresent(SuperiorApiDevConfigDO::getName, reqVO.getName())
                .eqIfPresent(SuperiorApiDevConfigDO::getValue, reqVO.getValue())
                .eqIfPresent(SuperiorApiDevConfigDO::getRemarks, reqVO.getRemarks())
                .eqIfPresent(SuperiorApiDevConfigDO::getInputType, reqVO.getInputType())
                .eqIfPresent(SuperiorApiDevConfigDO::getInputSelectValues, reqVO.getInputSelectValues())
                .betweenIfPresent(SuperiorApiDevConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SuperiorApiDevConfigDO::getId));
    }

    default void deleteByHaokaSuperiorApiId(Long haokaSuperiorApiId) {
        delete(new LambdaQueryWrapperX<SuperiorApiDevConfigDO>()
                .eqIfPresent(SuperiorApiDevConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId));
    }

    default PageResult<SuperiorApiDevConfigDO> selectPage(PageParam pageReqVO, Long haokaSuperiorApiId) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<SuperiorApiDevConfigDO>()
                .eqIfPresent(SuperiorApiDevConfigDO::getHaokaSuperiorApiId, haokaSuperiorApiId));
    }
}
