package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author whycode
 * @title: BootUpReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/119:34
 */

@Schema(description = "管理后台 - 用户启动数据 Request VO")
@Data
@ToString(callSuper = true)
public class BootUpReqVO {

    @Schema(description = "查询日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
