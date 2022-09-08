package cn.iocoder.yudao.module.by.controller.admin;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "管理后台 - Bybf测试接口")
@RestController
@RequestMapping("/bybf/test")
@Validated
public class DemoTestController {

    @GetMapping("/get")
    @ApiOperation("获取 test 信息")
    public CommonResult<String> get() {
        return success("true");
    }

}