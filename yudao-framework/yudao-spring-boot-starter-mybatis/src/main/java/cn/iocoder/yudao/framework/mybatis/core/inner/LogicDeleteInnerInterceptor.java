package cn.iocoder.yudao.framework.mybatis.core.inner;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import cn.iocoder.yudao.framework.mybatis.config.IdTypeEnvironmentPostProcessor;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class LogicDeleteInnerInterceptor implements InnerInterceptor {

    private final ConfigurableEnvironment environment;

    /**
     * 删除方法的 SQL 方法
     */
    private static final Set<String> DeleteMethods = SetUtils.asSet(
            SqlMethod.DELETE_BY_ID.getMethod(),
            SqlMethod.DELETE.getMethod(),
            SqlMethod.DELETE_BY_MAP.getMethod(),
            SqlMethod.DELETE_BATCH_BY_IDS.getMethod(),
            SqlMethod.LOGIC_DELETE_BY_ID.getMethod(),
            SqlMethod.LOGIC_DELETE.getMethod(),
            SqlMethod.LOGIC_DELETE_BY_MAP.getMethod(),
            SqlMethod.LOGIC_DELETE_BATCH_BY_IDS.getMethod()

    );

    /**
     * 不同数据库日期函数的枚举
     */
    private static final Map<DbType, String> NowTimeFuncMap = new HashMap<DbType, String>() {{
        put(DbType.MYSQL, "now()");
        put(DbType.ORACLE, "sysdate");
        put(DbType.SQL_SERVER, "getdate()");
        put(DbType.SQL_SERVER2005, "getdate()");
        put(DbType.POSTGRE_SQL, "now()");
    }};

    public LogicDeleteInnerInterceptor(ConfigurableEnvironment environment) {
        this.environment = environment;
    }


    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        String id = ms.getId();
        List<String> ids = StrUtil.split(id, ".");
        // 逻辑删除最后会转换成 update 方法，所以这里只处理 update 方法
        if (sct != SqlCommandType.UPDATE) {
            return;
        }

        if (ids.size() < 1) {
            return;
        }

        // 只处理内置的删除方法
        if (!DeleteMethods.contains(ids.get(ids.size() - 1))) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
        String sql = mpBs.sql();
        String[] sqls = sql.split(Constants.WHERE);
        DbType dbType = IdTypeEnvironmentPostProcessor.getDbType(environment);

        if (sql.contains(Constants.WHERE) && sqls.length == 2) {
            sql = sqls[0] + ", " + BaseDO.FIELD_DELETED_TIME + " = " + NowTimeFuncMap.get(dbType) + Constants.WHERE + sqls[1];
        } else if (sql.contains(Constants.WHERE) && sqls.length == 1) {
            sql = sql + ", " + BaseDO.FIELD_DELETED_TIME + " = " + NowTimeFuncMap.get(dbType);
        } else {
            log.error("逻辑删除sql解析错误,id：{},sql:{}", id, sql);
        }
        mpBs.sql(sql);
    }

}
