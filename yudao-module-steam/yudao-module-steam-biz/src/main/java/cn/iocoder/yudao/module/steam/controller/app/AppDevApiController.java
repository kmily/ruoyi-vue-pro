package cn.iocoder.yudao.module.steam.controller.app;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.SelExteriorPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.SelItemsetListReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.SelQualityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.SelRarityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.SelTypePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppDropListRespVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.dal.mysql.seltype.SelWeaponMapper;
import cn.iocoder.yudao.module.steam.service.OpenApiService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.selexterior.SelExteriorService;
import cn.iocoder.yudao.module.steam.service.selitemset.SelItemsetService;
import cn.iocoder.yudao.module.steam.service.selquality.SelQualityService;
import cn.iocoder.yudao.module.steam.service.selrarity.SelRarityService;
import cn.iocoder.yudao.module.steam.service.seltype.SelTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - Test")
@RestController
@RequestMapping("/devapi")
@Validated
public class AppDevApiController {
    @Resource
    private SelExteriorService selExteriorService;
    @Resource
    private SelItemsetService selItemsetService;
    @Resource
    private SelQualityService selQualityService;
    @Resource
    private SelTypeService selTypeService;
    @Resource
    private SelRarityService selRarityService;
    @Resource
    private OpenApiService openApiService;
    @Resource
    private SelWeaponMapper selWeaponMapper;
    @Resource
    private SteamService steamService;

    /**
     * 类别选择
     */
    @PostMapping("/openapi")
    @Operation(summary = "获取类别选择下拉信息")
    public CommonResult<String> openApi(@RequestBody @Validated OpenApiReqVo openApi) {
        try {
            String despatch = openApiService.despatch(openApi);
            return CommonResult.success(despatch);
        } catch (ServiceException e) {
            return CommonResult.error(new ErrorCode(01, "接口出错原因:" + e.getMessage()));
        }
    }

    /**
     * 类别选择
     */
    @GetMapping("/drop_list_quality")
    @Operation(summary = "获取类别选择下拉信息")
    public CommonResult<AppDropListRespVO> getQuality() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelQualityPageReqVO quality = new SelQualityPageReqVO();
        quality.setPageSize(200);
        quality.setPageNo(1);
        appDropListRespVO.setQuality(selQualityService.getSelQualityPage(quality).getList());
        return CommonResult.success(appDropListRespVO);
    }

    /**
     * 收藏品选择
     */
    @GetMapping("/drop_list_itemSet")
    @Operation(summary = "获取收藏品选择下拉信息")
    public CommonResult<AppDropListRespVO> getItemSet() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelItemsetListReqVO itemsetList = new SelItemsetListReqVO();
        appDropListRespVO.setItemset(selItemsetService.getSelItemsetList(itemsetList));
        return CommonResult.success(appDropListRespVO);
    }

    /**
     * 类型选择
     */
    @GetMapping("/drop_list_type")
    @Operation(summary = "获取类型选择下拉信息")
    public CommonResult<AppDropListRespVO> getType() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelTypePageReqVO type = new SelTypePageReqVO();
        type.setPageSize(200);
        type.setPageNo(1);
        appDropListRespVO.setType(selTypeService.getSelTypePage(type).getList());
        return CommonResult.success(appDropListRespVO);
    }

    /**
     * 品质选择
     */
    @GetMapping("/drop_list_rarity")
    @Operation(summary = "获取品质选择下拉信息")
    public CommonResult<AppDropListRespVO> getRarity() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelRarityPageReqVO rarity = new SelRarityPageReqVO();
        rarity.setPageSize(200);
        rarity.setPageNo(1);
        appDropListRespVO.setRarity(selRarityService.getSelRarityPage(rarity).getList());
        return CommonResult.success(appDropListRespVO);
    }

    /**
     * 武器选择
     */
    @GetMapping("/drop_list_weapon")
    @Operation(summary = "获取武器选择下拉信息")
    public CommonResult<AppDropListRespVO> getWeapon() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelTypePageReqVO type_weapon = new SelTypePageReqVO();
        type_weapon.setPageSize(200);
        type_weapon.setPageNo(1);
//        appDropListRespVO.setWeapon(selTypeService.getSelWeaponPage(type_weapon,typeId).getList());
//        selWeaponMapper.selectList();
        appDropListRespVO.setWeapon(selWeaponMapper.selectList());
        return CommonResult.success(appDropListRespVO);

    }

    /**
     * 外观选择
     */
    @GetMapping("/drop_list_exterior")
    @Operation(summary = "获取外观选择下拉信息")
    public CommonResult<AppDropListRespVO> getExterior() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelExteriorPageReqVO exterior = new SelExteriorPageReqVO();
        exterior.setPageSize(200);
        exterior.setPageNo(1);
        appDropListRespVO.setExterior(selExteriorService.getSelExteriorPage(exterior).getList());
        return CommonResult.success(appDropListRespVO);
    }
    @GetMapping("/test")

    public CommonResult<String> test() {
        steamService.fetchInventory("76561199388019188","730");
        return success("test");
    }

}
