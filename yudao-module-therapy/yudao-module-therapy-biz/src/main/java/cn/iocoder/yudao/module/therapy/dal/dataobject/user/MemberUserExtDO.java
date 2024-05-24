package cn.iocoder.yudao.module.therapy.dal.dataobject.user;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 患者扩展表
 **/
@Data
@TableName("hlgyy_member_user_ext")
public class MemberUserExtDO {
    //id                     int comment '自增ID',
    //appointment_date       date                                null comment '预约日期',
    //appointment_time_range varchar(20)                         not null comment '预约时间段范围',
    //user_id                bigint                              null comment '用户 ID',
    //created_at             timestamp default current_timestamp null comment '创建时间',
    //updated_at             timestamp default current_timestamp null on update CURRENT_TIMESTAMP,
    //constraint `uniq_idx_user_id`
    //

    @TableId
    private Integer id;
    private Date appointmentDate;
    private String appointmentTimeRange;
    private Long userId;
    private DateTime createdAt;
    private DateTime updatedAt;

}
