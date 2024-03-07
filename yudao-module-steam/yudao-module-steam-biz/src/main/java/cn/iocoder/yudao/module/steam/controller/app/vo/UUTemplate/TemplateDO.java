package cn.iocoder.yudao.module.steam.controller.app.vo.UUTemplate;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateDO implements Serializable {
    /**
     * 武器全称
     */
    private String name;
    /**
     * 武器英文全称
     */
    private String hashName;
    /**
     * 类型编号
     */
    private Integer typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型英文名称
     */
    private String typeHashName;
    /**
     * 武器编号
     */
    private Integer weaponId;
    /**
     * 武器名称
     */
    private String weaponName;
    /**
     * 武器英文名称
     */
    private String weaponHashName;
    /**
     * 主键ID
     */
    @TableId
    private Integer id;
    /**
     * 模板ID
     */
    private Integer templateId;

}
