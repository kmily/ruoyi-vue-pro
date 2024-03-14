package cn.iocoder.yudao.module.steam.service.devaccount;


import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.*;
import cn.iocoder.yudao.module.steam.controller.app.devaccount.vo.AppDevAccountSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.validation.Valid;
import java.util.List;

/**
 * 开放平台用户 Service 接口
 *
 * @author 芋道源码
 */
public interface DevAccountService {

    /**
     * 创建开放平台用户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDevAccount(@Valid DevAccountSaveReqVO createReqVO);

    /**
     * 更新开放平台用户
     *
     * @param updateReqVO 更新信息
     */
    void updateDevAccount(@Valid DevAccountSaveReqVO updateReqVO);

    /**
     * 删除开放平台用户
     *
     * @param id 编号
     */
    void deleteDevAccount(Long id);

    /**
     * 获得开放平台用户
     *
     * @param id 编号
     * @return 开放平台用户
     */
    DevAccountDO getDevAccount(Long id);

    /**
     * 获得开放平台用户分页
     *
     * @param pageReqVO 分页查询
     * @return 开放平台用户分页
     */
    PageResult<DevAccountDO> getDevAccountPage(DevAccountPageReqVO pageReqVO);
    /**
     * 前台用户申请开放平台权限
     *
     * @param pageReqVO 分页查询
     * @return 开放平台用户分页
     */
    String apply(AppDevAccountSaveReqVO pageReqVO);
    List<DevAccountDO> accountList();
    DevAccountDO selectByUserName (String userName, UserTypeEnum userType);

}