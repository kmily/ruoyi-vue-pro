package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvMergeToSellPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;

import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author lgm
 * @date 2024/02/23
 * @discription 定时刷新用户表绑定 Steam 账号的 CSGO 库存
 */
@Slf4j
@Service
public class SteamInvService {

    @Resource
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private InvService invService;


    // 从steam线上获取库存
    @Async
    public InventoryDto FistGetInventory(Long id, String appId) throws JsonProcessingException {
        // 用户第一次登录查询库存  根据用户ID查找绑定的Steam账号ID
        BindUserDO bindUserDO = bindUserMapper.selectById(id);
        if (bindUserDO == null) {
            throw new ServiceException(-1, "用户未绑定Steam账号");
        }
        if (bindUserDO.getSteamPassword() == null) {
            throw new ServiceException(-1, "用户未设置Steam密码,原因：未上传ma文件");
        }
        HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
        builder.url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String, String> pathVar = new HashMap<>();
        pathVar.put("steamId", bindUserDO.getSteamId());
        pathVar.put("app", appId);
        builder.pathVar(pathVar);
        HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build());
        if (Objects.isNull(proxyResponseVo.getStatus()) || proxyResponseVo.getStatus() != 200) {
            throw new ServiceException(-1, "初始化steam失败");
        }
        InventoryDto json = objectMapper.readValue(proxyResponseVo.getHtml(), InventoryDto.class);
        if (json == null) {
            throw new ServiceException(-1, "访问steam库存过于频繁，请稍后再试/或当前库存为空");
        }
        // 对比更新库存前，先查询本地库存是否为空
        InvPageReqVO steamInv = new InvPageReqVO();
        steamInv.setSteamId(bindUserDO.getSteamId());
        PageResult<InvDO> invDOPageResult = invMapper.selectPage(steamInv);
        // 第一次插入库存
        if (invDOPageResult.getList().isEmpty()) {
            for (InventoryDto.AssetsDTO item : json.getAssets()) {
                InvDO steamInvInsert = InsertInvDO(item, bindUserDO.getUserId(), bindUserDO.getSteamId(), bindUserDO.getId());
//                invMapper.insert(steamInvInsert);
            }
        } else {
//            // 更新库存 删除 steam_selling 和 steam_inv 表中的信息
//            InventoryDto.AssetsDTO item = new InventoryDto.AssetsDTO();
//            for (InventoryDto.AssetsDTO assetsDTO : json.getAssets()) {
//                item.setContextid(assetsDTO.getContextid());
//                item.setAssetid(assetsDTO.getAssetid());
//                item.setClassid(assetsDTO.getClassid());
//            }
//            ArrayList<InventoryDto.AssetsDTO> assetList = new ArrayList<>(json.getAssets());
//            // 遍历本地表，查找本地表存在，线上库存中不存在的商品（说明该商品已经在其他平台卖出），在本地库存将 status 设置为1
//            InvDO steamInvUpdate = InvUpdate(invDOPageResult, item, assetList, bindUserDO);
//            invMapper.update(new LambdaUpdateWrapper<>(steamInvUpdate).eq(InvDO::getAssetid, steamInvUpdate.getAssetid()));
//            SellingDO sellingDO = sellingMapper.selectById(steamInvUpdate.getId());
//            if (sellingDO != null) {
//                sellingMapper.updateById(new SellingDO().setStatus(CommonStatusEnum.DISABLE.getStatus()).setId(sellingDO.getId()));
//            }
            // 遍历线上库存表，查找线上库存表存在，本地库存中不存在的商品（说明该商品是玩家新获得道具），添加到本地库存表中
        }

