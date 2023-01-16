package cn.iocoder.yudao.module.scan.dal.mysql.users;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;

/**
 * 扫描用户 Mapper
 *
 * @author lyz
 */
@Mapper
public interface UsersMapper extends BaseMapperX<UsersDO> {

    default PageResult<UsersDO> selectPage(UsersPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UsersDO>()
                .betweenIfPresent(UsersDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(UsersDO::getName, reqVO.getName())
                .eqIfPresent(UsersDO::getAge, reqVO.getAge())
                .eqIfPresent(UsersDO::getSex, reqVO.getSex())
                .eqIfPresent(UsersDO::getPhone, reqVO.getPhone())
                .eqIfPresent(UsersDO::getDeviceId, reqVO.getDeviceId())
                .orderByDesc(UsersDO::getId));
    }

    default List<UsersDO> selectList(UsersExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UsersDO>()
                .betweenIfPresent(UsersDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(UsersDO::getName, reqVO.getName())
                .eqIfPresent(UsersDO::getAge, reqVO.getAge())
                .eqIfPresent(UsersDO::getSex, reqVO.getSex())
                .eqIfPresent(UsersDO::getPhone, reqVO.getPhone())
                .eqIfPresent(UsersDO::getDeviceId, reqVO.getDeviceId())
                .orderByDesc(UsersDO::getId));
    }

}
