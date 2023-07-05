package cn.iocoder.yudao.module.promotion.convert.seckill.seckillactivity;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityDetailRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.activity.SeckillActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.product.SeckillProductBaseVO;
import cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.product.SeckillProductUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckill.seckillactivity.SeckillActivityDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.seckill.seckillactivity.SeckillProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 秒杀活动 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface SeckillActivityConvert {

    SeckillActivityConvert INSTANCE = Mappers.getMapper(SeckillActivityConvert.class);

    SeckillActivityDO convert(SeckillActivityCreateReqVO bean);

    SeckillActivityDO convert(SeckillActivityUpdateReqVO bean);

    SeckillActivityRespVO convert(SeckillActivityDO bean);

    List<SeckillActivityRespVO> convertList(List<SeckillActivityDO> list);

    PageResult<SeckillActivityRespVO> convertPage(PageResult<SeckillActivityDO> page);

    SeckillActivityDetailRespVO convert(SeckillActivityDO seckillActivity, List<SeckillProductDO> seckillProducts);

    @Mappings({
            @Mapping(target = "activityId", source = "activityDO.id"),
            @Mapping(target = "configIds", source = "activityDO.configIds"),
            @Mapping(target = "spuId", source = "activityDO.spuId"),
            @Mapping(target = "skuId", source = "vo.skuId"),
            @Mapping(target = "seckillPrice", source = "vo.seckillPrice"),
            @Mapping(target = "stock", source = "vo.stock"),
            @Mapping(target = "activityStartTime", source = "activityDO.startTime"),
            @Mapping(target = "activityEndTime", source = "activityDO.endTime")
    })
    SeckillProductDO convert(SeckillActivityDO activityDO, SeckillProductBaseVO vo);

    default List<SeckillProductDO> convertList(SeckillActivityDO activityDO, List<? extends SeckillProductBaseVO> products) {
        List<SeckillProductDO> list = new ArrayList<>();
        products.forEach(sku -> {
            SeckillProductDO productDO = convert(activityDO, sku);
            productDO.setActivityStatus(CommonStatusEnum.ENABLE.getStatus());
            list.add(productDO);
        });
        return list;
    }

    default List<SeckillProductDO> convertList1(SeckillActivityDO activityDO, List<SeckillProductUpdateReqVO> vos, List<SeckillProductDO> productDOs) {
        Map<Long, Long> longMap = CollectionUtils.convertMap(productDOs, SeckillProductDO::getSkuId, SeckillProductDO::getId);
        List<SeckillProductDO> list = new ArrayList<>();
        vos.forEach(sku -> {
            SeckillProductDO productDO = convert(activityDO, sku);
            productDO.setId(longMap.get(sku.getSkuId()));
            productDO.setActivityStatus(CommonStatusEnum.ENABLE.getStatus());
            list.add(productDO);
        });
        return list;
    }

}
