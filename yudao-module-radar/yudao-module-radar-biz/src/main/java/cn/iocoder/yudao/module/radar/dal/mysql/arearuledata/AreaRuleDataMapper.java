package cn.iocoder.yudao.module.radar.dal.mysql.arearuledata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata.AreaRuleDataDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.*;

/**
 * 区域数据 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AreaRuleDataMapper extends BaseMapperX<AreaRuleDataDO> {

    default PageResult<AreaRuleDataDO> selectPage(AreaRuleDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AreaRuleDataDO>()
                .eqIfPresent(AreaRuleDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(AreaRuleDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(AreaRuleDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(AreaRuleDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(AreaRuleDataDO::getAreaNum, reqVO.getAreaNum())
                .eqIfPresent(AreaRuleDataDO::getAreaData, reqVO.getAreaData())
                .betweenIfPresent(AreaRuleDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AreaRuleDataDO::getId));
    }

    default List<AreaRuleDataDO> selectList(AreaRuleDataExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AreaRuleDataDO>()
                .eqIfPresent(AreaRuleDataDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(AreaRuleDataDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(AreaRuleDataDO::getTimeStamp, reqVO.getTimeStamp())
                .eqIfPresent(AreaRuleDataDO::getSeq, reqVO.getSeq())
                .eqIfPresent(AreaRuleDataDO::getAreaNum, reqVO.getAreaNum())
                .eqIfPresent(AreaRuleDataDO::getAreaData, reqVO.getAreaData())
                .betweenIfPresent(AreaRuleDataDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AreaRuleDataDO::getId));
    }

}
