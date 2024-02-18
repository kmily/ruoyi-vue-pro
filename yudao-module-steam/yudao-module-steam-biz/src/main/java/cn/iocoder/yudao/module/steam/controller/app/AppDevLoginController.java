package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.yudao.module.pay.api.notify.dto.PayRefundNotifyReqDTO;
import cn.iocoder.yudao.module.pay.controller.admin.demo.vo.order.PayDemoOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.SelExteriorPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.SelItemsetListReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.SelQualityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.SelRarityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.SelTypePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppDropListRespVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.LoginReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.PaySteamOrderCreateReqVO;
import cn.iocoder.yudao.module.steam.dal.mysql.seltype.SelWeaponMapper;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.invorder.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.selexterior.SelExteriorService;
import cn.iocoder.yudao.module.steam.service.selitemset.SelItemsetService;
import cn.iocoder.yudao.module.steam.service.selquality.SelQualityService;
import cn.iocoder.yudao.module.steam.service.selrarity.SelRarityService;
import cn.iocoder.yudao.module.steam.service.seltype.SelTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 *  @author steamboat
 *  @date 2023/05/05 14:41
 *  @description 获取登录用户id
 */
@Tag(name = "steam后台 - devLogin")
@RestController
@RequestMapping("devLogin")
@Validated
public class AppDevLoginController {

    /**
     * 【最常用】获得当前用户的编号，从上下文中
     *
     * @return 用户编号
     */
    @PostMapping("/userid")
    @Operation(summary = "获取登录用户id")
    public CommonResult<LoginReqVO> login() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        LoginReqVO loginReqVO = new LoginReqVO();
        loginReqVO.setUserid(userId);
        // 返回结果
        return success(loginReqVO);
    }

}
