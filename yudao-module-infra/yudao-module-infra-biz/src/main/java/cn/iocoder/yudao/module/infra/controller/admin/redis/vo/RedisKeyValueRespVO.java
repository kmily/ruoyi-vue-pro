package cn.iocoder.yudao.module.infra.controller.admin.redis.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(title = "管理后台 - 单个 Redis Key Value Response VO")
@Data
@AllArgsConstructor
public class RedisKeyValueRespVO {

    @Schema(title  = "c5f6990767804a928f4bb96ca249febf", required = true, example = "String")
    private String key;

    @Schema(required = true, example = "String")
    private String value;

}
