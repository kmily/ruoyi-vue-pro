package cn.iocoder.yudao.module.member.dal.mysql.serveraddress;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;

/**
 * 服务地址 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ServerAddressMapper extends BaseMapperX<ServerAddressDO>,BaseMapper<ServerAddressDO> {

    default PageResult<ServerAddressDO> selectPage(ServerAddressPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ServerAddressDO>()
                .eqIfPresent(ServerAddressDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ServerAddressDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(ServerAddressDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ServerAddressDO::getDetailAddress, reqVO.getDetailAddress())
                .eqIfPresent(ServerAddressDO::getDefaultStatus, reqVO.getDefaultStatus())
                .eqIfPresent(ServerAddressDO::getCoordinate, reqVO.getCoordinate())
                .betweenIfPresent(ServerAddressDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ServerAddressDO::getId));
    }

    default List<ServerAddressDO> selectList(ServerAddressExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ServerAddressDO>()
                .eqIfPresent(ServerAddressDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ServerAddressDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(ServerAddressDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ServerAddressDO::getDetailAddress, reqVO.getDetailAddress())
                .eqIfPresent(ServerAddressDO::getDefaultStatus, reqVO.getDefaultStatus())
                .eqIfPresent(ServerAddressDO::getCoordinate, reqVO.getCoordinate())
                .betweenIfPresent(ServerAddressDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ServerAddressDO::getId));
    }

    default List<ServerAddressDO> selectListByUserIdAndDefaulted(Long userId, Boolean defaulted) {
        return selectList(new LambdaQueryWrapperX<ServerAddressDO>().eq(ServerAddressDO::getUserId, userId)
                .eqIfPresent(ServerAddressDO::getDefaultStatus, defaulted));
    }

}
