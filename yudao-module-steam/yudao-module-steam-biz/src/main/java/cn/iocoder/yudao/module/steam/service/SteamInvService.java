package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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

    // 用户获得库存
    public InvDO AfterInventory(Long id, Long appid){
        BindUserDO bindUserDO = bindUserMapper.selectById(id);
        if (bindUserDO == null){
            throw new ServiceException(-1,"用户id错误，未查询到该用户的steam账户");
        }
        String steamid =  bindUserDO.getSteamId();
        List<InvDO> invDOS = invMapper.selectList().stream().filter(o -> o.getSteamId().equals(steamid)).collect(Collectors.toList());
        return (InvDO) invDOS;
    }
    public InventoryDto FistGetInventory(Long id, String appId){
        // 用户第一次登录查询库存  根据用户ID查找绑定的Steam账号ID
        BindUserDO bindUserDO = bindUserMapper.selectById(id);
        if (bindUserDO == null){
            throw new ServiceException(-1,"用户未绑定Steam账号");
        }
        if (bindUserDO.getSteamPassword() == null){
            throw new ServiceException(-1,"用户未设置Steam密码,原因：未上传ma文件");
        }
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.method(HttpUtil.Method.GET).url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String,String> pathVar=new HashMap<>();
        pathVar.put("steamId",bindUserDO.getSteamId());
        pathVar.put("app",appId);
        builder.pathVar(pathVar);
        Map<String, String> header = new HashMap<>();
        header.put("Connection", "close");
        builder.headers(header);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),HttpUtil.getClient(true,30000));
        InventoryDto json = sent.json(InventoryDto.class);
        for (InventoryDto.AssetsDTO item:json.getAssets()) {
            // steamid 和 绑定平台用户id 联合查询当前用户steam_inv的所有库存信息
            Long userId = bindUserDO.getUserId();
            InvPageReqVO steamInv= new InvPageReqVO();
            steamInv.setSteamId(bindUserDO.getSteamId());
            // invDOPageResult: 当前用户steam_inv的所有库存信息
            PageResult<InvDO> invDOPageResult = invMapper.selectPage(steamInv);;
            if (invDOPageResult.getTotal() > 0){
                // 更新库存 TODO 删除 steam_selling 和 steam_inv 表中的信息
                InvDO steamInvUpdate = InvUpdate(invDOPageResult, item, bindUserDO.getSteamId());
                invMapper.updateById(steamInvUpdate);
            } else {
                // 插入库存
                String steamId = bindUserDO.getSteamId();
                InvDO steamInvInsert = getInvDO(item,userId,steamId,id);
                invMapper.insert(steamInvInsert);
            }
        }
        List<InventoryDto.DescriptionsDTOX> descriptions = json.getDescriptions();
        for(InventoryDto.DescriptionsDTOX item:descriptions){
            InvDescPageReqVO invDescPageReqVO=new InvDescPageReqVO();
            invDescPageReqVO.setAppid(item.getAppid());
            invDescPageReqVO.setClassid(item.getClassid());
            invDescPageReqVO.setInstanceid(item.getInstanceid());
            List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eqIfPresent(InvDescDO::getAppid, item.getAppid())
                    .eqIfPresent(InvDescDO::getClassid, item.getClassid())
                    .eqIfPresent(InvDescDO::getInstanceid, item.getInstanceid())
                    .orderByDesc(InvDescDO::getId));
            if(invDescDOS.size()>0){
                Optional<InvDescDO> first = invDescDOS.stream().findFirst();
                InvDescDO invDescDO = first.get();
                invDescDO.setSteamId(bindUserDO.getSteamId());
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl(item.getIconUrl());
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
                if(type.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if(weapon.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if(itemSet.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if(quality.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if(rarity.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if(exterior.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
                invDescDO.setUpdateTime(LocalDateTime.now());
                invDescMapper.updateById(invDescDO);
            }else{
                InvDescDO invDescDO=new InvDescDO();
                invDescDO.setSteamId(bindUserDO.getSteamId());
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl(item.getIconUrl());
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
                if(type.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if(weapon.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if(itemSet.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if(quality.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if(rarity.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if(exterior.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
                invDescMapper.insert(invDescDO);
            }
        }
        return json;
    }

    /**
     * @Discription 插入库存
     * @return  steamInvInsert
     */
    @NotNull
    private static InvDO getInvDO(InventoryDto.AssetsDTO item,Long userId,String steamId,Long id) {
        InvDO steamInvInsert = new InvDO();
        steamInvInsert.setSteamId(steamId);
        steamInvInsert.setAppid(item.getAppid());
        steamInvInsert.setAssetid(item.getAssetid());
        steamInvInsert.setClassid(item.getClassid());
        steamInvInsert.setInstanceid(item.getInstanceid());
        steamInvInsert.setAmount(item.getAmount());
        // 第一次入库，所有道具均为未起售状态 0
        steamInvInsert.setTransferStatus(0);
        steamInvInsert.setBindUserId(id);
        steamInvInsert.setUserId(userId);
        steamInvInsert.setUserType(1);
        steamInvInsert.setContextid(item.getContextid());
        return steamInvInsert;
    }

//    /**
//     *   更新库存
//     * @return steamInvUpdate
//     */
//    @NotNull
//    private static InvDO InvUpdate(PageResult<InvDO> invDOPageResult, InventoryDto.AssetsDTO item) {
//        Optional<InvDO> first = invDOPageResult
//                .getList()
//                .stream().filter(o -> o.getStatus().equals(1))
//                .findFirst();
//        InvDO steamInvUpdate;
//        if (first.isPresent()) {
//            steamInvUpdate = first.get();
//            steamInvUpdate.setAmount(item.getAmount());
//            steamInvUpdate.setClassid(item.getClassid());
//            steamInvUpdate.setInstanceid(item.getInstanceid());
//        } else {
//            throw new ServiceException(-1, "库存更新失败");
//        }
//        return steamInvUpdate;
//    }

    /**
     * @Discription 更新库存
     * 1.查询steam_inv表里所有在售商品的数据
     * 2.判断查询结果是否为空
     * 3.查询steam接口中的库存数据
     * 4.对比表中在售商品数据是否在steam接口中存在
     * 5.如果存在，不做改变，如果不存在，则将steam_inv表中在售商品状态改为 1
     */
    @NotNull
    // TODO 如果出现更新库存后用户不能登录情况，检查steamInvUpdate.setStatus(1);
    private static InvDO InvUpdate(PageResult<InvDO> invDOPageResult, InventoryDto.AssetsDTO item,String steamid) {
        InvDO steamInvUpdate = new InvDO();
        // 获取所有在售商品
        List<InvDO> collect = invDOPageResult
                .getList()
                .stream()
                .filter(o -> o.getTransferStatus() == 1 && o.getPrice()!= null)
                .collect(Collectors.toList());
        // 遍历在售商品 判断是否在库存中
        for (InvDO invDO : collect) {
            if ( !(invDO.getAssetid().contains(item.getAssetid()))){
                steamInvUpdate.setTransferStatus(2);
                steamInvUpdate.setStatus(1);
            }
        }

        // invDOPageResult: 本地库存信息  item: steam接口返回的库存信息


        // 查询本地 steam_inv 表中的库存
        // 对比本地库存和线上库存，如果本地库存中存在线上库存中不存在的，则将本地库存状态改为 1
        return steamInvUpdate;
    }

}





