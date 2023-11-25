package cn.iocoder.yudao.module.wms.service.ro;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wms.controller.admin.ro.vo.*;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoLpnDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoMtrlDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 收料单 Service 接口
 *
 * @author Arlen
 */
public interface RoService {

    /**
     * 创建收料单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRo(@Valid RoSaveReqVO createReqVO);

    /**
     * 更新收料单
     *
     * @param updateReqVO 更新信息
     */
    void updateRo(@Valid RoSaveReqVO updateReqVO);

    /**
     * 删除收料单
     *
     * @param id 编号
     */
    void deleteRo(String id);

    /**
     * 获得收料单
     *
     * @param id 编号
     * @return 收料单
     */
    RoDO getRo(String id);

    /**
     * 获得收料单分页
     *
     * @param pageReqVO 分页查询
     * @return 收料单分页
     */
    PageResult<RoDO> getRoPage(RoPageReqVO pageReqVO);

    // ==================== 子表（收料单LPN明细） ====================

    /**
     * 获得收料单LPN明细列表
     *
     * @param roId 收料单ID
     * @return 收料单LPN明细列表
     */
    List<RoLpnDO> getRoLpnListByRoId(String roId);

    // ==================== 子表（收料单物料明细） ====================

    /**
     * 获得收料单物料明细列表
     *
     * @param roId 收料单ID
     * @return 收料单物料明细列表
     */
    List<RoMtrlDO> getRoMtrlListByRoId(String roId);

}