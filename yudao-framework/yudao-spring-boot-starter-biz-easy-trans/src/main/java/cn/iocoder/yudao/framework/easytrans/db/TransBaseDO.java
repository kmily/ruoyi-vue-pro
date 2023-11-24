package cn.iocoder.yudao.framework.easytrans.db;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础实体对象实现 TransPojo  接口，代表这个类需要被翻译或者被当作翻译的数据源
 *
 * @author HUIHUI
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TransBaseDO extends BaseDO implements TransPojo {
}
