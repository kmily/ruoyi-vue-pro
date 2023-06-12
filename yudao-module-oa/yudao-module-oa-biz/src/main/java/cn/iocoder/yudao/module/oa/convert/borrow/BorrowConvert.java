package cn.iocoder.yudao.module.oa.convert.borrow;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;

/**
 * 借支申请 Convert
 *
 * @author 东海
 */
@Mapper
public interface BorrowConvert {

    BorrowConvert INSTANCE = Mappers.getMapper(BorrowConvert.class);

    BorrowDO convert(BorrowCreateReqVO bean);

    BorrowDO convert(BorrowUpdateReqVO bean);

    BorrowRespVO convert(BorrowDO bean);

    List<BorrowRespVO> convertList(List<BorrowDO> list);

    PageResult<BorrowRespVO> convertPage(PageResult<BorrowDO> page);

    List<BorrowExcelVO> convertList02(List<BorrowDO> list);

}
