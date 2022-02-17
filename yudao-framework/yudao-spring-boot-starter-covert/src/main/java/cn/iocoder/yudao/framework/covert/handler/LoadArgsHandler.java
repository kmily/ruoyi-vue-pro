package cn.iocoder.yudao.framework.covert.handler;

import cn.iocoder.yudao.framework.covert.annotation.LoadDict;
import cn.iocoder.yudao.framework.covert.annotation.LoadUser;
import com.fasterxml.jackson.databind.BeanProperty;

public interface LoadArgsHandler {

    /**
     * 额外参数处理
     *
     * @param property
     * @param args
     * @return
     */
    static String[] handle(BeanProperty property, String[] args) {
        // 字典
        LoadDict loadDict = property.getAnnotation(LoadDict.class);
        if (loadDict != null) {
            String enumName = loadDict.value();
            return new String[]{enumName};
        }
        return args;
    }

}
