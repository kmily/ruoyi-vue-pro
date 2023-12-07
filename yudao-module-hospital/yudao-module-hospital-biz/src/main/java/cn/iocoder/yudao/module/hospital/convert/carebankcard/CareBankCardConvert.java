package cn.iocoder.yudao.module.hospital.convert.carebankcard;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo.CareBankCardAppCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;

/**
 * 提现银行卡 Convert
 *
 * @author 管理人
 */
@Mapper
public interface CareBankCardConvert {

    CareBankCardConvert INSTANCE = Mappers.getMapper(CareBankCardConvert.class);

    CareBankCardDO convert(CareBankCardCreateReqVO bean);

    CareBankCardDO convert(CareBankCardUpdateReqVO bean);

    CareBankCardRespVO convert(CareBankCardDO bean);

    List<CareBankCardRespVO> convertList(List<CareBankCardDO> list);

    PageResult<CareBankCardRespVO> convertPage(PageResult<CareBankCardDO> page);

    List<CareBankCardExcelVO> convertList02(List<CareBankCardDO> list);

    CareBankCardDO convert(CareBankCardAppCreateReqVO  bean);
}
