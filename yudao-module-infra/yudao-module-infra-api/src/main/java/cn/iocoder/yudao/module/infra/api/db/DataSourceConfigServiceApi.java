package cn.iocoder.yudao.module.infra.api.db;

import cn.iocoder.yudao.module.infra.api.db.dto.DataSourceConfigRespDTO;

/**
 * 数据源配置 API 接口
 *
 * @author 芋道源码
 */
public interface DataSourceConfigServiceApi {

    /**
     * 获得数据源配置
     *
     * @param id 编号
     * @return 数据源配置
     */
    DataSourceConfigRespDTO getDataSourceConfig(Long id);

}
