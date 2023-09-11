package cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author whycode
 * @title: AppEntryLeaveDetailPageReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/810:36
 */

@Schema(description = "用户 APP - 离回家详情 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppEntryLeaveDetailPageReqVO  extends PageParam {

    @Schema(description = "设备ID", example = "11", required = true)
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "查询日期", example = "2023-09-07")
    private String queryDate;



}
