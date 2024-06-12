package cn.iocoder.yudao.module.therapy.dal.mysql.report;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.iocoder.yudao.module.therapy.dal.dataobject.report.StatDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据统计 Mapper
 *
 * @author CBI系统管理员
 */
@Mapper
public interface StatMapper extends BaseMapperX<StatDO> {

//    default PageResult<StatDO> selectPage(StatPageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<StatDO>()
//                .eqIfPresent(StatDO::getStatType, reqVO.getStatType())
//                .eqIfPresent(StatDO::getStatData, reqVO.getStatData())
//                .eqIfPresent(StatDO::getRemark, reqVO.getRemark())
//                .eqIfPresent(StatDO::getUserId, reqVO.getUserId())
//                .betweenIfPresent(StatDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(StatDO::getId));
//    }

}