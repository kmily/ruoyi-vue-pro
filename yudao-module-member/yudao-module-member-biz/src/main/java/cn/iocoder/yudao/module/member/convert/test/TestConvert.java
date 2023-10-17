package cn.iocoder.yudao.module.member.convert.test;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.test.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.test.TestDO;

/**
 * 会员标签 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface TestConvert {

    TestConvert INSTANCE = Mappers.getMapper(TestConvert.class);

    TestDO convert(TestCreateReqVO bean);

    TestDO convert(TestUpdateReqVO bean);

    TestRespVO convert(TestDO bean);

    List<TestRespVO> convertList(List<TestDO> list);

    PageResult<TestRespVO> convertPage(PageResult<TestDO> page);

    List<TestExcelVO> convertList02(List<TestDO> list);

}
