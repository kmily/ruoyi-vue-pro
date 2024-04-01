package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class ApiUUBuyGoodsByIdReqVO {

    @Schema(description = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为0", example = "14652")
    private String templateId;

    @Schema(description = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为空", example = "14652")
    private String templateHashName;

    @Schema(description = "最高价格")
    @ExcelProperty("最高价格")
    private String maxPrice;

    @Schema(description = "是否优先急速购买,发货模式：0,卖家直发；1,极速发货")
    @ExcelProperty("是否优先急速购买")
    private String shippingMode;


}
