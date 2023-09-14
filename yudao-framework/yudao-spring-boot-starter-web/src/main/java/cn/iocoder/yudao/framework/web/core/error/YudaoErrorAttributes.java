package cn.iocoder.yudao.framework.web.core.error;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class YudaoErrorAttributes extends DefaultErrorAttributes {
    private final GlobalExceptionHandler globalExceptionHandler;
    private static final String REQUEST_INSTANCE = YudaoErrorAttributes.class.getName() + ".REQUEST_INSTANCE";

    public YudaoErrorAttributes(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 这里参考DefaultErrorAttributes#storeErrorAttributes(..)  方法的实现，保存request 实例，在后面使用。
        // 如果后面不使用，那么这个不是必要的。
        request.setAttribute(REQUEST_INSTANCE, request);
        return super.resolveException(request, response, handler, ex);
    }

    /**
     * 参考 {@link DefaultErrorAttributes#getAttribute(RequestAttributes, String)}
     */
    @SuppressWarnings("unchecked")
    private <T> T getAttributeRequestInstance(RequestAttributes requestAttributes) {
        return (T) requestAttributes.getAttribute(REQUEST_INSTANCE, RequestAttributes.SCOPE_REQUEST);
    }


    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        Throwable throwable = getError(webRequest);

        if (!ServiceException.class.isAssignableFrom(throwable.getClass())) {
            log.warn("some service exception.", throwable);
        }

        // 这里麻烦了一点，先转成json，再转回 map
        // 这里同时兼容了spring mvc 的异常处理，以及芋道原有的异常处理办法。如果两者有冲突，则会以芋道的值为准。
        CommonResult<?> commonResult = writeExceptionLog(webRequest, throwable);
        String jsonValue = JsonUtils.toJsonString(commonResult);
        Map<String, Object> commonJson = JsonUtils.parseMap(jsonValue, String.class, Object.class);
        errorAttributes.putAll(commonJson);

        return errorAttributes;
    }

    private CommonResult<?> writeExceptionLog(WebRequest webRequest, Throwable throwable) {
        try {
            HttpServletRequest request = getAttributeRequestInstance(webRequest);
            return globalExceptionHandler.allExceptionHandler(request, throwable);
        } catch (Exception e) {
            log.error("write exception log failed.", e);
            return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        }
    }

}
