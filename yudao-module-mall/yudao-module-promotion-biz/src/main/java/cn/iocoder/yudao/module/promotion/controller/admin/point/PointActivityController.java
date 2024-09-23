package cn.iocoder.yudao.module.promotion.controller.admin.point;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.product.api.spu.ProductSpuApi;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.activity.PointActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.activity.PointActivityRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.activity.PointActivitySaveReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.point.vo.product.PointProductRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.point.PointActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.point.PointProductDO;
import cn.iocoder.yudao.module.promotion.service.point.PointActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 积分商城活动")
@RestController
@RequestMapping("/promotion/point-activity")
@Validated
public class PointActivityController {

    @Resource
    private PointActivityService pointActivityService;
    @Resource
    private ProductSpuApi productSpuApi;

    @PostMapping("/create")
    @Operation(summary = "创建积分商城活动")
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:create')")
    public CommonResult<Long> createPointActivity(@Valid @RequestBody PointActivitySaveReqVO createReqVO) {
        return success(pointActivityService.createPointActivity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新积分商城活动")
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:update')")
    public CommonResult<Boolean> updatePointActivity(@Valid @RequestBody PointActivitySaveReqVO updateReqVO) {
        pointActivityService.updatePointActivity(updateReqVO);
        return success(true);
    }

    @PutMapping("/close")
    @Operation(summary = "关闭积分商城活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:close')")
    public CommonResult<Boolean> closeSeckillActivity(@RequestParam("id") Long id) {
        pointActivityService.closePointActivity(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除积分商城活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:delete')")
    public CommonResult<Boolean> deletePointActivity(@RequestParam("id") Long id) {
        pointActivityService.deletePointActivity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得积分商城活动")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:query')")
    public CommonResult<PointActivityRespVO> getPointActivity(@RequestParam("id") Long id) {
        PointActivityDO pointActivity = pointActivityService.getPointActivity(id);
        if (pointActivity == null) {
            return success(null);
        }

        List<PointProductDO> products = pointActivityService.getPointProductListByActivityIds(Collections.singletonList(id));
        PointActivityRespVO respVO = BeanUtils.toBean(pointActivity, PointActivityRespVO.class);
        respVO.setProducts(BeanUtils.toBean(products, PointProductRespVO.class));
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得积分商城活动分页")
    @PreAuthorize("@ss.hasPermission('promotion:point-activity:query')")
    public CommonResult<PageResult<PointActivityRespVO>> getPointActivityPage(@Valid PointActivityPageReqVO pageReqVO) {
        PageResult<PointActivityDO> pageResult = pointActivityService.getPointActivityPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        // 拼接数据
        List<PointProductDO> products = pointActivityService.getPointProductListByActivityIds(
                convertSet(pageResult.getList(), PointActivityDO::getId));
        Map<Long, List<PointProductDO>> productsMap = convertMultiMap(products, PointProductDO::getActivityId);
        Map<Long, ProductSpuRespDTO> spuMap = productSpuApi.getSpusMap(
                convertSet(pageResult.getList(), PointActivityDO::getSpuId));
        PageResult<PointActivityRespVO> result = BeanUtils.toBean(pageResult, PointActivityRespVO.class);
        result.getList().forEach(activity -> {
            activity.setProducts(BeanUtils.toBean(productsMap.get(activity.getId()), PointProductRespVO.class));
            MapUtils.findAndThen(spuMap, activity.getSpuId(),
                    spu -> activity.setSpuName(spu.getName()).setPicUrl(spu.getPicUrl()).setMarketPrice(spu.getMarketPrice()));

        });
        return success(result);
    }

}