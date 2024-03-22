package cn.iocoder.yudao.module.steam.service.invpreview;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.ItemResp;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.PreviewReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * 饰品在售预览 Service 实现类
 *
 * @author LeeAm
 */
@Service
public class InvPreviewExtService {

    @Resource
    private InvPreviewMapper invPreviewMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    InvPreviewService invPreviewService;

    public ItemResp getInvPreview(PreviewReqVO reqVO) {

        Optional<InvPreviewDO> first = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>().eq(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())).stream().findFirst();
        if(!first.isPresent()){
            markInvEnable(reqVO.getMarketHashName());
            Optional<InvPreviewDO> first2 = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>().eq(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())).stream().findFirst();
            if(first2.isPresent()){
                InvPreviewDO invPreviewDO = first2.get();
                ItemResp itemResp = BeanUtils.toBean(invPreviewDO, ItemResp.class);

                if(Objects.nonNull(invPreviewDO.getAutoPrice())){
                    itemResp.setAutoPrice(new BigDecimal(invPreviewDO.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
                }
                if(Objects.nonNull(invPreviewDO.getSalePrice())){
                    itemResp.setSalePrice(new BigDecimal(invPreviewDO.getSalePrice()).multiply(new BigDecimal("100")).intValue());
                }
                if(Objects.nonNull(invPreviewDO.getReferencePrice())){
                    itemResp.setReferencePrice(new BigDecimal(invPreviewDO.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
                }
                return itemResp;
            }
        }
        InvPreviewDO invPreviewDO = first.get();
        ItemResp itemResp = BeanUtils.toBean(invPreviewDO, ItemResp.class);

        if(Objects.nonNull(invPreviewDO.getAutoPrice())){
            itemResp.setAutoPrice(new BigDecimal(invPreviewDO.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
        }
        if(Objects.nonNull(invPreviewDO.getSalePrice())){
            itemResp.setSalePrice(new BigDecimal(invPreviewDO.getSalePrice()).multiply(new BigDecimal("100")).intValue());
        }
        if(Objects.nonNull(invPreviewDO.getReferencePrice())){
            itemResp.setReferencePrice(new BigDecimal(invPreviewDO.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
        }
        return itemResp;
    }

    public PageResult<ItemResp> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        PageResult<InvPreviewDO> invPreviewDOPageResult = invPreviewMapper.selectPage(pageReqVO);
        List<ItemResp> ret=new ArrayList<>();
        for (InvPreviewDO item:invPreviewDOPageResult.getList()){

            ItemResp itemResp = BeanUtils.toBean(item, ItemResp.class);
            if(Objects.nonNull(item.getAutoPrice())){
                itemResp.setAutoPrice(new BigDecimal(item.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getSalePrice())){
                itemResp.setSalePrice(new BigDecimal(item.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getReferencePrice())){
                itemResp.setReferencePrice(new BigDecimal(item.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
            }
            ret.add(itemResp);
        }
        return new PageResult<>(ret, invPreviewDOPageResult.getTotal());
    }
    public PageResult<ItemResp> getHot(InvPreviewPageReqVO pageReqVO) {
        PageResult<InvPreviewDO> invPreviewDOPageResult = invPreviewMapper.hotPage(pageReqVO);
        List<ItemResp> ret=new ArrayList<>();
        for (InvPreviewDO item:invPreviewDOPageResult.getList()){

            ItemResp itemResp = BeanUtils.toBean(item, ItemResp.class);
            if(Objects.nonNull(item.getAutoPrice())){
                itemResp.setAutoPrice(new BigDecimal(item.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getSalePrice())){
                itemResp.setSalePrice(new BigDecimal(item.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getReferencePrice())){
                itemResp.setReferencePrice(new BigDecimal(item.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
            }
            ret.add(itemResp);
        }
        return new PageResult<>(ret, invPreviewDOPageResult.getTotal());
    }
    /**
     * 增加库存标识,上架构和下架构 都可以进行调用
     * @param marketHashName 标签名称
     */
    public void markInvEnable(String marketHashName) {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getMarketHashName, marketHashName));
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(1);
        PageResult<SellingDO> sellingDOPageResult = sellingMapper.selectPage(pageParam, new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName, marketHashName)
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .orderByAsc(SellingDO::getPrice)
        );
        Optional<SellingDO> sellingDOOptional = sellingDOPageResult.getList().stream().findFirst();


        if(Objects.nonNull(invPreviewDOS) && invPreviewDOS.size()>0){
            invPreviewDOS.forEach(item->{
                C5ItemInfo itemInfo = item.getItemInfo();
                invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(sellingDOPageResult.getTotal()>0).setAutoQuantity(sellingDOPageResult.getTotal().toString())
                        .setMinPrice(sellingDOOptional.isPresent()?sellingDOOptional.get().getPrice():-1)
                        .setSelExterior(itemInfo.getExteriorName())
                        .setSelQuality(itemInfo.getQualityName())
                        .setSelRarity(itemInfo.getRarityName())
                        .setSelWeapon(itemInfo.getWeaponName())
                        .setSelType(itemInfo.getTypeName())
                        .setSelItemset(itemInfo.getItemSetName()));
            });
        }else{
            initPreView(marketHashName, sellingDOOptional,sellingDOPageResult.getTotal());
        }
    }

    /**
     * preview不存在的时候自动更新
     * @param marketHashName
     * @param sellingDOOptional
     * @param total
     */
    private void initPreView(String marketHashName,Optional<SellingDO> sellingDOOptional,Long total){
        if(Objects.isNull(marketHashName)){
            return;
        }
        Optional<InvDescDO> first = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>().eq(InvDescDO::getMarketHashName, marketHashName)).stream().findFirst();
        if(first.isPresent()){
            InvDescDO invDescDO = first.get();
            InvPreviewDO invPreviewDO=new InvPreviewDO();
            C5ItemInfo c5ItemInfo = new C5ItemInfo();

            //磨损
            Map<String, Pair<String, String>> exteriorMap = new HashMap<>();
            exteriorMap.put("WearCategoryNA", new MutablePair<>("无涂装", ""));
            exteriorMap.put("WearCategory0", new MutablePair<>("崭新出厂", "#228149"));
            exteriorMap.put("WearCategory1", new MutablePair<>("略有磨损", "#64b02b"));
            exteriorMap.put("WearCategory2", new MutablePair<>("久经沙场", "#efad4d"));
            exteriorMap.put("WearCategory3", new MutablePair<>("破损不堪", "#b7625f"));
            exteriorMap.put("WearCategory4", new MutablePair<>("战痕累累", "#993a38"));


            String selExterior = invDescDO.getSelExterior();
            Pair<String, String> exteriorInfo = exteriorMap.get(selExterior);
            if (exteriorInfo != null) {
                c5ItemInfo.setExteriorName(exteriorInfo.getLeft());
                c5ItemInfo.setExterior(exteriorInfo.getKey());
                invPreviewDO.setSelExterior(exteriorInfo.getKey());
                c5ItemInfo.setExteriorColor(exteriorInfo.getRight());
            } else {
                c5ItemInfo.setExteriorName("");
                c5ItemInfo.setExterior("");
                c5ItemInfo.setExteriorColor("");
            }

            //类别
            Map<String, Pair<String, String>> qualityMap = new HashMap<>();
            qualityMap.put("normal", new MutablePair<>("普通", ""));
            qualityMap.put("strange", new MutablePair<>("StatTrak™", "#cf6a32"));
            qualityMap.put("tournament", new MutablePair<>("纪念品", "#ffb100"));
            qualityMap.put("unusual", new MutablePair<>("★", "#8650ac"));
            qualityMap.put("unusual_strange", new MutablePair<>("★ StatTrak™", "#8650ac"));

            String selQuality = invDescDO.getSelQuality();
            Pair<String, String> qualityInfo = qualityMap.get(selQuality);
            if (qualityInfo != null) {
                c5ItemInfo.setQualityName(qualityInfo.getLeft());
                c5ItemInfo.setQuality(qualityInfo.getLeft());
                invPreviewDO.setSelQuality(qualityInfo.getKey());
                c5ItemInfo.setQualityColor(qualityInfo.getRight());
            } else {
                c5ItemInfo.setQualityName("");
                c5ItemInfo.setQuality("");
                c5ItemInfo.setQualityColor("");
            }
            //品质
            Map<String, Pair<String, String>> rarityMap = new HashMap<>();
            rarityMap.put("Rarity_Rare_Weapon", new MutablePair<>("军规级", "#4b69ff"));
            rarityMap.put("Rarity_Legendary_Weapon", new MutablePair<>("保密", "#d32ce6"));
            rarityMap.put("Rarity_Ancient_Weapon", new MutablePair<>("隐秘", "#f07373"));
            rarityMap.put("Rarity_Mythical_Weapon", new MutablePair<>("受限", "#8847ff"));
            rarityMap.put("Rarity_Uncommon_Weapon", new MutablePair<>("工业级", "#5e98d9"));
            rarityMap.put("Rarity_Common_Weapon", new MutablePair<>("消费级", "#90accc"));
            rarityMap.put("Rarity_Ancient", new MutablePair<>("非凡", "#eb4b4b"));
            rarityMap.put("Rarity_Mythical", new MutablePair<>("卓越", "#8847ff"));
            rarityMap.put("Rarity_Legendary", new MutablePair<>("奇异", "#d32ce6"));
            rarityMap.put("Rarity_Rare", new MutablePair<>("高级", "#4b69ff"));
            rarityMap.put("Rarity_Common", new MutablePair<>("普通级", "#90ACCC"));
            rarityMap.put("Rarity_Contraband", new MutablePair<>("违禁", "#E4AE39"));
            rarityMap.put("Rarity_Ancient_Character", new MutablePair<>("探员：大师", "#EB4B4B"));
            rarityMap.put("Rarity_Legendary_Character", new MutablePair<>("探员：非凡", "#D32CE6"));
            rarityMap.put("Rarity_Mythical_Character", new MutablePair<>("探员：卓越", "#8847FF"));
            rarityMap.put("Rarity_Rare_Character", new MutablePair<>("探员：高级", "#4B69FF"));

            String selRarity = invDescDO.getSelRarity();
            Pair<String, String> rarityInfo = rarityMap.get(selRarity);
            if (rarityInfo != null) {
                c5ItemInfo.setRarityName((rarityInfo.getLeft()));
                c5ItemInfo.setRarity(rarityInfo.getLeft());
                invPreviewDO.setSelRarity(rarityInfo.getKey());
                c5ItemInfo.setRarityColor(rarityInfo.getRight());
            } else {
                c5ItemInfo.setRarityName("");
                c5ItemInfo.setRarity("");
                c5ItemInfo.setRarityColor("");
            }

            c5ItemInfo.setItemSetName(invDescDO.getSelItemset());
            c5ItemInfo.setTypeName(invDescDO.getSelType());
            c5ItemInfo.setWeaponName(invDescDO.getSelWeapon());


            invPreviewDO.setMinPrice(sellingDOOptional.isPresent()?sellingDOOptional.get().getPrice(): -1).setExistInv(total>0)
                    .setAutoQuantity(total.toString())
                    .setMarketHashName(marketHashName)
                    .setImageUrl(invDescDO.getIconUrl())
                    .setItemName(invDescDO.getMarketName())
                    .setItemId(System.currentTimeMillis())
                    .setAutoPrice(String.valueOf(sellingDOOptional.isPresent()?sellingDOOptional.get().getPrice(): -1))
                    .setItemInfo(c5ItemInfo);


            invPreviewDO.setReferencePrice(new BigDecimal(invPreviewDO.getMinPrice()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP).toString());
/*
            invPreviewDO.setMinPrice(Integer.valueOf(new BigDecimal(invPreviewDO.getMinPrice()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP).toString()));
*/
            invPreviewMapper.insert(invPreviewDO);
        }

    }
    @Async
    public Integer updateIvnFlag() {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getExistInv, true));
        Integer count=0;
        if(Objects.nonNull(invPreviewDOS)){
            for(InvPreviewDO item:invPreviewDOS){
                count++;
                PageParam pageParam = new PageParam();
                pageParam.setPageNo(1);
                pageParam.setPageSize(1);
                PageResult<SellingDO> sellingDOPageResult = sellingMapper.selectPage(pageParam, new LambdaQueryWrapperX<SellingDO>()
                        .eq(SellingDO::getMarketHashName, item.getMarketHashName())
                        .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                        .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                        .orderByAsc(SellingDO::getPrice)
                );
                Optional<SellingDO> sellingDOOptional = sellingDOPageResult.getList().stream().findFirst();
                C5ItemInfo itemInfo = item.getItemInfo();
                invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(sellingDOPageResult.getTotal()>0).setAutoQuantity(sellingDOPageResult.getTotal().toString())
                        .setMinPrice(sellingDOOptional.isPresent()?sellingDOOptional.get().getPrice():-1)
                        .setSelExterior(itemInfo.getExteriorName())
                        .setSelQuality(itemInfo.getQualityName())
                        .setSelRarity(itemInfo.getRarityName())
                        .setSelWeapon(itemInfo.getWeaponName())
                        .setSelType(itemInfo.getTypeName())
                        .setSelItemset(itemInfo.getItemSetName()));
            }
        }
        return count;
    }
}