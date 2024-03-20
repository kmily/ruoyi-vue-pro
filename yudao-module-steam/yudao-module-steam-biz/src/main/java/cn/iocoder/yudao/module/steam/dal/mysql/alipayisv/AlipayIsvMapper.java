package cn.iocoder.yudao.module.steam.dal.mysql.alipayisv;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo.*;

/**
 * 签约ISV用户 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface AlipayIsvMapper extends BaseMapperX<AlipayIsvDO> {

    default PageResult<AlipayIsvDO> selectPage(AlipayIsvPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AlipayIsvDO>()
                .betweenIfPresent(AlipayIsvDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AlipayIsvDO::getSystemUserId, reqVO.getSystemUserId())
                .eqIfPresent(AlipayIsvDO::getSystemUserType, reqVO.getSystemUserType())
                .eqIfPresent(AlipayIsvDO::getIsvBizId, reqVO.getIsvBizId())
                .eqIfPresent(AlipayIsvDO::getAppAuthToken, reqVO.getAppAuthToken())
                .eqIfPresent(AlipayIsvDO::getAppId, reqVO.getAppId())
                .eqIfPresent(AlipayIsvDO::getAuthAppId, reqVO.getAuthAppId())
                .eqIfPresent(AlipayIsvDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AlipayIsvDO::getMerchantPid, reqVO.getMerchantPid())
                .eqIfPresent(AlipayIsvDO::getSignStatus, reqVO.getSignStatus())
                .eqIfPresent(AlipayIsvDO::getSignStatusList, reqVO.getSignStatusList())
                .orderByDesc(AlipayIsvDO::getId));
    }

}