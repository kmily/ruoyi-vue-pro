package cn.iocoder.yudao.module.member.controller.app.homepage;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.enums.HomePageType;
import org.springframework.data.redis.core.RedisTemplate;
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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.HOME_PAGE_NOT_EXISTS;

import cn.iocoder.yudao.module.member.controller.app.homepage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.homepage.HomePageDO;
import cn.iocoder.yudao.module.member.convert.homepage.HomePageConvert;
import cn.iocoder.yudao.module.member.service.homepage.HomePageService;

@Tag(name = "用户 APP - 首页数据卡片配置")
@RestController
@RequestMapping("/member/home-page")
@Validated
public class AppHomePageController {

    @Resource
    private HomePageService homePageService;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @PostMapping("/create")
    @Operation(summary = "创建首页数据卡片")
    @PreAuthenticated
    public CommonResult<Long> createHomePage(@Valid @RequestBody AppHomePageCreateReqVO createReqVO) {
        createReqVO.setMold((byte) 1);
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        return success(homePageService.createHomePage(createReqVO));
    }

    @PostMapping("/copy")
    @Operation(summary = "复制首页数据卡片")
    @PreAuthenticated
    public CommonResult<AppHomePageRespVO> copyHomePage(@Valid @RequestBody AppHomePageCopyReqVO copyReqVO) {
        AppHomePageCreateReqVO createReqVO = new AppHomePageCreateReqVO();
        createReqVO.setFamilyId(copyReqVO.getFamilyId());
        createReqVO.setType(copyReqVO.getType());
        createReqVO.setMold((byte) 1);
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        HomePageDO homePageDO = new HomePageDO();
        // 如果是自定义卡片则设置副标题
        if(createReqVO.getMold() == (byte)1){
            Long increment = redisTemplate.opsForValue().increment(createReqVO.key());
            homePageDO.setSubtitle("场景" + increment);
            homePageDO.setName(HomePageType.valueOf(createReqVO.getType()).name);
        }
        return success(HomePageConvert.INSTANCE.convert(homePageDO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新首页数据卡片")
    @PreAuthenticated
    public CommonResult<Boolean> updateHomePage(@Valid @RequestBody AppHomePageUpdateReqVO updateReqVO) {
        homePageService.updateHomePage(updateReqVO);
        return success(true);
    }

    @PutMapping("/saveOrUpdate")
    @Operation(summary = "保存或更新首页数据卡片")
    @PreAuthenticated
    public CommonResult<Boolean> saveOrUpdate(@Valid @RequestBody SaveUpdateDeleteVO saveUpdateDeleteVO){

        homePageService.saveOrUpdate(saveUpdateDeleteVO);

        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除首页数据卡片")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteHomePage(@RequestParam("id") Long id) {
        homePageService.deleteHomePage(id);
        return success(true);
    }

    @PutMapping("/bind-device")
    @Operation(summary = "首页数据卡片绑定设备")
    @PreAuthenticated
    public CommonResult<Boolean> bindDevice(@RequestBody AppHomePageBindReqVO bindReqVO) {
        homePageService.bindDevice(bindReqVO.getId(), bindReqVO.getDevices());
        return success(true);
    }


    @GetMapping("/get")
    @Operation(summary = "获得首页配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppHomePageRespVO> getHomePage(@RequestParam("id") Long id) {
        HomePageDO homePage = homePageService.getHomePage(id);
        return success(HomePageConvert.INSTANCE.convert(homePage));
    }

    @GetMapping("/list")
    @Operation(summary = "获得首页配置列表")
    @Parameter(name = "familyId", description = "家庭ID", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<List<AppHomePageRespVO>> getHomePageList(@RequestParam("familyId") Long familyId) {
        List<HomePageDO> list = homePageService.getHomePageList(familyId);
        list.sort(Comparator.comparingInt(HomePageDO::getSort));
        return success(HomePageConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得首页配置分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppHomePageRespVO>> getHomePagePage(@Valid AppHomePagePageReqVO pageVO) {
        PageResult<HomePageDO> pageResult = homePageService.getHomePagePage(pageVO);
        return success(HomePageConvert.INSTANCE.convertPage(pageResult));
    }

}
