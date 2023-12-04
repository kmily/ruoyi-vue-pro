package cn.iocoder.yudao.module.system.controller.app.helpcenter;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterRespVO;
import cn.iocoder.yudao.module.system.convert.helpcenter.HelpCenterConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;
import cn.iocoder.yudao.module.system.service.helpcenter.HelpCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "用户 App - 常见问题")
@RestController
@RequestMapping("/system/help-center")
@Validated
public class AppHelpCenterController {

    @Resource
    private HelpCenterService helpCenterService;

    @GetMapping("/list")
    @Operation(summary = "获得常见问题列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:help-center:query')")
    public CommonResult<List<HelpCenterRespVO>> getHelpCenterList(@RequestParam("ids") Collection<Long> ids) {
        List<HelpCenterDO> list = helpCenterService.getHelpCenterList(ids);
        return success(HelpCenterConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得常见问题分页")
    @PreAuthorize("@ss.hasPermission('system:help-center:query')")
    public CommonResult<PageResult<HelpCenterRespVO>> getHelpCenterPage(@Valid HelpCenterPageReqVO pageVO) {
        PageResult<HelpCenterDO> pageResult = helpCenterService.getHelpCenterPage(pageVO);
        return success(HelpCenterConvert.INSTANCE.convertPage(pageResult));
    }



}
