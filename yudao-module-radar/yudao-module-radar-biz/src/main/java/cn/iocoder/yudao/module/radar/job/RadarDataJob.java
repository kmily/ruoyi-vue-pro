package cn.iocoder.yudao.module.radar.job;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import cn.iocoder.yudao.module.radar.bean.entity.HealthData;
import cn.iocoder.yudao.module.radar.bean.entity.RequestData;
import cn.iocoder.yudao.module.radar.cache.RadarDataCache;
import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.AreaRuleDataCreateReqVO;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.HealthDataCreateReqVO;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.LineRuleDataCreateReqVO;
import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.enums.DeviceDataTypeEnum;
import cn.iocoder.yudao.module.radar.service.arearuledata.AreaRuleDataService;
import cn.iocoder.yudao.module.radar.service.healthdata.HealthDataService;
import cn.iocoder.yudao.module.radar.service.lineruledata.LineRuleDataService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author whycode
 * @title: RadarDataJob
 * @projectName ruoyi-vue-pro
 * @description: 保存雷达数据的调度任务
 * @date 2023/8/215:45
 */

@Slf4j
@Component
public class RadarDataJob implements InitializingBean, Runnable {
    @Resource
    private HealthDataService healthDataService;

    @Resource
    private AreaRuleDataService areaRuleDataService;

    @Resource
    private LineRuleDataService lineRuleDataService;

    @Resource
    private DeviceCache deviceCache;



    @Override
    public void afterPropertiesSet() throws Exception {

//        ExecutorService service1 = Executors.newSingleThreadExecutor();
//        service1.submit(this);
//        service1.shutdown();

        new Thread(this).start();

    }


    private void saveHealthData(RequestData requestData){
        HealthData healthData = requestData.getHealthData();
        HealthDataCreateReqVO healthDataDO = HealthDataConvert.INSTANCE.convert(healthData);
        healthDataDO.setTimeStamp(requestData.getTimeStamp());
        healthDataDO.setSeq(requestData.getSeq());
        healthDataDO.setDeviceCode(requestData.getDeviceCode());
        DeviceDTO deviceDTO = deviceCache.getBySn(requestData.getDeviceCode());
        try {
            if(deviceDTO.getId() != null){
                healthDataDO.setDeviceId(deviceDTO.getId());
                TenantContextHolder.setTenantId(deviceDTO.getTenantId());
            }else{
                TenantContextHolder.setIgnore(true);
            }
            healthDataService.createHealthData(healthDataDO);
        } finally {
            TenantContextHolder.clear();
        }
    }


    /**
     * 保存区域统计数据
     * @param requestData
     */
    private void saveAreaRuleData(RequestData requestData){

        AreaRuleDataCreateReqVO createReqVO = new AreaRuleDataCreateReqVO();
        createReqVO.setAreaData(JSON.toJSONString(requestData.getAreaRuleDataList()))
                .setAreaNum(requestData.getAreaNum())
                .setDeviceCode(requestData.getDeviceCode())
                .setSeq(requestData.getSeq())
                .setTimeStamp(requestData.getTimeStamp());
        DeviceDTO deviceDTO = deviceCache.getBySn(requestData.getDeviceCode());
        try {
            if(deviceDTO.getId() != null){
                createReqVO.setDeviceId(deviceDTO.getId());
                TenantContextHolder.setTenantId(deviceDTO.getTenantId());
            }else{
                TenantContextHolder.setIgnore(true);
            }
            areaRuleDataService.createAreaRuleData(createReqVO);
        } finally {
            TenantContextHolder.clear();
        }

    }

    /**
     * 保存绊线统计数据
     * @param requestData
     */
    private void saveLineRuleData(RequestData requestData){
        LineRuleDataCreateReqVO createReqVO = new LineRuleDataCreateReqVO();
        createReqVO.setLineData(JSON.toJSONString(requestData.getLineRuleDataList()))
                .setLineNum(requestData.getAreaNum())
                .setDeviceCode(requestData.getDeviceCode())
                .setSeq(requestData.getSeq())
                .setTimeStamp(requestData.getTimeStamp());
        DeviceDTO deviceDTO = deviceCache.getBySn(requestData.getDeviceCode());
        try {
            if(deviceDTO.getId() != null){
                createReqVO.setDeviceId(deviceDTO.getId());
                TenantContextHolder.setTenantId(deviceDTO.getTenantId());
            }else{
                TenantContextHolder.setIgnore(true);
            }
            lineRuleDataService.createLineRuleData(createReqVO);
        } finally {
            TenantContextHolder.clear();
        }
    }


    @Override
    public void run() {
        while (true){
            try {
                RequestData requestData = RadarDataCache.take();
                DeviceDataTypeEnum type = requestData.getType();
                switch (type){
                    case HEALTH:
                        saveHealthData(requestData);
                        break;
                    case AREA_RULE:
                        saveAreaRuleData(requestData);
                        break;
                    case LINE_RULE:
                        saveLineRuleData(requestData);
                        break;
                    default:
                }
            } catch (InterruptedException e) {
               // e.printStackTrace();
                log.error("[SAVE - DATA] 调度保存数据出现异常", e);
            }
        }
    }
}
