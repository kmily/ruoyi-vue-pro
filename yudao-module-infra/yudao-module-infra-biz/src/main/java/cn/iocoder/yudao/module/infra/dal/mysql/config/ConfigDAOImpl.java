package cn.iocoder.yudao.module.infra.dal.mysql.config;

import cn.iocoder.yudao.framework.apollo.internals.ConfigFrameworkDAO;
import cn.iocoder.yudao.framework.apollo.internals.dto.ConfigRespDTO;
import cn.iocoder.yudao.framework.common.util.spring.ApplicationUtils;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ConfigDAOImpl 实现类
 *
 * @author 芋道源码
 */
public class ConfigDAOImpl implements ConfigFrameworkDAO {

    private final JdbcTemplate jdbcTemplate;

    public ConfigDAOImpl(String jdbcUrl, String username, String password) {
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, username, password);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean selectExistsByUpdateTimeAfter(Date maxUpdateTime) {
        return jdbcTemplate.query("SELECT id FROM INFRA_CONFIG WHERE update_time > ? AND ROWNUM = 1",
                ResultSet::next, maxUpdateTime);
    }

    @Override
    public List<ConfigRespDTO> selectList() {
        return jdbcTemplate.query("SELECT CONFIG_KEY \"key\", value, update_time, deleted FROM INFRA_CONFIG", new BeanPropertyRowMapper<>(ConfigRespDTO.class));
    }

}
