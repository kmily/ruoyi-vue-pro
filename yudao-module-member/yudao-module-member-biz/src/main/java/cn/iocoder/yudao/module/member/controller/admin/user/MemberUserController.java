package cn.iocoder.yudao.module.member.controller.admin.user;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.member.controller.app.user.vo.AppUserInfoRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
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
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;


import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;
import cn.iocoder.yudao.module.member.convert.user.UserConvert;

@Tag(name = "管理后台 - 会员")
@RestController
@RequestMapping("/member/user")
@Validated
public class MemberUserController {

    @Resource
    private MemberUserService userService;


    @GetMapping("/get")
    @Operation(summary = "获得会员")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<AppUserInfoRespVO> getUser(@RequestParam("id") Long id) {
        MemberUserDO user = userService.getUser(id);
        return success(UserConvert.INSTANCE.convert(user));
    }


    @GetMapping("/page")
    @Operation(summary = "获得会员分页")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageVO) {
        PageResult<MemberUserDO> pageResult = userService.getUserPage(pageVO);
        return success(UserConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/mobile")
    @Operation(summary = "获得会员分页")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<UserRespVO> getUserByMobile(@RequestParam("mobile") String mobile) {
        MemberUserDO userDO = userService.getUserByMobile(mobile);
        return success(UserConvert.INSTANCE.convert3(userDO));
    }


    @GetMapping("/statistics")
    @Operation(summary = "用户数据统计")
    @PreAuthorize("@ss.hasPermission('member:user:statistics')")
    public CommonResult<UserStatisticsVO> statistics(){

        UserStatisticsVO statisticsVO = userService.statistics();

        return success(statisticsVO);
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出设备信息 Excel")
    @PreAuthorize("@ss.hasPermission('member:user:export')")
    @OperateLog(type = EXPORT)
    public void exportStatisticsExcel(HttpServletResponse response) throws IOException {
        List<UserStatisticsDetailVO> voList = userService.selectCountEveryMonth();
        // 导出 Excel
        List<UserStatisticsExcelVO> datas = UserConvert.INSTANCE.convertList02(voList);
        ExcelUtils.write(response, "用户总数明细.xls", "数据", UserStatisticsExcelVO.class, datas);
    }

}
