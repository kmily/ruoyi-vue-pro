package cn.iocoder.yudao.module.steam.service.invpreview;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.ItemResp;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.PreviewReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.hotwords.HotWordsMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapperExt;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.selling.SellingHotDO;
import cn.iocoder.yudao.module.steam.service.selling.SellingService;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private HotWordsMapper hotWordsMapper;
    @Resource
    private InvPreviewMapperExt invPreviewMapperExt;

    @Resource
    private SellingService sellingService;


    public ItemResp getInvPreview(PreviewReqVO reqVO) {

        Optional<InvPreviewDO> first = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>().eq(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())).stream().findFirst();
        if (!first.isPresent()) {
            markInvEnable(reqVO.getMarketHashName());
            Optional<InvPreviewDO> first2 = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>().eq(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())).stream().findFirst();
            if (first2.isPresent()) {
                InvPreviewDO invPreviewDO = first2.get();
                ItemResp itemResp = BeanUtils.toBean(invPreviewDO, ItemResp.class);

                if (Objects.nonNull(invPreviewDO.getAutoPrice())) {
                    itemResp.setAutoPrice(new BigDecimal(invPreviewDO.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
                }
                if (Objects.nonNull(invPreviewDO.getSalePrice())) {
                    itemResp.setSalePrice(new BigDecimal(invPreviewDO.getSalePrice()).multiply(new BigDecimal("100")).intValue());
                }
                if (Objects.nonNull(invPreviewDO.getReferencePrice())) {
                    itemResp.setReferencePrice(new BigDecimal(invPreviewDO.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
                }
                return itemResp;
            }
        }
        InvPreviewDO invPreviewDO = first.get();
        ItemResp itemResp = BeanUtils.toBean(invPreviewDO, ItemResp.class);

        if (Objects.nonNull(invPreviewDO.getAutoPrice())) {
            itemResp.setAutoPrice(new BigDecimal(invPreviewDO.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
        }
        if (Objects.nonNull(invPreviewDO.getSalePrice())) {
            itemResp.setSalePrice(new BigDecimal(invPreviewDO.getSalePrice()).multiply(new BigDecimal("100")).intValue());
        }
        if (Objects.nonNull(invPreviewDO.getReferencePrice())) {
            itemResp.setReferencePrice(new BigDecimal(invPreviewDO.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
        }
        return itemResp;
    }

    public PageResult<ItemResp> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        HotWordsDO hotWordsDO = hotWordsMapper.selectOne(new LambdaQueryWrapper<HotWordsDO>()
                .eq(HotWordsDO::getHotWords, pageReqVO.getItemName()));

        if (hotWordsDO != null) {
            pageReqVO.setItemName(hotWordsDO.getMarketName());
        }

        PageResult<InvPreviewDO> invPreviewDOPageResult = invPreviewMapperExt.selectPage(pageReqVO);
        List<ItemResp> ret = new ArrayList<>();
        for (InvPreviewDO item : invPreviewDOPageResult.getList()) {

            ItemResp itemResp = BeanUtils.toBean(item, ItemResp.class);
            if (Objects.nonNull(item.getAutoPrice())) {
                itemResp.setAutoPrice(new BigDecimal(item.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            }
            if (Objects.nonNull(item.getSalePrice())) {
                itemResp.setSalePrice(new BigDecimal(item.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            }
            if (Objects.nonNull(item.getReferencePrice())) {
                itemResp.setReferencePrice(new BigDecimal(item.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
            }
            ret.add(itemResp);
        }
        String sort = pageReqVO.getSort();
        if(sort != null && !sort.equals("0")){
            if (sort.equals("1")){
                List<ItemResp> sortedList = ret.stream()
                        .sorted((o1, o2) -> Double.compare(o2.getAutoPrice(), o1.getAutoPrice())) // 根据 AutoPrice 字段降序排序
                        .collect(Collectors.toList());
                return new PageResult<>(sortedList, invPreviewDOPageResult.getTotal());
            }
            if (sort.equals("2")){
                List<ItemResp> sortedList = ret.stream()
                        .sorted(Comparator.comparingDouble(ItemResp::getAutoPrice)) // 根据 AutoPrice 字段升序排序
                        .collect(Collectors.toList());
                return new PageResult<>(sortedList, invPreviewDOPageResult.getTotal());
            }
        }

        return new PageResult<>(ret, invPreviewDOPageResult.getTotal());
    }

    public PageResult<SellingHotDO> getHot(SellingPageReqVO pageReqVO) {
        // 获取所有数据
        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>().isNotNull(SellingDO::getDisplayWeight));

        // 按 display_weight 字段进行排序,数字越小权重越大
        List<SellingDO> sortedList = sellingDOS.parallelStream()
                .sorted(Comparator.comparingInt(SellingDO::getDisplayWeight))
                .collect(Collectors.toList());

        // 分页处理
        int pageSize = pageReqVO.getPageSize();
        int pageNum = pageReqVO.getPageNo();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, sortedList.size());
        List<SellingDO> resultList = sortedList.subList(startIndex, endIndex);


        List<SellingHotDO> sellingHotDOList = new ArrayList<>();

        Map<String, Pair<String, String>> exteriorMap = new HashMap<>();
        exteriorMap.put("WearCategoryNA", new MutablePair<>("无涂装", ""));
        exteriorMap.put("WearCategory0", new MutablePair<>("崭新出厂", "#228149"));
        exteriorMap.put("WearCategory1", new MutablePair<>("略有磨损", "#64b02b"));
        exteriorMap.put("WearCategory2", new MutablePair<>("久经沙场", "#efad4d"));
        exteriorMap.put("WearCategory3", new MutablePair<>("破损不堪", "#b7625f"));
        exteriorMap.put("WearCategory4", new MutablePair<>("战痕累累", "#993a38"));

        Map<String, Pair<String, String>> qualityMap = new HashMap<>();
        qualityMap.put("normal", new MutablePair<>("普通", ""));
        qualityMap.put("strange", new MutablePair<>("StatTrak™", "#cf6a32"));
        qualityMap.put("tournament", new MutablePair<>("纪念品", "#ffb100"));
        qualityMap.put("unusual", new MutablePair<>("★", "#8650ac"));
        qualityMap.put("unusual_strange", new MutablePair<>("★ StatTrak™", "#8650ac"));

        // 统计每个 market_hash_name 的数量和最低价格
        Map<String, Integer> sellNumMap = new HashMap<>();
        for (SellingDO item : sellingDOS) {
            String marketHashName = item.getMarketHashName();
            int sellNum = sellNumMap.getOrDefault(marketHashName, 0) + 1;
            sellNumMap.put(marketHashName, sellNum);

        }

        for (SellingDO item : resultList) {
            SellingHotDO sellingHotDO = new SellingHotDO();
            C5ItemInfo c5ItemInfo = new C5ItemInfo();

            String selExterior = item.getSelExterior();
            Pair<String, String> exteriorInfo = exteriorMap.get(selExterior);
            if (exteriorInfo != null) {
                c5ItemInfo.setExteriorName(exteriorInfo.getLeft());
                c5ItemInfo.setExterior(exteriorInfo.getKey());
                sellingHotDO.setSelExterior(exteriorInfo.getKey());
                c5ItemInfo.setExteriorColor(exteriorInfo.getRight());
            } else {
                c5ItemInfo.setExteriorName("");
                c5ItemInfo.setExterior("");
                c5ItemInfo.setExteriorColor("");
            }

            String selQuality = item.getSelQuality();
            Pair<String, String> qualityInfo = qualityMap.get(selQuality);
            if (qualityInfo != null) {
                c5ItemInfo.setQualityName(qualityInfo.getLeft());
                c5ItemInfo.setQuality(qualityInfo.getKey());
                sellingHotDO.setSelQuality(qualityInfo.getKey());
                c5ItemInfo.setQualityColor(qualityInfo.getRight());
            } else {
                c5ItemInfo.setQualityName("");
                c5ItemInfo.setQuality("");
                c5ItemInfo.setQualityColor("");
            }

            sellingHotDO.setIconUrl(item.getIconUrl());
            sellingHotDO.setPrice(item.getPrice());
            sellingHotDO.setItemInfo(c5ItemInfo);
            sellingHotDO.setDisplayWeight(item.getDisplayWeight());
            sellingHotDO.setMarketName(item.getMarketName());
            sellingHotDO.setMarketHashName(item.getMarketHashName());
            sellingHotDO.setSellNum(sellNumMap.get(item.getMarketHashName()));
            sellingHotDO.setShortName(item.getShortName());

            sellingHotDOList.add(sellingHotDO);
        }

        long totalCount = sellingDOS.size();
        return new PageResult<>(sellingHotDOList, totalCount);
    }
    /**
     * 增加库存标识,上架构和下架构 都可以进行调用
     *
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


        if (Objects.nonNull(invPreviewDOS) && invPreviewDOS.size() > 0) {
            invPreviewDOS.forEach(item -> {
                C5ItemInfo itemInfo = item.getItemInfo();
                invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(sellingDOPageResult.getTotal() > 0).setAutoQuantity(sellingDOPageResult.getTotal().toString())
                        .setMinPrice(sellingDOOptional.isPresent() ? sellingDOOptional.get().getPrice() : -1)
                        .setSelExterior(itemInfo.getExteriorName())
                        .setSelQuality(itemInfo.getQualityName())
                        .setSelRarity(itemInfo.getRarityName())
                        .setSelWeapon(itemInfo.getWeaponName())
                        .setSelType(itemInfo.getTypeName())
                        .setSelItemset(itemInfo.getItemSetName()));
            });
        } else {
            initPreView(marketHashName, sellingDOOptional, sellingDOPageResult.getTotal());
        }
    }

    /**
     * preview不存在的时候自动更新
     *
     * @param marketHashName
     * @param sellingDOOptional
     * @param total
     */
    private void initPreView(String marketHashName, Optional<SellingDO> sellingDOOptional, Long total) {
        if (Objects.isNull(marketHashName)) {
            return;
        }
        Optional<InvDescDO> first = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>().eq(InvDescDO::getMarketHashName, marketHashName)).stream().findFirst();
        if (first.isPresent()) {
            InvDescDO invDescDO = first.get();
            InvPreviewDO invPreviewDO = new InvPreviewDO();
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
                c5ItemInfo.setQuality(qualityInfo.getKey());
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
                c5ItemInfo.setRarity(rarityInfo.getKey());
                invPreviewDO.setSelRarity(rarityInfo.getKey());
                c5ItemInfo.setRarityColor(rarityInfo.getRight());
            } else {
                c5ItemInfo.setRarityName("");
                c5ItemInfo.setRarity("");
                c5ItemInfo.setRarityColor("");
            }

/*
            // 样式
            Map<String, String> typeMap = new HashMap<>();
            typeMap.put("CSGO_Type_Knife", "匕首");
            typeMap.put("CSGO_Type_Rifle", "步枪");
            typeMap.put("CSGO_Type_Pistol", "手枪");
            typeMap.put("CSGO_Type_Hands", "手套");
            typeMap.put("CSGO_Type_SMG", "微型冲锋枪");
            typeMap.put("CSGO_Type_Shotgun", "霰弹枪");
            typeMap.put("CSGO_Type_Machinegun", "机枪");
            typeMap.put("CSGO_Type_Sticker", "印花");
            typeMap.put("CSGO_Type_Other", "其他");
            typeMap.put("CSGO_Type_Customplayer", "探员");

            String selType = invDescDO.getSelType();
            List<String> typeList = new ArrayList<>(typeMap.keySet());

            if (!typeList.isEmpty()) {
                c5ItemInfo.setTypeName(typeMap.get(selType));
                c5ItemInfo.setType(selType);
                invPreviewDO.setSelType(selType);
            } else {
                c5ItemInfo.setTypeName("");
                c5ItemInfo.setType("");
            }

            // 收藏品
            //TODO 收藏品
            Map<String, String> itemSetMap = new HashMap<>();
            itemSetMap.put("weaponCollection", "武器收藏品");
            itemSetMap.put("mapCollection", "地图收藏品");

            String selItemSet = invDescDO.getSelItemset();

            if (selItemSet != null) {
                c5ItemInfo.setItemSetName(itemSetMap.get(selItemSet));
                c5ItemInfo.setItemSet(selItemSet);
                invPreviewDO.setSelItemset(selItemSet);
            } else {
                c5ItemInfo.setItemSetName("");
                c5ItemInfo.setItemSet("");
            }

            // 武器
            Map<String, String> weaponMap = new HashMap<>();
            weaponMap.put("weapon_knife_butterfly", "蝴蝶刀");
            weaponMap.put("weapon_knife_karambit", "爪子刀");
            weaponMap.put("weapon_knife_m9_bayonet", "M9 刺刀");
            weaponMap.put("weapon_knife_skeleton", "骷髅匕首");
            weaponMap.put("weapon_bayonet", "刺刀");
            weaponMap.put("weapon_knife_flip", "折叠刀");
            weaponMap.put("weapon_knife_stiletto", "短剑");
            weaponMap.put("weapon_knife_widowmaker", "锯齿爪刀");
            weaponMap.put("weapon_knife_outdoor", "流浪者匕首");
            weaponMap.put("weapon_knife_ursus", "熊刀");
            weaponMap.put("weapon_knife_css", "海报短刀");
            weaponMap.put("weapon_knife_tactical", "猎杀者匕首");
            weaponMap.put("weapon_knife_cord", "系绳匕首");
            weaponMap.put("weapon_knife_canis", "求生匕首");
            weaponMap.put("weapon_knife_falchion", "弯刀");
            weaponMap.put("weapon_knife_push", "暗影双匕");
            weaponMap.put("weapon_knife_survival_bowie", "鲍伊猎刀");
            weaponMap.put("weapon_knife_gut", "穿肠刀");
            weaponMap.put("weapon_gypsy_jackknife", "折刀");
            weaponMap.put("weapon_knife_kukri", "廓尔喀刀");

            weaponMap.put("Sport", "运动手套");
            weaponMap.put("Specialist", "专业手套");
            weaponMap.put("Moto", "摩托手套");
            weaponMap.put("Driver", "驾驶手套");
            weaponMap.put("Hand", "裹手");
            weaponMap.put("Broken", "狂牙手套");
            weaponMap.put("Hydra", "九头蛇手套");
            weaponMap.put("Bloodhound", "血猎手套");

            weaponMap.put("weapon_ak47", "AK-47");
            weaponMap.put("weapon_awp", "AWP");
            weaponMap.put("weapon_m4a1_silencer", "M4A1 消音型");
            weaponMap.put("weapon_m4a1", "M4A4");
            weaponMap.put("weapon_galilar", "加利尔 AR");
            weaponMap.put("weapon_famas", "法玛斯");
            weaponMap.put("weapon_ssg08", "SSG 08");
            weaponMap.put("weapon_aug", "AUG");
            weaponMap.put("weapon_sg556", "SG 553");
            weaponMap.put("weapon_scar20", "SCAR-20");
            weaponMap.put("weapon_g3sg1", "G3SG1");
            weaponMap.put("weapon_deagle", "沙漠之鹰");
            weaponMap.put("weapon_usp_silencer", "USP 消音版");
            weaponMap.put("weapon_glock", "格洛克 18 型");
            weaponMap.put("weapon_tec9", "Tec-9");
            weaponMap.put("weapon_fiveseven", "FN57");
            weaponMap.put("weapon_p250", "P250");
            weaponMap.put("weapon_elite", "双持贝瑞塔");
            weaponMap.put("weapon_cz75a", "CZ75 自动手枪");
            weaponMap.put("weapon_revolver", "R8 左轮手枪");
            weaponMap.put("weapon_hkp2000", "P2000");
            weaponMap.put("weapon_taser", "电击枪");
            weaponMap.put("weapon_mp9", "MP9");
            weaponMap.put("weapon_mac10", "MAC-10");
            weaponMap.put("weapon_p90", "P90");
            weaponMap.put("weapon_ump45", "UMP-45");
            weaponMap.put("weapon_mp7", "MP7");
            weaponMap.put("weapon_bizon", "PP-野牛");
            weaponMap.put("weapon_mp5sd", "MP5-SD");
            weaponMap.put("weapon_mag7", "MAG-7");
            weaponMap.put("weapon_xm1014", "XM1014");
            weaponMap.put("weapon_sawedoff", "截短霰弹枪");
            weaponMap.put("weapon_nova", "新星");
            weaponMap.put("weapon_negev", "内格夫");
            weaponMap.put("weapon_m249", "M249");

            weaponMap.put("crate_signature_pack_paris2023_group_players_collection", "23巴黎");
            weaponMap.put("crate_sticker_pack_community2022_capsule_lootlist", "22里约");
            weaponMap.put("crate_signature_pack_rio2022_group_players_collection", "10周年");
            weaponMap.put("crate_sticker_pack_csgo10_capsule_lootlist", "21斯德哥尔摩");
            weaponMap.put("crate_sticker_pack_stockh2021_challengers_collection", "作战室印花");
            weaponMap.put("crate_sticker_pack_bf2042_capsule_lootlist", "战地2042");
            weaponMap.put("crate_signature_pack_antwerp2022_group_players_collection", "22安特卫普");
            weaponMap.put("crate_sticker_pack_riptide_surfshop_lootlist", "激流冲浪店");
            weaponMap.put("crate_sticker_pack_op_riptide_capsule_lootlist", "激流大行动");
            weaponMap.put("crate_sticker_pack_community2021_capsule_lootlist", "2021 社区");
            weaponMap.put("crate_sticker_pack_poorly_drawn_lootlist", "绘制不佳");
            weaponMap.put("crate_sticker_pack_rmr2020_challengers_collection", "2020 RMR");
            weaponMap.put("crate_sticker_pack_broken_fang_lootlist", "狂牙");
            weaponMap.put("crate_sticker_pack_recoil_lootlist", "压枪印花");
            weaponMap.put("crate_sticker_pack_warhammer_lootlist", "战锤40k");
            weaponMap.put("crate_sticker_pack_hlalyx_capsule_lootlist", "半衰期：alyx");
            weaponMap.put("crate_sticker_pack_halo_capsule_lootlist", "光环");
            weaponMap.put("crate_sticker_pack_shattered_web_lootlist", "裂网");
            weaponMap.put("crate_sticker_pack_cs20_capsule_lootlist", "CS20");
            weaponMap.put("crate_sticker_pack_berlin2019_legends_collection", "19柏林");
            weaponMap.put("crate_sticker_pack_chicken_capsule_lootlist", "小鸡");
            weaponMap.put("crate_sticker_pack_feral_predators_capsule_lootlist", "猛兽");
            weaponMap.put("crate_signature_pack_katowice2019_group_players_collection", "19卡托");
            weaponMap.put("crate_sticker_pack_skillgroup_capsule_lootlist", "段位印花");
            weaponMap.put("crate_signature_pack_london2018_group_players_collection", "18伦敦");
            weaponMap.put("crate_sticker_pack_boston2018_legends_collection", "18波士顿");
            weaponMap.put("crate_sticker_pack_krakow2017_legends_collection", "17年克拉科夫");
            weaponMap.put("crate_sticker_pack_atlanta2017_legends_collection", "17年亚特兰大");
            weaponMap.put("crate_sticker_pack_cologne2016_challengers_collection", "16年科隆");
            weaponMap.put("crate_signature_pack_columbus2016_group_players_collection", "16年哥伦布");
            weaponMap.put("crate_sticker_pack_cluj2015_legends_collection", "15年 DreamHack");
            weaponMap.put("crate_signature_pack_eslcologne2015_group_players_collection", "15科隆");
            weaponMap.put("crate_sticker_pack_eslkatowice2015_02_collection", "15卡托");
            weaponMap.put("crate_sticker_pack_dhw2014_02", "14年 DreamHack");
            weaponMap.put("crate_sticker_pack_cologne2014_02", "14科隆");
            weaponMap.put("crate_sticker_pack_kat2014_02", "14卡托");
            weaponMap.put("crate_sticker_pack_comm2018_01_capsule_lootlist", "18年社区");
            weaponMap.put("crate_sticker_pack01", "1号印花");
            weaponMap.put("crate_sticker_pack02", "2 号印花");
            weaponMap.put("crate_sticker_pack_enfu_capsule_lootlist", "Enfu");
            weaponMap.put("crate_sticker_pack_illuminate_capsule_01_lootlist", "中国风1");
            weaponMap.put("crate_sticker_pack_illuminate_capsule_02_lootlist", "中国风2");
            weaponMap.put("crate_sticker_pack_community01", "1 号社区");
            weaponMap.put("crate_sticker_pack_bestiary_capsule_lootlist", "动物寓言");
            weaponMap.put("crate_sticker_pack_slid3_capsule_lootlist", "Slid3");
            weaponMap.put("crate_sticker_pack_sugarface_capsule_lootlist", "糖果脸谱");
            weaponMap.put("crate_sticker_pack_pinups_capsule_lootlist", "海报女郎");
            weaponMap.put("crate_sticker_pack_team_roles_capsule_lootlist", "团队定位");
            weaponMap.put("other_sticker", "其他");

            weaponMap.put("CSGO_Type_WeaponCase", "武器箱");
            weaponMap.put("CSGO_Type_MusicKit", "音乐盒");
            weaponMap.put("CSGO_Type_Tool", "工具");
            weaponMap.put("CSGO_Type_Spray", "涂鸦");
            weaponMap.put("CSGO_Type_Ticket", "通行证");
            weaponMap.put("CSGO_Type_Collectible", "收藏品/胸章");
            weaponMap.put("CSGO_Tool_Patch", "布章");
            weaponMap.put("CSGO_Tool_GiftTag", "礼物");
            weaponMap.put("CSGO_Tool_Name_TagTag", "标签");
            weaponMap.put("customplayer_terrorist", "恐怖分子");
            weaponMap.put("customplayer_counter_strike", "反恐精英");


            String selWeapon = invDescDO.getSelItemset();

            if (selWeapon != null) {
                c5ItemInfo.setWeaponName(weaponMap.get(selWeapon));
                c5ItemInfo.setWeapon(selWeapon);
                invPreviewDO.setSelWeapon(selWeapon);
            } else {
                c5ItemInfo.setWeaponName("");
                c5ItemInfo.setWeapon("");
            }
*/


            c5ItemInfo.setItemSetName(invDescDO.getSelItemset());
            c5ItemInfo.setTypeName(invDescDO.getSelType());
            c5ItemInfo.setWeaponName(invDescDO.getSelWeapon());


            invPreviewDO.setMinPrice(sellingDOOptional.isPresent() ? sellingDOOptional.get().getPrice() : -1).setExistInv(total > 0)
                    .setAutoQuantity(total.toString())
                    .setMarketHashName(marketHashName)
                    .setImageUrl(invDescDO.getIconUrl())
                    .setItemName(invDescDO.getMarketName())
                    .setItemId(System.currentTimeMillis())
                    .setAutoPrice(String.valueOf(sellingDOOptional.isPresent() ? sellingDOOptional.get().getPrice() : -1))
                    .setItemInfo(c5ItemInfo);


            invPreviewDO.setReferencePrice(new BigDecimal(invPreviewDO.getMinPrice()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString());
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
        Integer count = 0;
        if (Objects.nonNull(invPreviewDOS)) {
            for (InvPreviewDO item : invPreviewDOS) {
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
                invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(sellingDOPageResult.getTotal() > 0).setAutoQuantity(sellingDOPageResult.getTotal().toString())
                        .setMinPrice(sellingDOOptional.isPresent() ? sellingDOOptional.get().getPrice() : -1)
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