package cn.iocoder.yudao.module.infra.service.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库表 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class DatabaseTableServiceImpl implements DatabaseTableService {

    /**
     * 表信息缓存，key 是 dataSourceConfigId，value 是表信息
     */
    private volatile Map<Long, List<TableInfo>> TableInfoCache;

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public List<TableInfo> getTableList(Long dataSourceConfigId, String nameLike, String commentLike) {
        List<TableInfo> tables = getTableList0(dataSourceConfigId, null);
        return tables.stream().filter(tableInfo -> (StrUtil.isEmpty(nameLike) || tableInfo.getName().contains(nameLike))
                        && (StrUtil.isEmpty(commentLike) || tableInfo.getComment().contains(commentLike)))
                .collect(Collectors.toList());
    }

    @Override
    public TableInfo getTable(Long dataSourceConfigId, String name) {
        return CollUtil.getFirst(getTableList0(dataSourceConfigId, name));
    }

    private List<TableInfo> getTableList0(Long dataSourceConfigId, String name) {
        List<TableInfo> tables = TableInfoCache.get(dataSourceConfigId);
        Assert.notNull(tables, "数据源({}) 不存在！", dataSourceConfigId);
        if (StrUtil.isNotEmpty(name)) {
            return tables.stream().filter(tableInfo -> tableInfo.getName().equals(name))
                    .collect(Collectors.toList());
        }
        return tables;
    }

    /**
     * 初始化本地缓存，这里没有使用定时任务补偿是因为只有在开发阶段才会使用到这个功能
     */
    @PostConstruct
    private void initTableInfoCache() {
        List<DataSourceConfigDO> configList = dataSourceConfigService.getDataSourceConfigList();
        if (CollUtil.isEmpty(configList)) {
            TableInfoCache = new HashMap<>(1);
            return;
        }
        // 初始化缓存
        TableInfoCache = new HashMap<>(configList.size());
        for (DataSourceConfigDO config : configList) {
            // 使用 MyBatis Plus Generator 解析表结构
            DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            ).build();
            StrategyConfig strategyConfig = new StrategyConfig.Builder().build();
            GlobalConfig globalConfig = new GlobalConfig.Builder().dateType(DateType.TIME_PACK).build(); // 只使用 Date 类型，不使用 LocalDate

            ConfigBuilder builder = new ConfigBuilder(
                    null,
                    dataSourceConfig,
                    strategyConfig,
                    null,
                    globalConfig,
                    null
            );


            List<TableInfo> tables = builder.getTableInfoList();
            tables = StreamUtil.of(tables)
                    // 过滤工作流和定时任务前缀的表名 // TODO 未来做成可配置
                    .filter(tableInfo -> {
                        String name = tableInfo.getName();
                        return !StrUtil.startWithAnyIgnoreCase(name, "QRTZ_", "ACT_", "FLW_");
                    })
                    // 按照名字排序
                    .sorted(Comparator.comparing(TableInfo::getName))
                    .collect(Collectors.toList());

            TableInfoCache.put(config.getId(), tables);
        }
    }

}
