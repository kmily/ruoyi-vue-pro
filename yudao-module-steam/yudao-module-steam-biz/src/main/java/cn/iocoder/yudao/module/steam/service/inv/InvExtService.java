package cn.iocoder.yudao.module.steam.service.inv;

import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户库存储 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class InvExtService {

    @Resource
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private PayNoRedisDAO noRedisDAO;
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private PaySteamOrderService paySteamOrderService;

    public void fetchInv(BindUserDO bindUserDO){
        InventoryDto inventoryDto = gitInvFromSteam3(bindUserDO);
        log.info("inv{}",inventoryDto);
        String batchNo = noRedisDAO.generate("INV" + bindUserDO.getSteamId());

        for(InventoryDto.DescriptionsDTOX item:inventoryDto.getDescriptions()){

            Optional<InvDescDO> first = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getSteamId, bindUserDO.getSteamId())
                    .eq(InvDescDO::getInstanceid, item.getInstanceid())
                    .eq(InvDescDO::getClassid, item.getClassid())
            ).stream().findFirst();
            InvDescDO invDescDO = first.orElseGet(()->new InvDescDO());
            invDescDO.setBatchNo(batchNo).setSteamId(bindUserDO.getSteamId());
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
            if(Objects.isNull(invDescDO.getId())){
                invDescMapper.insert(invDescDO);
            }else{
                invDescMapper.updateById(invDescDO);
            }
        }
        for(InventoryDto.AssetsDTO item:inventoryDto.getAssets()){
            Optional<InvDO> first = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                    .eq(InvDO::getSteamId, bindUserDO.getSteamId())
                    .eq(InvDO::getAssetid, item.getAssetid())
                    .eq(InvDO::getClassid, item.getClassid())
                    .eq(InvDO::getInstanceid, item.getInstanceid())
            ).stream().findFirst();
            InvDO invDO = first.orElseGet(()->new InvDO());
            Optional<InvDescDO> devOptional = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getSteamId, bindUserDO.getSteamId())
                    .eq(InvDescDO::getInstanceid, item.getInstanceid())
                    .eq(InvDescDO::getClassid, item.getClassid())
                    .eq(InvDescDO::getAppid, item.getAppid())
            ).stream().findFirst();
            if(devOptional.isPresent()){
                invDO.setInvDescId(devOptional.get().getId());
            }
            invDO.setSteamId(bindUserDO.getSteamId()).setBatchNo(batchNo);

            invDO.setClassid(item.getClassid());
            invDO.setInstanceid(item.getInstanceid());
            invDO.setAppid(item.getAppid());
            invDO.setAssetid(item.getAssetid());
            invDO.setAmount(item.getAmount());
            invDO.setSteamId(bindUserDO.getSteamId());
//            invDO.setStatus(0);   // 默认为0
            invDO.setPrice(0);
            invDO.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
            invDO.setUserId(bindUserDO.getUserId());
            invDO.setUserType(bindUserDO.getUserType());
            invDO.setBindUserId(bindUserDO.getId());
            invDO.setContextid(item.getContextid());
            if(Objects.isNull(invDO.getId())){
                invMapper.insert(invDO);
            }else{
                invMapper.updateById(invDO);
            }
        }
        //处理过时的数据，以下数据为失效的库存

        List<InvDO> invDOList = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getSteamId, bindUserDO.getSteamId())
                .ne(InvDO::getBatchNo, batchNo)
        );
        List<Long> invIds = invDOList.stream().map(InvDO::getId).collect(Collectors.toList());
        if(invIds.size()>0){
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .in(SellingDO::getInvId, invIds)
            );
            invDOList.forEach(invDO -> {
                Optional<SellingDO> first = sellingDOS.stream().filter(i -> i.getInvId().equals(invDO.getId())).findFirst();
                if(!first.isPresent()){
                    invMapper.deleteById(invDO.getId());
                    return;
                }
                SellingDO sellingDO = first.get();
                if (paySteamOrderService.getExpOrder(sellingDO.getId()).size() > 0) {
                    invMapper.updateById(new InvDO().setId(invDO.getId()).setStatus(CommonStatusEnum.DISABLE.getStatus()));
                    return;
                }
                if(sellingDO.getTransferStatus().equals(InvTransferStatusEnum.INIT.getStatus())){
                    sellingMapper.deleteById(sellingDO.getId());
                    invMapper.deleteById(invDO.getId());
                }
                if(sellingDO.getTransferStatus().equals(InvTransferStatusEnum.SELL.getStatus())){
                    sellingMapper.deleteById(sellingDO.getId());
                    invMapper.deleteById(invDO.getId());
                }
            });
        }
        List<InvDescDO> invDescDOList = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .eq(InvDescDO::getSteamId, bindUserDO.getSteamId())
                .ne(InvDescDO::getBatchNo, batchNo)
        );
        invDescDOList.forEach(invDescDO -> {
            invDescMapper.updateById(new InvDescDO().setId(invDescDO.getId()).setTradable(0));
        });
    }
    // 从steam获取用户库存信息
    public InventoryDto gitInvFromSteam (BindUserDO bindUserDO)  {
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
        InventoryDto json = JacksonUtils.readValue(proxyResponseVo.getHtml(), InventoryDto.class);
        if (json == null) {
            throw new ServiceException(-1, "访问steam库存过于频繁，请稍后再试/或当前库存为空");
        }
        return json;
    }
    public InventoryDto gitInvFromSteam2 (BindUserDO bindUserDO) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();//-- builder = HttpUtil.ProxyRequestVo.builder();
        builder.url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String, String> pathVar = new HashMap<>();
        pathVar.put("steamId", bindUserDO.getSteamId());
        pathVar.put("app", "730");
        builder.pathVar(pathVar);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        InventoryDto json1 = sent.json(InventoryDto.class);
        if (json1 == null) {
            throw new ServiceException(-1, "访问steam库存过于频繁，请稍后再试/或当前库存为空");
        }
        return json1;
    }
    public InventoryDto gitInvFromSteam3 (BindUserDO bindUserDO) {
        String data= null;
        try {
            data = IoUtil.read(new FileReader("text.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InventoryDto json = JacksonUtils.readValue(data, InventoryDto.class);
        if (json == null) {
            throw new ServiceException(-1, "访问steam库存过于频繁，请稍后再试/或当前库存为空");
        }
        return json;
    }
}