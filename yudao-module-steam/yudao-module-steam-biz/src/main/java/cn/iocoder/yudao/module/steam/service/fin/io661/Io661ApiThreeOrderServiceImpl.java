package cn.iocoder.yudao.module.steam.service.fin.io661;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5ItemListVO;
import cn.iocoder.yudao.module.steam.service.fin.v5.vo.V5page;
import cn.iocoder.yudao.module.steam.service.fin.vo.*;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

//@Service
@Slf4j
public class Io661ApiThreeOrderServiceImpl implements ApiThreeOrderService {
    @Resource
    private SellingMapper sellingMapper;
    @Override
    public PlatCodeEnum getPlatCode() {
        return PlatCodeEnum.IO661;
    }

    @Override
    public ApiCommodityRespVo query(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO) {
        ApiCommodityRespVo apiCommodityRespVo=new ApiCommodityRespVo();
        List<SellingDO> sellingDOS = sellingMapper.selectList(new Page<>(1, 1), new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus())
                .orderByAsc(SellingDO::getPrice)
        );
        if(sellingDOS.size()>0){
            SellingDO sellingDO = sellingDOS.get(0);

            apiCommodityRespVo.setIsSuccess(true);
            apiCommodityRespVo.setPlatCode(getPlatCode());
            apiCommodityRespVo.setPrice(sellingDO.getPrice());
        }else{
            apiCommodityRespVo.setIsSuccess(false);
            apiCommodityRespVo.setErrorCode(OpenApiCode.NO_DATA);
        }
        return apiCommodityRespVo;
    }

    @Override
    public ApiBuyItemRespVo buyItem(LoginUser loginUser, ApiQueryCommodityReqVo createReqVO, Long orderId) {
        return null;
    }

    @Override
    public String queryOrderDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public String queryCommodityDetail(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public Integer getOrderSimpleStatus(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public ApiOrderCancelRespVo orderCancel(LoginUser loginUser, String orderNo, Long orderId) {
        return null;
    }

    @Override
    public ApiOrderCancelRespVo releaseIvn(LoginUser loginUser, String orderNo, Long orderId) {
        return ApiThreeOrderService.super.releaseIvn(loginUser, orderNo, orderId);
    }

    @Override
    public ApiProcessNotifyResp processNotify(String jsonData, String msgNo) {
        return null;
    }

    @Override
    public V5ItemListVO getItemList(V5page v5page) {
        return null;
    }
}
