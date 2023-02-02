package cn.iocoder.yudao.module.system.service.mqtttopic;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.mqtttopic.dto.MqttTopicRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * mqtt主题 Service 接口
 *
 * @author ZanGe
 */
public interface MqttTopicService {

    /**
     * 创建mqtt主题
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMqttTopic(@Valid MqttTopicCreateReqVO createReqVO);

    /**
     * 更新mqtt主题
     *
     * @param updateReqVO 更新信息
     */
    void updateMqttTopic(@Valid MqttTopicUpdateReqVO updateReqVO);

    /**
     * 删除mqtt主题
     *
     * @param id 编号
     */
    void deleteMqttTopic(Long id);

    /**
     * 获得mqtt主题
     *
     * @param id 编号
     * @return mqtt主题
     */
    MqttTopicDO getMqttTopic(Long id);

    /**
     * 获得mqtt主题列表
     *
     * @param ids 编号
     * @return mqtt主题列表
     */
    List<MqttTopicDO> getMqttTopicList(Collection<Long> ids);

    /**
     * 获得mqtt主题分页
     *
     * @param pageReqVO 分页查询
     * @return mqtt主题分页
     */
    PageResult<MqttTopicDO> getMqttTopicPage(MqttTopicPageReqVO pageReqVO);

    /**
     * 获得mqtt主题列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return mqtt主题列表
     */
    List<MqttTopicDO> getMqttTopicList(MqttTopicExportReqVO exportReqVO);

    /**
     * 初始化redis中主题信息缓存
     */
    void initMqttTopicCache();

    /**
     * 从redis缓存获取数据，不存在则从数据查询并插入缓存
     *
     * @subTopic 订阅主题
     */
    MqttTopicRespDTO getTopicFromCache(String subTopic);
}
