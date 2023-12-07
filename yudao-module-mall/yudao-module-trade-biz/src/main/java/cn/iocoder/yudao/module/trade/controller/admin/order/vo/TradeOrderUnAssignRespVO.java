package cn.iocoder.yudao.module.trade.controller.admin.order.vo;

import cn.iocoder.yudao.module.member.api.serverperson.dto.ServerPersonRespDTO;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whycode
 * @title: TradeOrderUnAssignRespVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/3015:22
 */

@Schema(description = "管理后台 - 未分配订单 Request VO")
@Data
public class TradeOrderUnAssignRespVO {
    // ========== 订单基本信息 ==========

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "订单流水号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1146347329394184195")
    private String no;

    @Schema(description = "下单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "服务项目")
    private String spuName;
    @Schema(description = "服务套餐项目")
    private String skuName;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    private Long userId;

    @Schema(description = "用户备注", requiredMode = Schema.RequiredMode.REQUIRED, example = "你猜")
    private String userRemark;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "应付金额（总）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private Integer payPrice;

    @Schema(description = "医护人员编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
    private Long careId;

    @Schema(description = "服务日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceDate;

    @Schema(description = "服务时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceTime;

    @Schema(description = "被护人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
    private Long servicePersonId;

    @Schema(description = "被护人详情",  example = "888")
    private String servicePerson;

    @Schema(description = "图片资料", example = "888")
    private List<String> images;

    @Schema(description = "是否日期", example = "888")
    private LocalDateTime payTime;

    private MemberUserRespDTO user;

    private ServerPersonRespDTO serverPerson;


    @Schema(description = "地域编号", example = "888")
    private Integer receiverAreaId;

    @Schema(description = "详细地址", example = "888")
    private String receiverDetailAddress;

    @Schema(description = "收件人地区名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海 上海市 普陀区")
    private String receiverAreaName;
}
