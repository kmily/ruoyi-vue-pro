package cn.iocoder.yudao.module.therapy.controller.app.vo;

import cn.iocoder.boot.module.therapy.enums.SignState;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
@Schema(description = "患者日程 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TreatmentScheduleRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8684")
    private Long id;

//    @Schema(description = "患者id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14131")
//    private Long userId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String name;

    @Schema(description = "预计能完成的概率")
    private Integer estimateCompletedRate;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime beginTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    public String getStateCh() {
        if(this.state.equals(SignState.SIGNED.getType())){
            return "已完成";
        }
        if(this.state.equals(SignState.UNSIGNED.getType()) && LocalDateTime.now().isAfter(this.endTime)){
            return "已过期";
        }
        if(this.state.equals(SignState.UNSIGNED.getType()) && LocalDateTime.now().isBefore(this.beginTime)){
            return "未开始";
        }
        return "进行中";
    }

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "状态")
    private Integer stateCh;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "未完成原因")
    private String unfinishedCause;

    @Schema(description = "克服办法")
    private String solution;

    @Schema(description = "活动前评分")
    private Integer beforeScore;

    @Schema(description = "活动后评分")
    private Integer afterScore;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;



}