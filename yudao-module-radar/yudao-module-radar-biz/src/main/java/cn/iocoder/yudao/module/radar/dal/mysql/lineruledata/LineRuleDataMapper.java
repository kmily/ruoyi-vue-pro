package cn.iocoder.yudao.module.radar.dal.mysql.lineruledata;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo.*;

/**
 * 绊线数据 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface LineRuleDataMapper extends BaseMapperX<LineRuleDataDO> {

    default PageResult<LineRuleDataDO> selectPage(LineRuleDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LineRuleDataDO>()
                .eqIfPresent(LineRuleDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(LineRuleDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(LineRuleDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(LineRuleDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(LineRuleDataDO::getLineNum, reqVO.getLineNum())
                .eqIfPresent(LineRuleDataDO::getLineData, reqVO.getLineData())
                .betweenIfPresent(LineRuleDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LineRuleDataDO::getId));
    }

    default List<LineRuleDataDO> selectList(LineRuleDataExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<LineRuleDataDO>()
                .eqIfPresent(LineRuleDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(LineRuleDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(LineRuleDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(LineRuleDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(LineRuleDataDO::getLineNum, reqVO.getLineNum())
                .eqIfPresent(LineRuleDataDO::getLineData, reqVO.getLineData())
                .betweenIfPresent(LineRuleDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LineRuleDataDO::getId));
    }


    default List<LineRuleDataDO> selectList(Long deviceId, String start, String end){
        return selectList(new LambdaQueryWrapperX<LineRuleDataDO>()
                .eq(LineRuleDataDO::getDeviceId, deviceId)
                .between(BaseDO::getCreateTime, start, end)
                .orderByDesc(LineRuleDataDO::getId));
    }

}