        List<InventoryDto.DescriptionsDTOX> descriptions = json.getDescriptions();
        for (InventoryDto.DescriptionsDTOX item : descriptions) {
            InvDescPageReqVO invDescPageReqVO = new InvDescPageReqVO();
            invDescPageReqVO.setAppid(item.getAppid());
            invDescPageReqVO.setClassid(item.getClassid());
            invDescPageReqVO.setInstanceid(item.getInstanceid());
            List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eqIfPresent(InvDescDO::getAppid, item.getAppid())
                    .eqIfPresent(InvDescDO::getClassid, item.getClassid())
                    .eqIfPresent(InvDescDO::getInstanceid, item.getInstanceid())
                    .orderByDesc(InvDescDO::getId));
            if (invDescDOS.size() > 0) {
                Optional<InvDescDO> first = invDescDOS.stream().findFirst();
                InvDescDO invDescDO = first.get();
                invDescDO.setSteamId(bindUserDO.getSteamId());
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl("https://community.steamstatic.com/economy/image/" + item.getIconUrl());
                invDescDO.setIconUrlLarge(item.getIconUrlLarge());
                invDescDO.setDescriptions(item.getDescriptions());
                invDescDO.setTradable(item.getTradable());
                invDescDO.setActions(item.getActions());
                invDescDO.setName(item.getName());
                invDescDO.setNameColor(item.getNameColor());
                invDescDO.setType(item.getType());
                invDescDO.setMarketName(item.getMarketName());
                invDescDO.setMarketHashName(item.getMarketHashName());
                invDescDO.setMarketActions(item.getMarketActions());
                invDescDO.setCommodity(item.getCommodity());
                invDescDO.setMarketTradableRestriction(item.getMarketTradableRestriction());
                invDescDO.setMarketable(item.getMarketable());
                invDescDO.setTags(item.getTags());
                //解析tags
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> type = item.getTags().stream().filter(i -> i.getCategory().equals("Type")).findFirst();
                if (type.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if (weapon.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if (itemSet.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if (quality.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if (rarity.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if (exterior.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
                invDescDO.setUpdateTime(LocalDateTime.now());
                invDescMapper.updateById(invDescDO);
            } else {
                InvDescDO invDescDO = new InvDescDO();
                invDescDO.setSteamId(bindUserDO.getSteamId());
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl("https://community.steamstatic.com/economy/image/" + item.getIconUrl());
                invDescDO.setIconUrlLarge(item.getIconUrlLarge());
                invDescDO.setDescriptions(item.getDescriptions());
                invDescDO.setTradable(item.getTradable());
                invDescDO.setActions(item.getActions());
                invDescDO.setName(item.getName());
                invDescDO.setNameColor(item.getNameColor());
                invDescDO.setType(item.getType());
                invDescDO.setMarketName(item.getMarketName());
                invDescDO.setMarketHashName(item.getMarketHashName());
                invDescDO.setMarketActions(item.getMarketActions());
                invDescDO.setCommodity(item.getCommodity());
                invDescDO.setMarketTradableRestriction(item.getMarketTradableRestriction());
                invDescDO.setMarketable(item.getMarketable());
                invDescDO.setTags(item.getTags());
                //解析tags
                // 类型选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> type = item.getTags().stream().filter(i -> i.getCategory().equals("Type")).findFirst();
                if (type.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                //武器选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if (weapon.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                // 收藏品选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if (itemSet.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                //类别选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if (quality.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                // 品质选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if (rarity.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                // 外观选择
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if (exterior.isPresent()) {
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
//                invDescMapper.insert(invDescDO);
                List<InvDescDO> invDescDOS1 = invDescMapper.selectList(new QueryWrapper<InvDescDO>()
                        .eq("classid", item.getClassid()));
                InvDO invDO = new InvDO();
                invDO.setInstanceid(item.getInstanceid());
                invDO.setClassid(item.getClassid());
                invDO.setInvDescId(invDescDOS1.get(0).getId());
//                invMapper.update(invDO, new UpdateWrapper<InvDO>()
//                        .eq("classid", item.getClassid()));
            }
        }
        return json;
    }

    /**
     * @return steamInvInsert
     * @Discription 插入库存
     */
    @NotNull
    private static InvDO InsertInvDO(InventoryDto.AssetsDTO item, Long userId, String steamId, Long id) {
        InvDO steamInvInsert = new InvDO();
        steamInvInsert.setSteamId(steamId);
        steamInvInsert.setAppid(item.getAppid());
        steamInvInsert.setAssetid(item.getAssetid());
        steamInvInsert.setClassid(item.getClassid());
        steamInvInsert.setInstanceid(item.getInstanceid());
        steamInvInsert.setAmount(item.getAmount());
        // 第一次入库，所有道具均为未起售状态 0
        steamInvInsert.setTransferStatus(0);
        // 用户表id
        steamInvInsert.setBindUserId(id);
        steamInvInsert.setUserId(userId);
        steamInvInsert.setUserType(1);
        steamInvInsert.setContextid(item.getContextid());
        return steamInvInsert;
    }

    /**
     * 更新库存
     *
     * @return steamInvUpdate
     * invDOPageResult 库存信息
     * InventoryDto.AssetsDTO item 线上steam信息
     */
    @NotNull
    private static InvDO InvUpdate(PageResult<InvDO> invDOPageResult, InventoryDto.AssetsDTO item, ArrayList<InventoryDto.AssetsDTO> itemList, BindUserDO bindUserDO) {

        // 本地表
        List<InvDO> invList = new ArrayList<>(invDOPageResult.getList());
        // 提取本地库存的唯一资产id
        List<Object> list = new ArrayList<>();
        for (InvDO assetidList : invList) {
            list.add(assetidList.getAssetid());
        }
        // 提取线上库存的唯一资产id
        List<Object> list1 = new ArrayList<>();
        for (InventoryDto.AssetsDTO item1 : itemList) {
            list1.add(item1.getAssetid());
        }

        InvDO steamInvUpdate = new InvDO();

        for (InvDO inv : invList) {
            // 本地库存 不存在于 steam 线上库存 （商品已在其他平台出售）
            if (!list1.contains(inv.getAssetid())) {
                if (inv.getTransferStatus() == 0) {
                    steamInvUpdate.setStatus(CommonStatusEnum.DISABLE.getStatus());
                    steamInvUpdate.setAssetid(inv.getAssetid());
                    steamInvUpdate.setClassid(inv.getClassid());
                    steamInvUpdate.setInstanceid(inv.getInstanceid());
                    steamInvUpdate.setId(inv.getId());
                    return steamInvUpdate;
                }
//                invMapper.updateById(steamInvUpdate);
            }
        }
        for (InventoryDto.AssetsDTO assetsDTO : itemList) {
            // 线上 steam 库存 不存在于 本地库存 （玩家新获得库存）
            if (!list.contains(assetsDTO.getAssetid())) {
                steamInvUpdate.setSteamId(bindUserDO.getSteamId());
                steamInvUpdate.setAppid(assetsDTO.getAppid());
                steamInvUpdate.setAssetid(assetsDTO.getAssetid());
                steamInvUpdate.setClassid(assetsDTO.getClassid());
                steamInvUpdate.setInstanceid(assetsDTO.getInstanceid());
                steamInvUpdate.setAmount(assetsDTO.getAmount());
                // 第一次入库，所有道具均为未起售状态 0
                steamInvUpdate.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
                steamInvUpdate.setBindUserId(bindUserDO.getId());
                steamInvUpdate.setUserId(bindUserDO.getUserId());
                steamInvUpdate.setUserType(1);
                steamInvUpdate.setContextid(assetsDTO.getContextid());
                // desc的id 需要根据 appid 和 contextid 查询
//                steamInvUpdate.setInvDescId();
//                invMapper.insert(steamInvUpdate);
            }
        }
        return steamInvUpdate;
    }


    // 第二次访问库存(只读库存表)
    public PageResult<AppInvPageReqVO> getInvPage1(InvPageReqVO invPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, invPageReqVO.getSteamId()));
        if(Objects.isNull(collect) || collect.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        invPageReqVO.setUserId(loginUser.getId());
        invPageReqVO.setBindUserId(collect.get(0).getId());
        // 用户库存
        PageResult<InvDO> invPage = invService.getInvPage(invPageReqVO);
        if (invPage.getList().isEmpty()) {
            throw new ServiceException(-1, "您暂时没有库存");
        }
        // 提取每个库存对应的详情表主键
        ArrayList<Object> list = new ArrayList<>();
        for (InvDO invDO : invPage.getList()) {
            list.add(invDO.getInvDescId());
        }
        List<InvDescDO> invDescDOS = invDescMapper.selectList(new QueryWrapper<InvDescDO>().in("id", list));

        Map<Long, InvDescDO> map = new HashMap<>();
        for (InvDescDO invDescDO : invDescDOS) {
            map.put(invDescDO.getId(), invDescDO);
        }
        List<AppInvPageReqVO> appInvPageReqVO = new ArrayList<>();

        for (InvDO invDO : invPage.getList()) {
            AppInvPageReqVO appInvPageReqVO1 = new AppInvPageReqVO();
            if (map.isEmpty()) {
                appInvPageReqVO1.setMarketName("null");
                appInvPageReqVO1.setMarketName("null");
            } else {
                appInvPageReqVO1.setIconUrl(map.get(invDO.getInvDescId()).getIconUrl());
                appInvPageReqVO1.setMarketName(map.get(invDO.getInvDescId()).getMarketName());

                // 分类选择字段
                appInvPageReqVO1.setSelExterior(map.get(invDO.getInvDescId()).getSelExterior());
                appInvPageReqVO1.setSelType(map.get(invDO.getInvDescId()).getSelType());
                appInvPageReqVO1.setSelWeapon(map.get(invDO.getInvDescId()).getSelWeapon());
                appInvPageReqVO1.setSelRarity(map.get(invDO.getInvDescId()).getSelRarity());
                appInvPageReqVO1.setSelQuality(map.get(invDO.getInvDescId()).getSelQuality());
                appInvPageReqVO1.setSelType(map.get(invDO.getInvDescId()).getSelType());

                appInvPageReqVO1.setId(invDO.getId());
                appInvPageReqVO1.setSteamId(invPageReqVO.getSteamId());
                appInvPageReqVO1.setStatus(invDO.getStatus());
                appInvPageReqVO1.setTransferStatus(invDO.getTransferStatus());
                appInvPageReqVO1.setUserType(invDO.getUserType());
                appInvPageReqVO1.setPrice(invDO.getPrice());
                appInvPageReqVO1.setAssetid(invDO.getAssetid());
                appInvPageReqVO1.setTags(map.get(invDO.getInvDescId()).getTags());
                appInvPageReqVO1.setTradeable(map.get(invDO.getInvDescId()).getTradable());
                appInvPageReqVO.add(appInvPageReqVO1);
            }
        }
        return new PageResult<>(appInvPageReqVO, invPage.getTotal());
    }


    /**
     * 合并显示库存
     * @param invPage1
     * @return
     */
    public PageResult<AppInvMergeToSellPageReqVO> mergeInv(PageResult<AppInvPageReqVO> invPage1){
        Map<String,Integer> map = new HashMap<>();
//        Map<String,String> AssetIdList = new HashMap<>();
        Map<String,AppInvMergeToSellPageReqVO> invPage = new HashMap<>();
//        List<AppInvMergeToSellPageReqVO> invPage = new ArrayList<>();
        // 统计每一个 markName 的个数，并插入invPage
        for(AppInvPageReqVO element : invPage1.getList()){
            if(Objects.nonNull(invPage.get(element.getMarketName()))){
                AppInvMergeToSellPageReqVO appInvMergeToSellPageReqVO = invPage.get(element.getMarketName());
                ArrayList<String> strings = new ArrayList<>(appInvMergeToSellPageReqVO.getAssetIdList());
                strings.add(String.valueOf(element.getId()));
                appInvMergeToSellPageReqVO.setAssetIdList(strings);
            }else{
                AppInvMergeToSellPageReqVO appInvPageReqVO = new AppInvMergeToSellPageReqVO();
                appInvPageReqVO.setMarketName(element.getMarketName());
//                appInvPageReqVO.setAssetId(element.getAssetid());
                appInvPageReqVO.setPrice(element.getPrice());
                appInvPageReqVO.setIconUrl(element.getIconUrl());
                appInvPageReqVO.setSelQuality(element.getSelQuality());
                appInvPageReqVO.setSelWeapon(element.getSelWeapon());
                appInvPageReqVO.setSelExterior(element.getSelExterior());
                appInvPageReqVO.setSelRarity(element.getSelRarity());
                appInvPageReqVO.setSelItemset(element.getSelItemset());
                appInvPageReqVO.setSelType(element.getSelType());
                appInvPageReqVO.setId(element.getId());
                appInvPageReqVO.setTags(element.getTags());
                appInvPageReqVO.setTradeable(element.getTradeable());
                appInvPageReqVO.setAssetIdList(Arrays.asList(String.valueOf(element.getId())));
                invPage.put(element.getMarketName(),appInvPageReqVO);
            }
        }

        List<AppInvMergeToSellPageReqVO> invMergePage = new ArrayList<>();
        for(Map.Entry<String,AppInvMergeToSellPageReqVO> key : invPage.entrySet()){
            invMergePage.add(key.getValue());
        }
        return new PageResult<>(invMergePage,invPage1.getTotal());
    }

}





