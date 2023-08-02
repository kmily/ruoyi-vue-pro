package cn.iocoder.yudao.module.promotion.convert.bargainactivity;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;

/**
 * 砍价 Convert
 *
 * @author WangBosheng
 */
@Mapper
public interface BargainActivityConvert {

    BargainActivityConvert INSTANCE = Mappers.getMapper(BargainActivityConvert.class);

    BargainActivityDO convert(BargainActivityCreateReqVO bean);

    BargainActivityDO convert(BargainActivityUpdateReqVO bean);

    BargainActivityRespVO convert(BargainActivityDO bean);

    List<BargainActivityRespVO> convertList(List<BargainActivityDO> list);

    PageResult<BargainActivityRespVO> convertPage(PageResult<BargainActivityDO> page);

    List<BargainActivityExcelVO> convertList02(List<BargainActivityDO> list);

}
