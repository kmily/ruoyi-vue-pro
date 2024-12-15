package cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;

/**
 * 上游API接口 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SuperiorApiMapper extends BaseMapperX<SuperiorApiDO> {

    default PageResult<SuperiorApiDO> selectPage(SuperiorApiPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SuperiorApiDO>()
                .likeIfPresent(SuperiorApiDO::getName, reqVO.getName())
                .eqIfPresent(SuperiorApiDO::getEnableSelectNum, reqVO.getEnableSelectNum())
                .eqIfPresent(SuperiorApiDO::getAbnormalOrderHandleMethod, reqVO.getAbnormalOrderHandleMethod())
                .eqIfPresent(SuperiorApiDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(SuperiorApiDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(SuperiorApiDO::getIsDevConfined, reqVO.getIsDevConfined())
                .eqIfPresent(SuperiorApiDO::getIsSkuConfined, reqVO.getIsSkuConfined())
                .eqIfPresent(SuperiorApiDO::getDeptId, reqVO.getDeptId())
                .betweenIfPresent(SuperiorApiDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SuperiorApiDO::getId));
    }

}