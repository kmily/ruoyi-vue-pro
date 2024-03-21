package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.vo.order.QueryOrderReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.selling.SellingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Slf4j
public class InvOrderExtService {
    @Resource
    private InvOrderMapper invOrderMapper;
    @Resource
    private SellingMapper sellingMapper;
    public CommonResult<List<InvOrderDO>> getSellOrderWithPage(QueryOrderReqVo reqVo, LoginUser loginUser) {
        List<InvOrderDO> invOrderDO = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getSellUserId, loginUser.getId())
                .eq(InvOrderDO::getSellUserType, loginUser.getUserType()));
        // 返回图片
        List<SellingDO> sellingDO = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType()));

        List<InvOrderDO> invOrderDOList = new ArrayList<>();
        for (InvOrderDO invOrderDOTemp : invOrderDO){
            invOrderDOTemp.setOrderNo(reqVo.getOrderNo());
            invOrderDOList.add(invOrderDOTemp);
        }

        return CommonResult.success(invOrderDOList);
    }
}
