package cn.iocoder.yudao.module.mp.dal.dataobject.message;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/3 20:47
 */
@TableName(value = "mp_template", autoResultMap = true)
@KeySequence("mp_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpTemplateDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 是否有效
     */
    private Integer status;
    /**
     * 小程序地址
     */
    private String appPath;
    /**
     * 链接
     */
    private String url;
    /**
     * 消息内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String,String>> messageData;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 公众号模板ID
     */
    private String templateId;
    /**
     * 公众号 appId
     */
    private String appId;
    /**
     * 公众号账号的编号
     */
    private Long accountId;
}
