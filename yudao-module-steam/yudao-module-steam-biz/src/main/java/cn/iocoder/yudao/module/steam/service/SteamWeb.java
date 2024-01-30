package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.module.steam.service.steam.CustomCookieJar;
import cn.iocoder.yudao.module.steam.service.steam.LoginValue;
import cn.iocoder.yudao.module.steam.service.steam.RsaValue;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeanUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SteamWeb {
    private OkHttpClient okHttpClient;
    private List<Cookie> cookies=new ArrayList<>();

    public SteamWeb() {
        okHttpClient=getClient(true,30);
    }
    public static OkHttpClient getClient(boolean retry, int timeOut) {
        OkHttpClient.Builder OKHTTP_BUILD = new OkHttpClient.Builder();
        OKHTTP_BUILD.writeTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.readTimeout(timeOut, TimeUnit.SECONDS);
        OKHTTP_BUILD.connectTimeout(3, TimeUnit.SECONDS);
        OKHTTP_BUILD.retryOnConnectionFailure(retry);
        OKHTTP_BUILD.hostnameVerifier(HttpUtil.getHostnameVerifier());
        OKHTTP_BUILD.sslSocketFactory(HttpUtil.getSSLSocketFactory(), HttpUtil.getTrustManager());
        OKHTTP_BUILD.cookieJar(new CustomCookieJar());
        return OKHTTP_BUILD.build();
    }
    /**
     * 登录 steam帐号
     * @param user 用户名
     * @param passwd 密码
     * @param sharedSecret sharedSecret
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public void login(String user,String passwd,String sharedSecret) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/login/getrsakey");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username",user);
        builder.form(stringStringHashMap).method(HttpUtil.Method.FORM);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(), okHttpClient);
        cookies.addAll(sent.getCookies());
        RsaValue json = sent.json(RsaValue.class);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(json.getPublickeyMod(), 16), new BigInteger(json.getPublickeyExp(), 16));
        KeyFactory rsa = KeyFactory.getInstance("RSA");
        PublicKey publicKey = rsa.generatePublic(rsaPublicKeySpec);
        Cipher rsa1 = Cipher.getInstance("RSA");
        rsa1.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] bytes1 = rsa1.doFinal(passwd.getBytes());
        stringStringHashMap.put("password", Base64.getEncoder().encodeToString(bytes1));

        log.info(stringStringHashMap.toString());
        LoginValue loginValue=new LoginValue();
        do {
            HttpUtil.HttpRequest.HttpRequestBuilder builder1 = HttpUtil.HttpRequest.builder();
            builder1.url("https://steamcommunity.com/login/dologin/");
            builder1.method(HttpUtil.Method.FORM);
            stringStringHashMap.put("remember_login", "true");
            stringStringHashMap.put("rsatimestamp", json.getTimestamp());
            if(Objects.isNull(stringStringHashMap.get("emailauth"))) {
                stringStringHashMap.put("emailauth", getSteamAuthCode(sharedSecret));
            }
            stringStringHashMap.put("emailsteamid", "");
            if(Objects.isNull(stringStringHashMap.get("captchagid"))) {
                stringStringHashMap.put("captchagid", "-1");
            }
            if(Objects.isNull(stringStringHashMap.get("twofactorcode"))){
                stringStringHashMap.put("twofactorcode", getSteamAuthCode(sharedSecret));
            }
//            log.info("post{}", JSON.toJSON(stringStringHashMap));
            builder1.form(stringStringHashMap);
            HttpUtil.HttpResponse sent1 = HttpUtil.sent(builder1.build(), okHttpClient);
            cookies.addAll(sent.getCookies());
            LoginValue json1 = sent1.json(LoginValue.class);
            BeanUtils.copyProperties(json1,loginValue);
            Thread.sleep(200l);
            if(Objects.nonNull(json1.getRequiresTwofactor())){
                stringStringHashMap.put("twofactorcode", getSteamAuthCode(sharedSecret));
            }
            if(Objects.nonNull(json1.getEmailAuthNeeded())){
                stringStringHashMap.put("emailauth", getSteamAuthCode(sharedSecret));
            }
            if(Objects.nonNull(json1.getCaptchaGid())){
                log.warn("此帐号需要使用图片进行登录验证，https://steamcommunity.com/public/captcha.php?gid="+json1.getCaptchaGid());
                stringStringHashMap.put("captchagid", json1.getCaptchaGid());
                Scanner scanner=new Scanner(System.in);
                String next = scanner.next();
                stringStringHashMap.put("captcha_text", next);
//                log.warn("此帐号需要使用图片进行登录验证，登录失败不再尝试登录");
//                break;
            }
        }while (!loginValue.getSuccess());
        log.info(loginValue.toString());
        return;
    }
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     *	获取API-KEY
     *	@return string $apikey
     */
    public Optional<String> getApiKey() {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url("https://steamcommunity.com/dev/apikey");
        builder.method(HttpUtil.Method.GET);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),okHttpClient);
        Pattern compile = Pattern.compile("/<h2>Access Denied</h2>/");
        if(compile.matcher(sent.html()).find()){
            return Optional.empty();
        }
        Pattern compile2 = Pattern.compile("/<p>Key: (.*)</p>/");
        Matcher matcher = compile2.matcher(sent.html());
        if(matcher.find()){
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
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


    public static void main(String[] args) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        SteamWeb steamWeb=new SteamWeb();
        steamWeb.login("pbgx5e","deNXffnN","Um+FCAwJIBh9/a7qn2vP+tigoaM=");
//        steamWeb.login("6WwS6f","QFhG9jSs","NTDfP+vPKSLS2O8lXKJgdAp5QRI=");
        log.info(steamWeb.getApiKey().get());
//        String steamAuthCode = steamWeb.getSteamAuthCode("Um+FCAwJIBh9/a7qn2vP+tigoaM=");
//        log.info(steamAuthCode);
//        String steamAuthCode1 = steamWeb.getSteamAuthCode("Um+FCAwJIBh9/a7qn2vP+tigoaM=");
//        String steamAuthCode1 = steamWeb.getSteamAuthCode("Um+FCAwJIBh9/a7qn2vP+tigoaM=");
//        log.info(steamAuthCode1);
//        log.info(steamAuthCode1);
    }
}
