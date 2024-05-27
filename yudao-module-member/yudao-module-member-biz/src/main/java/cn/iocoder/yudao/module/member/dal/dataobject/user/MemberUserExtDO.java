package cn.iocoder.yudao.module.member.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 患者扩展表
 **/
@Data
@TableName(value ="hlgyy_member_user_ext",autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserExtDO implements Serializable, TransPojo {
    //id                     int comment '自增ID',
    //appointment_date       date                                null comment '预约日期',
    //appointment_time_range varchar(20)                         not null comment '预约时间段范围',
    //user_id                bigint                              null comment '用户 ID',
    //created_at             timestamp default current_timestamp null comment '创建时间',
    //updated_at             timestamp default current_timestamp null on update CURRENT_TIMESTAMP,
    //constraint `uniq_idx_user_id`
    //

    @TableId
    private Long userId;
    private Date appointmentDate;
    private String appointmentTimeRange;
    /**
     * 父母教育程序
     * 字典类型:edu_type
     */
    private Integer parentEduType;
    /**
     * 学业情况
     * 字典类型:study_state
     */
    private Integer studyState;
    /**
     * 父母婚姻情况
     * 字典类型:marital_state
     */
    private Integer parentMaritalState;
    /**
     * 生活状况
     * 字典类型:living_state
     */
    private Integer livingState;
    /**
     * 父母详细居住地类型
     * 字典类型:live_area_type
     */
    private Integer parentLiveAreaType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
