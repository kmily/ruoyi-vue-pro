package cn.iocoder.yudao.module.by.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "用户 App - Test")
@RestController
@RequestMapping("/bybf/test")
@Validated
@Slf4j
public class AppDemoTestController {

    @GetMapping("/get")
    @ApiOperation("获取 test 信息")
    public CommonResult<String> get() {
        log.info("[测试接口数据]");
        return success("true");
    }
    @GetMapping("/post")
    @ApiOperation("获取 七牛云token 信息")
    public CommonResult<String> getToken() {
        log.info("[测试接口数据]");
        String bucket = "bybf";
        return success("true");
    }

}
