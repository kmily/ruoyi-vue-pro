package cn.iocoder.yudao.module.system.dal.dataobject.signreq;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 注册申请 DO
 *
 * @author 芋道源码
 */
@TableName("oa_sign_req")
@KeySequence("oa_sign_req_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignReqDO extends BaseDO {

    /**
     * 申请id,主键(自增策略)
     */
    @TableId
    private Long id;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 处理状态 1.未绑定 2.已绑定 3.已拒绝
     */
    private String status;
    /**
     * openId
     */
    private String openId;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别(0男 1女 2未知）
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private String sex;

}
