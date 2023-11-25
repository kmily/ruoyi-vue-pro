package cn.iocoder.yudao.module.wms.controller.admin.ro.vo;

import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoLpnDO;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoMtrlDO;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 收料单新增/修改 Request VO")
@Data
public class RoSaveReqVO {
    @TableId
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键不能为空")
    private String roId;

    @Schema(description = "收料单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "收料单号不能为空")
    private String roCode;

    @Schema(description = "收料类型")
    private String roType;

    @Schema(description = "收料状态")
    private String roStatus;

    @Schema(description = "加急")
    private String isUrgent;

    @Schema(description = "ASN")
    private String asn;

    @Schema(description = "供应商ID", example = "3418")
    private String supId;

    @Schema(description = "客户ID", example = "31016")
    private String custId;

    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22020")
    @NotEmpty(message = "部门ID不能为空")
    private String deptId;

    @Schema(description = "员工ID", example = "5408")
    private String empId;

    @Schema(description = "来源")
    private String rosrcType;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "审核时间")
    private LocalDateTime checkTime;

    @Schema(description = "关闭")
    private String closed;

    @Schema(description = "组织ID", example = "31660")
    private Long orgId;

    @Schema(description = "审核人ID")
    private String checker;

    @Schema(description = "关闭人")
    private String closer;

    @Schema(description = "收料单LPN明细列表")
    private List<RoLpnDO> roLpns;

    @Schema(description = "收料单物料明细列表")
    private List<RoMtrlDO> roMtrls;

}