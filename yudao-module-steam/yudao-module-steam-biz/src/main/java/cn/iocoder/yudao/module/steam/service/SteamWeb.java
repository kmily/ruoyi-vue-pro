package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.service.steam.*;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * steam机器人
 * 注意此类为非线程安全，因此不能作为spring bean直接注入，建议直接new相关接口
 * @author glzaboy
 */
@Slf4j
public class SteamWeb {
    /**
     * 登录后cookie信息
     */
    private String cookieString = "timezoneOffset=28800,0; steamRefresh_steam=76561199392362087%7C%7CeyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInN0ZWFtIiwgInN1YiI6ICI3NjU2MTE5OTM5MjM2MjA4NyIsICJhdWQiOiBbICJ3ZWIiLCAicmVuZXciLCAiZGVyaXZlIiBdLCAiZXhwIjogMTcyNjM5OTE1MywgIm5iZiI6IDE2OTk1OTg5NjUsICJpYXQiOiAxNzA4MjM4OTY1LCAianRpIjogIjFCNEFfMjNGNzFBNEVfNzNFNzYiLCAib2F0IjogMTcwODIzODk2NSwgInBlciI6IDEsICJpcF9zdWJqZWN0IjogIjExMy4yNTAuNjcuNyIsICJpcF9jb25maXJtZXIiOiAiMTEzLjI1MC42Ny43IiB9.EgQYxYR13O05XKxUCPYQDdnsv9DSANBjFOLp72MBhrD4qhE48TDMpzdpEc2qWoDQlEFoiHN18LaMP8fdZ9g9Aw; ak_bmsc=221E6702EE7231B8FC66B420FD4B854B~000000000000000000000000000000~YAAQLzHFF1KpGmWNAQAAEw36uhYU3R/rzv7Qg+w3w/5x3rAeUt1DmpOFq8Lm4croyiRzy6aNqPIakrwst2Pn5Q+6gnVelMP8YQg1vsEB9uD/jt8B7U7RkxXlwQ3SWp5nHC/uKBg2Gb6SVTJi9MCv7MHc5Jm4CyeWEhDIPX/ioqNW76SaPIuzXWB1jOACQN6eVrWbhK2G7/+6rmIv87BwSfRd7DiMhhJIyHsnOYbDVBCays3xl1/VT+cX7ebIgtMMpB5p/RvdMV4avrt4O/x6tUPDfI0IgitsepC8gcimEALkOxR3RMR+dFEwYLnIMsUQhYtxuXlW/pzvQhbpH1ufiI6sV7wQxYfVkdGmGlX604WiSs9yLT4dxoYtRrFpJ3KMog==; steamCountry=CN%7Cd753c23f070da204866d2ca95871a7f3; steamLoginSecure=76561199392362087%7C%7CeyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInI6MUI0QV8yM0Y3MUE0RV83M0U3NiIsICJzdWIiOiAiNzY1NjExOTkzOTIzNjIwODciLCAiYXVkIjogWyAid2ViOmNvbW11bml0eSIgXSwgImV4cCI6IDE3MDgzMjU2ODcsICJuYmYiOiAxNjk5NTk4OTY1LCAiaWF0IjogMTcwODIzODk2NSwgImp0aSI6ICIxQjRCXzIzRjcxQTUwXzFCMDZGIiwgIm9hdCI6IDE3MDgyMzg5NjUsICJydF9leHAiOiAxNzI2Mzk5MTUzLCAicGVyIjogMCwgImlwX3N1YmplY3QiOiAiMTEzLjI1MC42Ny43IiwgImlwX2NvbmZpcm1lciI6ICIxMTMuMjUwLjY3LjciIH0.rq7BkEmo8q5TSOwOOlzoJITtU9kMSmpFphwA_G6-jCvTFCaoXjlU96RooWAmXQwFQSpz8CvG1n0wxvB8sUaaCQ";
    /**
     * webapikey
     */
    @Getter
    private Optional<String> webApiKey = Optional.empty();
    /**
     * steamId
     */
    @Getter
    private Optional<String> steamId = Optional.empty();
    /**
     * sessionId
     * 会话ID
     */
    @Getter
    private Optional<String> sessionId = Optional.empty();
    /**
     * 交易链接
     */
    @Getter
    private Optional<String> treadUrl = Optional.empty();


    private ConfigService configService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Optional<String> browserid;

    private SteamMaFile steamMaFile;

    public SteamWeb(ConfigService configService) {
        this.configService = configService;
    }

