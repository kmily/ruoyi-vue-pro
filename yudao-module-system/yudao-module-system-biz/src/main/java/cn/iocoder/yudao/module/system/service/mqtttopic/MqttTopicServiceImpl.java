package cn.iocoder.yudao.module.system.service.mqtttopic;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.mqtttopic.dto.MqttTopicRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicUpdateReqVO;
import cn.iocoder.yudao.module.system.convert.mqtttopic.MqttTopicConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import cn.iocoder.yudao.module.system.dal.mysql.mqtttopic.MqttTopicMapper;
import cn.iocoder.yudao.module.system.dal.redis.mqtttopic.MqttTopicRedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MQTT_TOPIC_NOT_EXISTS;

/**
 * mqtt主题 Service 实现类
 *
 * @author ZanGe
 */
@Service
@Validated
@Slf4j
public class MqttTopicServiceImpl implements MqttTopicService {

    @Resource
    private MqttTopicMapper mqttTopicMapper;
    @Resource
    private MqttTopicRedisDAO mqttTopicRedisDAO;

    @Override
    public Long createMqttTopic(MqttTopicCreateReqVO createReqVO) {
        // 插入
        MqttTopicDO mqttTopic = MqttTopicConvert.INSTANCE.convert(createReqVO);
        mqttTopicMapper.insert(mqttTopic);
        // 返回
        return mqttTopic.getId();
    }

    @Override
    public void updateMqttTopic(MqttTopicUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateMqttTopicExists(updateReqVO.getId());
        // 更新
        MqttTopicDO updateObj = MqttTopicConvert.INSTANCE.convert(updateReqVO);
        mqttTopicMapper.updateById(updateObj);
    }

    @Override
    public void deleteMqttTopic(Long id) {
        // 校验存在
        this.validateMqttTopicExists(id);
        // 删除
        mqttTopicMapper.deleteById(id);
    }

    private void validateMqttTopicExists(Long id) {
        if (mqttTopicMapper.selectById(id) == null) {
            throw exception(MQTT_TOPIC_NOT_EXISTS);
        }
    }

    @Override
    public MqttTopicDO getMqttTopic(Long id) {
        return mqttTopicMapper.selectById(id);
    }

    @Override
    public List<MqttTopicDO> getMqttTopicList(Collection<Long> ids) {
        return mqttTopicMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MqttTopicDO> getMqttTopicPage(MqttTopicPageReqVO pageReqVO) {
        return mqttTopicMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MqttTopicDO> getMqttTopicList(MqttTopicExportReqVO exportReqVO) {
        return mqttTopicMapper.selectList(exportReqVO);
    }

    /**
     * 初始化redis中主题信息缓存
     */
    @Override
    @PostConstruct
    public void initMqttTopicCache() {
        List<MqttTopicDO> list = mqttTopicMapper.selectList(new MqttTopicExportReqVO().setStatus(0));
        for (MqttTopicDO mqttTopicDO : list) {
            mqttTopicRedisDAO.set(mqttTopicDO);
        }
        log.info("[initTopicCache][缓存MQTT主题，数量为:{}]", list.size());
    }

    /**
     * 从redis缓存获取数据，不存在则从数据查询并插入缓存
     *
     * @param subTopic 订阅主题
     */
    @Override
    public MqttTopicRespDTO getTopicFromCache(String subTopic) {
        MqttTopicDO mqttTopic = mqttTopicRedisDAO.get(subTopic);
        if (mqttTopic == null) {
            mqttTopic = mqttTopicMapper.selectBySubTopic(subTopic);
            if (0 == mqttTopic.getStatus()) {
                mqttTopicRedisDAO.set(mqttTopic);
            }
        }
        return MqttTopicConvert.INSTANCE.convert01(mqttTopic);
    }

}
