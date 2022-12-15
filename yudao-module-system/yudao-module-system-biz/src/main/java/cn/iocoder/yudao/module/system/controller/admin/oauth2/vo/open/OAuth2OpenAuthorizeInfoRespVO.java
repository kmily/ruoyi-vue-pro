package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.open;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(title = "管理后台 - 授权页的信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2OpenAuthorizeInfoRespVO {

    /**
     * 客户端
     */
    private Client client;

    @Schema(title = "scope 的选中信息", requiredMode = Schema.RequiredMode.REQUIRED, description = "使用 List 保证有序性，Key 是 scope，Value 为是否选中")
    private List<KeyValue<String, Boolean>> scopes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {

        @Schema(title = "应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "土豆")
        private String name;

        @Schema(title = "应用图标", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
        private String logo;

    }

}
