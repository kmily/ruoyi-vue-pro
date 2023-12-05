package cn.iocoder.yudao.module.trade.controller.admin.order.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum.*;

/**
 * @author whycode
 * @title: AppTradeOrderCarePageReqVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/12/410:34
 */

@Schema(description = "交易订单分页 Request VO")
@Data
public class TradeOrderCarePageReqVO extends PageParam {

    @Schema(description = "医护编号")
    @NotNull(message = "医护编号不能为空")
    private Long careId;

    @Schema(description = "查询状态 空-全部，0-待处理，1-服务中, 60-已完成, 70-已取消, 80-已拒绝")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;


    @JsonIgnore
    public Integer[] queryStatus(){

//        UNRECEIVE(20, "待接单"),
//                NOSTART(30, "待出发"),
//                UNSERVER(40, "待服务"),
//                SERVERING(50, "服务中"),
//                COMPLETED(60, "已完成"),
        //CANCELED(70, "已取消"),
//                REFUSE(80, "已拒绝"),
        if(status == null){
            return new Integer[]{UNRECEIVE.getStatus(), NOSTART.getStatus(), UNSERVER.getStatus(),
                                SERVERING.getStatus(), CANCELED.getStatus(),REFUSE.getStatus(),
                                COMPLETED.getStatus()};
        }else if(0 == status){
            return new Integer[]{UNRECEIVE.getStatus()};
        }else if(1 == status){
            return new Integer[]{UNRECEIVE.getStatus(), NOSTART.getStatus(), UNSERVER.getStatus(),
                    SERVERING.getStatus()};
        }else {
            return new Integer[]{status};
        }
    }

}
