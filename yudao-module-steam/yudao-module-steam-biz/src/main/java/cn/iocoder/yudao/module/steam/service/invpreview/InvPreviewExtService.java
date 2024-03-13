package cn.iocoder.yudao.module.steam.service.invpreview;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.ItemResp;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 饰品在售预览 Service 实现类
 *
 * @author LeeAm
 */
@Service
public class InvPreviewExtService {

    @Resource
    private InvPreviewMapper invPreviewMapper;



    public PageResult<ItemResp> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        PageResult<InvPreviewDO> invPreviewDOPageResult = invPreviewMapper.selectPage(pageReqVO);
        List<ItemResp> ret=new ArrayList<>();
        for (InvPreviewDO item:invPreviewDOPageResult.getList()){

            ItemResp itemResp = BeanUtils.toBean(item, ItemResp.class);
            itemResp.setAutoPrice(new BigDecimal(item.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            itemResp.setSalePrice(new BigDecimal(item.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            ret.add(itemResp);
        }
        return new PageResult<>(ret, invPreviewDOPageResult.getTotal());
    }

    /**
     * 增加库存标识
     * @param marketHashName 标签名称
     */
    public void markInv(String marketHashName) {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getMarketHashName, marketHashName));
        if(Objects.nonNull(invPreviewDOS)){
            invPreviewDOS.forEach(item->
                    invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(true)));
        }
    }
}