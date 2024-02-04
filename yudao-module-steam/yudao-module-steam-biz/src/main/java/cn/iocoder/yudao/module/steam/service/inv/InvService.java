package cn.iocoder.yudao.module.steam.service.inv;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * steam用户库存储 Service 接口
 *
 * @author 芋道源码
 */
public interface InvService {

    /**
     * 创建steam用户库存储
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createInv(@Valid InvSaveReqVO createReqVO);

    /**
     * 更新steam用户库存储
     *
     * @param updateReqVO 更新信息
     */
    void updateInv(@Valid InvSaveReqVO updateReqVO);

    /**
     * 删除steam用户库存储
     *
     * @param id 编号
     */
    void deleteInv(Integer id);

    /**
     * 获得steam用户库存储
     *
     * @param id 编号
     * @return steam用户库存储
     */
    InvDO getInv(Integer id);

    /**
     * 获得steam用户库存储分页
     *
     * @param pageReqVO 分页查询
     * @return steam用户库存储分页
     */
    PageResult<InvDO> getInvPage(InvPageReqVO pageReqVO);

}