    public static OkHttpClient getClient(boolean retry, int timeOut, String cookies, String url) {
        OkHttpClient.Builder OKHTTP_BUILD = new OkHttpClient.Builder();
        OKHTTP_BUILD.writeTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.readTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.connectTimeout(3, TimeUnit.SECONDS);
        OKHTTP_BUILD.retryOnConnectionFailure(retry);
        OKHTTP_BUILD.hostnameVerifier(HttpUtil.getHostnameVerifier());
        OKHTTP_BUILD.sslSocketFactory(HttpUtil.getSSLSocketFactory(), HttpUtil.getTrustManager());
        OKHTTP_BUILD.cookieJar(new SteamCustomCookieJar(cookies, url));
        return OKHTTP_BUILD.build();
    }

    /**
     * 登录steam网站
     *
     * @param passwd 密码
     * @param maFile ma文件结构
     */
    public void login(String passwd, SteamMaFile maFile) {
        steamMaFile = maFile;
        //steam登录代理
        ConfigDO configByKey = configService.getConfigByKey("steam.proxy");
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(configByKey.getValue()+"login");
//        builder.url("http://127.0.0.1:25852/login");
        builder.method(HttpUtil.Method.FORM);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username", steamMaFile.getAccountName());
        stringStringHashMap.put("password", passwd);
        stringStringHashMap.put("token_code", steamMaFile.getSharedSecret());
        builder.form(stringStringHashMap);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 30000, null, null));
        SteamCookie json = sent.json(SteamCookie.class);
        if (json.getCode() != 0) {
            log.error("Steam通讯失败{}", json);
            throw new ServiceException(-1, "Steam通讯失败" + json.getMsg());
        }
        cookieString = json.getData().getCookie();
        initApiKey();
    }

    /**
     * 初始化apikey session browserid等数据为必调接口
     */
    private void initApiKey() {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/dev/apikey");
        builder.method(HttpUtil.Method.GET);
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 3000, cookieString, "https://steamcommunity.com/dev/apikey"));
        Pattern pattern = Pattern.compile("密钥: (.*?)<"); // 正则表达式匹配API密钥
        Matcher matcher = pattern.matcher(sent.html());
        if (matcher.find()) {
            webApiKey = Optional.of(matcher.group(1));
        }
        Pattern pattern2 = Pattern.compile("https://steamcommunity.com/profiles/(\\d+)/");
        Matcher matcher2 = pattern2.matcher(sent.html());
        if (matcher2.find()) {
            steamId = Optional.of(matcher2.group(1));
        }
        //set browserid
        Optional<Cookie> browserOptional = sent.getCookies().stream().filter(item -> item.name().equals("browserid")).findFirst();
        if (browserOptional.isPresent()) {
            browserid = Optional.of(browserOptional.get().value());
            cookieString += ";browserid=" + browserid.get();
        } else {
            throw new ServiceException(-1, "获取浏览器ID失败");
        }
        Optional<Cookie> sessionOptional = sent.getCookies().stream().filter(item -> item.name().equals("sessionid")).findFirst();
        if (sessionOptional.isPresent()) {
            sessionId = Optional.of(sessionOptional.get().value());
            cookieString += ";sessionid=" + sessionId.get();
        } else {
            throw new ServiceException(-1, "获取sesionID失败");
        }
    }

    /**
     * 初始化交易链接
     */
    public void initTradeUrl() {
        if (!steamId.isPresent()) {
            initApiKey();
        }
        if (!steamId.isPresent()) {
            throw new ServiceException(-1, "初始化steam失败，请重新登录");
        }
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/profiles/:steamId/tradeoffers/privacy");
        builder.method(HttpUtil.Method.GET);
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        Map<String, String> pathVar = new HashMap<>();
        pathVar.put("steamId", steamId.get());
        builder.pathVar(pathVar);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 3000, cookieString, "https://steamcommunity.com/dev/apikey"));
        Pattern pattern = Pattern.compile("value=\"(.*?)\"");
        Matcher matcher = pattern.matcher(sent.html());
        if (matcher.find()) {
            treadUrl = Optional.of(matcher.group(1));
        } else {
            log.error("获取tradeUrl失败steamId{}", steamId);
            throw new ServiceException(-1, "获取tradeUrl失败");
        }
    }

    /**
     * 计算桌面令牌
     *
     * @param secret secret 桌面共享key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Deprecated
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
        SecretKeySpec secretKeySpec = new SecretKeySpec(decode, "HmacSHA1");
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
        return new String(chars);
    }

    /**
     * 转换ID
     *
     * @param id
     * @return
     */
    public String toCommunityID(String id) {
        if (id.startsWith("STEAM_")) {
            String[] parts = id.split(":");
            BigInteger part1 = new BigInteger(parts[1]);
            BigInteger part2 = new BigInteger(parts[2]);
            BigInteger communityID = part1.add(part2.multiply(BigInteger.valueOf(2))).add(BigInteger.valueOf(76561197960265728L));
            return communityID.toString();
        } else if (id.matches("\\d+") && id.length() < 16) {
            BigInteger steamID = new BigInteger(id);
            BigInteger communityID = steamID.add(BigInteger.valueOf(76561197960265728L));
            return communityID.toString();
        } else {
            return id;
        }
    }

    /**
     * 我方发送报价是到对方 本接口会自动进行手机确认
     * 参考
     * https://www.coder.work/article/8018121#google_vignette
     *
     * @param steamInvDto 我方交易商品
     * @param tradeUrl    接收方交易链接 如 https://steamcommunity.com/tradeoffer/new/?partner=1432096359&token=giLGhxtN
     * @return
     */
    public SteamTradeOfferResult trade(SteamInvDto steamInvDto, String tradeUrl) {
        try {
            URI uri = URI.create(tradeUrl);
            String query = uri.getQuery();
            log.info(query);
            Map<String, String> stringStringMap = parseQuery(query);
            log.info(String.valueOf(stringStringMap));
            HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
            builder.url("https://steamcommunity.com/tradeoffer/new/send");
            builder.method(HttpUtil.Method.FORM);
            Map<String, String> post = new HashMap<>();
            post.put("sessionid", sessionId.get());
            post.put("serverid", "1");
            post.put("partner", toCommunityID(stringStringMap.get("partner")));
            post.put("captcha", "");
            post.put("trade_offer_create_params", "{\"trade_offer_access_token\":\"" + stringStringMap.get("token") + "\"}");
            SteamTradeOffer steamTradeOffer = new SteamTradeOffer();
            steamTradeOffer.setNewversion(true);
            steamTradeOffer.setVersion(2);
            SteamTradeOffer.MeDTO meDTO = new SteamTradeOffer.MeDTO();
            meDTO.setReady(true);
            meDTO.setCurrency(new ArrayList<>());
            SteamTradeOffer.MeDTO.AssetsDTO assetsDTO = new SteamTradeOffer.MeDTO.AssetsDTO();
            assetsDTO.setAppid(steamInvDto.getAppid());
            assetsDTO.setContextid(steamInvDto.getContextid());
            assetsDTO.setAmount(Integer.valueOf(steamInvDto.getAmount()));
            assetsDTO.setAssetid(steamInvDto.getAssetid());
            meDTO.setAssets(Collections.singletonList(assetsDTO));
            steamTradeOffer.setMe(meDTO);
            SteamTradeOffer.ThemDTO themDTO = new SteamTradeOffer.ThemDTO();
            themDTO.setAssets(Collections.emptyList());
            themDTO.setCurrency(Collections.emptyList());
            themDTO.setReady(false);
            steamTradeOffer.setThem(themDTO);
            post.put("json_tradeoffer", objectMapper.writeValueAsString(steamTradeOffer));
            post.put("tradeoffermessage", "交易测试" + LocalDateTime.now());
            builder.form(post);
            Map<String, String> header = new HashMap<>();
            header.put("Accept-Language", "zh-CN,zh;q=0.9");
            header.put("Referer", tradeUrl);
            builder.headers(header);
            log.info("发送到对方服务器数据{}", objectMapper.writeValueAsString(post));
            HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 3000, "browserid=" + browserid.get() + "; strTradeLastInventoryContext=730_2; " + cookieString, "https://steamcommunity.com/dev/apikey"));
            log.info("交易结果{}", sent.html());
            SteamTradeOfferResult json = sent.json(SteamTradeOfferResult.class);
            Optional<MobileConfList.ConfDTO> confDTO = confirmOfferList(json.getTradeofferid());
            if (confDTO.isPresent()) {
//                confirmOffer(confDTO.get(), ConfirmAction.CANCEL);
                confirmOffer(confDTO.get(), ConfirmAction.ALLOW);
            }else {
                log.warn("交易单据未进行手机自动确认{}",json.getTradeofferid());
            }
            return json;
        } catch (UnsupportedEncodingException e) {
            log.error("解码出错{}", e);
            throw new ServiceException(-1, "发起交易报价失败原因，解码输入出错");
        } catch (JsonProcessingException e) {
            log.error("交易出错，可能 库存不正确{}", e);
            throw new ServiceException(-1, "发起交易报价失败原因，交易出错，可能 库存不正确");
        }
    }

    /**
     * 生成确认列表签名
     *
     * @param tag
     * @return
     */
    private ConfirmHash generateConfirmationHashForTime(String tag) throws NoSuchAlgorithmException, InvalidKeyException {
        long time = System.currentTimeMillis() / 1000l;
        byte[] arr = new byte[8];
        // 将 long 类型的 time 转换为 8 个字节的大端序
        for (int i = 0; i < 8; i++) {
            arr[i] = (byte) (time >> ((7 - i) * 8));
        }

        // 将 tag 转换为 UTF-8 编码的字节数组，并确保长度不超过 32 字节
        byte[] tagBytes = tag.getBytes(StandardCharsets.UTF_8);
        if (tagBytes.length > 32) {
            tagBytes = Arrays.copyOf(tagBytes, 32);
        }

        // 将时间字节和标签字节合并
        byte[] data = new byte[arr.length + tagBytes.length];
        System.arraycopy(arr, 0, data, 0, arr.length);
        System.arraycopy(tagBytes, 0, data, arr.length, tagBytes.length);

        // 创建 HMAC-SHA1 Mac 实例
        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        byte[] decode = Base64.getDecoder().decode(steamMaFile.getIdentitySecret());
        SecretKeySpec secret_key = new SecretKeySpec(decode, "HmacSHA1");
        sha1_HMAC.init(secret_key);

        // 计算 HMAC 并返回 Base64 编码的哈希值
        byte[] hashedData = sha1_HMAC.doFinal(data);
        ConfirmHash confirmHash = new ConfirmHash();
        confirmHash.setHash(Base64.getEncoder().encodeToString(hashedData));
        confirmHash.setTime(time);
        return confirmHash;
    }

    public SteamResult confirmOffer(MobileConfList.ConfDTO confDTO, ConfirmAction confirmAction) {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/mobileconf/ajaxop");
        builder.method(HttpUtil.Method.GET);
        Map<String, String> query = new HashMap<>();
        query.put("op", confirmAction.getCode());
        query.put("cid", confDTO.getId());
        query.put("ck", confDTO.getNonce());
        query.put("p", steamMaFile.getDeviceId());
        query.put("a", steamId.get());
        query.put("m", "react");
        String tag = "reject";
        switch (confirmAction) {
            case ALLOW:
                tag = "accept";
                break;
            case CANCEL:
                tag = "reject";
                break;
            default:
                throw new ServiceException(-1, "不支持的操作");
        }
        query.put("tag", tag);
        try {
            ConfirmHash confirmHash = generateConfirmationHashForTime(tag);
            query.put("k", confirmHash.getHash());
            query.put("t", String.valueOf(confirmHash.getTime()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        builder.query(query);
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 1000, cookieString, "https://steamcommunity.com/mobileconf/getlist"));
        SteamResult json = sent.json(SteamResult.class);
        return json;
    }

    /**
     * 获取指定待手机确认的订单
     *
     * @param tradeOrderId 订单号
     * @return
     */
    public Optional<MobileConfList.ConfDTO> confirmOfferList(String tradeOrderId) {
        List<MobileConfList.ConfDTO> confDTOS = confirmOfferList();
        return confDTOS.stream().filter(item -> item.getCreatorId().equals(tradeOrderId)).findFirst();
    }
    /**
     * 获取所有待手机确认的订单
     * @return
     */
    public List<MobileConfList.ConfDTO> confirmOfferList() {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/mobileconf/getlist");
        builder.method(HttpUtil.Method.GET);
        Map<String, String> query = new HashMap<>();
        query.put("p", steamMaFile.getDeviceId());
        query.put("a", steamId.get());
        try {
            ConfirmHash confirmHash = generateConfirmationHashForTime("conf");
            query.put("k", confirmHash.getHash());
            query.put("t", String.valueOf(confirmHash.getTime()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        query.put("m", "react");
        query.put("tag", "conf");
        builder.query(query);
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), getClient(true, 1000, cookieString, "https://steamcommunity.com/mobileconf/getlist"));
        MobileConfList json = sent.json(MobileConfList.class);
        return json.getConf();
    }
    private Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> ret = new HashMap<>();
        String[] split = query.split("&");
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split("=");
            ret.put(split1[0], URLDecoder.decode(split1[1], "utf-8"));
        }
        return ret;
    }
