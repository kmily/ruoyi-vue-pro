package com.cw.module.trade.controller.admin.syncrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 账号同步记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class SyncRecordBaseVO {

    @Schema(description = "账户id", example = "6815")
    private Long accountId;

    @Schema(description = "同步类型", example = "2")
    private String type;

    @Schema(description = "第三方数据")
    private String thirdData;

}
