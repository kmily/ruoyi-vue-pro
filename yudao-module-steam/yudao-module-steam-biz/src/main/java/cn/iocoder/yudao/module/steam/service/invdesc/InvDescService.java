package cn.iocoder.yudao.module.steam.service.invdesc;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 库存信息详情 Service 接口
 *
 * @author 芋道源码
 */
public interface InvDescService {

    /**
     * 创建库存信息详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInvDesc(@Valid InvDescSaveReqVO createReqVO);

    /**
     * 更新库存信息详情
     *
     * @param updateReqVO 更新信息
     */
    void updateInvDesc(@Valid InvDescSaveReqVO updateReqVO);

    /**
     * 删除库存信息详情
     *
     * @param id 编号
     */
    void deleteInvDesc(Long id);

    /**
     * 获得库存信息详情
     *
     * @param id 编号
     * @return 库存信息详情
     */
    InvDescDO getInvDesc(Long id);

    /**
     * 获得库存信息详情分页
     *
     * @param pageReqVO 分页查询
     * @return 库存信息详情分页
     */
    PageResult<InvDescDO> getInvDescPage(InvDescPageReqVO pageReqVO);

}