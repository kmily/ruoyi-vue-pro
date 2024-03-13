package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.AppSellingPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
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

    //TODO 导航栏搜索
    public PageResult<SellingDO> sellList(AppSellingPageReqVO pageReqVO){
        PageResult<SellingDO> sellingDOPageResult = sellingMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName,pageReqVO.getMarketHashName())
                .eq(SellingDO::getStatus,CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                        .geIfPresent(SellingDO::getPrice,pageReqVO.getMinPrice())
                        .leIfPresent(SellingDO::getPrice,pageReqVO.getMaxPrice())
                .orderByDesc(SellingDO::getPrice)
        );
        return sellingDOPageResult;
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
}
