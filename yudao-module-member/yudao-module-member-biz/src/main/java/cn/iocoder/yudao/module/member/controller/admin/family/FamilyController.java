package cn.iocoder.yudao.module.member.controller.admin.family;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.controller.app.family.vo.FamilyExportReqVO;
import cn.iocoder.yudao.module.member.controller.app.family.vo.FamilyRespVO;
import cn.iocoder.yudao.module.member.convert.family.FamilyConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.module.member.service.family.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @author whycode
 * @title: FamilyController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1616:06
 */

@Tag(name = "管理后台 - 会员家庭")
@RestController
@RequestMapping("/member/family")
@Validated
public class FamilyController {

    @Resource
    private FamilyService familyService;

    @GetMapping("/list")
    @Operation(summary = "获得用户家庭列表")
    @Parameter(name = "userId", description = "用户ID")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<List<FamilyRespVO>> getFamilyList(@RequestParam("userId") Long userId) {
        List<FamilyDO> list = familyService.getFamilyList(new FamilyExportReqVO().setUserId(userId));
        return success(FamilyConvert.INSTANCE.convertList(list));
    }

}
