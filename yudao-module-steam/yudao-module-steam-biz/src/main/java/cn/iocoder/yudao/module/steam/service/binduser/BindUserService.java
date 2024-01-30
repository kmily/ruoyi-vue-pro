package cn.iocoder.yudao.module.steam.service.binduser;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

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
    Integer createBindUser(@Valid BindUserSaveReqVO createReqVO);

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
    void deleteBindUser(Integer id);

    /**
     * 获得 steam用户绑定
     *
     * @param id 编号
     * @return  steam用户绑定
     */
    BindUserDO getBindUser(Integer id);

    /**
     * 获得 steam用户绑定分页
     *
     * @param pageReqVO 分页查询
     * @return  steam用户绑定分页
     */
    PageResult<BindUserDO> getBindUserPage(BindUserPageReqVO pageReqVO);

}