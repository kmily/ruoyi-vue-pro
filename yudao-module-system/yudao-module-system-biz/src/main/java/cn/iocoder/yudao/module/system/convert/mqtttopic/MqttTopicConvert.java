package cn.iocoder.yudao.module.system.convert.mqtttopic;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.mqtttopic.dto.MqttTopicRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicRespVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * mqtt主题 Convert
 *
 * @author ZanGe
 */
@Mapper
public interface MqttTopicConvert {

    MqttTopicConvert INSTANCE = Mappers.getMapper(MqttTopicConvert.class);

    MqttTopicDO convert(MqttTopicCreateReqVO bean);

    MqttTopicDO convert(MqttTopicUpdateReqVO bean);

    MqttTopicRespVO convert(MqttTopicDO bean);

    List<MqttTopicRespVO> convertList(List<MqttTopicDO> list);

    PageResult<MqttTopicRespVO> convertPage(PageResult<MqttTopicDO> page);

    List<MqttTopicExcelVO> convertList02(List<MqttTopicDO> list);

    MqttTopicRespDTO convert01(MqttTopicDO bean);

}
