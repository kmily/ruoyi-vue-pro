package cn.iocoder.yudao.framework.desensitization.interceptor;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;
import cn.iocoder.yudao.framework.desensitization.core.handler.DesensitizationHandler;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

/**
 * @author VampireAchao
 * @since 2022/10/6 11:24
 */
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class DesensitizationInterceptor extends JsqlParserSupport implements InnerInterceptor, Interceptor {

    /**
     * {@link StatementHandler#prepare(Connection, Integer)} 操作前置处理
     * <p>
     * 改改sql啥的
     *
     * @param sh                 StatementHandler(可能是代理对象)
     * @param connection         Connection
     * @param transactionTimeout transactionTimeout
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE) {
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(parserMulti(mpBs.sql(), mpBs));
        }
    }


    @Override
    public void setProperties(Properties properties) {
        InnerInterceptor.super.setProperties(properties);
    }


    /**
     * 更新
     *
     * @param update
     * @param index
     * @param sql
     * @param obj
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        if (!(obj instanceof PluginUtils.MPBoundSql)) {
            return;
        }
        PluginUtils.MPBoundSql boundSql = (PluginUtils.MPBoundSql) obj;
        Object parameterObject = boundSql.parameterObject();
        if (!(parameterObject instanceof MapperMethod.ParamMap<?>)) {
            return;
        }
        MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) parameterObject;
        Object entity = paramMap.get(Constants.ENTITY);
        if (!Objects.nonNull(entity)) {
            return;
        }
        processDesensitization(entity);
    }

    /**
     * 新增
     *
     * @param insert
     * @param index
     * @param sql
     * @param obj
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        if (!(obj instanceof PluginUtils.MPBoundSql)) {
            return;
        }
        PluginUtils.MPBoundSql boundSql = (PluginUtils.MPBoundSql) obj;
        Object entity = boundSql.parameterObject();
        processDesensitization(entity);
    }

    private void processDesensitization(Object entity) {
        for (Field field : ReflectUtil.getFields(entity.getClass())) {
            if (!field.isAnnotationPresent(Desensitization.class)) {
                continue;
            }
            Desensitization desensitization = field.getAnnotation(Desensitization.class);
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            Opt.ofBlankAble(fieldValue).map(Object::toString).ifPresent(value -> {
                DesensitizationHandler desensitizationHandler = ReflectUtil.newInstance(desensitization.handler());
                String newValue = desensitizationHandler.apply(value, desensitization);
                ReflectUtil.setFieldValue(entity, field, newValue);
            });
        }
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        if (Collection.class.isAssignableFrom(proceed.getClass())) {
            Collection<?> collection = (Collection<?>) proceed;
            collection.forEach(this::processDesensitization);
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
