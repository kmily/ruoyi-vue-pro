package cn.iocoder.yudao.module.member.controller.admin.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.TerminalEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginReqVO;
import cn.iocoder.yudao.module.member.controller.app.user.vo.AppMemberUserResetPasswordReqVO;
import cn.iocoder.yudao.module.member.convert.user.MemberUserConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserExtDO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 会员用户")
@RestController
@RequestMapping("/member/user")
@Validated
public class MemberUserController {

    @Resource
    private MemberUserService memberUserService;
    //    @Resource
//    private MemberTagService memberTagService;
    @Resource
    private MemberLevelService memberLevelService;
    //    @Resource
//    private MemberGroupService memberGroupService;
    @Resource
    private MemberPointRecordService memberPointRecordService;



    @PutMapping("/update")
    @Operation(summary = "更新会员用户")
    @PreAuthorize("@ss.hasPermission('member:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody MemberUserUpdateReqVO updateReqVO) {
        memberUserService.updateUser(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-level")
    @Operation(summary = "更新会员用户等级")
    @PreAuthorize("@ss.hasPermission('member:user:update-level')")
    public CommonResult<Boolean> updateUserLevel(@Valid @RequestBody MemberUserUpdateLevelReqVO updateReqVO) {
        memberLevelService.updateUserLevel(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-point")
    @Operation(summary = "更新会员用户积分")
    @PreAuthorize("@ss.hasPermission('member:user:update-point')")
    public CommonResult<Boolean> updateUserPoint(@Valid @RequestBody MemberUserUpdatePointReqVO updateReqVO) {
        memberPointRecordService.createPointRecord(updateReqVO.getId(), updateReqVO.getPoint(),
                MemberPointBizTypeEnum.ADMIN, String.valueOf(getLoginUserId()));
        return success(true);
    }

    @PutMapping("/update-balance")
    @Operation(summary = "更新会员用户余额")
    @PreAuthorize("@ss.hasPermission('member:user:update-balance')")
    public CommonResult<Boolean> updateUserBalance(@Valid @RequestBody Long id) {
        // todo @jason：增加一个【修改余额】
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<MemberUserRespVO> getUser(@RequestParam("id") Long id) {
        MemberUserDO user = memberUserService.getUser(id);
        MemberUserExtDO extDO = memberUserService.getUserExtInfo(id);
        MemberUserRespVO respVO = MemberUserConvert.INSTANCE.convert03(user);
        BeanUtil.copyProperties(extDO, respVO);
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员用户分页")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<PageResult<MemberUserRespVO>> getUserPage(@Valid MemberUserPageReqVO pageVO) {
        PageResult<MemberUserDO> pageResult = memberUserService.getUserPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

//        // 处理用户标签返显
//        Set<Long> tagIds = pageResult.getList().stream()
//                .map(MemberUserDO::getTagIds)
//                .filter(Objects::nonNull)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toSet());
//        List<MemberTagDO> tags = memberTagService.getTagList(tagIds);
//        // 处理用户级别返显
//        List<MemberLevelDO> levels = memberLevelService.getLevelList(
//                convertSet(pageResult.getList(), MemberUserDO::getLevelId));
//        // 处理用户分组返显
//        List<MemberGroupDO> groups = memberGroupService.getGroupList(
//                convertSet(pageResult.getList(), MemberUserDO::getGroupId));
//        return success(MemberUserConvert.INSTANCE.convertPage(pageResult, tags, levels, groups));
//        //处理患者最新的治疗流程返显
//        List<Long> userIds = pageResult.getList().stream().map(p -> p.getId()).collect(Collectors.toList());
//        Map<Long, TreatmentInstanceDO> map = treatmentStatisticsDataService.queryLatestTreatmentInstanceId(userIds);
//        Map<Long, List<String>> map1 = treatmentStatisticsDataService.queryPsycoTroubleCategory(userIds);
//        //处理扩展信息
//        List<MemberUserExtDO> extDOS=memberUserService.getUserExtInfoList(userIds);
//        Map<Long,MemberUserExtDO> map2=CollectionUtils.convertMap(extDOS,MemberUserExtDO::getUserId);
//
        PageResult<MemberUserRespVO> res = MemberUserConvert.INSTANCE.convertPage(pageResult);
//        for (MemberUserRespVO item : res.getList()) {
//            if (map.containsKey(item.getId())) {
//                TreatmentInstanceDO instanceDO = map.get(item.getId());
//                item.setInstanceState(instanceDO.getStatus());
//                item.setTreatmentInstanceId(instanceDO.getId());
//            }
//            if (map1 != null && map1.containsKey(item.getId())) {
//                item.setLlm(map1.get(item.getId()));
//            }
//            if (map2 != null && map2.containsKey(item.getId())) {
//                item.setAppointmentDate(map2.get(item.getId()).getAppointmentDate());
//                item.setAppointmentTimeRange(map2.get(item.getId()).getAppointmentTimeRange());
//            }
//        }

        return success(res);
    }

    @PostMapping("/create")
    @Operation(summary = "使用手机号和密码创建患者")
    public CommonResult<MemberUserBaseVO> create(@RequestBody @Valid AppAuthLoginReqVO reqVO, HttpServletRequest request) {
        String ip = ServletUtils.getClientIP(request);
        MemberUserDO userDO = memberUserService.createUserByAdmin(reqVO.getMobile(), reqVO.getPassword(), "", TerminalEnum.ADMIN_WEB.getTerminal());
        return success(MemberUserConvert.INSTANCE.convert04(userDO));
    }

    @PutMapping("/reset-password")
    @Operation(summary = "重置密码", description = "用户忘记密码时使用")
    public CommonResult<Boolean> resetUserPassword(@RequestBody @Valid AppMemberUserResetPasswordReqVO reqVO) {
        memberUserService.resetUserPassword(reqVO);
        return success(true);
    }

    @GetMapping("/total")
    @Operation(summary = "总患者数量", description = "统计目前患者总数")
    public CommonResult<Long> total() {
        return success(memberUserService.total());
    }

}
