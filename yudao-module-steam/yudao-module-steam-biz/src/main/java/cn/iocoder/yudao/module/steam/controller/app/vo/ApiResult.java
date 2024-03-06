package cn.iocoder.yudao.module.steam.controller.app.vo;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * 有品Api接口返回
 * @param <T>
 */
@Data
public class ApiResult<T> extends CommonResult<T> {
    private Long timestamp=System.currentTimeMillis();
    public static <T> ApiResult<T> error(Integer code, String message,Class<T> t) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        ApiResult<T> result = new ApiResult<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }
    public static <T> ApiResult<T> success(T data,String msg) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(GlobalErrorCodeConstants.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public static <T> ApiResult<T> success(T data) {
        return success(data,"");
    }
}
