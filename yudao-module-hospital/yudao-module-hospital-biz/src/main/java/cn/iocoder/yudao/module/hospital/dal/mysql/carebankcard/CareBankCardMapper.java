package cn.iocoder.yudao.module.hospital.dal.mysql.carebankcard;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;

/**
 * 提现银行卡 Mapper
 *
 * @author 管理人
 */
@Mapper
public interface CareBankCardMapper extends BaseMapperX<CareBankCardDO>,BaseMapper<CareBankCardDO> {

    default PageResult<CareBankCardDO> selectPage(CareBankCardPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CareBankCardDO>()
                .eqIfPresent(CareBankCardDO::getCareId, reqVO.getCareId())
                .likeIfPresent(CareBankCardDO::getName, reqVO.getName())
                .eqIfPresent(CareBankCardDO::getCardNo, reqVO.getCardNo())
                .eqIfPresent(CareBankCardDO::getBank, reqVO.getBank())
                .eqIfPresent(CareBankCardDO::getIdCard, reqVO.getIdCard())
                .eqIfPresent(CareBankCardDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CareBankCardDO::getDefaultStatus, reqVO.getDefaultStatus())
                .betweenIfPresent(CareBankCardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareBankCardDO::getId));
    }

    default List<CareBankCardDO> selectList(CareBankCardExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CareBankCardDO>()
                .eqIfPresent(CareBankCardDO::getCareId, reqVO.getCareId())
                .likeIfPresent(CareBankCardDO::getName, reqVO.getName())
                .eqIfPresent(CareBankCardDO::getCardNo, reqVO.getCardNo())
                .eqIfPresent(CareBankCardDO::getBank, reqVO.getBank())
                .eqIfPresent(CareBankCardDO::getIdCard, reqVO.getIdCard())
                .eqIfPresent(CareBankCardDO::getMobile, reqVO.getMobile())
                .eqIfPresent(CareBankCardDO::getDefaultStatus, reqVO.getDefaultStatus())
                .betweenIfPresent(CareBankCardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareBankCardDO::getId));
    }

    default List<CareBankCardDO> selectListByCareIdAndDefaulted(Long careId, Boolean defaulted) {
        return selectList(new LambdaQueryWrapperX<CareBankCardDO>().eq(CareBankCardDO::getCareId, careId)
                .eqIfPresent(CareBankCardDO::getDefaultStatus, defaulted));
    }

}
