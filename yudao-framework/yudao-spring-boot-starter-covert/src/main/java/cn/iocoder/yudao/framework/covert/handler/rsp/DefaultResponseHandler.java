package cn.iocoder.yudao.framework.covert.handler.rsp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 默认返回值处理，不处理
 */
public class DefaultResponseHandler implements ResponseHandler {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 不处理
     *
     * @param rsp
     * @return
     */
    @Override
    public Object handle(String bean, String method, Object rsp, Object... params) {
        if (rsp instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) rsp;
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return null;
            }
        }
        return rsp;
    }
}
