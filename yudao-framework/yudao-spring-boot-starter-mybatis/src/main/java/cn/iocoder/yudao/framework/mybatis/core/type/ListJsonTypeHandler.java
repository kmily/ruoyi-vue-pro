package cn.iocoder.yudao.framework.mybatis.core.type;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author whycode
 * @title: ListJsonTypeHandler
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/814:30
 */
public class ListJsonTypeHandler extends BaseTypeHandler<List<Object>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Object> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONUtil.toJsonStr(list));
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        return getResult(value);
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        return getResult(value);
    }

    @Override
    public List<Object> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        return getResult(value);
    }

    private List<Object> getResult(String value) {
        if (value == null) {
            return null;
        }
        return JSONUtil.toList(value, Object.class);
    }

}
