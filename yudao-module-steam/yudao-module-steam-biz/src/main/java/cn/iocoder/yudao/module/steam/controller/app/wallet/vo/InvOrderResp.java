package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InvOrderResp implements Serializable {
    private InvOrderDO invOrderDO;
    private InvDescDO invDescDO;
}
