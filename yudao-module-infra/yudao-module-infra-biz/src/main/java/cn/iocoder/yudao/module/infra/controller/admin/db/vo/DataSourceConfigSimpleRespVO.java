package cn.iocoder.yudao.module.infra.controller.admin.db.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 数据源配置的精简 Response VO")
@Data
public class DataSourceConfigSimpleRespVO {

    @Schema(description = "主键编号", required = true, example = "1024")
    private Integer id;

    @Schema(description = "数据源名称", required = true, example = "test")
    private String name;

}
