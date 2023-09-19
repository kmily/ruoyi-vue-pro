package cn.iocoder.yudao.module.trade.controller.admin.brokerage.withdraw;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.withdraw.vo.BrokerageWithdrawAuditReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.withdraw.vo.BrokerageWithdrawPageReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.brokerage.withdraw.vo.BrokerageWithdrawRespVO;
import cn.iocoder.yudao.module.trade.convert.brokerage.withdraw.BrokerageWithdrawConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.brokerage.withdraw.BrokerageWithdrawDO;
import cn.iocoder.yudao.module.trade.enums.brokerage.BrokerageWithdrawStatusEnum;
import cn.iocoder.yudao.module.trade.service.brokerage.withdraw.BrokerageWithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 佣金提现")
@RestController
@RequestMapping("/trade/brokerage-withdraw")
@Validated
public class BrokerageWithdrawController {

    @Resource
    private BrokerageWithdrawService brokerageWithdrawService;

    @Resource
    private MemberUserApi memberUserApi;

    @PutMapping("/approve")
    @Operation(summary = "佣金提现 - 通过申请")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:audit')")
    public CommonResult<Boolean> approveBrokerageWithdraw(@RequestParam("id") Integer id) {
        brokerageWithdrawService.auditBrokerageWithdraw(id, BrokerageWithdrawStatusEnum.AUDIT_SUCCESS, "");
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "审核佣金提现 - 驳回申请")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:audit')")
    public CommonResult<Boolean> rejectBrokerageWithdraw(@Valid @RequestBody BrokerageWithdrawAuditReqVO reqVO) {
        brokerageWithdrawService.auditBrokerageWithdraw(reqVO.getId(), BrokerageWithdrawStatusEnum.AUDIT_FAIL, reqVO.getAuditReason());
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得佣金提现")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:query')")
    public CommonResult<BrokerageWithdrawRespVO> getBrokerageWithdraw(@RequestParam("id") Integer id) {
        BrokerageWithdrawDO brokerageWithdraw = brokerageWithdrawService.getBrokerageWithdraw(id);
        return success(BrokerageWithdrawConvert.INSTANCE.convert(brokerageWithdraw));
    }

    @GetMapping("/page")
    @Operation(summary = "获得佣金提现分页")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:query')")
    public CommonResult<PageResult<BrokerageWithdrawRespVO>> getBrokerageWithdrawPage(@Valid BrokerageWithdrawPageReqVO pageVO) {
        // 分页查询
        PageResult<BrokerageWithdrawDO> pageResult = brokerageWithdrawService.getBrokerageWithdrawPage(pageVO);

        // 涉及到的用户
        Set<Long> userIds = convertSet(pageResult.getList(), BrokerageWithdrawDO::getUserId);
        // 查询用户信息
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);

        return success(BrokerageWithdrawConvert.INSTANCE.convertPage(pageResult, userMap));
    }

}
