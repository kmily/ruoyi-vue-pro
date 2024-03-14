package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 饰品在售预览新增/修改 Request VO")
@Data
public class BatchChangePriceReqVo {
    @Valid
    @NotNull(message = "在售不能为空，至少为一个在售")
    @NotEmpty(message = "在售不能为空，至少为一个在售")
    private List<BatchChangePriceReqVo.Item> items;
    @Data
    public static class Item{
        @NotNull(message = "Primary Key")
        private Long id;
        @NotNull(message = "价格不能为空")
        private Integer price;

    }


}
