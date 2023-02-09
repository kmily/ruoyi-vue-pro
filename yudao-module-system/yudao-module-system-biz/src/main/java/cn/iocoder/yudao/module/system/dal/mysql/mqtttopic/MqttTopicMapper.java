package cn.iocoder.yudao.module.system.dal.mysql.mqtttopic;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo.MqttTopicPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mqtttopic.MqttTopicDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * mqtt主题 Mapper
 *
 * @author ZanGe
 */
@Mapper
public interface MqttTopicMapper extends BaseMapperX<MqttTopicDO> {

    default PageResult<MqttTopicDO> selectPage(MqttTopicPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MqttTopicDO>()
                .likeIfPresent(MqttTopicDO::getTopicName, reqVO.getTopicName())
                .eqIfPresent(MqttTopicDO::getSubTopic, reqVO.getSubTopic())
                .eqIfPresent(MqttTopicDO::getPusTopic, reqVO.getPusTopic())
                .eqIfPresent(MqttTopicDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MqttTopicDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MqttTopicDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MqttTopicDO::getId));
    }

    default List<MqttTopicDO> selectList(MqttTopicExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MqttTopicDO>()
                .likeIfPresent(MqttTopicDO::getTopicName, reqVO.getTopicName())
                .eqIfPresent(MqttTopicDO::getSubTopic, reqVO.getSubTopic())
                .eqIfPresent(MqttTopicDO::getPusTopic, reqVO.getPusTopic())
                .eqIfPresent(MqttTopicDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MqttTopicDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(MqttTopicDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MqttTopicDO::getId));
    }

    default MqttTopicDO selectBySubTopic(String subTopic) {
        return selectOne(new LambdaQueryWrapperX<MqttTopicDO>()
                .eqIfPresent(MqttTopicDO::getSubTopic, subTopic)
                .eqIfPresent(MqttTopicDO::getStatus, 0)
                .last("limit 1"));
    }

}
