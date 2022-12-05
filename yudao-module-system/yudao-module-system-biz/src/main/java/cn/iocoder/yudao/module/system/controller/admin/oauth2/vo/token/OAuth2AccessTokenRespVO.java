package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.token;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(title = "管理后台 - 访问令牌 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessTokenRespVO {

    @Schema(title  = "编号", required = true, example = "1024")
    private Long id;

    @Schema(title  = "访问令牌", required = true, example = "tudou")
    private String accessToken;

    @Schema(title  = "刷新令牌", required = true, example = "nice")
    private String refreshToken;

    @Schema(title  = "用户编号", required = true, example = "666")
    private Long userId;

    @Schema(title  = "用户类型", required = true, example = "2", description = "参见 UserTypeEnum 枚举")
    private Integer userType;

    @Schema(title  = "客户端编号", required = true, example = "2")
    private String clientId;

    @Schema(title  = "创建时间", required = true)
    private LocalDateTime createTime;

    @Schema(title  = "过期时间", required = true)
    private LocalDateTime expiresTime;

}
