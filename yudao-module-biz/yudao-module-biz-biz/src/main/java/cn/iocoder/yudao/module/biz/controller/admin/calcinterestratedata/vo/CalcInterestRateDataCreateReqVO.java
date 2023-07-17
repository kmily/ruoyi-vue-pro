package cn.iocoder.yudao.module.biz.controller.admin.calcinterestratedata.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 利率数据创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CalcInterestRateDataCreateReqVO extends CalcInterestRateDataBaseVO {

}
