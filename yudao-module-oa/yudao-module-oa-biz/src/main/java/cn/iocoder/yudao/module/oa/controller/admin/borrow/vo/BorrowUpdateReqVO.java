package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 借支申请更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BorrowUpdateReqVO extends BorrowBaseVO {

    @Schema(description = "id", required = true, example = "29419")
    @NotNull(message = "id不能为空")
    private Long id;

}
