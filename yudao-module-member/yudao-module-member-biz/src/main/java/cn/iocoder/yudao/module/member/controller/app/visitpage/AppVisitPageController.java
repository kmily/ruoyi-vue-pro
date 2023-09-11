package cn.iocoder.yudao.module.member.controller.app.visitpage;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.VisitPageCreateReqVO;
import cn.iocoder.yudao.module.member.service.visitpage.VisitPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AppVisitPageController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1113:42
 */

@Tag(name = "用户 APP - 页面访问数据")
@RestController
@RequestMapping("/member/visit-page")
@Validated
public class AppVisitPageController {


    @Resource
    private VisitPageService visitPageService;

    @PostMapping("/create")
    @Operation(summary = "创建页面访问数据")
    public CommonResult<Long> createVisitPage(@Valid @RequestBody VisitPageCreateReqVO createReqVO) {
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());

        return success(visitPageService.createVisitPage(createReqVO));
    }


    @PostMapping("/exit")
    @Operation(summary = "退出页面")
    @Parameter(name = "id", description = "编号")
    public CommonResult<Boolean> exit(@RequestParam("id") Long id){
        visitPageService.exit(id);
        return success(true);
    }

}
