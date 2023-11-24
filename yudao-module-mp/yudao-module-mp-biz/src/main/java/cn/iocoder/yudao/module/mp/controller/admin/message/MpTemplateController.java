package cn.iocoder.yudao.module.mp.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateFormUserReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplatePageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateUpdateReqVO;
import cn.iocoder.yudao.module.mp.convert.message.MpTemplateConvert;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpTemplateDO;
import cn.iocoder.yudao.module.mp.service.message.MpTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 模板消息")
@RestController
@RequestMapping("/mp/template")
@Validated
public class MpTemplateController {

    @Resource
    private MpTemplateService mpTemplateService;

    @GetMapping("/page")
    @Operation(summary = "获得公众号模板分页")
    @PreAuthorize("@ss.hasPermission('mp:template:query')")
    public CommonResult<PageResult<MpTemplateRespVO>> getTemplatePage(@Valid MpTemplatePageReqVO pageVO) {
        PageResult<MpTemplateDO> pageResult = mpTemplateService.getTemplatePage(pageVO);
        return success(MpTemplateConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get")
    @Operation(summary = "获得公众号模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mp:template:query')")
    public CommonResult<MpTemplateRespVO> getTemplate(@RequestParam("id") Long id) {
        return success(MpTemplateConvert.INSTANCE.convert(mpTemplateService.getTemplate(id)));
    }

    @GetMapping("/contentGet")
    @Operation(summary = "获得公众号模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mp:template:query')")
    public CommonResult<String> getContent(@RequestParam("id") Long id) {
        return success(mpTemplateService.getTemplateContent(id));
    }

    @PutMapping("/update")
    @Operation(summary = "修改公众号模板")
    @PreAuthorize("@ss.hasPermission('mp:template:update')")
    public CommonResult<Boolean> updateTemplate(@RequestBody MpTemplateUpdateReqVO mpTemplateUpdateReqVO) {
        mpTemplateService.updateTemplate(MpTemplateConvert.INSTANCE.convert(mpTemplateUpdateReqVO));
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除公众号模板")
    @PreAuthorize("@ss.hasPermission('mp:template:delete')")
    public CommonResult<Boolean> deleteTemplate(@RequestParam("id") Long id) {
        mpTemplateService.deleteTemplate(id);
        return success(true);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步公众号模板")
    @Parameter(name = "accountId", description = "公众号账号的编号", required = true)
    @PreAuthorize("@ss.hasPermission('mp:template:sync')")
    public CommonResult<Boolean> syncSendTemplate(@RequestParam("accountId") Long accountId) {
        mpTemplateService.syncTemplate(accountId);
        return success(true);
    }

    /**
     * 批量向用户发送模板消息
     * 通过用户筛选条件（一般使用标签筛选），将消息发送给数据库中所有符合筛选条件的用户
     */
    @PostMapping("/sendMsgBatch")
    @Operation(summary = "批量向用户发送模板消息")
    @PreAuthorize("@ss.hasPermission('mp:template:send')")
    public CommonResult<Boolean> sendMsgBatch(@RequestBody MpTemplateFormUserReqVO form) {
        mpTemplateService.sendMsgBatch(form);
        return success(true);
    }

}
