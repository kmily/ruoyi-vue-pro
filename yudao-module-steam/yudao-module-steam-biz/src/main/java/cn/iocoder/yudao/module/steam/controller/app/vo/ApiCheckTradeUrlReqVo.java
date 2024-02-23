package cn.iocoder.yudao.module.steam.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiCheckTradeUrlReqVo {
    @Schema(description = "交易地址", example = "https://steamcommunity.com/tradeoffer/new/?partner=111&token=222")
    @NotNull(message = "交易地址不能为空")
    private String tradeUrl;
}
