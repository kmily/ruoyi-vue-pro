package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OtherSellListItemResp extends OtherSellingDO implements Serializable {
    private MemberUserRespDTO memberUserRespDTO;
}
