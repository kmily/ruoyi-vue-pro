package cn.iocoder.yudao.module.steam.dal.mysql.binduser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;

/**
 *  steam用户绑定 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BindUserMapper extends BaseMapperX<BindUserDO> {

    default PageResult<BindUserDO> selectPage(BindUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BindUserDO>()
                .likeIfPresent(BindUserDO::getSteamName, reqVO.getSteamName())
                .eqIfPresent(BindUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(BindUserDO::getSteamId, reqVO.getSteamId())
                .eqIfPresent(BindUserDO::getTradeUrl, reqVO.getTradeUrl())
                .eqIfPresent(BindUserDO::getApiKey, reqVO.getApiKey())
                .eqIfPresent(BindUserDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BindUserDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BindUserDO::getMaFile, reqVO.getMaFile())
                .orderByDesc(BindUserDO::getId));
    }

}