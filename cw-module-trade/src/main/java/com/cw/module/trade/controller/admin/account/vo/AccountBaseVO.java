package com.cw.module.trade.controller.admin.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 交易账号 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AccountBaseVO {

    @Schema(description = "账号名称", example = "李四")
    private String name;

    @Schema(description = "api访问key")
    private String appKey;

    @Schema(description = "api访问秘钥")
    private String appSecret;

    @Schema(description = "余额（第三方查询返回信息）")
    private String balance;

    @Schema(description = "最后一次余额查询时间(时间戳)")
    private Long lastBalanceQueryTime;

    @Schema(description = "账号管理用户ID")
    private Long relateUser;
    
    /** 关联配置 */
    private String followCfg;
    
    /** 关联账号 */
    private Long followAccount;

}
