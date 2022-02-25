package cn.iocoder.yudao.framework.covert.handler.rsp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 默认返回值处理，不处理
 */
public class DefaultResponseHandler implements ResponseHandler {

    /**
     * 不处理
     *
     * @param rsp
     * @return
     */
    @Override
    public Object handle(String bean, String method, Object rsp, Class<?> cls, Object... params) {
        if (cls != Object.class) {
            return rsp;
        } else if (rsp.getClass() == cls) {
            return rsp;
        }
        return null;
    }
}