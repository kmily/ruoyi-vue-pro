package cn.iocoder.yudao.module.member.convert.visitpage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.visitpage.VisitPageDO;

/**
 * 页面访问数据 Convert
 *
 * @author 和尘同光
 */
@Mapper
public interface VisitPageConvert {

    VisitPageConvert INSTANCE = Mappers.getMapper(VisitPageConvert.class);

    VisitPageDO convert(VisitPageCreateReqVO bean);

    VisitPageDO convert(VisitPageUpdateReqVO bean);

    VisitPageRespVO convert(VisitPageDO bean);

    List<VisitPageRespVO> convertList(List<VisitPageDO> list);

    PageResult<VisitPageRespVO> convertPage(PageResult<VisitPageDO> page);

    List<VisitPageExcelVO> convertList02(List<VisitPageDO> list);

}
