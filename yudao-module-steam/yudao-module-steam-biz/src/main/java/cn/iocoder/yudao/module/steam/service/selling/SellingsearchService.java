package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.invdesc.InvDescService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class SellingsearchService {

    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private InvDescService invDescService;
    @Resource
    private SellingService sellingService;


    public PageResult<SellingRespVO> sellingPageSearch(SellingPageReqVO pageReqVO) {
        List<SellingDO> collect = sellingMapper.selectPage(pageReqVO).getList().stream().distinct().collect(Collectors.toList());
        PageResult<SellingDO> invPage = sellingService.getSellingPage(pageReqVO);
        InvDescPageReqVO invDescPageReqVO = new InvDescPageReqVO();
        invDescPageReqVO.setClassid(pageReqVO.getClassid());
        invDescPageReqVO.setInstanceid(pageReqVO.getInstanceid());
        PageResult<InvDescDO> invDescDOPageResult = invDescMapper.selectPage(invDescPageReqVO);
        SellingRespVO sellingRespVO = new SellingRespVO();
        for(SellingDO s : collect){
            sellingRespVO.setId(s.getId());
            sellingRespVO.setInvId(s.getInvId());
            sellingRespVO.setInvDescId(s.getInvDescId());
            sellingRespVO.setAssetid(s.getAssetid());
            sellingRespVO.setClassid(s.getClassid());
            sellingRespVO.setInstanceid(s.getInstanceid());
            sellingRespVO.setSteamId(s.getSteamId());
            sellingRespVO.setStatus(s.getStatus());
            // 价格
            sellingRespVO.setPrice(s.getPrice());
            // 图片
            sellingRespVO.setIconUrl(invDescDOPageResult.getList().get(0).getIconUrl());
            // 武器名称
            sellingRespVO.setMarketName(invDescDOPageResult.getList().get(0).getMarketName());
        }
        return new PageResult<>(Collections.singletonList(sellingRespVO), invPage.getTotal());

    }
}
