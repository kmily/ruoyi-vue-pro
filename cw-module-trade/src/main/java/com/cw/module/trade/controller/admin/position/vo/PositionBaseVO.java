package com.cw.module.trade.controller.admin.position.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 账户持仓信息 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class PositionBaseVO {

    @Schema(description = "账户id", example = "2873")
    private Long accountId;

    @Schema(description = "交易对")
    private String symbol;

    @Schema(description = "持仓数量")
    private BigDecimal quantity;

    @Schema(description = "第三方数据")
    private String thirdData;

}
