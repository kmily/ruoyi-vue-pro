package cn.iocoder.yudao.module.bpm.controller.admin.definition;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.form.BpmFormRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmFormConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmFormDO;
import cn.iocoder.yudao.module.bpm.service.definition.BpmFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 动态表单")
@RestController
@RequestMapping("/bpm/form")
@Validated
public class BpmFormController {

    @Resource
    private BpmFormService formService;

    @PostMapping("/create")
    @Operation(summary = "创建动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:create')")
    public CommonResult<Long> createForm(@Valid @RequestBody BpmFormCreateReqVO createReqVO) {
        return success(formService.createForm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:update')")
    public CommonResult<Boolean> updateForm(@Valid @RequestBody BpmFormUpdateReqVO updateReqVO) {
        formService.updateForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:delete')")
    public CommonResult<Boolean> deleteForm(@RequestParam("id") Long id) {
        formService.deleteForm(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态表单")
    @PreAuthorize("@ss.hasPermission('bpm:form:query')")
    public CommonResult<BpmFormRespVO> getForm(@RequestParam("id") Long id) {
        BpmFormDO form = formService.getForm(id);
        return success(BpmFormConvert.INSTANCE.convert(form));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得动态表单的精简列表", description = "用于表单下拉框")
    public CommonResult<List<BpmFormSimpleRespVO>> getSimpleForms() {
        List<BpmFormDO> list = formService.getFormList();
        return success(BpmFormConvert.INSTANCE.convertList2(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态表单分页")
    @PreAuthorize("@ss.hasPermission('bpm:form:query')")
    public CommonResult<PageResult<BpmFormRespVO>> getFormPage(@Valid BpmFormPageReqVO pageVO) {
        PageResult<BpmFormDO> pageResult = formService.getFormPage(pageVO);
        return success(BpmFormConvert.INSTANCE.convertPage(pageResult));
    }

}
