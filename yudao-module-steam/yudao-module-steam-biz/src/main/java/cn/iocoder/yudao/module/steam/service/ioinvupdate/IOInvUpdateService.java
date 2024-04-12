package cn.iocoder.yudao.module.steam.service.ioinvupdate;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.OtherSellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.OtherTemplatePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppOtherInvDetailListVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppOtherInvListDto;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.OtherSellingV5DetailListVDo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.OtherSellingV5ListDo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.OtherSellingV5WeaponDo;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.AvatarResponse;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.otherselling.OtherSellingMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.othertemplate.OtherTemplateMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewService;
import cn.iocoder.yudao.module.steam.service.otherselling.OtherSellingService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.service.steam.OtherSellingStatusEnum;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.ehcache.xml.model.ThreadPoolsType;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Component
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

    @Resource
    private InvPreviewMapper invPreviewMapper;
    @Resource
    private InvPreviewService invPreviewService;
    @Resource
    private SteamService steamService;
    @Resource
    private OtherSellingService otherSellingService;

    @Resource
    private OtherSellingMapper otherSellingMapper;
    @Resource
    private OtherTemplateMapper otherTemplateMapper;
    @Resource
    private ApplicationContext applicationContext;

//    @Async

    // 从steam获取用户库存信息
    public InventoryDto gitInvFromSteam(BindUserDO bindUserDO) throws JsonProcessingException {
        HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
        builder.url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=1000");
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String, String> pathVar = new HashMap<>();
        pathVar.put("steamId", bindUserDO.getSteamId());
        pathVar.put("app", "730");
        builder.pathVar(pathVar);
        HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build(), steamService.getBindUserIp(bindUserDO));
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
     * 插入库存
     */
    @Transactional
    public List<InvDescDO> firstInsertInventory(InventoryDto inventoryDto, BindUserDO bindUserDO) {
        // inv 表入库
        List<InvDO> invDOList = new ArrayList<>();
        for (InventoryDto.AssetsDTO assetsDTO : inventoryDto.getAssets()) {
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
        for (InventoryDto.DescriptionsDTOX item : inventoryDto.getDescriptions()) {
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
        // 插入desc
        invDescMapper.insertBatch(invDescDOList);
        List<InvDescDO> invDescIdList = new ArrayList<>();
        // 给inv表绑定descID
        for (InvDO item : invDOList) {
            List<InvDescDO> invDescIDList = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getInstanceid, item.getInstanceid())
                    .eq(InvDescDO::getSteamId, item.getSteamId())
                    .eq(InvDescDO::getClassid, item.getClassid())
                    .orderByDesc(InvDescDO::getId));
            InvDO invDescId = new InvDO();
            invDescId.setInstanceid(item.getInstanceid());
            invDescId.setClassid(item.getClassid());
            invDescId.setInvDescId(invDescIDList.get(0).getId());
            invDescId.setSteamId(item.getSteamId());
            invMapper.update(invDescId, new LambdaQueryWrapperX<InvDO>()
                    .eq(InvDO::getInstanceid, invDescId.getInstanceid())
                    .eq(InvDO::getClassid, invDescId.getClassid())
                    .eq(InvDO::getSteamId, invDescId.getSteamId())
                    .eq(InvDO::getTransferStatus, 0));
            invDescIdList.add(invDescIDList.get(0));
        }
        // 返回每一条 inv 对应的 desc 详情描述信息
        return invDescIdList;
    }


    /**
     * 删除库存  删除原有的 transferStatus = 0 的库存 插入新的库存，并比对 selling 表中的内容
     */
    public void deleteInventory(BindUserDO bindUserDO) {
        InvPageReqVO invDO = new InvPageReqVO();
        invDO.setSteamId(bindUserDO.getSteamId());
        invDO.setUserId(bindUserDO.getUserId());
        invDO.setBindUserId(bindUserDO.getId());
        // 查找未上架的库存并删除
        List<InvDO> invDOS = invMapper.selectList((new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getSteamId, invDO.getSteamId()))
                .eq(InvDO::getTransferStatus, 0));

        List<Long> invDescIdList = new ArrayList<>();
        List<Long> invIdList = new ArrayList<>();

        for (InvDO inv : invDOS) {
            invIdList.add(inv.getId());
            invDescIdList.add(inv.getInvDescId());
        }
        if (!invIdList.isEmpty()) {
            invMapper.deleteBatchIds(invIdList);
            invDescMapper.deleteBatchIds(invDescIdList);
        }
    }


    /**
     * 合并库存----查询库存方法  不分页
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
     * 合并库存----查询库存方法  不分页
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
     * 按入参查询库存  或者合并库存
     * 入参可传  库存主键ID  唯一资产ID
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


    @Async
    // 其他平台饰品模板入库
    public void otherTemplateInsert() {
        otherSellingMapper.delete(new LambdaQueryWrapperX<OtherSellingDO>().eq(OtherSellingDO::getPlatformIdentity,OtherSellingStatusEnum.C5.getStatus()));
        IOInvUpdateService bean = applicationContext.getBean(IOInvUpdateService.class);
        int page = 1;
        String[] types = {
                "CSGO_Type_Knife",
                "Type_Hands",
                "CSGO_Type_Rifle",
                "CSGO_Type_Pistol",
                "CSGO_Type_SMG",
                "CSGO_Type_Shotgun",
                "CSGO_Type_Machinegun",
                "CSGO_Tool_Sticker",
                "CSGO_Type_other",
                "type_customplayer"
        };
        for (String type : types) {
            while (true) {
                OkHttpClient client = new OkHttpClient();
                String url = "https://app.zbt.com/open/product/v2/search?appId=730&language=zh-CN&type=" + type + "&app-key=3453fd2c502c51bcd6a7a68c6e812f85&page=" + Integer.toString(page++);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseBody = response.body().string();
                    AppOtherInvListDto response_ = objectMapper.readValue(responseBody, new TypeReference<AppOtherInvListDto>() {
                    });
                    for (AppOtherInvListDto.Data_.GoodsInfo item : response_.getData().getList()) {
                        if (item == null || item.getPriceInfo() == null || item.getPriceInfo().getQuantity() <= 0) {
                            continue;
                        }
                        int getNum = (int) Math.ceil(200 * Math.min(7, Math.log(item.getPriceInfo().getQuantity())) / 7 * (Math.random() * 0.2 + 0.7));
                        bean.otherSellingInsert(item, getNum);
                    }
                    if (page >= response_.getData().getPages()) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }


    // 其他平台自动在售入库
    @Async
    public void otherSellingInsert(AppOtherInvListDto.Data_.GoodsInfo goodsInfo, Integer getNum) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://app.zbt.com/open/product/v1/sell/list?appId=730&language=zh-CN&app-key=3453fd2c502c51bcd6a7a68c6e812f85&limit="
                + getNum + "&itemId=" + goodsInfo.getItemId();
        Request detailList = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(detailList).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper objectMappers = new ObjectMapper();
                objectMappers.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                AppOtherInvDetailListVO response_ = objectMappers.readValue(responseBody, new TypeReference<AppOtherInvDetailListVO>() {
                });
                int userGoodsNum = (int) (Math.random() * 7) + 3;
                int userGoodsNumNow = 0;
                Float lastPrice = 0f;
                Map<String, String> UserInfo = randomUserInfo();

                for (AppOtherInvDetailListVO.Data__.CommodityInfo items : response_.getData().getList()) {
                    if (userGoodsNumNow++ >= userGoodsNum || !Objects.equals(lastPrice, items.getPrice())) {
                        if (items == null) {
                            continue;
                        }
                        lastPrice = items.getPrice();
                        userGoodsNumNow = 0;
                        UserInfo = randomUserInfo();
                    }
                    OtherSellingDO otherSellingDO = new OtherSellingDO();
                    otherSellingDO.setAppid(730);
                    otherSellingDO.setIconUrl(items.getImageUrl());
                    otherSellingDO.setMarketName(items.getItemName());
                    otherSellingDO.setMarketHashName(items.getMarketHashName());
                    otherSellingDO.setPlatformIdentity(OtherSellingStatusEnum.C5.getStatus());
                    otherSellingDO.setPrice((int) (items.getPrice() * 6.75 * 100));
                    otherSellingDO.setSelExterior(items.getItemInfo().getExteriorName());
                    otherSellingDO.setSelQuality(items.getItemInfo().getQualityName());
                    otherSellingDO.setSelRarity(items.getItemInfo().getRarityName());
                    otherSellingDO.setSelType(items.getItemInfo().getTypeName());
                    otherSellingDO.setSellingAvator(UserInfo.get("avatarUrl"));
                    otherSellingDO.setSellingUserName(UserInfo.get("nickName"));
                    otherSellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
                    otherSellingMapper.insert(otherSellingDO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> randomUserInfo() {
        Map<String, String> userInfo = new HashMap<String, String>();

        // 获取随机昵称
        String text = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ靖涵远舟浩润依夫博弘杰洋禹朋昊珂展舟韵辉泽杭彬元雨帆颂鸣文正丰元弘熙翰华君宇依祥升安晓琳德佑智远轩棋彬尚展畅兆栋沐霖世星俊绍晋哲泽辉浩哲家庆延政彬泰棋文泽城智月宇灿彪尊依林翰辰旭泽清乾圣恒涛博宗杰启宸柯依哲舟健瀚韶华棋胜晨杭天淇知国弘琪弘桦荣庭涛文顺楠志毅寒云至海秀楷君谦瀚辰政骏依蓓君冠庸玉弘羽星磊昌德亚亮忆夏风宇君翔棋帆浩天瀚帆锋启博振浩璋博若宇帆韵磊温谨龙升佳妍志鹤弘恒泽韶宇鹤江潮亦升越叶若溪博均晋新博亦苑涛乐彬璐瑶强恒文鹤智翎弘元昊柏经辰宜涵旭豪浩修青林寒霖昌耀源奕亦欣宇栋轩峻鸿润昌信翰轩天迅泽俊昌国知彰弘易博恩棋苑信坤琬琰弘瑜文德柯仁远辰全智霖健奕恺德欢文义尚兴依振鼎茂迅茂宜宏辉轩圣伯弘泰文庭万松祥衡楚阳远丰睿圣常柯棋天棕德曦咏灵芯天佑泰运维新志朋彤逸仲信宏仪荣茂泰恒彬逸达鸣宇承清磊远哲圣瀚文方鸿颜圣德世翰家林嘉乐宇尚晋辰晋拓翰瑞涛瀚宽经浩轩瀚景昌辉天鸣明耀博远鑫亦意隆文峻涛影博奇昌智瀚东品江毅宸韵皓翰宇浩龙智新翰尚辰卫璟妍展轩辉宇德信杰融文盛龙龙睿淇弥茂俊桦文楷圣龙君遥江佑译星圣琪俊翰文林圣楠月华嘉浚智轩光辉旭柏博仁致波晓奇永浚正阳仁顺哲伦明楷志伟文博信永嘉佑旭哲博帆寒远博昌博皓明震咏笑远达文青志材彤玮博辰祖奕懿桐智城鼎辉文语远松云新奕兴柯洁晏逸龙赫宏烁朋良庆浩松建铭煊成源侨秋世秋钧汉亮锡浩玮萧彬同彦伟伟渝世渝辰纶霖廷俊润梦成祖儒磊毅秦凯杉云磊尊榕筱桐辰栋航鸣聪家洋威乐毅磊田宇瀚雄浩兴正浩海启杭坤宗源勤一航喜锦庆硕克亮卓奕司楚勤然楠皓洋瀚嘉祥彦汉锦涛权信海奇思瑜武建瀚瑄海喜宥奇廷隆蓝中霖杰志廷景良晟谦锡喜宥嘉顺经沐林咏亮霆河勤志友政维友宥林书琳兆京颂旭旭森明杜楚立克伟笙夏云宗侨霆品京建源麟宥飞航思雨信然飞镇榕岳隽鸿尊梁龙玮顾诚林宸伟宏霖宁祥新乐天麟浩佳宏兆正劲鸣杜梁天岳杜盛瀚锋明辰基德正廉正鹏楠麟瑞良阳辉昊熙嘉健伊智梁协才骏庆龙杭亦裕隆良颂奕武纶枫勇家田权辉司仕腾瑄宗毅生民宇鑫阳健明家铭明睿成田品启树宽卓译麟良喜霖浩乐铭晟轩友思镇同琛廉郴杭力瑾曦伊振大武勤兴林译祥安道源淑宸信郴衡勇杭衡隆硕杉风辉友毅硕镇鸿正腾瑞维田镇乐广汉亦俊成峰禹晟卫栋森新力烽辰升琛祎涵聪亦心振生崴协杰永森东风皓熙聪亦志阳麟贯建兴冬镇琛轩智健贯源宽轩勇震卫俊兴武航楚俊逸鸿威风羲立仲罡森伦安泽涛尚志超茂浩子诚顺健勤剑宽蓝伦鸿轩辰斌伟锡琦经顺超协同佑友元子滨琛阳源良尊榕盛琛旻瑞广雨喆河晟毅浩浩曦楠曦庆宸仲然梓霖澄瀚海晨良海林杜东传宸传家泰东浩松涛子云海锋维超伟锋越桐曦鸣乐瑞狄杰阳冬兆斌渝伦云雪雄嘉宸林硕纶才驹东辰锋伊樱绮坤勤格蓝新思麟奇传锦品家艺柯明洋腾同哲勇建威广峰岳锡芊荟辰轩树奕勤浩健建劲树羲纶辰昊宁磊滨楚阳宸信基力逸晟京子艺羲旭狄枫卫嘉辰涛宽飞峰祖浩楠道协世毅云烽同兴曦震云伟宇宁锋明杰生驹华彪树建彪龙雷嘉彤廉绍希颂谦霖衡廷崴蓝晟钧铭宇景兴景司嘉亮隆洲河佳祖江佳琦泽旭树谦澄力京隆秦臣霆琛方萌威源勇嘉鸿烽森伊勤雄梁子楚琳传洋喜森聪辉冬家伊明镇格文静龙瀚瑄涛浩新秋智云冬源志斯浩烽彬郴明广佳森凯启渝浚轩振达飞浩汉逸云瑄成冬祥贯震冬智岚楚树云云榕超迅兴民源秦侨雅维枫坤铭霖榕政彦天霖楚浚亦晴岚洋禹伊中良协钧晏楠奕清德禹轩宥郴瀚宥迅洲景彬东瀚旭炎尹忆郴诚尊正建绍宸利哲海中镇奕彤炎永琦洋晏同庆鹏宸朋树朋若华鸣朋勤麟广毅兴航风源森宗冠宇聪景伦仲佑彦楠兴镇谦梁云锦汐哲奇龙政晗伦海绍庆威朗志美茜基树雷镇斌源明飞家勤同新亚洛洋成朋权喜清盛宇新茂浩政小雯宥羲毅家儒锡祖哲毅司基聪筱贝栋旻曦喜冬兆信利隽信希友枫宸健东司仲杰泽健谦杰鑫仲良小贺海祖晏立海钧景宽瑄晟佑成莱夏旭澄隆涛子永亦朗毅景洋译奕熹亦浩协庆朋茂曦宸杰亦升坤伟桐茂衡云盛晏镇毅宏栋镇钧永涵蕴超岳梁洋品顺杭铭协洋骏品贺烨楠云廉永霆辰京硕麟宇新建佳茵家杰玮兴品友鹏嘉毅伊森中俊宇奕品格阳超臣志秋洲庆成坤慧珍曦然凯雷锋彦武冬庆曦瀚志欣远景兴旻浩郴民钧立经旻滨宇晓彤中聪坤志正学协希源锡隽硕歆雯龙迅天瑄俊田杜浩泽中民旭晋嘉曦毅林俊顺弘云霖彦明宽羲桐羽永成生晏建辰才狄磊维民威晗璐勇江顺秋涛辰树霖隆震兴明昶睿建林渝新风洋镇彦海瑞喜烽妤瑶伊生崴希廷新晏鹏宥铭武明健伟钧瑞志华臣诚贤杉兴杰利升泽涛辰翰驹雄译建兆镇佳伟生亮善怡岳曦锋霖旭斌清经同杜源信怡莹乐智宏辰楚茂新晟嘉秦廉盛沐薇旻建腾雷希霆勤兆晗航源坤羽祥品威健新浩晨威剑聪海品旭梓尧佳霆辰成伟硕良心冬品岳洋希文经江学兴廉奕伊经轩飞峰志贺北力鑫奕臣民协宁尊然品栋喜东旭风麟梁景协兆诚锦铭聪权海祖依宥洋宸海中景思仲晗楚浩锦浩渝顺曦杜景清绍铭鸿信旻亚嘉子杰纶硕铭仲渝彬坤铭云东鹏海亚宁辰健俊硕滨杉杜子维卫崴泽清正曦奕岳奕强宥松森良伟琦乐政思铭龙宸勇鸣树腾基才兴林茂锦翎贤林维生卫思勇云辰狄驹克依菲明剑景力浩宽司伟宁宸志信依乐成希毅晏隆雄廷品林逸同浩志悦廉权伦辰奕晏蓝楠大毅立蓝贺宁钧晟道学泽威源郴纶学家传浩瀚彦兴东彬宁勤嘉汉镇坤清崴承露立源远聪经澄钧驹俊广洋嘉祥展彦烽经维镇茂钧志彦枫华狄旭英锋伦翰硕楠鑫廷榕智嘉兆兴锦程尊坤锋翰琛嘉心奇友谦伊林心蕊辰兴庆谦宇锋汉正朋剑武建韵淇辰瀚斌伊源镇威晟秋罡琛侨阳光鑫经冬鹏庆卓钧建家奕庆飞诗卿思琛健臣杜智东明曦乐信兴溪晨钧浩腾世森森聪森乐霖侨洋筱栩勇杰基宽政禹伦涛希锦学道一权宁朗钧麟奇道亮宁宸贤隆杉胤锡秦钧浚顺源正俊建秦辉林晗乔雷侨永卓新格翰浩基琛渝坤榕梓雨宥瑄盛锦华岳良琛勇铭良仲韫迪亚楠亦斌安彦风家骏远聪海锦晟洋玮伟羲永基东喜浩滨新清宏彬晨景喜斌河强旭奕正振升新绮晴正宁力辰卓瀚霖哲新旭生世文豪伦铭秋林硕振轩哲骏瀚宏彦逸川天兴云尊远利明维超道然海楚文磊仲海良启新道新宗冬威琛奕丞景家立升曦剑浚霖硕泽鹏浩晨兮嘉亦贯晏源家成思伟亦晏楠靖扬卫鹏喜麟世然亦勇兴蓝诚麟念承晟旭传权曦楚霖廉浩晨洋锋丽莉乐杰勇升景岳镇源喜驹浚佳沐谦镇海冬硕哲侨飞树贯秦宸俊雨佳浩辰建伟亮轩杰田权志卓嘉庆波旭云儒聪霆志天磊辰克秋建泓杰鸿杰祖轩宽子盛楠琛镇瑞辉怡熠庆航信锋霆杜顺源霖毅宸铭浩天强辰京亦冬炎聪绍逸鹏浩炎梓博飞麟镇羲廉浩锋安颂曦坤安晓慧志奕威才建江仲希蓝鑫朗品哲旭朗琛霖震乐武子建骏浩司锋诗语剑宁升友霖晗侨林河凯臣威诗雯洋钧亦司心森世硕兴信生蓝裕翔景大克宸雷建浩明智权启浩润鑫鑫郴迅宏辰瑞祖中杰航海大佳星海崴协奕杰信秋骏毅宽侨罡紫妍翰家伟郴楚枫明喜玮瑞然曦泽桐明旭瑞顺崴佳涛硕杉辰澄毅翊竣权格渝生锦祥新政盛侨华宇萌淼烽坤狄坤杜亚宥狄浩剑世喜如冰枫伊彦宁树辰田亮宗森廷咏玺文飞烽兴冬狄威琛罡伊杭坤海煜赫兴骏硕锡彬彪勤勤宇景秋旻浩丞新汉亮海瀚云新生诚轩喜源礼辰秦瀚锡兆奕琦俊民铭洲庆天梦霖云浩力天鸣家勇逸华司颂新世轩龙浚健云奇鸣杉良司俊正祥维涛品榕旭乐曦茂绍劲诚政龙风宇豪权曦鹏兴智武道曦祥奕澄洲春雨郴宁志云镇成咏锡朋思兆志南旭洋民浩晏罡海乐明腾哲泽江洋梵毅司咏浚树烽瀚震树海绍宗志宏克成清祖廉诚俊隽玮兆兴锦跃昂瀚峰腾家炎源家协硕家彪诚星林栋亦楚风羲浚泽秋勇凯家尊宏丽希乐兴俊伦广力维铭晟峰永铭川河树奕基兆远建风毅崴咏诚鑫嘉阳冬轩锡麟中逸子辰秦传浩嘉璐海嘉瀚崴兴铭栋家凯鸣东震佳艺信卓航玮建顺毅霖瀚冬辉仲茂臻羲坤良栋洋崴钧宗洋浚超侨依扬杰栋景麟洋凯麟彪道明亦蓝羽汐弘禹绍霖成兴冬顺超新顺辰馨艺辰华兴伟海隽才宸源升宽学家钧麟辰升浩狄晏永钧谦浚学健琛凯志鹏辰楚绍斌尊彦榕思欣言启源超兴烽锋子佳钧祥乐天勤绍智汉飞衡阳彬曦勤依柠琦广然谦道滨贯栋钧信沫言廷克钧浩风宗骏大浚儒怡彤伦佑驹岳祖震咏彬新劲星圆宇仲琛同阳心浩朗振成伊浩栋超冬远鸿子烽景盛曦浩杭锦克隽瀚伦杜驹麟冬浩思浩友希树权勇飞骏景子安杭祥立兴阳腾乐毅榕蓝狄洋伊学伦林宏瀚民嘉岳锋嘉超朋家亦驹伦卓森亚栋嘉生坤峰中弘彦然希崴友超奕乐亦浩汉广廷成廷琦晏亦振龙海超栋炎基贯建朋松渝森新晗宏臣然锋杭智海道启树彦品楚震广浚安安镇渝天旻骏道毅良庆林树琛辰伟玮远绍龙庆茂朋镇家江秦辰杉嘉利栋";
        Random random = new Random();
        int charCount = random.nextInt(3) + 2; // 随机生成2-4个字
        Set<Character> selectedChars = new HashSet<>();
        while (selectedChars.size() < charCount) {
            int index = random.nextInt(text.length());
            char ch = text.charAt(index);

            // 确保字符不是空格,并且之前没有被选过
            if (ch != ' ' && !selectedChars.contains(ch)) {
                selectedChars.add(ch);
            }
        }
        StringBuilder randomChars = new StringBuilder();
        for (Character ch : selectedChars) {
            randomChars.append(ch);
        }
        String nickName = randomChars.toString();
        userInfo.put("nickName", nickName);

        // 获取随机头像
        String[] avatarUrls = {
                "https://api.uomg.com/api/rand.avatar?sort=男&format=json",
                "https://api.uomg.com/api/rand.avatar?sort=女&format=json",
                "https://api.uomg.com/api/rand.avatar?sort=动漫男&format=json",
                "https://api.uomg.com/api/rand.avatar?sort=动漫女&format=json"
        };
        Random randomImg = new Random();
        int randomIndex = randomImg.nextInt(avatarUrls.length);
        String selectedAvatarUrl = avatarUrls[randomIndex];
        String avatarUrl = "";
        try {
            URL url = new URL(selectedAvatarUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // 解析 JSON 数据
                String json = response.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                AvatarResponse avatarResponse = objectMapper.readValue(json, AvatarResponse.class);
                avatarUrl = avatarResponse.getImgUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInfo.put("avatarUrl", avatarUrl);
        return userInfo;
    }


    @Async
    public void otherTemplateInsertV5() {
        otherSellingMapper.delete(new LambdaQueryWrapperX<OtherSellingDO>().eq(OtherSellingDO::getPlatformIdentity,OtherSellingStatusEnum.V5.getStatus()));
        IOInvUpdateService bean = applicationContext.getBean(IOInvUpdateService.class);
        int page = 1;
            while (true) {
                OkHttpClient client = new OkHttpClient();
                String url = "https://v5item.com/api/market/weaponList";

                JSONObject paramObject = new JSONObject();
                paramObject.put("marketType", 0);
                paramObject.put("pageIndex", page++);
                paramObject.put("pageNum", 42);

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.12")
                        .addHeader("Accept","application/json, text/plain, */*")
                        .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString()))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseBody = response.body().string();
                    OtherSellingV5ListDo response_ = objectMapper.readValue(responseBody, new TypeReference<OtherSellingV5ListDo>() {
                    });
                    for (OtherSellingV5ListDo.Data_ item : response_.getData()) {
                        if (item == null ||  item.getOnSaleCount() <= 0) {
                            continue;
                        }
                        int getNum = (int) Math.ceil(200 * Math.min(7, Math.log(item.getOnSaleCount())) / 7 * (Math.random() * 0.2 + 0.7));
                        bean.otherSellingInsertV5(item, getNum);
                    }
                    if (page >= response_.getTotalCount() / 42) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
        }
    }

    @Async
    public void otherSellingInsertV5(OtherSellingV5ListDo.Data_ goodsInfo, Integer getNum) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://v5item.com/api/market/onSaleList";

        JSONObject paramObject = new JSONObject();
        paramObject.put("marketHashName", goodsInfo.getMarketHashName());
        paramObject.put("pageIndex", 1);
        paramObject.put("pageNum", getNum);

        Request detailList = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.12")
                .addHeader("Accept","application/json, text/plain, */*")
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString()))
                .build();

        OkHttpClient client2 = new OkHttpClient();
        String url2 = "https://v5item.com/api/market/weaponDetail";

        JSONObject paramObject2 = new JSONObject();
        paramObject2.put("marketHashName", goodsInfo.getMarketHashName());

        Request detailList2 = new Request.Builder()
                .url(url2)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.12")
                .addHeader("Accept","application/json, text/plain, */*")
                .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject2.toString()))
                .build();

        try (Response response = client.newCall(detailList).execute();Response response2 = client2.newCall(detailList2).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();String responseBody2 = response2.body().string();
                ObjectMapper objectMappers = new ObjectMapper();
                objectMappers.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                OtherSellingV5DetailListVDo response_ = objectMappers.readValue(responseBody, new TypeReference<OtherSellingV5DetailListVDo>() {
                });
                OtherSellingV5WeaponDo response2_ = objectMappers.readValue(responseBody2, new TypeReference<OtherSellingV5WeaponDo>() {
                });
                int userGoodsNum = (int) (Math.random() * 7) + 3;
                int userGoodsNumNow = 0;
                Float lastPrice = 0f;
                Map<String, String> UserInfo = randomUserInfo();

                for (OtherSellingV5DetailListVDo.Data_ items : response_.getData()) {
                    if (userGoodsNumNow++ >= userGoodsNum || !Objects.equals(lastPrice, items.getSalePrice())) {
                        if (items == null) {
                            continue;
                        }
                        lastPrice = items.getSalePrice();
                        userGoodsNumNow = 0;
                        UserInfo = randomUserInfo();
                    }
                    OtherSellingDO otherSellingDO = new OtherSellingDO();
                    otherSellingDO.setAppid(730);
                    otherSellingDO.setIconUrl(items.getWeaponInfo().getItemImg());
                    otherSellingDO.setMarketName(items.getWeaponInfo().getItemName());
                    otherSellingDO.setMarketHashName(items.getWeaponInfo().getMarketHashName());
                    otherSellingDO.setPlatformIdentity(OtherSellingStatusEnum.V5.getStatus());
                    otherSellingDO.setPrice((int) (items.getSalePrice() * 1.03 * 100));
                    otherSellingDO.setSelExterior(response2_.getData().getItemInfo().getItemExteriorName());
                    otherSellingDO.setSelQuality(response2_.getData().getItemInfo().getItemQualityName());
                    otherSellingDO.setSelRarity(response2_.getData().getItemInfo().getItemRarityName());
                    otherSellingDO.setSelType(response2_.getData().getItemInfo().getItemTypeName());
                    otherSellingDO.setSellingAvator(UserInfo.get("avatarUrl"));
                    otherSellingDO.setSellingUserName(UserInfo.get("nickName"));
                    otherSellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
                    otherSellingMapper.insert(otherSellingDO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

