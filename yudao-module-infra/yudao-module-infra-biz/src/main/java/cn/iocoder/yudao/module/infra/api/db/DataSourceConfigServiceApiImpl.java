package cn.iocoder.yudao.module.infra.api.db;

import cn.iocoder.yudao.module.infra.api.db.dto.DataSourceConfigRespDTO;
import cn.iocoder.yudao.module.infra.convert.db.DataSourceConfigConvert;
import cn.iocoder.yudao.module.infra.service.db.DataSourceConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 数据源配置 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class DataSourceConfigServiceApiImpl implements DataSourceConfigServiceApi {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public DataSourceConfigRespDTO getDataSourceConfig(Long id) {
        return DataSourceConfigConvert.INSTANCE.convert02(dataSourceConfigService.getDataSourceConfig(id));
    }

}
