package cn.iocoder.yudao.module.im.dal.dataobject.group;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 群 DO
 *
 * @author 芋道源码
 */
@TableName("im_group")
@KeySequence("im_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImGroupDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 群名字
     */
    private String groupName;
    /**
     * 群主id
     */
    private Long ownerId;
    /**
     * 群头像
     */
    private String headImage;
    /**
     * 群头像缩略图
     */
    private String headImageThumb;
    /**
     * 群公告
     */
    private String notice;
    /**
     * 群备注
     */
    private String remark;

}