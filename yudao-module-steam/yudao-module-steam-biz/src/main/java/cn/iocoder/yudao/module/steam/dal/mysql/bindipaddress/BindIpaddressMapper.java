package cn.iocoder.yudao.module.steam.dal.mysql.bindipaddress;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo.*;

/**
 * 绑定用户IP地址池 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface BindIpaddressMapper extends BaseMapperX<BindIpaddressDO> {

    default PageResult<BindIpaddressDO> selectPage(BindIpaddressPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BindIpaddressDO>()
                .eqIfPresent(BindIpaddressDO::getPort, reqVO.getPort())
                .betweenIfPresent(BindIpaddressDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BindIpaddressDO::getIpAddress, reqVO.getIpAddress())
                .orderByDesc(BindIpaddressDO::getId));
    }

}