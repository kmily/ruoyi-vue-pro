package cn.iocoder.yudao.module.steam.service.uu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiCheckTradeUrlReSpVo implements Serializable {
    /**
     * 状态
     * 1，2，3，4，5，6，7
     */
    @Schema(description = "交易链接账户steam ID", example = "https://steamcommunity.com/tradeoffer/new/?partner=111&token=222")
    private Integer status;
    /**
     * 说明：1：正常交易 2:交易链接格式错误 3:请稍后重试 4:账号交易权限被封禁，无法交易 5:该交易链接已不再可用 6:该账户库存私密无法交易 7:该账号个人资料私密无法交易
     */
    @Schema(description = "交易链接账户steam ID", example = "https://steamcommunity.com/tradeoffer/new/?partner=111&token=222")
    private String msg;
}
