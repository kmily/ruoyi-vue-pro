package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author lgm
 * @date 2024/02/23
 * @discription 定时刷新用户表绑定 Steam 账号的 CSGO 库存
 */
@Slf4j
@Service
public class SteamInvService {

    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private InvService invService;

    @Resource
    private InvPreviewMapper invPreviewMapper;



    // 第二次访问库存(只读库存表)  分表查询
    public PageResult<AppInvPageReqVO> getInvPage1(InvPageReqVO invPageReqVO) {
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
     * 合并显示库存  部分可出售库存
     *
     * @param invToMerge
     * @return
     */
    public List<AppInvPageReqVO> mergeInv(List<InvDO> invToMerge) {
        // 用户库存
        if (invToMerge.isEmpty()) {
            return new ArrayList<>();
        }
        // 库存对应的详情表主键
        ArrayList<Long> invDescIdList = new ArrayList<>();
        for (InvDO invDO : invToMerge) {
            invDescIdList.add(invDO.getInvDescId());
        }
        // 根据 InvDescId 查询详情信息
        List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .in(InvDescDO::getId, invDescIdList)
                .eq(InvDescDO::getTradable, 1));
        List<Long> enableInvDescId = invDescDOS.stream().map(InvDescDO::getId).collect(Collectors.toList());
        List<InvDO> collect = invToMerge.stream().filter(i -> enableInvDescId.contains(i.getInvDescId())).collect(Collectors.toList());

        HashMap<Long, InvDescDO> map = new HashMap<>();
        for (InvDescDO invDescDO : invDescDOS) {
            map.put(invDescDO.getId(), invDescDO);
        }

        List<String> sellingHashNameList = invDescDOS.stream().map(InvDescDO::getMarketHashName).distinct().collect(Collectors.toList());
        List<InvPreviewDO> invPreviewDOS;
        if(sellingHashNameList.isEmpty()){
             invPreviewDOS = new ArrayList<>();
        } else {
            invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
            .in(InvPreviewDO::getMarketHashName, sellingHashNameList));
        }

        Map<String, InvPreviewDO> mapInvPreview = new HashMap<>();
        if (Objects.nonNull(invPreviewDOS)) {
            mapInvPreview = invPreviewDOS.stream().collect(Collectors.toMap(InvPreviewDO::getMarketHashName, i -> i, (v1, v2) -> v1));
        }

        // 合并显示库存
        Map<Long, AppInvPageReqVO> stringAppInvPageReqVOMap = new HashMap<>();

        for (InvDO item : collect) {
            AppInvPageReqVO appInvPageReqVO = stringAppInvPageReqVOMap.get(item.getInvDescId());
            if (Objects.isNull(appInvPageReqVO)) {
                appInvPageReqVO = new AppInvPageReqVO();
                // 图片
                appInvPageReqVO.setIconUrl(map.get(item.getInvDescId()).getIconUrl());
                // 中文名称
                appInvPageReqVO.setMarketName(map.get(item.getInvDescId()).getMarketName());
                appInvPageReqVO.setMarketHashName(map.get(item.getInvDescId()).getMarketHashName());
                if(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName())==null){
                    appInvPageReqVO.setPrice(0);
                    appInvPageReqVO.setItemInfo(new C5ItemInfo());
                } else {
                    appInvPageReqVO.setPrice(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName()).getMinPrice());
                    appInvPageReqVO.setItemInfo(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName()).getItemInfo());
                }
                appInvPageReqVO.setInvId(Collections.emptyList());
                stringAppInvPageReqVOMap.put(item.getInvDescId(), appInvPageReqVO);
            }
            List<Long> invId = appInvPageReqVO.getInvId();
            appInvPageReqVO.setInvId(Stream.of(invId, Arrays.asList(item.getId())).flatMap(Long -> Long.stream()).collect(Collectors.toList()));
        }
        return stringAppInvPageReqVOMap.values().stream().collect(Collectors.toList());
    }



    /**
     * 合并显示库存  全部库存  包含不可出售库存
     *
     * @param invToMerge
     * @return
     */
    public List<AppInvPageReqVO> mergeInvAll(List<InvDO> invToMerge) {
        // 用户库存
        if (invToMerge.isEmpty()) {
            return new ArrayList<>();
        }
        // 库存对应的详情表主键
        ArrayList<Long> invDescIdList = new ArrayList<>();
        for (InvDO invDO : invToMerge) {
            invDescIdList.add(invDO.getInvDescId());
        }
        // 根据 InvDescId 查询详情信息
        List<InvDescDO> invDescDOS = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .in(InvDescDO::getId, invDescIdList));
//                .eq(InvDescDO::getTradable, 1));
//        List<Long> enableInvDescId = invDescDOS.stream().map(InvDescDO::getId).collect(Collectors.toList());
//        List<InvDO> collect = invToMerge.stream().filter(i -> enableInvDescId.contains(i.getInvDescId())).collect(Collectors.toList());

        HashMap<Long, InvDescDO> map = new HashMap<>();
        for (InvDescDO invDescDO : invDescDOS) {
            map.put(invDescDO.getId(), invDescDO);
        }

        List<String> sellingHashNameList = invDescDOS.stream().map(InvDescDO::getMarketHashName).distinct().collect(Collectors.toList());
        List<InvPreviewDO> invPreviewDOS;
        if(sellingHashNameList.isEmpty()){
            invPreviewDOS = new ArrayList<>();
        } else {
            invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .in(InvPreviewDO::getMarketHashName, sellingHashNameList));
        }

        Map<String, InvPreviewDO> mapInvPreview = new HashMap<>();
        if (Objects.nonNull(invPreviewDOS)) {
            mapInvPreview = invPreviewDOS.stream().collect(Collectors.toMap(InvPreviewDO::getMarketHashName, i -> i, (v1, v2) -> v1));
        }

        // 合并显示库存
//        List<AppInvPageReqVO> appInvPageReqVO = new ArrayList<>();
        Map<Long, AppInvPageReqVO> stringAppInvPageReqVOMap = new HashMap<>();

        for (InvDO item : invToMerge) {
            AppInvPageReqVO appInvPageReqVO = stringAppInvPageReqVOMap.get(item.getInvDescId());
            if (Objects.isNull(appInvPageReqVO)) {
                appInvPageReqVO = new AppInvPageReqVO();
                // 图片
                appInvPageReqVO.setIconUrl(map.get(item.getInvDescId()).getIconUrl());
                // 中文名称
                appInvPageReqVO.setMarketName(map.get(item.getInvDescId()).getMarketName());
                appInvPageReqVO.setMarketHashName(map.get(item.getInvDescId()).getMarketHashName());
                if(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName())==null){
                    appInvPageReqVO.setPrice(0);
                    appInvPageReqVO.setItemInfo(new C5ItemInfo());
                } else {
                    appInvPageReqVO.setPrice(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName()).getMinPrice());
                    appInvPageReqVO.setItemInfo(mapInvPreview.get(map.get(item.getInvDescId()).getMarketHashName()).getItemInfo());
                }

                appInvPageReqVO.setInvId(Collections.emptyList());
                stringAppInvPageReqVOMap.put(item.getInvDescId(), appInvPageReqVO);
            }

            List<Long> invId = appInvPageReqVO.getInvId();
            appInvPageReqVO.setInvId(Stream.of(invId, Arrays.asList(item.getId())).flatMap(Long -> Long.stream()).collect(Collectors.toList()));

        }
        return stringAppInvPageReqVOMap.values().stream().collect(Collectors.toList());


    }



}
