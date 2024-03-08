package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescRespVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.enums.CommonStatusEnum.DISABLE;
import static cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum.INORDER;
import static cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum.OFFSHELF;


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
    public InventoryDto FistGetInventory(Long id, String appId) throws JsonProcessingException {
        // 用户第一次登录查询库存  根据用户ID查找绑定的Steam账号ID
        BindUserDO bindUserDO = bindUserMapper.selectById(id);
        if (bindUserDO == null){
            throw new ServiceException(-1,"用户未绑定Steam账号");
        }
        if (bindUserDO.getSteamPassword() == null){
            throw new ServiceException(-1,"用户未设置Steam密码,原因：未上传ma文件");
        }
        HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
        builder.url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String,String> pathVar=new HashMap<>();
        pathVar.put("steamId",bindUserDO.getSteamId());
        pathVar.put("app",appId);
        builder.pathVar(pathVar);
        HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build());
        if(Objects.isNull(proxyResponseVo.getStatus()) || proxyResponseVo.getStatus()!=200){
            throw new ServiceException(-1, "初始化steam失败");
        }
        InventoryDto json = objectMapper.readValue(proxyResponseVo.getHtml(), InventoryDto.class);

        for (InventoryDto.AssetsDTO item:json.getAssets()) {
            // steamid 和 绑定平台用户id 联合查询当前用户steam_inv的所有库存信息
            Long userId = bindUserDO.getUserId();
            InvPageReqVO steamInv= new InvPageReqVO();
            steamInv.setSteamId(bindUserDO.getSteamId());
            // invDOPageResult: 当前用户steam_inv的所有库存信息
            PageResult<InvDO> invDOPageResult = invMapper.selectPage(steamInv);;
            if (invDOPageResult.getTotal() > 0){
                // 更新库存 TODO 删除 steam_selling 和 steam_inv 表中的信息
                ArrayList<InventoryDto.AssetsDTO> invCollect = new ArrayList<>(json.getAssets());
                for (InvDO invDO : invDOPageResult.getList()){
                    if(!invCollect.contains(invDO)){
                        SellingDO updateDo = new SellingDO();
                        // 默认0有效 1失效
                        updateDo.setStatus(CommonStatusEnum.DISABLE.getStatus());
                        updateDo.setId(invDO.getId());
                        sellingMapper.updateById(updateDo);
                        invMapper.deleteById(invDO.getId());
                    }
                }
                InvDO steamInvUpdate = InvUpdate(invDOPageResult,item);
                invMapper.insert(steamInvUpdate);
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
                invDescDO.setIconUrl("https://community.steamstatic.com/economy/image/"+item.getIconUrl());
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
                invDescDO.setIconUrl("https://community.steamstatic.com/economy/image/"+item.getIconUrl());
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

    /**
     *   更新库存
     * @return steamInvUpdate
     * invDOPageResult 库存信息
     * InventoryDto.AssetsDTO item 线上steam信息
     */
    @NotNull
    private static InvDO InvUpdate(PageResult<InvDO> invDOPageResult, InventoryDto.AssetsDTO item) {
        log.info("{}",item);
        log.info("{}",invDOPageResult);
        List<InvDO> invList = new ArrayList<>(invDOPageResult.getList());
        InvDO steamInvUpdate = new InvDO();

        // 线上steam库存中存在，本地不存在,插入本地库
        for(InvDO inv : invList){
            if(!inv.getAssetid().contains(item.getAssetid())){
                steamInvUpdate.setSteamId(inv.getSteamId());
                steamInvUpdate.setAppid(item.getAppid());
                steamInvUpdate.setAssetid(item.getAssetid());
                steamInvUpdate.setClassid(item.getClassid());
                steamInvUpdate.setInstanceid(item.getInstanceid());
                steamInvUpdate.setAmount(item.getAmount());
                // 第一次入库，所有道具均为未起售状态 0
                steamInvUpdate.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
                steamInvUpdate.setBindUserId(inv.getId());
                steamInvUpdate.setUserId(inv.getUserId());
                steamInvUpdate.setUserType(1);
                steamInvUpdate.setContextid(item.getContextid());
            }
        }
        return steamInvUpdate;
    }


    public InvPageReqVO getInvPage1(InvPageReqVO invPageReqVO){
        // 用户库存
        PageResult<InvDO> invPage = invService.getInvPage(invPageReqVO);
        InvPageReqVO invPageReqVO1 = new InvPageReqVO();

        for(InvDO invDO : invPage.getList()){

            // 价格   状态
            invPageReqVO1.setId(invDO.getId());
            invPageReqVO1.setPrice(invDO.getPrice());
            invPageReqVO1.setStatus(invDO.getStatus());
            invPageReqVO1.setClassid(invDO.getClassid());
            invPageReqVO1.setInstanceid(invDO.getInstanceid());
            List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getClassid, invDO.getClassid())
                    .eq(InvDescDO::getInstanceid, invDO.getInstanceid()));
            invPageReqVO1.setPictureUrl(invDescDOS.get(0).getIconUrl());
            invPageReqVO1.setMarketName(invDescDOS.get(0).getMarketName());
        }
        return invPageReqVO1;

    }



//    // 修改getInvPage1接口
//    public List<InvPageReqVO> getInvPage2(InvPageReqVO invPageReqVO){
//        // 用户库存
//        PageResult<InvDO> invPage = invService.getInvPage(invPageReqVO);
//        List<InvDescDO> invDescDOS1 = invDescMapper.selectList();
//
//        ArrayList<InvPageReqVO> invPageReqVO1 = new ArrayList<>();
//
//        for(InvDO invDO : invPage.getList()){
//            // 价格   状态
//            InvPageReqVO invPageReqVO11 = new InvPageReqVO();
//            invPageReqVO11.setPrice(invDO.getPrice());
//            invPageReqVO11.setStatus(invDO.getStatus());
//            invPageReqVO11.setClassid(invDO.getClassid());
//            invPageReqVO11.setInstanceid(invDO.getInstanceid());
//
//
//        }
//
//
//
//        return invPageReqVO1;
//    }


}





