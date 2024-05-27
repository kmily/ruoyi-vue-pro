package cn.iocoder.yudao.framework.mybatis.core.injector;

import cn.iocoder.yudao.framework.mybatis.core.method.DeleteAbsoluteById;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

public class MpSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> abstractMethods=super.getMethodList(mapperClass, tableInfo);
        abstractMethods.add(new DeleteAbsoluteById("deleteAbsoluteById"));
        return abstractMethods;
    }
}
