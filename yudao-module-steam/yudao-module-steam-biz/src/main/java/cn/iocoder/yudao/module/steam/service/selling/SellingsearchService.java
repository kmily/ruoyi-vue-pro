package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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


    public PageResult<SellingRespVO> sellingPageSearch(SellingPageReqVO pageReqVO) {
        List<SellingDO> collect = sellingMapper.selectPage(pageReqVO).getList().stream().distinct().collect(Collectors.toList());
        PageResult<SellingDO> invPage = sellingService.getSellingPage(pageReqVO);
        InvDescPageReqVO invDescPageReqVO = new InvDescPageReqVO();
        invDescPageReqVO.setClassid(pageReqVO.getClassid());
        invDescPageReqVO.setInstanceid(pageReqVO.getInstanceid());
        PageResult<InvDescDO> invDescDOPageResult = invDescMapper.selectPage(invDescPageReqVO);

        List<SellingRespVO> sellingRespVOList = new ArrayList<>();
        for(SellingDO s : collect){
            SellingRespVO tempSellingRespVO = new SellingRespVO();
            tempSellingRespVO.setId(s.getId());
            tempSellingRespVO.setInvId(s.getInvId());
            tempSellingRespVO.setInvDescId(s.getInvDescId());
            tempSellingRespVO.setAssetid(s.getAssetid());
            tempSellingRespVO.setClassid(s.getClassid());
            tempSellingRespVO.setInstanceid(s.getInstanceid());
            tempSellingRespVO.setSteamId(s.getSteamId());
            tempSellingRespVO.setStatus(s.getStatus());
            // 价格
            tempSellingRespVO.setPrice(s.getPrice() / 100);
            // 图片
            tempSellingRespVO.setIconUrl(invDescDOPageResult.getList().get(0).getIconUrl());
            // 武器名称
            tempSellingRespVO.setMarketName(invDescDOPageResult.getList().get(0).getMarketName());
            sellingRespVOList.add(tempSellingRespVO);
        }
        return new PageResult<>(sellingRespVOList, invPage.getTotal());

    }


    // 在售商品列表
    public PageResult<SellingDO> sellView(SellingPageReqVO pageReqVO) {

        PageResult<SellingDO> sellingPage = sellingService.getSellingPage(pageReqVO);
        List<SellingDO> sellingDO = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getInstanceid,sellingPage.getList().get(0).getInstanceid())
                .eqIfPresent(SellingDO::getStatus,sellingPage.getList().get(0).getStatus()));

        return new PageResult<>(sellingDO, sellingDO.stream().count());
    }

}
