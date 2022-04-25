package cn.iocoder.yudao.module.system.dal.dataobject.notice;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.enums.notice.NoticeTypeEnum;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告表
 * @author ruoyi
 */
@TableName(value = "SYSTEM_NOTICE", autoResultMap = true)
@KeySequence(value = "SEQ_SYSTEM_NOTICE",dbType = DbType.ORACLE)
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeDO extends BaseDO {

    /**
     * 公告ID
     */
    @TableId
    private Long id;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告类型
     * <p>
     * 枚举 {@link NoticeTypeEnum}
     */
    @TableField("notice_type")
    private Integer type;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 公告状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
