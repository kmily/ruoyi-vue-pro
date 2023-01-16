package cn.iocoder.yudao.module.scan.service.users;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.scan.controller.admin.users.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 扫描用户 Service 接口
 *
 * @author lyz
 */
public interface UsersService {

    /**
     * 创建扫描用户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUsers(@Valid UsersCreateReqVO createReqVO);

    /**
     * 更新扫描用户
     *
     * @param updateReqVO 更新信息
     */
    void updateUsers(@Valid UsersUpdateReqVO updateReqVO);

    /**
     * 删除扫描用户
     *
     * @param id 编号
     */
    void deleteUsers(Long id);

    /**
     * 获得扫描用户
     *
     * @param id 编号
     * @return 扫描用户
     */
    UsersDO getUsers(Long id);

    /**
     * 获得扫描用户列表
     *
     * @param ids 编号
     * @return 扫描用户列表
     */
    List<UsersDO> getUsersList(Collection<Long> ids);

    /**
     * 获得扫描用户分页
     *
     * @param pageReqVO 分页查询
     * @return 扫描用户分页
     */
    PageResult<UsersDO> getUsersPage(UsersPageReqVO pageReqVO);

    /**
     * 获得扫描用户列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 扫描用户列表
     */
    List<UsersDO> getUsersList(UsersExportReqVO exportReqVO);

}
