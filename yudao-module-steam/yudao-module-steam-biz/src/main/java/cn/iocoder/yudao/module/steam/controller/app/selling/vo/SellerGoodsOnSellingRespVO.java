package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import lombok.Data;

import java.util.List;

@Data
public class SellerGoodsOnSellingRespVO {

    // 头像
    private String avatar;
    // 名称
    private String nickname;
    private PageResult<SellingDO> selling;
    private List<SellingDO> sold;

}

