package cn.iocoder.yudao.module.therapy.dal.dataobject.definition.common;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Method;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.capitalize;

public interface JsonFieldAccessible {

    default Map<String, Object> getJsonField(String column_name) {
        try {
            Method method = this.getClass().getMethod("get" + capitalize(column_name));
            String text = (String) method.invoke(this);
            return JSON.parseObject(text, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

    default void setJsonField(String column_name, Map<String, Object> value) {
        try {
            Method method = this.getClass().getMethod("set" + capitalize(column_name), String.class);
            method.invoke(this, JSON.toJSONString(value));
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }


}