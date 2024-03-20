package cn.iocoder.yudao.module.steam.service.ioinvupdate;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IOInvUpdateService {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private InvMapper invMapper;

    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private SellingMapper sellingMapper;



//    @Async

    // 从steam获取用户库存信息
    public InventoryDto gitInvFromSteam (BindUserDO bindUserDO) throws JsonProcessingException {
        HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
        builder.url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String, String> pathVar = new HashMap<>();
        pathVar.put("steamId", bindUserDO.getSteamId());
        pathVar.put("app", "730");
        builder.pathVar(pathVar);
        HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build());
        if (Objects.isNull(proxyResponseVo.getStatus()) || proxyResponseVo.getStatus() != 200) {
            throw new ServiceException(-1, "初始化steam失败");
        }
        InventoryDto json = objectMapper.readValue(proxyResponseVo.getHtml(), InventoryDto.class);
        if (json == null) {
            throw new ServiceException(-1, "访问steam库存过于频繁，请稍后再试/或当前库存为空");
        }
        return json;
    }


    /**
     * 插入库存 (对应账户库存为空)
     */
    @Transactional
    public void firstInsertInventory(InventoryDto inventoryDto, BindUserDO bindUserDO) {
        // inv 表入库
        List<InvDO> invDOList = new ArrayList<>();
        for(InventoryDto.AssetsDTO assetsDTO :inventoryDto.getAssets()){
            InvDO invDO = new InvDO();
            invDO.setClassid(assetsDTO.getClassid());
            invDO.setInstanceid(assetsDTO.getInstanceid());
            invDO.setAppid(assetsDTO.getAppid());
            invDO.setAssetid(assetsDTO.getAssetid());
            invDO.setAmount(assetsDTO.getAmount());
            invDO.setSteamId(bindUserDO.getSteamId());
//            invDO.setStatus(0);   // 默认为0
            invDO.setPrice(0);
            invDO.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
            invDO.setUserId(bindUserDO.getUserId());
            invDO.setUserType(bindUserDO.getUserType());
            invDO.setBindUserId(bindUserDO.getId());
            invDO.setContextid(assetsDTO.getContextid());
            invDOList.add(invDO);
        }
        invMapper.insertBatch(invDOList);
        // inv_desc 表入库
        List<InvDescDO> invDescDOList = new ArrayList<>();
        for(InventoryDto.DescriptionsDTOX item :inventoryDto.getDescriptions()){
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
            invDescDOList.add(invDescDO);
        }
        // 批量插入 desc
        invDescMapper.insertBatch(invDescDOList);

        // 给inv表绑定descID
        for(InvDO item : invDOList){
            List<InvDescDO> invDescIDList = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getInstanceid, item.getInstanceid())
                    .eq(InvDescDO::getClassid, item.getClassid()));
            InvDO invDescId = new InvDO();
            invDescId.setInstanceid(item.getInstanceid());
            invDescId.setClassid(item.getClassid());
            invDescId.setInvDescId(invDescIDList.get(0).getId());
            invMapper.update(invDescId,new LambdaQueryWrapperX<InvDO>()
                    .eq(InvDO::getInstanceid, invDescId.getInstanceid())
                    .eq(InvDO::getClassid, invDescId.getClassid()));
        }
    }


    /**
     * 删除库存  删除原有的 transferStatus = 0 的库存 插入新的库存，并比对 selling 表中的内容
     */
    public void deleteInventory( BindUserDO bindUserDO) {
        InvPageReqVO invDO = new InvPageReqVO();
        invDO.setSteamId(bindUserDO.getSteamId());
        invDO.setUserId(bindUserDO.getUserId());
        invDO.setBindUserId(bindUserDO.getId());

        invMapper.delete(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getSteamId, invDO.getSteamId())
                .eq(InvDO::getUserId, invDO.getUserId())
                .eq(InvDO::getBindUserId, invDO.getBindUserId())
                .eq(InvDO::getTransferStatus, "0"));
    }

    /**
     *  合并库存----查询库存方法  不分页
     */
    public List<InvDO> getInvToMerge(@RequestParam InvDO invToMergeVO) {
        // 查询库存 (所有库存不分页查询)
        return invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getTransferStatus, invToMergeVO.getTransferStatus())
                .eq(InvDO::getUserId, invToMergeVO.getUserId())
                .eq(InvDO::getSteamId, invToMergeVO.getSteamId())
                .eq(InvDO::getBindUserId, invToMergeVO.getBindUserId()));
    }

    /**
     *  合并库存----查询库存方法  不分页
     */
    public List<InvDO> getInvToMerge1(@RequestParam InvDO invToMergeVO) {
        // 查询库存 (所有库存不分页查询)
        return invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eqIfPresent(InvDO::getTransferStatus, invToMergeVO.getTransferStatus())
                .eq(InvDO::getUserId, invToMergeVO.getUserId())
                .eq(InvDO::getSteamId, invToMergeVO.getSteamId())
                .eq(InvDO::getBindUserId, invToMergeVO.getBindUserId()));
    }


    /**
     *   按入参查询库存  或者合并库存
     *   入参可传  库存主键ID  唯一资产ID
     */
    public List<AppInvPageReqVO> searchInv(List<InvDO> invToMerge) {
        // 提取每个库存对应的详情表主键
        ArrayList<Object> DescIdList = new ArrayList<>();
        for (InvDO invDO : invToMerge) {
            DescIdList.add(invDO.getInvDescId());
        }
        List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .in(InvDescDO::getId, DescIdList));
//                .eq(InvDescDO::getTradable, 1));
//        List<Long> enableInvDescId = invDescDOS.stream().map(InvDescDO::getId).collect(Collectors.toList());
//        List<InvDO> collect = invToMerge.stream().filter(i -> enableInvDescId.contains(i.getInvDescId())).collect(Collectors.toList());

//        for(InvDO invDO : invToMerge){
//            if(invDescDOS.contains(invDO.getInvDescId())){
//                invToMerge.remove(invDO.getInvDescId());
//            }
//        }
        Map<Long, InvDescDO> map = new HashMap<>();

        // 提取每个库存对应的详情表主键
        for (InvDescDO invDescDO : invDescDOS) {
            map.put(invDescDO.getId(), invDescDO);
        }
        List<AppInvPageReqVO> appInvPageReqVO = new ArrayList<>();

        for (InvDO invDO : invToMerge) {
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
                    appInvPageReqVO1.setSteamId(invDO.getSteamId());
                    appInvPageReqVO1.setStatus(invDO.getStatus());
                    appInvPageReqVO1.setTransferStatus(invDO.getTransferStatus());
                    appInvPageReqVO1.setUserType(invDO.getUserType());
                    appInvPageReqVO1.setPrice(invDO.getPrice());
                    appInvPageReqVO1.setAssetid(invDO.getAssetid());
                    appInvPageReqVO1.setTags(map.get(invDO.getInvDescId()).getTags());
                    appInvPageReqVO1.setTradeable(map.get(invDO.getInvDescId()).getTradable());
                    appInvPageReqVO1.setMarketHashName(map.get(invDO.getInvDescId()).getMarketHashName());
                    appInvPageReqVO.add(appInvPageReqVO1);
                }
            }
            return appInvPageReqVO;
        }


    }

