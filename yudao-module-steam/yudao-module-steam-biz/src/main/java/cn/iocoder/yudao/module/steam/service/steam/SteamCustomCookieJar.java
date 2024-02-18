package cn.iocoder.yudao.module.steam.service.steam;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.net.URI;
import java.util.*;

@Slf4j
public class SteamCustomCookieJar implements CookieJar {
    private final List<Cookie> cookies = new ArrayList<>();

    public SteamCustomCookieJar(String coolieString, String url) {
        if(Objects.isNull(coolieString)){
            return;
        }
        String host="localhost";
        if(Objects.nonNull(url)){
            URI uri = URI.create(url);
            host = uri.getHost();
        }
        String[] split = coolieString.split(";");
        for(int i=0;i<split.length;i++){
            String[] split1 = split[i].split("=");
            Cookie.Builder builder=new Cookie.Builder();
            builder.name(split1[0].trim()).value(split1[1].trim()).domain(host);
            cookies.add(builder.build());
            log.info("addcookie {} ,{},{}",split1[0].trim(),split1[1].trim(),host);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookies;
    }
}
