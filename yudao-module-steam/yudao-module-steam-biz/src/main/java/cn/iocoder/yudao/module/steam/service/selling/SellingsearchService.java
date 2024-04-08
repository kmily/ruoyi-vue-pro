package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppSellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.SellListItemResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.api.AppBatchGetOnSaleCommodityInfoRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingHashSummary;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.SteamQualityEnum;
import cn.iocoder.yudao.module.steam.enums.SteamRarityEnum;
import cn.iocoder.yudao.module.steam.enums.SteamWearCategoryEnum;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class SellingsearchService {

    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private SellingService sellingService;

    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private InvDescService invDescService;
    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private SellingExtMapper sellingExtMapper;
    //TODO 导航栏搜索
    public PageResult<SellListItemResp> sellList(AppSellingPageReqVO pageReqVO){
        PageResult<SellingDO> sellingDOPageResult = sellingMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName,pageReqVO.getMarketHashName())
                .eq(SellingDO::getStatus,CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getUserType, UserTypeEnum.MEMBER.getValue())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                        .geIfPresent(SellingDO::getPrice,pageReqVO.getMinPrice())
                        .leIfPresent(SellingDO::getPrice,pageReqVO.getMaxPrice())
                .orderByAsc(SellingDO::getPrice)
        );
        PageResult<SellListItemResp> sellingPageReqVOPageResult = BeanUtils.toBean(sellingDOPageResult, SellListItemResp.class);

        List<Long> collect = sellingDOPageResult.getList().stream().map(SellingDO::getUserId).distinct().collect(Collectors.toList());
        Map<Long, MemberUserRespDTO> memberUserRespDTOMap = memberUserApi.getUserList(collect).stream().collect(Collectors.toMap(MemberUserRespDTO::getId, item -> item));
        for (SellListItemResp item:sellingPageReqVOPageResult.getList()) {
            MemberUserRespDTO memberUserRespDTO = memberUserRespDTOMap.get(item.getUserId());
            memberUserRespDTO.setMobile("");
            memberUserRespDTO.setStatus(999);
            memberUserRespDTO.setPoint(999);
            memberUserRespDTO.setCreateTime(LocalDateTime.now());
            memberUserRespDTO.setLevelId(999l);
            item.setMemberUserRespDTO(memberUserRespDTO);
        }
        return sellingPageReqVOPageResult;
    }

    public PageResult<SellingRespVO> sellingPageSearch(SellingPageReqVO pageReqVO) {
//        List<SellingDO> collect = sellingMapper.selectPage(pageReqVO).getList().stream().distinct().collect(Collectors.toList());
        PageResult<SellingDO> invPage = sellingService.getSellingPage(pageReqVO);
        List<Long> collect2 = invPage.getList().stream().map(SellingDO::getInvDescId).collect(Collectors.toList());
        Map<Long, InvDescDO> invDescMap = new HashMap<>();
        if (!collect2.isEmpty()) {
            invDescMap = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .inIfPresent(InvDescDO::getId, collect2)).stream().collect(Collectors.toMap(InvDescDO::getId, o -> o, (v1, v2) -> v1));
        }
        List<SellingRespVO> sellingRespVOList = new ArrayList<>();
        for (SellingDO s : invPage.getList()) {
            SellingRespVO tempSellingRespVO = new SellingRespVO();
            tempSellingRespVO.setId(s.getId());
            tempSellingRespVO.setInvId(s.getInvId());
            tempSellingRespVO.setInvDescId(s.getInvDescId());
            tempSellingRespVO.setAssetid(s.getAssetid());
            tempSellingRespVO.setClassid(s.getClassid());
            tempSellingRespVO.setInstanceid(s.getInstanceid());
            tempSellingRespVO.setSteamId(s.getSteamId());
            tempSellingRespVO.setStatus(s.getStatus());
            tempSellingRespVO.setIconUrl(s.getIconUrl());
            tempSellingRespVO.setMarketName(s.getMarketName());
//            tempSellingRespVO.setMarketHashName(s.getMarketHashName());
            // 价格
            tempSellingRespVO.setPrice(s.getPrice());
            InvDescDO invDescDO = invDescMap.get(s.getInvDescId());
            if (!Objects.isNull(invDescDO)) {
                // 图片
                tempSellingRespVO.setIconUrl(invDescDO.getIconUrl());
                // 武器名称
                tempSellingRespVO.setMarketName(invDescDO.getMarketName());
            }
            sellingRespVOList.add(tempSellingRespVO);
        }
        return new PageResult<>(sellingRespVOList, invPage.getTotal());
    }

    // 在售商品列表
    public PageResult<SellingDO> sellView(SellingPageReqVO pageReqVO) {

        pageReqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        if (pageReqVO.getInstanceid() == null) {
            throw new ServiceException(-1, "instanceId不能为空");
        }
        PageResult<SellingDO> sellingPage;
        sellingPage = sellingService.getSellingPage(pageReqVO);
        return sellingPage;
    }

    //TODO 导航栏搜索
    public PageResult<SellingDO> getSearch(SellingPageReqVO pageReqVO){

        PageResult<SellingDO> invPage = sellingService.getSellingPage(pageReqVO);

        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .like(SellingDO::getMarketName, pageReqVO.getMarketName()));
            invPage.setTotal(Long.valueOf(sellingDOS.size()));
        return new PageResult<>(sellingDOS, invPage.getTotal());
    }

    /**
     * 根据hashnaem进行统计
     * @param marketHashName
     */
    public List<AppBatchGetOnSaleCommodityInfoRespVO> summaryByHashName(List<String> marketHashName) {
        List<SellingHashSummary> sellingHashSummaries = sellingExtMapper.SelectByHashName(marketHashName);
        return sellingHashSummaries.stream().map(item->{
            AppBatchGetOnSaleCommodityInfoRespVO appBatchGetOnSaleCommodityInfoRespVO = new AppBatchGetOnSaleCommodityInfoRespVO();
            AppBatchGetOnSaleCommodityInfoRespVO.SaleCommodityResponseDTO saleCommodityResponseDTO = new AppBatchGetOnSaleCommodityInfoRespVO.SaleCommodityResponseDTO();
            saleCommodityResponseDTO.setSellNum(item.getSellNum());
            saleCommodityResponseDTO.setMinSellPrice(item.getMinSellPrice());
            appBatchGetOnSaleCommodityInfoRespVO.setSaleCommodityResponse(saleCommodityResponseDTO);
            AppBatchGetOnSaleCommodityInfoRespVO.SaleTemplateResponseDTO saleTemplateResponseDTO = new AppBatchGetOnSaleCommodityInfoRespVO.SaleTemplateResponseDTO();
            saleTemplateResponseDTO.setTemplateHashName(item.getMarketHashName());
            saleTemplateResponseDTO.setIconUrl(item.getIconUrl());
            if(Objects.nonNull(item.getSelRarity())){
                saleTemplateResponseDTO.setRarityName(SteamRarityEnum.valueOf(item.getSelRarity()).getName());
            }
            if(Objects.nonNull(item.getSelQuality())){
                saleTemplateResponseDTO.setQualityName(SteamQualityEnum.valueOf(item.getSelQuality()).getName());
            }
            if(Objects.nonNull(item.getSelExterior())){
                saleTemplateResponseDTO.setExteriorName(SteamWearCategoryEnum.valueOf(item.getSelExterior()).getName());
            }
            appBatchGetOnSaleCommodityInfoRespVO.setSaleTemplateResponse(saleTemplateResponseDTO);
            return appBatchGetOnSaleCommodityInfoRespVO;
        }).collect(Collectors.toList());
    }
}
