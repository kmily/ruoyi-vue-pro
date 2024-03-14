package cn.iocoder.yudao.module.steam.service.ioinvupdate;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

//=======================================

    //    测试 TODO  lgm

//========================================
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
     * 第一次插入库存
     */
    public void firstInsertInventory(InventoryDto inventoryDto, BindUserDO bindUserDO) {
        // inv 表入库
        if(inventoryDto.getAssets() == null){
            throw new ServiceException(-1,"steam库存为空");
        }
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
            // inv_desc_id
            invMapper.insert(invDO);
        }
        // inv_desc 表入库

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
            invDescMapper.insert(invDescDO);
            List<InvDescDO> invDescDOS = invDescMapper.selectList()
                    .stream()
                    .filter(i -> i.getClassid().equals(invDescDO.getClassid()) && i.getInstanceid().equals(invDescDO.getInstanceid()))
                    .collect(Collectors.toList());
            InvDO invDescId = new InvDO();
            invDescId.setInstanceid(item.getInstanceid());
            invDescId.setClassid(item.getClassid());
            invDescId.setInvDescId(invDescDOS.get(0).getId());
            invMapper.update(invDescId, new QueryWrapper<InvDO>().eq("instanceid", item.getInstanceid()).eq("classid", item.getClassid()));
        }
    }


    /**
     * 删除库存  删除原有的 transferStatus != 0 的库存 插入新的库存，并比对 selling 表中的内容
     */
    public void deleteInventory( BindUserDO bindUserDO) {
        InvPageReqVO invDO = new InvPageReqVO();
        invDO.setSteamId(bindUserDO.getSteamId());
        invDO.setUserId(bindUserDO.getUserId());

        // TODO 校验三个字段 或者（校验steamId 剩下两个字段二选一）
        // 删除原有的 transferStatus != 0 的库存
        List<InvDO> invDOS = invMapper.selectPage(invDO).getList();
        for(InvDO deleteItem : invDOS){
            if(deleteItem.getTransferStatus() == 0){
                invMapper.deleteById(deleteItem.getId());
            }
        }

    }
}
