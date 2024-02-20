package cn.iocoder.yudao.module.steam.service.withdrawal;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 提现 Service 接口
 *
 * @author 芋道源码
 */
public interface WithdrawalService {

    /**
     * 创建提现
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWithdrawal(@Valid WithdrawalSaveReqVO createReqVO);

    /**
     * 更新提现
     *
     * @param updateReqVO 更新信息
     */
    void updateWithdrawal(@Valid WithdrawalSaveReqVO updateReqVO);

    /**
     * 删除提现
     *
     * @param id 编号
     */
    void deleteWithdrawal(Long id);

    /**
     * 获得提现
     *
     * @param id 编号
     * @return 提现
     */
    WithdrawalDO getWithdrawal(Long id);

    /**
     * 获得提现分页
     *
     * @param pageReqVO 分页查询
     * @return 提现分页
     */
    PageResult<WithdrawalDO> getWithdrawalPage(WithdrawalPageReqVO pageReqVO);

}