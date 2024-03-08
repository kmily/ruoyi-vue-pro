package cn.iocoder.yudao.module.steam.service.bindipaddress;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import javax.validation.Valid;

/**
 * 绑定用户IP地址池 Service 接口
 *
 * @author 管理员
 */
public interface BindIpaddressService {

    /**
     * 创建绑定用户IP地址池
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBindIpaddress(@Valid BindIpaddressSaveReqVO createReqVO);

    /**
     * 更新绑定用户IP地址池
     *
     * @param updateReqVO 更新信息
     */
    void updateBindIpaddress(@Valid BindIpaddressSaveReqVO updateReqVO);

    /**
     * 删除绑定用户IP地址池
     *
     * @param id 编号
     */
    void deleteBindIpaddress(Long id);

    /**
     * 获得绑定用户IP地址池
     *
     * @param id 编号
     * @return 绑定用户IP地址池
     */
    BindIpaddressDO getBindIpaddress(Long id);

    /**
     * 获得绑定用户IP地址池分页
     *
     * @param pageReqVO 分页查询
     * @return 绑定用户IP地址池分页
     */
    PageResult<BindIpaddressDO> getBindIpaddressPage(BindIpaddressPageReqVO pageReqVO);

}