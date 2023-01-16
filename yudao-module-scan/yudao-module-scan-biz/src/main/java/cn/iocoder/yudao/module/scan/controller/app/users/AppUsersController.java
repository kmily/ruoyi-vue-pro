package cn.iocoder.yudao.module.scan.controller.app.users;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.module.scan.controller.app.users.vo.*;
import cn.iocoder.yudao.module.scan.convert.users.UsersConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.users.UsersDO;
import cn.iocoder.yudao.module.scan.service.users.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "扫描APP - 扫描用户")
@RestController
@RequestMapping("/scan/users")
@Validated
public class AppUsersController {

    @Resource
    private UsersService usersService;

    @PostMapping("/create")
    @ApiOperation("创建扫描用户")
    @Idempotent(timeout = 10, message = "重复请求，请稍后重试")
    public CommonResult<Long> createUsers(@Valid @RequestBody AppUsersCreateReqVO createReqVO) {
        return success(usersService.createUsers(UsersConvert.INSTANCE.convert(createReqVO)));
    }


    @GetMapping("/get")
    @ApiOperation("获得扫描用户")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    public CommonResult<AppUsersRespVO> getUsers(@RequestParam("id") Long id) {
        UsersDO users = usersService.getUsers(id);
        return success(UsersConvert.INSTANCE.convert02(users));
    }


}
