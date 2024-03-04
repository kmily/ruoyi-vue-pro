package cn.iocoder.yudao.module.steam.controller.app.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ApiCheckTradeUrlReqVo implements Serializable {
    @Schema(description = "交易链接", example = "https://steamcommunity.com/tradeoffer/new/?partner=111&token=222")
    @NotNull(message = "交易链接不能为空")
    private String tradeLinks;
}
