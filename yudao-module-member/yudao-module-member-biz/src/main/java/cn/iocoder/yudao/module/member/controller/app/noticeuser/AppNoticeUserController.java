package cn.iocoder.yudao.module.member.controller.app.noticeuser;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.app.noticeuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.noticeuser.NoticeUserDO;
import cn.iocoder.yudao.module.member.convert.noticeuser.NoticeUserConvert;
import cn.iocoder.yudao.module.member.service.noticeuser.NoticeUserService;

@Tag(name = "用户 APP - 用户消息关联")
@RestController
@RequestMapping("/member/notice-user")
@Validated
public class AppNoticeUserController {

    @Resource
    private NoticeUserService noticeUserService;


    @GetMapping("/unRead")
    @Operation(summary = "查询未读消息数量")
    @Parameter(name = "familyId", description = "家庭ID")
    @PreAuthenticated
    public CommonResult<Long> getUnRead(@RequestParam(value = "familyId", required = false) Long familyId) {
        Long count = noticeUserService.getUnRead(SecurityFrameworkUtils.getLoginUserId(), familyId);
        return success(count);
    }



    @DeleteMapping("/delete")
    @Operation(summary = "删除用户消息关联")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteNoticeUser(@RequestParam("id") Long id) {
        noticeUserService.deleteNoticeUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户消息关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppNoticeUserRespVO> getNoticeUser(@RequestParam("id") Long id) {
        NoticeUserDO noticeUser = noticeUserService.getNoticeUser(id);
        return success(NoticeUserConvert.INSTANCE.convert(noticeUser));
    }

    @GetMapping("/list")
    @Operation(summary = "获得用户消息关联列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<AppNoticeUserRespVO>> getNoticeUserList(@RequestParam("ids") Collection<Long> ids) {
        List<NoticeUserDO> list = noticeUserService.getNoticeUserList(ids);
        return success(NoticeUserConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户消息关联分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppNoticeUserRespVO>> getNoticeUserPage(@Valid AppNoticeUserPageReqVO pageVO) {
        PageResult<NoticeUserDO> pageResult = noticeUserService.getNoticeUserPage(pageVO);
        return success(NoticeUserConvert.INSTANCE.convertPage(pageResult));
    }

}
