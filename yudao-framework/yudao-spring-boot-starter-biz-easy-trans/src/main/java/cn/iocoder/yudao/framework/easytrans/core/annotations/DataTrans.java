package cn.iocoder.yudao.framework.easytrans.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据翻译注解用于字段指定翻译相关参数-目前只支持 id 和 ids 翻译
 *
 * @author HUIHUI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DataTrans {

    /**
     * 类型：指定处理器类型
     */
    String type();

    /**
     * 需要哪些字段，比如需要 AdminUserDO 的 {"nickname","deptId"}
     */
    String[] fields();

    /**
     * 需要映射在哪些属性上（需要与 fields 字段位置对应），比如说映射到 RespVO 中的 {"username","deptId"} 上。
     * 这样的话最后 nickname 的值会设置到 username 上以此类推
     */
    String[] resultMapping();

}
