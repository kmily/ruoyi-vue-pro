package com.cw.module.trade.service.position;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import com.cw.module.trade.controller.admin.position.vo.*;
import com.cw.module.trade.dal.dataobject.position.PositionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 账户持仓信息 Service 接口
 *
 * @author chengjiale
 */
public interface PositionService {

    /**
     * 创建账户持仓信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPosition(@Valid PositionCreateReqVO createReqVO);

    /**
     * 更新账户持仓信息
     *
     * @param updateReqVO 更新信息
     */
    void updatePosition(@Valid PositionUpdateReqVO updateReqVO);

    /**
     * 删除账户持仓信息
     *
     * @param id 编号
     */
    void deletePosition(Long id);

    /**
     * 获得账户持仓信息
     *
     * @param id 编号
     * @return 账户持仓信息
     */
    PositionDO getPosition(Long id);

    /**
     * 获得账户持仓信息列表
     *
     * @param ids 编号
     * @return 账户持仓信息列表
     */
    List<PositionDO> getPositionList(Collection<Long> ids);

    /**
     * 获得账户持仓信息分页
     *
     * @param pageReqVO 分页查询
     * @return 账户持仓信息分页
     */
    PageResult<PositionDO> getPositionPage(PositionPageReqVO pageReqVO);

    /**
     * 获得账户持仓信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 账户持仓信息列表
     */
    List<PositionDO> getPositionList(PositionExportReqVO exportReqVO);

    /**
     * * 通报不账号持仓信息
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    void syncPosition();

    /**
     * * 查询最新的持仓。
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    PositionDO selectLastestPosition(Long accountId, String symbol);

    /**
     * * 新增持仓信息
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    void addPosition(Long accountId, String symbol, BigDecimal positionAmt, String position);

    /**
     * * 同步指定账户的持仓信息
     * @date 2023年5月29日
     * @author wuqiaoxin
     */
    void syncAccountPositionById(Long accountId);

}
