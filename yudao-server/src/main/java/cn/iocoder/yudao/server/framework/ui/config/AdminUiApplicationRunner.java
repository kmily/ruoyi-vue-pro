package cn.iocoder.yudao.server.framework.ui.config;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 项目启动自动打开首页
 * <p>
 * 参考 https://blog.csdn.net/sqk1988/article/details/6752488
 *
 * @author tangkc
 */
@Slf4j
@Component
public class AdminUiApplicationRunner implements ApplicationRunner {

    private final String url;

    @Value("${spring.profiles.active}")
    private String env;

    public AdminUiApplicationRunner(@Value("${server.port}") Integer port,
                                    @Value("${yudao.web.index-url:/admin-ui/index}") String indexUrl) {
        StringBuilder strBuffer = new StringBuilder("http://127.0.0.1:");
        strBuffer.append(port);
        if (indexUrl.startsWith("/")) {
            strBuffer.append(indexUrl);
        } else {
            strBuffer.append("/").append(indexUrl);
        }

        this.url = strBuffer.toString();
    }

    @Override
    public void run(ApplicationArguments args) {
        // 只在本地开发环境打开
        if (!"local".equals(env)) {
            return;
        }

        // 打开首页
        openBrowse(url);
    }

    /**
     * 利用默认浏览器打开指定网页
     *
     * @param url 网页地址
     */
    private void openBrowse(String url) {
        OsInfo osInfo = SystemUtil.getOsInfo();

        try {

            if (osInfo.isWindows()) {
                // windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);

                return;
            }

            if (osInfo.isMac()) {
                // mac
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});

                return;
            }

            if (osInfo.isLinux()) {
                // Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null)
                    throw new NoSuchMethodException("Could not find web browser");
                else
                    Runtime.getRuntime().exec(new String[]{browser, url});
            }

        } catch (Exception ex) {
            log.error("[open browse error] osInfo = {}", osInfo.toString(), ex);
        }

    }

}
