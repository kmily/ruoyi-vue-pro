package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvMergeToSellPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;

import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.LowestSellingPriceVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
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

    @Resource
    private InvPreviewMapper invPreviewMapper;



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
                appInvPageReqVO1.setMarketHashName(map.get(invDO.getInvDescId()).getMarketHashName());
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
            if(Objects.nonNull(invPage.get(element.getMarketName())) ){
                AppInvMergeToSellPageReqVO appInvMergeToSellPageReqVO = invPage.get(element.getMarketName());
                ArrayList<String> list = new ArrayList<>(appInvMergeToSellPageReqVO.getAssetIdList());
                list.add(String.valueOf(element.getId()));
                appInvMergeToSellPageReqVO.setAssetIdList(list);
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
                appInvPageReqVO.setMarketHashName(element.getMarketHashName());
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


    /**
     * 查询在售参考价
     * 入参是库存的 MarketHashName
     */
//    public List<Map<String,Integer>> getLowestSellingPrice(LowestSellingPriceVO lowestSellingPriceVO){
//        List<InvPreviewDO> invPreviewDOS = new ArrayList<>();
//        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
//                .eq(InvPreviewDO::getMarketHashName, lowestSellingPriceVO.getMarketHashName()));
//        AppInvMergeToSellPageReqVO priceVO = new LowestSellingPriceVO();
//        for(InvPreviewDO item : invPreviewDOS){
//            priceVO.setMarketHashName(item.getMarketHashName());
//            priceVO.setAutoPrice(item.getAutoPrice());
//        }


//        return ;
//    }

}





