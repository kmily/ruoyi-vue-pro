package cn.iocoder.yudao.ssodemo.framework.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 客户端工具类
 *
 * @author 芋道源码
 */
public class ServletUtils {

    /**
     * 返回 JSON 字符串
     *
     * @param response 响应
     * @param object 对象，会序列化成 JSON 字符串
     */
    @SuppressWarnings("deprecation") // 必须使用 APPLICATION_JSON_UTF8_VALUE，否则会乱码
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JSONUtil.toJsonStr(object);
        write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    public static void write(HttpServletResponse response, String text, String contentType){
        response.setContentType(contentType);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException var8) {
            throw new UtilException(var8);
        } finally {
            IoUtil.close(writer);
        }
    }
}
