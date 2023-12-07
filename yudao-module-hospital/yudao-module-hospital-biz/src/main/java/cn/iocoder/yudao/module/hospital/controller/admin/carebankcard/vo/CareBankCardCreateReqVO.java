package cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 提现银行卡创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareBankCardCreateReqVO extends CareBankCardBaseVO {

}
