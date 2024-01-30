package cn.iocoder.yudao.module.steam.service.steam;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCookieJar implements CookieJar {
    private final Map<String, List<Cookie>> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> resultCookies = new ArrayList<>();

        // 获取当前URL对应的所有cookie
        List<Cookie> storedCookies = cookies.get(url.host());

//        if (storedCookies != null && !storedCookies.isEmpty()) {
//            for (Cookie cookie : storedCookies) {
//                // 判断该cookie是否仍然有效（根据需要）
//                if (!isExpired(cookie)) {
//                    resultCookies.add(cookie);
//                } else {
//                    // 如果cookie已经失效则从列表中移除
//                    storedCookies.remove(cookie);
//                }
//            }
//
//            // 更新保存的cookie列表
//            cookies.put(url.host(), storedCookies);
//        }

        return storedCookies!=null?storedCookies:new ArrayList<>();
    }

//    private boolean isExpired(Cookie cookie) {
//        long l = cookie.expiresAt();
//        long currentTimeMillis = System.currentTimeMillis();
//
//        return Objects.isNull(l) || l<currentTimeMillis;
//    }
}
