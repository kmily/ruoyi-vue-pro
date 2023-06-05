package com.cw.module.trade.dal.mysql.position;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cw.module.trade.controller.admin.position.vo.PositionExportReqVO;
import com.cw.module.trade.controller.admin.position.vo.PositionPageReqVO;
import com.cw.module.trade.dal.dataobject.position.PositionDO;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 账户持仓信息 Mapper
 *
 * @author chengjiale
 */
@Mapper
public interface PositionMapper extends BaseMapperX<PositionDO> {

    default PageResult<PositionDO> selectPage(PositionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PositionDO>()
                .betweenIfPresent(PositionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PositionDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(PositionDO::getSymbol, reqVO.getSymbol())
                .eqIfPresent(PositionDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(PositionDO::getThirdData, reqVO.getThirdData())
                .orderByDesc(PositionDO::getId));
    }

    default List<PositionDO> selectList(PositionExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PositionDO>()
                .betweenIfPresent(PositionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PositionDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(PositionDO::getSymbol, reqVO.getSymbol())
                .eqIfPresent(PositionDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(PositionDO::getThirdData, reqVO.getThirdData())
                .orderByDesc(PositionDO::getId));
    }

    @Select("SELECT * FROM `account_position` where account_id = #{accountId} "
            + "and symbol = #{symbol} order by id desc limit 1;")
    PositionDO selectLastestPosition(@Param("accountId") Long accountId, @Param("symbol") String symbol);
    
    
    @Select("SELECT distinct symbol FROM `account_position` where "
            + "account_id = #{accountId} and quantity != 0   ;")
    List<String> selectAccountSymbolPosition(@Param("accountId") Long accountId);
    
    @Select("SELECT distinct symbol FROM `account_position` where "
            + "account_id = #{accountId} and quantity != 0  and create_time &gt; #{start}, and create_time &lt; #{end}")
    List<String> selectTodayAccountSymbolPosition(@Param("accountId") Long accountId, Date start, Date end);

}
