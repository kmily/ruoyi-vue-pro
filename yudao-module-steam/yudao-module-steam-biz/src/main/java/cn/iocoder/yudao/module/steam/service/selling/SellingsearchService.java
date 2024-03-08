package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class SellingsearchService {

    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private InvDescMapper invDescMapper;


    public SellingRespVO sellingPageSearch(SellingPageReqVO pageReqVO) {
        List<SellingDO> collect = sellingMapper.selectPage(pageReqVO).getList().stream().distinct().collect(Collectors.toList());
        InvDescPageReqVO invDescPageReqVO = new InvDescPageReqVO();
        invDescPageReqVO.setClassid(pageReqVO.getClassid());
        invDescPageReqVO.setInstanceid(pageReqVO.getInstanceid());
        PageResult<InvDescDO> invDescDOPageResult = invDescMapper.selectPage(invDescPageReqVO);
//        Long invDescDOPageResult.getList().get(0).getIconUrl();
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
        return sellingRespVO;
    }
}
