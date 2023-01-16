package cn.iocoder.yudao.module.scan.convert.users;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.controller.app.users.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;

/**
 * 扫描用户 Convert
 *
 * @author lyz
 */
@Mapper
public interface UsersConvert {

    UsersConvert INSTANCE = Mappers.getMapper(UsersConvert.class);

    UsersDO convert(UsersCreateReqVO bean);

    UsersDO convert(UsersUpdateReqVO bean);

    UsersRespVO convert(UsersDO bean);
    AppUsersRespVO convert02(UsersDO bean);

    List<UsersRespVO> convertList(List<UsersDO> list);

    PageResult<UsersRespVO> convertPage(PageResult<UsersDO> page);

    List<UsersExcelVO> convertList02(List<UsersDO> list);
    UsersCreateReqVO convert(AppUsersCreateReqVO bean);
}
