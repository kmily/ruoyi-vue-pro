package cn.iocoder.yudao.module.member.controller.app.family;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


import cn.iocoder.yudao.module.member.controller.app.family.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.module.member.convert.family.FamilyConvert;
import cn.iocoder.yudao.module.member.service.family.FamilyService;

@Tag(name = "用户 APP - 用户家庭")
@RestController
@RequestMapping("/member/family")
@Validated
public class AppFamilyController {

    @Resource
    private FamilyService familyService;

    @PostMapping("/create")
    @Operation(summary = "创建用户家庭")
    @PreAuthenticated
    public CommonResult<Long> createFamily(@Valid @RequestBody FamilyCreateReqVO createReqVO) {
        createReqVO.setUserId(getLoginUserId());
        return success(familyService.createFamily(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户家庭")
    @PreAuthenticated
    public CommonResult<Boolean> updateFamily(@Valid @RequestBody FamilyUpdateReqVO updateReqVO) {
        familyService.updateFamily(updateReqVO);
        return success(true);
    }


    @PostMapping("/add-mobile")
    @Operation(summary = "新增手机号")
    @PreAuthenticated
    public CommonResult<Collection<String>> addMobile(@RequestBody @Valid FamilyAddMobileVO mobileVO){
        Collection<String> mobiles = familyService.addMobile(mobileVO);
        return success(mobiles);
    }

    @PutMapping("/update-mobile")
    @Operation(summary = "修改手机号")
    @PreAuthenticated
    public CommonResult<Collection<String>> updateMobile(@RequestBody @Valid FamilyUpdateMobileVO mobileVO){
        Collection<String> mobiles = familyService.updateMobile(mobileVO);
        return success(mobiles);
    }


    @DeleteMapping("/delete-mobile")
    @Operation(summary = "删除手机号")
    @PreAuthenticated
    public CommonResult<Collection<String>> deleteMobile(@RequestBody @Valid FamilyAddMobileVO mobileVO){
        Collection<String> mobiles = familyService.deleteMobile(mobileVO);
        return success(mobiles);
    }



    @DeleteMapping("/delete")
    @Operation(summary = "删除用户家庭")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteFamily(@RequestParam("id") Long id) {
        familyService.deleteFamily(id);
        return success(true);
    }


    @GetMapping("/get")
    @Operation(summary = "获得用户家庭")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<FamilyRespVO> getFamily(@RequestParam("id") Long id) {
        FamilyDO family = familyService.getFamily(id);
        return success(FamilyConvert.INSTANCE.convert(family));
    }

    @GetMapping("/list")
    @Operation(summary = "获得用户家庭列表")
    @PreAuthenticated
    public CommonResult<List<FamilyRespVO>> getFamilyList() {
        List<FamilyDO> list = familyService.getFamilyList(new FamilyExportReqVO().setUserId(getLoginUserId()));
        return success(FamilyConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户家庭分页")
    @PreAuthenticated
    public CommonResult<PageResult<FamilyRespVO>> getFamilyPage(@Valid FamilyPageReqVO pageVO) {
        PageResult<FamilyDO> pageResult = familyService.getFamilyPage(pageVO);
        return success(FamilyConvert.INSTANCE.convertPage(pageResult));
    }
}
