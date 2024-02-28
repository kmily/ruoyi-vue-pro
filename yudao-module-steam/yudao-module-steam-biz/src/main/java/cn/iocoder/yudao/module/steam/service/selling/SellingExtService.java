package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingSaveReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.SELLING_NOT_EXISTS;

/**
 * 在售饰品 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class SellingExtService {

    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private InvMapper invMapper;
    @Autowired
    private SellingService sellingService;
    @Resource
    private InvDescMapper invDescMapper;

    /**
     * @param invPageReqVos
     * @return
     */
    public SellingDO getInvPage(InvPageReqVo invPageReqVos) {
        InvDO invDO = invMapper.selectById(invPageReqVos.getId());
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
            sellingDO.setPrice(invPageReqVos.getPrice());
            sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            sellingMapper.insert(sellingDO);
            return sellingDO;
        }
    }
}