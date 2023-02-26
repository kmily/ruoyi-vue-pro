package cn.iocoder.yudao.module.infra.api.db.dto;

import lombok.Data;

/**
 * 数据源配置 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class DataSourceConfigRespDTO {

    /**
     * 主键编号
     */
    private Long id;
    /**
     * 连接名
     */
    private String name;

    /**
     * 数据源连接
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

}
