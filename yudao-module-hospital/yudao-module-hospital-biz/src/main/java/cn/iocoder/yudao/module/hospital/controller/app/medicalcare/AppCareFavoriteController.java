package cn.iocoder.yudao.module.hospital.controller.app.medicalcare;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.CareFavoriteDO;
import cn.iocoder.yudao.module.hospital.convert.medicalcare.CareFavoriteConvert;
import cn.iocoder.yudao.module.hospital.service.medicalcare.CareFavoriteService;

@Tag(name = "用户 APP - 医护收藏")
@RestController
@RequestMapping("/hospital/care-favorite")
@Validated
public class AppCareFavoriteController {

    @Resource
    private CareFavoriteService careFavoriteService;

    @PostMapping("/create")
    @Operation(summary = "创建医护收藏")
    @Parameter(name = "careId", description = "医护编号", required = true)
    public CommonResult<Long> createCareFavorite(@RequestParam("careId") Long careId) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return success(null);
        }
        return success(careFavoriteService.createCareFavorite(careId, loginUser.getId()));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除医护收藏")
    @Parameter(name = "careId", description = "医护编号", required = true)
    public CommonResult<Boolean> deleteCareFavorite(@RequestParam("careId") Long careId) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return success(false);
        }else{
            careFavoriteService.deleteCareFavorite(careId, loginUser.getId());
            return success(true);
        }
    }

    @GetMapping("/get")
    @Operation(summary = "校验是否收藏")
    @Parameter(name = "careId", description = "医护编号", required = true)
    public CommonResult<Boolean> getCareFavorite(@RequestParam("careId") Long careId) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(loginUser == null){
            return success(false);
        }else{
            Long count = careFavoriteService.getCareFavorite(careId, loginUser.getId());
            return success(!Objects.equals(count, 0L));
        }
    }

    @GetMapping("/page")
    @Operation(summary = "获得医护收藏分页")
    public CommonResult<PageResult<AppCareFavoriteRespVO>> getCareFavoritePage(@Valid AppCareFavoritePageReqVO pageVO) {
        PageResult<CareFavoriteDO> pageResult = careFavoriteService.getCareFavoritePage(pageVO);
        return success(CareFavoriteConvert.INSTANCE.convertPage(pageResult));
    }
}
