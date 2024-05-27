package cn.iocoder.yudao.framework.mybatis.core.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class DeleteAbsoluteById extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String method="deleteAbsoluteById";
//        SqlMethod sqlMethod= SqlMethod.DELETE;
//        String sql=String.format(sqlMethod.getSql(),tableInfo.getTableName(),tableInfo.getKeyColumn(),tableInfo.getKeyProperty());
//        SqlSource sqlSource=this.languageDriver.createSqlSource(this.configuration,sql,Object.class);
//        return this.addDeleteMappedStatement(mapperClass,method,sqlSource);

        SqlMethod sqlMethod = SqlMethod.DELETE;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo),
                sqlComment());
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, method, sqlSource);
    }

    public DeleteAbsoluteById(String methodName){
        super(methodName);
    }

    public DeleteAbsoluteById(){
        super("deleteAbsoluteById");
    }
}
