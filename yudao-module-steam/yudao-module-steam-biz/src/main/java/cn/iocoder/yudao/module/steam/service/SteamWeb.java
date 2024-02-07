package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.service.steam.SteamCookie;
import cn.iocoder.yudao.module.steam.service.steam.SteamCustomCookieJar;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class SteamWeb {
    private OkHttpClient okHttpClient;
    private String cookieString="";
    private Optional<String> webApiKey=Optional.empty();
    private Optional<String> steamId=Optional.empty();
    private Optional<String> treadUrl=Optional.empty();

    private ConfigService configService;
    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }


    public SteamWeb() {
        okHttpClient=getClient(true,30,null,null);
    }
    public static OkHttpClient getClient(boolean retry, int timeOut,String cookies,String url) {
        OkHttpClient.Builder OKHTTP_BUILD = new OkHttpClient.Builder();
        OKHTTP_BUILD.writeTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.readTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.connectTimeout(3, TimeUnit.SECONDS);
        OKHTTP_BUILD.retryOnConnectionFailure(retry);
        OKHTTP_BUILD.hostnameVerifier(HttpUtil.getHostnameVerifier());
        OKHTTP_BUILD.sslSocketFactory(HttpUtil.getSSLSocketFactory(), HttpUtil.getTrustManager());
        OKHTTP_BUILD.cookieJar(new SteamCustomCookieJar(cookies,url));
        return OKHTTP_BUILD.build();
    }
    public void login3(BindUserDO bindUserDO){
        //steam登录代理
//        ConfigDO configByKey = configService.getConfigByKey("steam.proxy");
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
//        builder.url(configByKey.getValue()+"login");
        builder.url("http://127.0.0.1:25852/login");
        builder.method(HttpUtil.Method.FORM);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username",bindUserDO.getLoginName());
        stringStringHashMap.put("password",bindUserDO.getLoginPassword());
        stringStringHashMap.put("token_code",bindUserDO.getLoginSharedSecret());
        builder.form(stringStringHashMap);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 30000,null,null));
        SteamCookie json = sent.json(SteamCookie.class);
        if(json.getCode()!=0){
            log.error("Steam通讯失败{}",json);
            throw new ServiceException(-1,"Steam通讯失败"+json.getMsg());
        }
        cookieString=json.getData().getCookie();
    }
    public void initApiKey() {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/dev/apikey");
        builder.method(HttpUtil.Method.GET);
        Map<String,String> header=new HashMap<>();
        header.put("Accept-Language","zh-CN,zh;q=0.9");
        builder.headers(header);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),getClient(true,3000,cookieString,"https://steamcommunity.com/dev/apikey"));
        Pattern pattern = Pattern.compile("密钥: (.*?)<"); // 正则表达式匹配API密钥
        Matcher matcher = pattern.matcher(sent.html());
        if (matcher.find()) {
            webApiKey=Optional.of(matcher.group(1));
        }
        Pattern pattern2 = Pattern.compile("https://steamcommunity.com/profiles/(\\d+)/");
        Matcher matcher2 = pattern2.matcher(sent.html());
        if (matcher2.find()) {
            steamId =Optional.of(matcher2.group(1));
        }
    }
    public void initTradeUrl() {
        if(!steamId.isPresent()){
            initApiKey();
        }
        if(!steamId.isPresent()){
            throw new ServiceException(-1,"初始化steam失败，请重新登录");
        }
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/profiles/:steamId/tradeoffers/privacy");
        builder.method(HttpUtil.Method.GET);
        Map<String,String> header=new HashMap<>();
        header.put("Accept-Language","zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String,String> pathVar=new HashMap<>();
        pathVar.put("steamId",steamId.get());
        builder.pathVar(pathVar);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),getClient(true,3000,cookieString,"https://steamcommunity.com/dev/apikey"));
        Pattern pattern = Pattern.compile("value=\"(.*?)\"");
        Matcher matcher = pattern.matcher(sent.html());
        if (matcher.find()) {
            treadUrl=Optional.of(matcher.group(1));
        }else{
            throw new ServiceException(-1,"获取tradeUrl失败");
        }
    }
    /**
     * 计算桌面令牌
     * @param secret secret 桌面共享key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */

    public static String getSteamAuthCode(String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        long time = System.currentTimeMillis() / 30000; // Equivalent to Python's time() / 30
        System.out.println(time);
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(time);
        byte[] packedTime = buffer.array();
        byte[] msg = new byte[8];
        System.arraycopy(packedTime, 0, msg, 0, 8);
        byte[] decode = Base64.getDecoder().decode(secret);// Equivalent to Python's b64encode
        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKeySpec=new SecretKeySpec(decode,"HmacSHA1");
        sha1_HMAC.init(secretKeySpec); // Assuming secret is a valid Base64 encoded string
        byte[] mac = sha1_HMAC.doFinal(msg);
        int offset = mac[mac.length - 1] & 0x0f; // Equivalent to Python's & 0x0f
        int binary = ByteBuffer.wrap(mac, offset, 4).order(ByteOrder.BIG_ENDIAN).getInt() & 0x7fffffff; // Equivalent to Python's struct unpack and & 0x7fffffff
        char[] codestr = "23456789BCDFGHJKMNPQRTVWXY".toCharArray(); // This part seems unnecessary as the original Python code only uses it once, so I'll assume this is correct for now
        char[] chars = new char[5];
        for (int i = 0; i < 5; i++) {
            chars[i] = codestr[binary % 26]; // Equivalent to Python's % 26 and indexing
            binary /= 26; // Equivalent to Python's //= 26
        }
        String code = new String(chars); // Convert char array to string
        return code;
    }


    public static void main(String[] args) {
        SteamWeb steamWeb=new SteamWeb();
        BindUserDO bindUserDO=new BindUserDO();
        bindUserDO.setLoginName("6WwS6f").setLoginPassword("QFhG9jSs").setLoginSharedSecret("NTDfP+vPKSLS2O8lXKJgdAp5QRI=");
        steamWeb.login3(bindUserDO);
        steamWeb.initApiKey();
        steamWeb.initTradeUrl();
        if(steamWeb.webApiKey.isPresent()){
            System.out.println(steamWeb.webApiKey.get());
        }
        if(steamWeb.steamId.isPresent()){
            System.out.println(steamWeb.steamId.get());
        }
        if(steamWeb.treadUrl.isPresent()){
            System.out.println(steamWeb.treadUrl.get());
        }

    }
}
