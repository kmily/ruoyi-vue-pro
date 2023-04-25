package cn.iocoder.yudao.module.system.service.signreq;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;
import cn.iocoder.yudao.module.system.dal.mysql.signreq.SignReqMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link SignReqServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(SignReqServiceImpl.class)
public class SignReqServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SignReqServiceImpl signReqService;

    @Resource
    private SignReqMapper signReqMapper;

    @Test
    public void testCreateSignReq_success() {
        // 准备参数
        SignReqCreateReqVO reqVO = randomPojo(SignReqCreateReqVO.class);

        // 调用
        Long signReqId = signReqService.createSignReq(reqVO);
        // 断言
        assertNotNull(signReqId);
        // 校验记录的属性是否正确
        SignReqDO signReq = signReqMapper.selectById(signReqId);
        assertPojoEquals(reqVO, signReq);
    }

    @Test
    public void testUpdateSignReq_success() {
        // mock 数据
        SignReqDO dbSignReq = randomPojo(SignReqDO.class);
        signReqMapper.insert(dbSignReq);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SignReqUpdateReqVO reqVO = randomPojo(SignReqUpdateReqVO.class, o -> {
            o.setId(dbSignReq.getId()); // 设置更新的 ID
        });

        // 调用
        signReqService.updateSignReq(reqVO);
        // 校验是否更新正确
        SignReqDO signReq = signReqMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, signReq);
    }

    @Test
    public void testUpdateSignReq_notExists() {
        // 准备参数
        SignReqUpdateReqVO reqVO = randomPojo(SignReqUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> signReqService.updateSignReq(reqVO), SIGN_REQ_NOT_EXISTS);
    }

    @Test
    public void testDeleteSignReq_success() {
        // mock 数据
        SignReqDO dbSignReq = randomPojo(SignReqDO.class);
        signReqMapper.insert(dbSignReq);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSignReq.getId();

        // 调用
        signReqService.deleteSignReq(id);
       // 校验数据不存在了
       assertNull(signReqMapper.selectById(id));
    }

    @Test
    public void testDeleteSignReq_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> signReqService.deleteSignReq(id), SIGN_REQ_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSignReqPage() {
       // mock 数据
       SignReqDO dbSignReq = randomPojo(SignReqDO.class, o -> { // 等会查询到
           o.setOpenId(null);
           o.setEmail(null);
           o.setPhonenumber(null);
           o.setUserName(null);
       });
       signReqMapper.insert(dbSignReq);
       // 测试 openId 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setOpenId(null)));
       // 测试 email 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setEmail(null)));
       // 测试 phonenumber 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setPhonenumber(null)));
       // 测试 userName 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setUserName(null)));
       // 准备参数
       SignReqPageReqVO reqVO = new SignReqPageReqVO();
       reqVO.setOpenId(null);
       reqVO.setEmail(null);
       reqVO.setPhonenumber(null);
       reqVO.setUserName(null);

       // 调用
       PageResult<SignReqDO> pageResult = signReqService.getSignReqPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSignReq, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSignReqList() {
       // mock 数据
       SignReqDO dbSignReq = randomPojo(SignReqDO.class, o -> { // 等会查询到
           o.setOpenId(null);
           o.setEmail(null);
           o.setPhonenumber(null);
           o.setUserName(null);
       });
       signReqMapper.insert(dbSignReq);
       // 测试 openId 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setOpenId(null)));
       // 测试 email 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setEmail(null)));
       // 测试 phonenumber 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setPhonenumber(null)));
       // 测试 userName 不匹配
       signReqMapper.insert(cloneIgnoreId(dbSignReq, o -> o.setUserName(null)));
       // 准备参数
       SignReqExportReqVO reqVO = new SignReqExportReqVO();
       reqVO.setOpenId(null);
       reqVO.setEmail(null);
       reqVO.setPhonenumber(null);
       reqVO.setUserName(null);

       // 调用
       List<SignReqDO> list = signReqService.getSignReqList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbSignReq, list.get(0));
    }

}
