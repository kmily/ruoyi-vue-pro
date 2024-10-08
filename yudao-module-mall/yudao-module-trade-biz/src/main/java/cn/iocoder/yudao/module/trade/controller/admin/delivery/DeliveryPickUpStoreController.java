package cn.iocoder.yudao.module.trade.controller.admin.delivery;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.delivery.vo.pickup.*;
import cn.iocoder.yudao.module.trade.convert.delivery.DeliveryPickUpStoreConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryPickUpStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 自提门店")
@RestController
@RequestMapping("/trade/delivery/pick-up-store")
@Validated
public class DeliveryPickUpStoreController {

    @Resource
    private DeliveryPickUpStoreService deliveryPickUpStoreService;

    @PostMapping("/create")
    @Operation(summary = "创建自提门店")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:create')")
    public CommonResult<Long> createDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpStoreCreateReqVO createReqVO) {
        return success(deliveryPickUpStoreService.createDeliveryPickUpStore(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新自提门店")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:update')")
    public CommonResult<Boolean> updateDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpStoreUpdateReqVO updateReqVO) {
        deliveryPickUpStoreService.updateDeliveryPickUpStore(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自提门店")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:delete')")
    public CommonResult<Boolean> deleteDeliveryPickUpStore(@RequestParam("id") Long id) {
        deliveryPickUpStoreService.deleteDeliveryPickUpStore(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得自提门店")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<DeliveryPickUpStoreRespVO> getDeliveryPickUpStore(@RequestParam("id") Long id) {
        DeliveryPickUpStoreDO deliveryPickUpStore = deliveryPickUpStoreService.getDeliveryPickUpStore(id);
        return success(DeliveryPickUpStoreConvert.INSTANCE.convert(deliveryPickUpStore));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得自提门店精简信息列表")
    public CommonResult<List<DeliveryPickUpStoreSimpleRespVO>> getSimpleDeliveryPickUpStoreList() {
        List<DeliveryPickUpStoreDO> storeStaffDOS = deliveryPickUpStoreService.selectStaffByUserId(getLoginUserId());
        List<Long> storeIds = storeStaffDOS.stream().map(DeliveryPickUpStoreDO::getId).toList();
        if(!storeIds.isEmpty()){
            List<DeliveryPickUpStoreDO> list = deliveryPickUpStoreService.getDeliveryPickUpStoreListByStatus(
                    CommonStatusEnum.ENABLE.getStatus(), storeIds);
            return success(DeliveryPickUpStoreConvert.INSTANCE.convertList1(list));
        }else{
            return success(new ArrayList<>());
        }

    }

    @GetMapping("/list")
    @Operation(summary = "获得自提门店列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<List<DeliveryPickUpStoreRespVO>> getDeliveryPickUpStoreList(@RequestParam("ids") Collection<Long> ids) {
        List<DeliveryPickUpStoreDO> list = deliveryPickUpStoreService.getDeliveryPickUpStoreList(ids);
        return success(DeliveryPickUpStoreConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得自提门店分页")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<PageResult<DeliveryPickUpStoreRespVO>> getDeliveryPickUpStorePage(@Valid DeliveryPickUpStorePageReqVO pageVO) {
        PageResult<DeliveryPickUpStoreDO> pageResult = deliveryPickUpStoreService.getDeliveryPickUpStorePage(pageVO);
        return success(DeliveryPickUpStoreConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定自提店员")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:create')")
    public CommonResult<Boolean> bindDeliveryPickUpBindStoreStaffId(@Valid @RequestBody DeliveryPickUpBindStoreStaffIdReqVO bindStoreStaffIdVO) {
        deliveryPickUpStoreService.bindDeliveryPickUpBindStoreStaffId(bindStoreStaffIdVO);
        return success(true);
    }

    @GetMapping("/get-store-staff")
    @Operation(summary = "查询门店绑定情况")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<DeliveryPickUpBindStoreStaffIdReqsVO> getDeliveryPickUpStoreStaff(@RequestParam("id") Long id) {
        return success(deliveryPickUpStoreService.getDeliveryPickUpStoreStaff(id));
    }


}
