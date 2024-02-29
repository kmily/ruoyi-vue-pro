package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.SellingReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Optional;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 在售饰品 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
@Slf4j
public class SellingExtService {

    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;

    /**
     * @param invPageReqVos
     * @return
     */
    public SellingDO getToSale(InvPageReqVo invPageReqVos) {
        InvDO invDO = invMapper.selectById(invPageReqVos.getId());
        // TODO 上架验证
        if (invDO.getTransferStatus() != 0) {
            throw new ServiceException(-1, "商品已上架");
        } else {
            invDO.setPrice(invPageReqVos.getPrice());
            invDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            invMapper.updateById(invDO);
            Optional<InvDescDO> invDescDO = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                    .eq(InvDescDO::getClassid, invDO.getClassid())
                    .eq(InvDescDO::getInstanceid, invDO.getInstanceid())).stream().findFirst();
            if (!invDescDO.isPresent()) {
                throw new ServiceException(-1, "exists");
            }
            Long l = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getId, invPageReqVos.getId())
            );
            if (l > 0) {
                throw new ServiceException(-1, "exists");
            }
            SellingDO sellingDO = new SellingDO();
            sellingDO.setAppid(invDO.getAppid());
            sellingDO.setAssetid(invDO.getAssetid());
            sellingDO.setClassid(invDO.getClassid());
            sellingDO.setInstanceid(invDO.getInstanceid());
            sellingDO.setAmount(invDO.getAmount());
            sellingDO.setSteamId(invDO.getSteamId());
            sellingDO.setUserId(invDO.getUserId());
            sellingDO.setUserType(invDO.getUserType());
            sellingDO.setBindUserId(invDO.getBindUserId());
            sellingDO.setContextid(invDO.getContextid());
            sellingDO.setInvDescId(invDescDO.get().getId());
            if (invPageReqVos.getPrice() == null){
                throw new ServiceException(-1 ,"未设置价格");
            }
            sellingDO.setPrice(invPageReqVos.getPrice());
            sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            sellingMapper.insert(sellingDO);
            return sellingDO;
        }
    }

    public Optional<SellingDO> getOffSale(@Valid SellingReqVo sellingReqVo) {
        // 访问用户库存
        InvDO invDO = invMapper.selectById(sellingReqVo.getId());
        // 获取当前上架信息
        Optional<SellingDO> sellingDO1 = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getClassid, invDO.getClassid())
                .eq(SellingDO::getInstanceid, invDO.getInstanceid())).stream().findFirst();
        // 用户主动下架,当前饰品还存在用户steam库存中
        if (invDO.getTransferStatus() == InvTransferStatusEnum.INIT.getStatus()) {
            // 是否在库存
            throw new ServiceException(-1, "商品未上架");
        }
        if (sellingDO1.get().getTransferStatus() == InvTransferStatusEnum.INORDER.getStatus()){
            throw new ServiceException(-1, "商品已下单");
        }
        log.info(String.valueOf(sellingDO1.get().getId()));
        Long id = sellingDO1.get().getId();
        // 下架(设置transferstatus为‘0’未出售)
        invDO.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
        invDO.setPrice(null);
        invMapper.updateById(invDO);
        sellingMapper.deleteById(id);
        return sellingDO1;
    }
}