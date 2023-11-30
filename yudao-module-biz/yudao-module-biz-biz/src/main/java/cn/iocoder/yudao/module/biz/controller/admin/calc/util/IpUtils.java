package cn.iocoder.yudao.module.biz.controller.admin.calc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;

/**
 *
 */
@Slf4j
public abstract class IpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtils.class);

    /**
     * Gets the client ip addr.
     *
     * @return the client ip addr
     */
    public static String getClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //X-Real-IP是Nginx用来获取用户的真实ip地址，但是有时候可能获取不到返回是UNKONWN
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
            return ip;
        //X-Forwarded-For 有时候可能获取不到，因为服务器不一定使用了反向代理，所以可能返回的也是UNKONWN
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1)
                return ip.substring(0, index);
            else
                return ip;
        }
        //Proxy-Client-IP 是apache服务器定制一个请求头，这个属性只在apache服务器才可以看到。所以当服务器不是apache服务器，也会返回UNKONWN
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        //WL- Proxy-Client-IP 其中WL是weblogic的缩写，这通常用户weblogic服务器环境中，也可能返回UNKONWN
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        //HTTP_CLIENT_IP 有些代理服务器会加上此配置
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        //HTTP_X_FORWARD_FOR 可以在nginx配置此项，一般为 HTTP_X_FORWARD_FOR $remote_addr;
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * Gets the real ip.
     *
     * @return the real ip
     */
    public static String getRealIp() {
        String ip = null;
        try {
            ip = getClientIpAddr();
        } catch (Exception e) {
            LOGGER.error("getClientIp is Error", e.getMessage());
            return null;
        }
        return ip;
    }

    public static String getCityInfo(String ip) {
        //db
        String dbPath = IpUtils.class.getResource("/data/ip2region.db").getPath();

        File file = new File(dbPath);
        if (file.exists() == false) {
            System.out.println("Error: Invalid ip2region.db file");
        }

        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);

            //define the method
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }

            DataBlock dataBlock = null;
            if (org.lionsoul.ip2region.Util.isIpAddress(ip) == false) {
                System.out.println("Error: Invalid ip address");
            }

            dataBlock = (DataBlock) method.invoke(searcher, ip);

            return dataBlock.getRegion();

        } catch (Exception e) {
            log.error("发生异常", e);
        }

        return null;
    }

}