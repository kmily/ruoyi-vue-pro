package cn.iocoder.yudao.module.steam.controller.app.droplist;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.SelExteriorPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.SelItemsetListReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.SelQualityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.SelRarityPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.SelTypePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppDropListRespVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppSellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.ItemResp;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewService;
import cn.iocoder.yudao.module.steam.service.selexterior.SelExteriorService;
import cn.iocoder.yudao.module.steam.service.selitemset.SelItemsetService;
import cn.iocoder.yudao.module.steam.service.selling.SellingService;
import cn.iocoder.yudao.module.steam.service.selling.SellingsearchService;
import cn.iocoder.yudao.module.steam.service.selquality.SelQualityService;
import cn.iocoder.yudao.module.steam.service.selrarity.SelRarityService;
import cn.iocoder.yudao.module.steam.service.seltype.SelTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "获取下拉选择信息")
@RestController
@RequestMapping("steam-app/drop_list")
@Slf4j
@Validated
public class AppDropListController {

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
    private InvPreviewService invPreviewService;
    @Resource
    private SellingService sellingService;
    @Resource
    private SellingsearchService sellingsearchService;

    private InvPreviewExtService invPreviewExtService;
    @Autowired
    public void setInvPreviewExtService(InvPreviewExtService invPreviewExtService) {
        this.invPreviewExtService = invPreviewExtService;
    }

    /**
     * 类别选择
     */
    @GetMapping("/quality")
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
    @GetMapping("/itemSet")
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
    @GetMapping("/type")
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
    @GetMapping("/rarity")
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
    @GetMapping("/weapon")
    @Operation(summary = "获取武器选择下拉信息")
    public CommonResult<AppDropListRespVO> getWeapon(Long typeId) {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelTypePageReqVO type_weapon = new SelTypePageReqVO();
        type_weapon.setPageSize(200);
        type_weapon.setPageNo(1);
//        appDropListRespVO.setWeapon(selTypeService.getSelWeaponPage(type_weapon,typeId).getList());
        return CommonResult.success(appDropListRespVO);
    }

    /**
     * 外观选择
     */
    @GetMapping("/exterior")
    @Operation(summary = "获取外观选择下拉信息")
    public CommonResult<AppDropListRespVO> getExterior() {
        AppDropListRespVO appDropListRespVO = new AppDropListRespVO();
        SelExteriorPageReqVO exterior = new SelExteriorPageReqVO();
        exterior.setPageSize(200);
        exterior.setPageNo(1);
        appDropListRespVO.setExterior(selExteriorService.getSelExteriorPage(exterior).getList());
        return CommonResult.success(appDropListRespVO);
    }

    @GetMapping("/search")
    @Operation(summary = "饰品在售预览")
    public  CommonResult<PageResult<SellingRespVO>> getPreview(SellingPageReqVO sellingReqVo){
        sellingReqVo.setPageSize(200);
        PageResult<SellingRespVO> sellingRespVO = sellingsearchService.sellingPageSearch(sellingReqVo);
        return CommonResult.success(sellingRespVO);
    }

    @GetMapping("/getSearch")
    @Operation(summary = "搜索导航栏")
    public CommonResult<PageResult<SellingDO>> getSearch(SellingPageReqVO sellingReqVo) {
        sellingReqVo.setPageSize(200);
        PageResult<SellingDO> sellingRespVO = sellingsearchService.getSearch(sellingReqVo);
        return CommonResult.success(sellingRespVO);
    }

    @GetMapping("/search/viewSell")
    @Operation(summary = "在售商品列表")
    public CommonResult<PageResult<SellingRespVO>> getSellView(@Valid SellingPageReqVO sellingPageReqVO) {
        PageResult<SellingDO> viewSell = sellingsearchService.sellView(sellingPageReqVO);
        return success(BeanUtils.toBean(viewSell, SellingRespVO.class));
    }

    @GetMapping("items/730/search")
    @Operation(summary = "在售商品列表")
    public CommonResult<PageResult<ItemResp>> itemSearch(@Valid InvPreviewPageReqVO sellingPageReqVO) {
        sellingPageReqVO.setExistInv(true);
        return success(invPreviewExtService.getInvPreviewPage(sellingPageReqVO));
    }
    @GetMapping("sell/list")
    @Operation(summary = "在售商品列表")
    public CommonResult<PageResult<SellingDO>> sellList(@Valid AppSellingPageReqVO reqVO) {
        PageResult<SellingDO> sellingDOPageResult = sellingsearchService.sellList(reqVO);
//        PageResult<ItemResp> itemRespPageResult = BeanUtils.toBean(invPreviewPage, ItemResp.class);
        return success(sellingDOPageResult);
    }

}