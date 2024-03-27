package cn.iocoder.yudao.module.im.dal.dataobject.group;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * IM 群信息 DO
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
public class GroupDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    // TODO @hao：name，如果这个表已经是 group 了，不用在带额外的
    /**
     * 群名字
     */
    private String groupName;
    // TODO @hao：关联字段；
    /**
     * 群主编号
     */
    private Long ownerId;
    // TODO @hao：头像使用 avatar 好了，整个项目统一；然后 Thumb 是不是不用存，这个更多是文件服务做裁剪
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