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
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.OtherSellListItemResp;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.SellListItemResp;
import cn.iocoder.yudao.module.steam.controller.app.vo.api.AppBatchGetOnSaleCommodityInfoRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.otherselling.OtherSellingMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingHashSummary;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.SteamQualityEnum;
import cn.iocoder.yudao.module.steam.enums.SteamRarityEnum;
import cn.iocoder.yudao.module.steam.enums.SteamWearCategoryEnum;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import cn.iocoder.yudao.module.steam.service.otherselling.vo.OtherSellingListDo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
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
    @Resource
    private OtherSellingMapper otherSellingMapper;

    //TODO 导航栏搜索
    public PageResult<SellListItemResp> sellList(AppSellingPageReqVO pageReqVO) {
        PageResult<SellingDO> sellingDOPageResult = sellingMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName, pageReqVO.getMarketHashName())
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getUserType, UserTypeEnum.MEMBER.getValue())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .geIfPresent(SellingDO::getPrice, pageReqVO.getMinPrice())
                .leIfPresent(SellingDO::getPrice, pageReqVO.getMaxPrice())
                .orderByAsc(SellingDO::getPrice)
        );
        PageResult<SellListItemResp> sellingPageReqVOPageResult = BeanUtils.toBean(sellingDOPageResult, SellListItemResp.class);

        List<Long> collect = sellingDOPageResult.getList().stream().map(SellingDO::getUserId).distinct().collect(Collectors.toList());
        Map<Long, MemberUserRespDTO> memberUserRespDTOMap = memberUserApi.getUserList(collect).stream().collect(Collectors.toMap(MemberUserRespDTO::getId, item -> item));
        for (SellListItemResp item : sellingPageReqVOPageResult.getList()) {
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
    public PageResult<SellingDO> getSearch(SellingPageReqVO pageReqVO) {

        PageResult<SellingDO> invPage = sellingService.getSellingPage(pageReqVO);

        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .like(SellingDO::getMarketName, pageReqVO.getMarketName()));
        invPage.setTotal(Long.valueOf(sellingDOS.size()));
        return new PageResult<>(sellingDOS, invPage.getTotal());
    }

    /**
     * 根据hashnaem进行统计
     *
     * @param marketHashName
     */
    public List<AppBatchGetOnSaleCommodityInfoRespVO> summaryByHashName(List<String> marketHashName) {
        List<SellingHashSummary> sellingHashSummaries = sellingExtMapper.SelectByHashName(marketHashName);
        return sellingHashSummaries.stream().map(item -> {
            AppBatchGetOnSaleCommodityInfoRespVO appBatchGetOnSaleCommodityInfoRespVO = new AppBatchGetOnSaleCommodityInfoRespVO();
            AppBatchGetOnSaleCommodityInfoRespVO.SaleCommodityResponseDTO saleCommodityResponseDTO = new AppBatchGetOnSaleCommodityInfoRespVO.SaleCommodityResponseDTO();
            saleCommodityResponseDTO.setSellNum(item.getSellNum());
            saleCommodityResponseDTO.setMinSellPrice(item.getMinSellPrice());
            appBatchGetOnSaleCommodityInfoRespVO.setSaleCommodityResponse(saleCommodityResponseDTO);
            AppBatchGetOnSaleCommodityInfoRespVO.SaleTemplateResponseDTO saleTemplateResponseDTO = new AppBatchGetOnSaleCommodityInfoRespVO.SaleTemplateResponseDTO();
            saleTemplateResponseDTO.setTemplateHashName(item.getMarketHashName());
            saleTemplateResponseDTO.setIconUrl(item.getIconUrl());
            if (Objects.nonNull(item.getSelRarity())) {
                saleTemplateResponseDTO.setRarityName(SteamRarityEnum.valueOf(item.getSelRarity()).getName());
            }
            if (Objects.nonNull(item.getSelQuality())) {
                saleTemplateResponseDTO.setQualityName(SteamQualityEnum.valueOf(item.getSelQuality()).getName());
            }
            if (Objects.nonNull(item.getSelExterior())) {
                saleTemplateResponseDTO.setExteriorName(SteamWearCategoryEnum.valueOf(item.getSelExterior()).getName());
            }
            appBatchGetOnSaleCommodityInfoRespVO.setSaleTemplateResponse(saleTemplateResponseDTO);
            return appBatchGetOnSaleCommodityInfoRespVO;
        }).collect(Collectors.toList());
    }

    public PageResult<SellListItemResp> allSaleList(AppSellingPageReqVO pageReqVO) {
        List<OtherSellingDO> otherSellingDOPageResult = otherSellingMapper.selectList(new LambdaQueryWrapperX<OtherSellingDO>()
                .eq(OtherSellingDO::getMarketHashName, pageReqVO.getMarketHashName())
                .eq(OtherSellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .orderByAsc(OtherSellingDO::getPrice));

        List<SellingDO> sellingDOPageResult = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName, pageReqVO.getMarketHashName())
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getUserType, UserTypeEnum.MEMBER.getValue())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .geIfPresent(SellingDO::getPrice, pageReqVO.getMinPrice())
                .leIfPresent(SellingDO::getPrice, pageReqVO.getMaxPrice())
                .orderByAsc(SellingDO::getPrice)
        );
        List<SellListItemResp> sellingPageReqVOPageResult = BeanUtils.toBean(sellingDOPageResult, SellListItemResp.class);
        List<Long> collect = sellingDOPageResult.stream().map(SellingDO::getUserId).distinct().collect(Collectors.toList());
        Map<Long, MemberUserRespDTO> memberUserRespDTOMap = memberUserApi.getUserList(collect).stream().collect(Collectors.toMap(MemberUserRespDTO::getId, item -> item));
        for (SellListItemResp item : sellingPageReqVOPageResult) {
            MemberUserRespDTO memberUserRespDTO = memberUserRespDTOMap.get(item.getUserId());
            memberUserRespDTO.setMobile("");
            memberUserRespDTO.setStatus(999);
            memberUserRespDTO.setPoint(999);
            memberUserRespDTO.setCreateTime(LocalDateTime.now());
            memberUserRespDTO.setLevelId(999l);
            item.setMemberUserRespDTO(memberUserRespDTO);
        }


        List<SellListItemResp> otherSellingDOPageResultA = new ArrayList<SellListItemResp>();
        for (OtherSellingDO element : otherSellingDOPageResult) {
            SellListItemResp otherSellingListDo1 = new SellListItemResp();
            otherSellingListDo1.setMarketHashName(element.getMarketHashName());
            otherSellingListDo1.setPrice(element.getPrice());
            otherSellingListDo1.setIconUrl(element.getIconUrl());
            otherSellingListDo1.setSelType(element.getSelType());
            otherSellingListDo1.setSelExterior(element.getSelExterior());
            otherSellingListDo1.setSelQuality(element.getSelQuality());
            otherSellingListDo1.setSelRarity(element.getSelRarity());
            otherSellingListDo1.setAppid(element.getAppid());
            otherSellingListDo1.setMarketName(element.getMarketName());
            MemberUserRespDTO memberUserRespDTO1 = new MemberUserRespDTO();
            memberUserRespDTO1.setNickname(element.getSellingUserName());
            memberUserRespDTO1.setAvatar(element.getSellingAvator());
            memberUserRespDTO1.setMobile("");
            memberUserRespDTO1.setStatus(999);
            memberUserRespDTO1.setPoint(999);
            memberUserRespDTO1.setCreateTime(LocalDateTime.now());
            memberUserRespDTO1.setLevelId(999l);
            otherSellingListDo1.setMemberUserRespDTO(memberUserRespDTO1);
            otherSellingDOPageResultA.add(otherSellingListDo1);
        }

        // 排序处理
        int otherIndex = 0, sellIndex = 0;
        int otherMax = otherSellingDOPageResultA.size() - 1;
        int sellMax = sellingDOPageResult.size() - 1;
        int otherPrice = 0, sellPrice = 0;

        List<SellListItemResp> returnList = new ArrayList<SellListItemResp>();
        while (true) {
            if (otherIndex > otherMax) {
                for (int i = sellIndex; i <= sellMax; i++) {
                    returnList.add(sellingPageReqVOPageResult.get(i));
                }
                break;
            } else if (sellIndex > sellMax) {
                for (int i = otherIndex; i <= otherMax; i++) {
                    returnList.add(otherSellingDOPageResultA.get(i));
                }
                break;
            }
            otherPrice = otherSellingDOPageResultA.get(otherIndex).getPrice();
            sellPrice = sellingPageReqVOPageResult.get(sellIndex).getPrice();
            if (otherPrice > sellPrice) {
                returnList.add(sellingPageReqVOPageResult.get(sellIndex));
                sellIndex++;
            } else {
                returnList.add(otherSellingDOPageResultA.get(otherIndex));
                otherIndex++;
            }
        }

        int PageNo = pageReqVO.getPageNo() <= 0 ? 1 : pageReqVO.getPageNo();
        int pageSize = pageReqVO.getPageSize() > 200 ? 200 : pageReqVO.getPageSize();
        pageSize = pageSize <= 0 ? 10 : pageSize;

        PageResult<SellListItemResp> resultList = new PageResult<SellListItemResp>();
        Integer dataSize = returnList.size();
        resultList.setTotal((long) dataSize);

        if ((PageNo - 1) * pageSize > dataSize) {
            resultList.setList(new ArrayList<SellListItemResp>());
        } else {
            returnList.subList(Math.max(0, (PageNo - 1) * pageSize), Math.min(dataSize, PageNo * pageSize));
            resultList.setList(returnList);
        }
        return resultList;
    }
}
