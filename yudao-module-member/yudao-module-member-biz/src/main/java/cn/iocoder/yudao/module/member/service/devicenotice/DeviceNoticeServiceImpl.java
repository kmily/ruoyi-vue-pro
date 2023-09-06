package cn.iocoder.yudao.module.member.service.devicenotice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;
import cn.iocoder.yudao.module.member.service.detectionalarmsettings.DetectionAlarmSettingsService;
import cn.iocoder.yudao.module.member.service.deviceuser.dto.FamilyAndRoomDeviceDTO;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.member.service.healthalarmsettings.HealthAlarmSettingsService;
import cn.iocoder.yudao.module.radar.enums.DeviceDataTypeEnum;
import cn.iocoder.yudao.module.system.api.notice.NoticeApi;
import cn.iocoder.yudao.module.system.api.notice.dto.NoticeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.devicenotice.DeviceNoticeConvert;
import cn.iocoder.yudao.module.member.dal.mysql.devicenotice.DeviceNoticeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 设备通知 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceNoticeServiceImpl implements DeviceNoticeService {

    @Resource
    private DeviceNoticeMapper deviceNoticeMapper;

    @Resource
    private HealthAlarmSettingsService healthAlarmSettingsService;

    @Resource
    private DetectionAlarmSettingsService detectionAlarmSettingsService;

    @Resource
    private DeviceUserService deviceUserService;

    @Resource
    private NoticeApi noticeApi;



    @Override
    public Long createDeviceNotice(AppDeviceNoticeCreateReqVO createReqVO) {
        // 插入
        DeviceNoticeDO deviceNotice = DeviceNoticeConvert.INSTANCE.convert(createReqVO);
        deviceNoticeMapper.insert(deviceNotice);
        // 返回
        return deviceNotice.getId();
    }

    @Override
    public void updateDeviceNotice(AppDeviceNoticeUpdateReqVO updateReqVO) {
        // 校验存在
        validateDeviceNoticeExists(updateReqVO.getId());
        // 更新
        DeviceNoticeDO updateObj = DeviceNoticeConvert.INSTANCE.convert(updateReqVO);
        deviceNoticeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceNotice(Long id) {
        // 校验存在
        validateDeviceNoticeExists(id);
        // 删除
        deviceNoticeMapper.deleteById(id);
    }

    private void validateDeviceNoticeExists(Long id) {
        if (deviceNoticeMapper.selectById(id) == null) {
            throw exception(DEVICE_NOTICE_NOT_EXISTS);
        }
    }

    @Override
    public DeviceNoticeDO getDeviceNotice(Long id) {
        return deviceNoticeMapper.selectById(id);
    }

    @Override
    public List<DeviceNoticeDO> getDeviceNoticeList(Collection<Long> ids) {
        return deviceNoticeMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceNoticeDO> getDeviceNoticePage(AppDeviceNoticePageReqVO pageReqVO) {
        return deviceNoticeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceNoticeDO> getDeviceNoticeList(AppDeviceNoticeExportReqVO exportReqVO) {
        return deviceNoticeMapper.selectList(exportReqVO);
    }

    @Async
    @Override
    public void createDeviceNotice(int type, String content, String number) {

        if(type == DeviceDataTypeEnum.HEALTH.type){
            JSONObject entry = JSONUtil.parseObj(content);
            Double heart = entry.getDouble("heart");
            Long time = entry.getLong("time");
            Long device = entry.getLong("device");
            TenantUtils.executeIgnore(() -> {
                HealthAlarmSettingsDO settings = healthAlarmSettingsService.getHealthAlarmSettings(device);
                if(Boolean.TRUE.equals(settings.getHeart())){
                    DeviceUserDO userDO = deviceUserService.getDeviceUserByDevice(device);
                    String range = settings.getHeartRange();
                    String[] split = range.split("-");
                    double min = Double.parseDouble(split[0]);
                    double max = Double.parseDouble(split[1]);
                    String format = DateUtil.format(new Date(time * 1000), "HH点mm分ss秒");
                    String msg = format + "设别到";

                    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(8));

                    if(min > heart || max < heart){
                        msg += "体征监测雷达检测到心率异常。心率["+ heart.intValue() + "次/分]，正常范围为" + range + "次/分";
                        deviceNoticeMapper.insert(new DeviceNoticeDO()
                                .setDeviceId(device)
                                .setFamilyId(userDO.getFamilyId())
                                .setContent(msg)
                                .setUserId(userDO.getUserId())
                                .setType((byte)type)
                                .setHappenTime(dateTime));
                    }
                }
            });
        }else if(type == DeviceDataTypeEnum.LINE_RULE.type){
            JSONObject entry = JSONUtil.parseObj(content);
            dealWithLineRuleData(entry);


        }

    }

    // 处理绊线数据通知
    private void dealWithLineRuleData(JSONObject entry) {

        Long device = entry.getLong("device");
        Long time = entry.getLong("time");
        Integer enter = entry.getInt("enter", 0);
        Integer goOut = entry.getInt("goOut", 0);
        TenantUtils.executeIgnore(() -> {
            DetectionAlarmSettingsDO alarmSettings = detectionAlarmSettingsService.getDetectionAlarmSettings(device);
            List<FamilyAndRoomDeviceDTO> familyAndRoomDeviceDTOS = deviceUserService.selectFamilyAndRoom(Collections.singleton(device));
            if(familyAndRoomDeviceDTOS.isEmpty()){
                return;
            }
            FamilyAndRoomDeviceDTO roomDeviceDTO = familyAndRoomDeviceDTOS.get(0);

            String temp = "人体检测雷达检测到有人回家！\n" +
                          "时间：%s\n" +
                          "地点：%s";

            String leave = "人体检测雷达检测到有人离家！\n" +
                    "时间：%s\n" +
                    "地点：%s";

            String format = DateUtil.format(new Date(time * 1000), FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(8));
            String address = roomDeviceDTO.getFamily() + "," + roomDeviceDTO.getRoom();
            DeviceUserDO userDO = deviceUserService.getDeviceUserByDevice(device);
            if(enter > 0 && alarmSettings.getEnter()){
                String content = String.format(temp, format, address);
                deviceNoticeMapper.insert(new DeviceNoticeDO()
                        .setDeviceId(device)
                        .setFamilyId(userDO.getFamilyId())
                        .setContent(content)
                        .setUserId(userDO.getUserId())
                        .setType((byte)DeviceDataTypeEnum.HEALTH.type)
                        .setHappenTime(dateTime));
            }
            if(goOut > 0 && alarmSettings.getGoOut()){
                String content = String.format(leave, format, address);
                deviceNoticeMapper.insert(new DeviceNoticeDO()
                        .setDeviceId(device)
                        .setFamilyId(userDO.getFamilyId())
                        .setContent(content)
                        .setUserId(userDO.getUserId())
                        .setType((byte)DeviceDataTypeEnum.HEALTH.type)
                        .setHappenTime(dateTime));
            }
        });
    }

    @Override
    public Long getUnReadCount(Long loginUserId, Long familyId) {
        return deviceNoticeMapper.selectUnReadCount(loginUserId, familyId);
    }

    @Override
    public Long getUnRead(Long userId, Long familyId) {
        Long count = this.getUnReadCount(userId, familyId);
        LocalDateTime maxDate =  deviceNoticeMapper.selectMaxDate(userId);
        List<NoticeDTO> notices = noticeApi.getNotices(maxDate);
        if(CollUtil.isNotEmpty(notices)){
            List<DeviceNoticeDO> noticeUserDOs = notices.stream().map(item -> new DeviceNoticeDO()
                    .setDeviceId(item.getId())
                    .setCategory((byte)1)
                    .setHappenTime(item.getCreateTime())
                    .setFamilyId(0L)
                    .setContent(item.getContent())
                    .setUserId(userId)).collect(Collectors.toList());
            deviceNoticeMapper.insertBatch(noticeUserDOs, 100);
        }
        return (count == null? 0: count) + notices.size();
    }

    @Override
    public void deleteByNoticeId(Long noticeId) {
        deviceNoticeMapper.delete(new LambdaQueryWrapper<DeviceNoticeDO>().eq(DeviceNoticeDO::getDeviceId, noticeId)
                .eq(DeviceNoticeDO::getCategory, 1));
    }

    @Override
    public void updateByNoticeId(Long noticeId, String content) {
        deviceNoticeMapper.updateByNoticeId(noticeId, content);
    }

    @Override
    public Long selectCount() {
        return deviceNoticeMapper.selectCount();
    }

}
