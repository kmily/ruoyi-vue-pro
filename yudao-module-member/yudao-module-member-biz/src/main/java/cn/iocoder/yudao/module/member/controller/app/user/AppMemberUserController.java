package cn.iocoder.yudao.module.member.controller.app.user;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.enums.AreaTypeEnum;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.controller.app.user.vo.*;
import cn.iocoder.yudao.module.member.convert.user.MemberUserConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.member.controller.app.user.vo.HotlineRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 用户个人中心")
@RestController
@RequestMapping("/member/user")
@Validated
@Slf4j
public class AppMemberUserController {

    @Resource
    private MemberUserService userService;
    @Resource
    private MemberLevelService levelService;

    @Resource
    DictDataService dictDataService;

    @Resource
    MemberUserService memberUserService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    @PreAuthenticated
    public CommonResult<AppMemberUserInfoRespVO> getUserInfo() {
        MemberUserDO user = userService.getUser(getLoginUserId());
        MemberLevelDO level = levelService.getLevel(user.getLevelId());
        return success(MemberUserConvert.INSTANCE.convert(user, level));
    }

    @PutMapping("/update")
    @Operation(summary = "修改基本信息")
    @PreAuthenticated
    public CommonResult<Boolean> updateUser(@RequestBody @Valid AppMemberUserUpdateReqVO reqVO) {
        userService.updateUser(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-mobile")
    @Operation(summary = "修改用户手机")
    @PreAuthenticated
    public CommonResult<Boolean> updateUserMobile(@RequestBody @Valid AppMemberUserUpdateMobileReqVO reqVO) {
        userService.updateUserMobile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-mobile-by-weixin")
    @Operation(summary = "基于微信小程序的授权码，修改用户手机")
    @PreAuthenticated
    public CommonResult<Boolean> updateUserMobileByWeixin(@RequestBody @Valid AppMemberUserUpdateMobileByWeixinReqVO reqVO) {
        userService.updateUserMobileByWeixin(getLoginUserId(), reqVO);
        return success(true);
    }

//    @PutMapping("/update-password")
//    @Operation(summary = "修改用户密码", description = "用户修改密码时使用")
//    @PreAuthenticated
//    public CommonResult<Boolean> updateUserPassword(@RequestBody @Valid AppMemberUserUpdatePasswordReqVO reqVO) {
//        userService.updateUserPassword(getLoginUserId(), reqVO);
//        return success(true);
//    }
//
//    @PutMapping("/reset-password")
//    @Operation(summary = "重置密码", description = "用户忘记密码时使用")
//    public CommonResult<Boolean> resetUserPassword(@RequestBody @Valid AppMemberUserResetPasswordReqVO reqVO) {
//        userService.resetUserPassword(reqVO);
//        return success(true);
//    }

    @GetMapping("/get-hotline")
    @Operation(summary = "获取危机干预热线")
    public CommonResult<HotlineRespVO> getCrisisInterventionHotline() {
        HotlineRespVO hotlineRespVO = new HotlineRespVO();

        DictDataDO nationalHotline = dictDataService.parseDictData("crisis_intervention_hotline", "全国");
        if (nationalHotline != null) {
            hotlineRespVO.setNationalHotline(nationalHotline.getValue());
        }

        MemberUserDO user = memberUserService.getUser(getLoginUserId());
        if (user == null || NumberUtils.isZero(user.getAreaId())) {
            return success(hotlineRespVO);
        }

        Area area = AreaUtils.getArea(user.getAreaId());

        if (area == null) {
            return success(hotlineRespVO);
        }
        //取城市
        if (area.getType() == AreaTypeEnum.CITY.getType()) {
            DictDataDO cityHotline = dictDataService.parseDictData("crisis_intervention_hotline", area.getName());
            if (cityHotline != null) {
                hotlineRespVO.setCityHotline(cityHotline.getValue());
            }
        } else if (area.getType() == AreaTypeEnum.DISTRICT.getType()) {
            //取城市
            Area city = area.getParent();
            if (city != null) {
                DictDataDO cityHotline = dictDataService.parseDictData("crisis_intervention_hotline", city.getName());
                if (cityHotline != null) {
                    hotlineRespVO.setCityHotline(cityHotline.getValue());
                }
            }
        } else {
            DictDataDO cityHotline = dictDataService.parseDictData("crisis_intervention_hotline", area.getName());
            if (cityHotline != null) {
                hotlineRespVO.setCityHotline(cityHotline.getValue());
            }

        }

        return success(hotlineRespVO);
    }

}

