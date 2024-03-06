package cn.iocoder.yudao.module.steam.controller.app.vo.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDetailDataQueryApllyReqVo implements Serializable {
    @Schema(description = "明细类型，1=订单明细，2=资金流水", example = "1")
    @NotNull(message = "明细信息不能为空")
    private Integer dataType;

    @Schema(description = "开始时间", example = "2016-01-01 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String startTime;

    @Schema(description = "结束时间", example = "2016-01-01 12:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String endTime;

}
