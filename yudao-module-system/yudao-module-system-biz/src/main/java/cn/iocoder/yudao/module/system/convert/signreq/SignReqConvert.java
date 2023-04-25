package cn.iocoder.yudao.module.system.convert.signreq;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;

/**
 * 注册申请 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface SignReqConvert {

    SignReqConvert INSTANCE = Mappers.getMapper(SignReqConvert.class);

    SignReqDO convert(SignReqCreateReqVO bean);

    SignReqDO convert(SignReqUpdateReqVO bean);

    SignReqRespVO convert(SignReqDO bean);

    List<SignReqRespVO> convertList(List<SignReqDO> list);

    PageResult<SignReqRespVO> convertPage(PageResult<SignReqDO> page);

    List<SignReqExcelVO> convertList02(List<SignReqDO> list);

}
