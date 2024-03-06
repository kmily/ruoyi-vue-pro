package cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.*;

/**
 * 用户查询明细 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface YouyouDetailsMapper extends BaseMapperX<YouyouDetailsDO> {

    default PageResult<YouyouDetailsDO> selectPage(YouyouDetailsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<YouyouDetailsDO>()
                .eqIfPresent(YouyouDetailsDO::getAppkey, reqVO.getAppkey())
                .eqIfPresent(YouyouDetailsDO::getTimestamp, reqVO.getTimestamp())
                .eqIfPresent(YouyouDetailsDO::getSign, reqVO.getSign())
                .eqIfPresent(YouyouDetailsDO::getDataType, reqVO.getDataType())
                .betweenIfPresent(YouyouDetailsDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(YouyouDetailsDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(YouyouDetailsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(YouyouDetailsDO::getApplyCode, reqVO.getApplyCode())
                .eqIfPresent(YouyouDetailsDO::getData, reqVO.getData())
                .orderByDesc(YouyouDetailsDO::getId));
    }

}