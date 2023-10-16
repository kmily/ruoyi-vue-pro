package cn.iocoder.yudao.module.member.service.test;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.test.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.test.TestDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.test.TestConvert;
import cn.iocoder.yudao.module.member.dal.mysql.test.TestMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 会员标签 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TestServiceImpl implements TestService {

    @Resource
    private TestMapper testMapper;

    @Override
    public Long createTest(TestCreateReqVO createReqVO) {
        // 插入
        TestDO test = TestConvert.INSTANCE.convert(createReqVO);
        testMapper.insert(test);
        // 返回
        return test.getId();
    }

    @Override
    public void updateTest(TestUpdateReqVO updateReqVO) {
        // 校验存在
        validateTestExists(updateReqVO.getId());
        // 更新
        TestDO updateObj = TestConvert.INSTANCE.convert(updateReqVO);
        testMapper.updateById(updateObj);
    }

    @Override
    public void deleteTest(Long id) {
        // 校验存在
        validateTestExists(id);
        // 删除
        testMapper.deleteById(id);
    }

    private void validateTestExists(Long id) {
        if (testMapper.selectById(id) == null) {
           // throw exception(TEST_NOT_EXISTS);
        }
    }

    @Override
    public TestDO getTest(Long id) {
        return testMapper.selectById(id);
    }

    @Override
    public List<TestDO> getTestList(Collection<Long> ids) {
        return testMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<TestDO> getTestPage(TestPageReqVO pageReqVO) {
        return testMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TestDO> getTestList(TestExportReqVO exportReqVO) {
        return testMapper.selectList(exportReqVO);
    }

}
