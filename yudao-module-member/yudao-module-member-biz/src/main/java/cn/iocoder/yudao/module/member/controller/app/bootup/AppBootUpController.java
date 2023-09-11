package cn.iocoder.yudao.module.member.controller.app.bootup;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.BootUpCreateReqVO;
import cn.iocoder.yudao.module.member.service.bootup.BootUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AppBootUpController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/714:25
 */

@Tag(name = "用户 APP - 用户启动数据")
@RestController
@RequestMapping("/member/boot-up")
@Validated
@Slf4j
public class AppBootUpController {

    @Resource
    private BootUpService bootUpService;

    @PostMapping("/create")
    @Operation(summary = "创建用户启动数据")
    public CommonResult<Long> createBootUp() {
        BootUpCreateReqVO createReqVO = new BootUpCreateReqVO();
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        createReqVO.setUserId( loginUserId == null? 0L: loginUserId );
        return success(bootUpService.createBootUp(createReqVO));
    }

}
