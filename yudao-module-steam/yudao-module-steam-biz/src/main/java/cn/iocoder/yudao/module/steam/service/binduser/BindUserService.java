package cn.iocoder.yudao.module.steam.service.binduser;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 *  steam用户绑定 Service 接口
 *
 * @author 芋道源码
 */
public interface BindUserService {

    /**
     * 创建 steam用户绑定
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBindUser(@Valid BindUserSaveReqVO createReqVO);

    /**
     * 更新 steam用户绑定
     *
     * @param updateReqVO 更新信息
     */
    void updateBindUser(@Valid BindUserSaveReqVO updateReqVO);

    /**
     * 删除 steam用户绑定
     *
     * @param id 编号
     */
    void deleteBindUser(Long id);

    /**
     * 获得 steam用户绑定
     *
     * @param id 编号
     * @return  steam用户绑定
     */
    BindUserDO getBindUser(Long id);

    /**
     * 获得 steam用户绑定分页
     *
     * @param pageReqVO 分页查询
     * @return  steam用户绑定分页
     */
    PageResult<BindUserDO> getBindUserPage(BindUserPageReqVO pageReqVO);
    /**
     * 保存用户cookie 专用于steamweb更新
     * @param bindUserDO
     */
    void changeBindUserCookie(BindUserDO bindUserDO);
}