package cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 提现银行卡创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareBankCardAppCreateReqVO extends CareBankCardAppBaseVO {

}
