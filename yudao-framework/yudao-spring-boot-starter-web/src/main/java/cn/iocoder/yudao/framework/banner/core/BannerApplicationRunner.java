package cn.iocoder.yudao.framework.banner.core;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 * @author 芋道源码
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    private static final Map<String, String> MODULE_MAP = MapBuilder.create(new LinkedHashMap<String, String>())
            // 数据报表
            .put("cn.iocoder.yudao.module.report.framework.security.config.SecurityConfiguration",
                    "[报表模块 yudao-module-report - 已禁用][参考 https://doc.iocoder.cn/report/ 开启]")
            // 工作流
            .put("cn.iocoder.yudao.module.bpm.framework.flowable.config.BpmFlowableConfiguration",
                    "[工作流模块 yudao-module-bpm - 已禁用][参考 https://doc.iocoder.cn/bpm/ 开启]")
            // 商城系统
            .put("cn.iocoder.yudao.module.trade.framework.web.config.TradeWebConfiguration",
                    "[商城系统 yudao-module-mall - 已禁用][参考 https://doc.iocoder.cn/mall/build/ 开启]")
            // ERP 系统
            .put("cn.iocoder.yudao.module.erp.framework.web.config.ErpWebConfiguration",
                    "[ERP 系统 yudao-module-erp - 已禁用][参考 https://doc.iocoder.cn/erp/build/ 开启]")
            // CRM 系统
            .put("cn.iocoder.yudao.module.crm.framework.web.config.CrmWebConfiguration",
                    "[CRM 系统 yudao-module-crm - 已禁用][参考 https://doc.iocoder.cn/crm/build/ 开启]")
            // 微信公众号
            .put("cn.iocoder.yudao.module.mp.framework.mp.config.MpConfiguration",
                    "[微信公众号 yudao-module-mp - 已禁用][参考 https://doc.iocoder.cn/mp/build/ 开启]")
            // 支付平台
            .put("cn.iocoder.yudao.module.pay.framework.pay.config.PayConfiguration",
                    "[支付系统 yudao-module-pay - 已禁用][参考 https://doc.iocoder.cn/pay/build/ 开启]")
            // AI 大模型
            .put("cn.iocoder.yudao.module.ai.framework.web.config.AiWebConfiguration",
                    "[AI 大模型 yudao-module-ai - 已禁用][参考 https://doc.iocoder.cn/ai/build/ 开启]")
            // IOT 物联网
            .put("cn.iocoder.yudao.module.iot.framework.web.config.IotWebConfiguration",
                    "[IOT 物联网 yudao-module-iot - 已禁用][参考 https://doc.iocoder.cn/iot/build/ 开启]")
            .build();

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" +
                            "接口文档: \t{} \n\t" +
                            "开发文档: \t{} \n\t" +
                            "视频教程: \t{} \n" +
                            "----------------------------------------------------------",
                    "https://doc.iocoder.cn/api-doc/",
                    "https://doc.iocoder.cn",
                    "https://t.zsxq.com/02Yf6M7Qn");

            MODULE_MAP.entrySet()
                    .stream()
                    .filter(t -> isNotPresent(t.getKey()))
                    .forEach(entry -> System.out.println(entry.getValue()));
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
