package cn.iocoder.yudao.module.radar.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * Created by l09655 on 2022/7/16.
 * HTTP相关工具类
 */
@Slf4j
public class HttpUtil {


    public static void setContentLength(HttpResponse res, int i) {
    }

    /**
     * @return java.util.List<java.lang.String>
     * @description: TODO 获取本地ip，无法得知用户需要哪个，列出能获取到的ip自己选择
     * @author l09655 2022年07月20日
     */
    public List<String> getLocalAddressList() {
        log.info("获取本机IP地址...\n");
        Set<String> ipSet = new HashSet<>();
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    // 排除回环地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // site-local地址
                            log.info("inetAddr===" + inetAddr + "\n");
                            ipSet.add(inetAddr.getHostAddress());
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                            log.info("candidateAddress===" + candidateAddress + "\n");
                            ipSet.add(candidateAddress.getHostAddress());
                        }
                    }
                }
            }
            //最次选
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                log.info("获取本机IP地址为空，请检查网络环境\n");
            }
            log.info("jdkSuppliedAddress===" + jdkSuppliedAddress + "\n");

            ipSet.add(jdkSuppliedAddress.getHostAddress());
            log.info("获取本机IP地址成功\n");
        } catch (Exception e) {
            log.info("获取本机IP地址失败，请检查网络环境\n");
            e.printStackTrace();
        }
        List<String> ipList = new ArrayList<>(ipSet);
        //一般用候选地址较多，所以调换顺序（暂定）
        Collections.reverse(ipList);
        return ipList;
    }

    /**
     * @param request
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @description: TODO 获取FullHttpRequest携带的参数
     * @author l09655 2022年07月19日
     */
    public static Map<String, String> getRequestParams(FullHttpRequest request) {
        Map<String, String> params = new HashMap<>();

        //判断请求方式，get请求获取url拼接的参数，post请求获取body
        if (HttpMethod.GET == request.method()) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> params.put(key, value.get(0)));
        } else if (HttpMethod.POST == request.method()) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            List<InterfaceHttpData> httpDataList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : httpDataList) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    params.put(attribute.getName(), attribute.getValue());
                }
            }
        } else {
            log.info("不支持的请求方法：" + request.method() + "\n");
        }
        return params;
    }

}
