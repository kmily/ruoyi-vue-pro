package cn.iocoder.yudao.module.steam.service.invpreview;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.ItemResp;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.PreviewReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 饰品在售预览 Service 实现类
 *
 * @author LeeAm
 */
@Service
public class InvPreviewExtService {

    @Resource
    private InvPreviewMapper invPreviewMapper;
    @Resource
    private SellingMapper sellingMapper;

    public ItemResp getInvPreview(PreviewReqVO reqVO) {

        Optional<InvPreviewDO> first = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>().eq(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())).stream().findFirst();
        if(first.isPresent()){
            InvPreviewDO invPreviewDO = first.get();
            ItemResp itemResp = BeanUtils.toBean(invPreviewDO, ItemResp.class);

            if(Objects.nonNull(invPreviewDO.getAutoPrice())){
                itemResp.setAutoPrice(new BigDecimal(invPreviewDO.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(invPreviewDO.getSalePrice())){
                itemResp.setSalePrice(new BigDecimal(invPreviewDO.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(invPreviewDO.getReferencePrice())){
                itemResp.setReferencePrice(new BigDecimal(invPreviewDO.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
            }
            return itemResp;
        }else{
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public PageResult<ItemResp> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        PageResult<InvPreviewDO> invPreviewDOPageResult = invPreviewMapper.selectPage(pageReqVO);
        List<ItemResp> ret=new ArrayList<>();
        for (InvPreviewDO item:invPreviewDOPageResult.getList()){

            ItemResp itemResp = BeanUtils.toBean(item, ItemResp.class);
            if(Objects.nonNull(item.getAutoPrice())){
                itemResp.setAutoPrice(new BigDecimal(item.getAutoPrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getSalePrice())){
                itemResp.setSalePrice(new BigDecimal(item.getSalePrice()).multiply(new BigDecimal("100")).intValue());
            }
            if(Objects.nonNull(item.getReferencePrice())){
                itemResp.setReferencePrice(new BigDecimal(item.getReferencePrice()).multiply(new BigDecimal("100")).intValue());
            }

            ret.add(itemResp);
        }
        return new PageResult<>(ret, invPreviewDOPageResult.getTotal());
    }

    /**
     * 增加库存标识
     * @param marketHashName 标签名称
     */
    public void markInvEnable(String marketHashName) {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getMarketHashName, marketHashName));
        if(Objects.nonNull(invPreviewDOS)){
            Long aLong = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getMarketHashName, marketHashName)
                    .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                    .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
            );
            invPreviewDOS.forEach(item->{
                C5ItemInfo itemInfo = item.getItemInfo();
                invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(aLong>0).setAutoQuantity(aLong.toString())
                        .setSelExterior(itemInfo.getExteriorName())
                        .setSelQuality(itemInfo.getQualityName())
                        .setSelRarity(itemInfo.getRarityName())
                        .setSelWeapon(itemInfo.getWeaponName())
                        .setSelType(itemInfo.getTypeName())
                        .setSelItemset(itemInfo.getItemSetName()));
            });
        }
    }
    @Async
    public Integer updateIvnFlag() {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getExistInv, true));
        Integer count=0;
        if(Objects.nonNull(invPreviewDOS)){
            for(InvPreviewDO item:invPreviewDOS){
                count++;
                updateIvnFlagAndQuantity(item);
            }
        }
        return count;
    }
    public void updateIvnFlagAndQuantity(InvPreviewDO invPreviewDO) {
        Long aLong = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getMarketHashName, invPreviewDO.getMarketHashName())
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
        );
        invPreviewMapper.updateById(new InvPreviewDO().setId(invPreviewDO.getId()).setAutoQuantity(aLong.toString()).setExistInv(aLong>0));
    }
}