//    public static void main(String[] args) {
//
//        String maFileString = "{\"shared_secret\":\"NTDfP+vPKSLS2O8lXKJgdAp5QRI=\",\"serial_number\":\"3438498994078918164\",\"revocation_code\":\"R34847\",\"uri\":\"otpauth://totp/Steam:6wws6f?secret=GUYN6P7LZ4USFUWY54SVZITAOQFHSQIS&issuer=Steam\",\"server_time\":1701078470,\"account_name\":\"6wws6f\",\"token_gid\":\"2fba3858dfc17a80\",\"identity_secret\":\"q4roYdO+Cz/QOSVHB5m7pbYAyCc=\",\"secret_1\":\"m6OeZ+3cuVr/I/kEK2tjqhgbzSs=\",\"status\":1,\"device_id\":\"android:06ad9382-4690-45c1-90e9-2fc31cf803dc\",\"fully_enrolled\":true,\"Session\":{\"SteamID\":76561199392362087,\"AccessToken\":\"eyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInI6MEU3Nl8yM0VDNjYwQV9FNDA1MiIsICJzdWIiOiAiNzY1NjExOTkzOTIzNjIwODciLCAiYXVkIjogWyAid2ViIiwgIm1vYmlsZSIgXSwgImV4cCI6IDE3MDczNzI1MjEsICJuYmYiOiAxNjk4NjQ0Mzc5LCAiaWF0IjogMTcwNzI4NDM3OSwgImp0aSI6ICIwRTdCXzIzRUM2NjEwX0RFREQ0IiwgIm9hdCI6IDE3MDcyODQzNzksICJydF9leHAiOiAxNzI1NTcyOTA5LCAicGVyIjogMCwgImlwX3N1YmplY3QiOiAiMTEzLjI1MS45OS4yMTIiLCAiaXBfY29uZmlybWVyIjogIjExMy4yNTEuOTkuMjEyIiB9.MwVd4CC8DZcjENIKU9X4zDrHYauOsBclYEoyvfOsOtxOM8sYkhhwW9PxzjlweFa7_tkpSKrkXw13Quhwp6X3CA\",\"RefreshToken\":\"eyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInN0ZWFtIiwgInN1YiI6ICI3NjU2MTE5OTM5MjM2MjA4NyIsICJhdWQiOiBbICJ3ZWIiLCAicmVuZXciLCAiZGVyaXZlIiwgIm1vYmlsZSIgXSwgImV4cCI6IDE3MjU1NzI5MDksICJuYmYiOiAxNjk4NjQ0Mzc5LCAiaWF0IjogMTcwNzI4NDM3OSwgImp0aSI6ICIwRTc2XzIzRUM2NjBBX0U0MDUyIiwgIm9hdCI6IDE3MDcyODQzNzksICJwZXIiOiAxLCAiaXBfc3ViamVjdCI6ICIxMTMuMjUxLjk5LjIxMiIsICJpcF9jb25maXJtZXIiOiAiMTEzLjI1MS45OS4yMTIiIH0.zYwXhy5shkMyjUJ4s42bbKovBSk6LTmo9-JFUHoGRpUKBRqexr9k35olVDezMX3MVIZ8nN5gwnXYLGQ9ayVWAQ\",\"SessionID\":null}}";
//
//        SteamWeb steamWeb = new SteamWeb();
//        try {
//            SteamMaFile steamMaFile = steamWeb.objectMapper.readValue(maFileString, SteamMaFile.class);
//            steamWeb.login("QFhG9jSs", steamMaFile);
//            steamWeb.initApiKey();
//            String tradeUrl = "https://steamcommunity.com/tradeoffer/new/?partner=1440000356&token=5_JGX9AA";
//            SteamInvDto steamInvDto = new SteamInvDto();
//            steamInvDto.setAppid(730);
//            steamInvDto.setContextid("2");
//            steamInvDto.setAssetid("35681966437");
//            steamInvDto.setInstanceid("302028390");
//            steamInvDto.setClassid("4901046679");
//            steamInvDto.setAmount("1");
//            SteamTradeOfferResult trade = steamWeb.trade(steamInvDto, tradeUrl);
//            log.info("将临信息{}", trade);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
}
