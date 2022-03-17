package cn.iocoder.yudao.framework.loader.handler.rsp;

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
        if (cls == Object.class) {
            return rsp;
        } else if (rsp.getClass() == cls) {
            return rsp;
        }
        return null;
    }
}