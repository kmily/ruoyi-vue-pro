package cn.iocoder.yudao.module.steam.service.youyoudetails;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails.YouyouDetailsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link YouyouDetailsServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouDetailsServiceImpl.class)
public class YouyouDetailsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private YouyouDetailsServiceImpl youyouDetailsService;

    @Resource
    private YouyouDetailsMapper youyouDetailsMapper;

    @Test
    public void testCreateYouyouDetails_success() {
        // 准备参数
        YouyouDetailsSaveReqVO createReqVO = randomPojo(YouyouDetailsSaveReqVO.class).setId(null);

        // 调用
        Integer youyouDetailsId = youyouDetailsService.createYouyouDetails(createReqVO);
        // 断言
        assertNotNull(youyouDetailsId);
        // 校验记录的属性是否正确
        YouyouDetailsDO youyouDetails = youyouDetailsMapper.selectById(youyouDetailsId);
        assertPojoEquals(createReqVO, youyouDetails, "id");
    }

    @Test
    public void testUpdateYouyouDetails_success() {
        // mock 数据
        YouyouDetailsDO dbYouyouDetails = randomPojo(YouyouDetailsDO.class);
        youyouDetailsMapper.insert(dbYouyouDetails);// @Sql: 先插入出一条存在的数据
        // 准备参数
        YouyouDetailsSaveReqVO updateReqVO = randomPojo(YouyouDetailsSaveReqVO.class, o -> {
            o.setId(dbYouyouDetails.getId()); // 设置更新的 ID
        });

        // 调用
        youyouDetailsService.updateYouyouDetails(updateReqVO);
        // 校验是否更新正确
        YouyouDetailsDO youyouDetails = youyouDetailsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, youyouDetails);
    }

    @Test
    public void testUpdateYouyouDetails_notExists() {
        // 准备参数
        YouyouDetailsSaveReqVO updateReqVO = randomPojo(YouyouDetailsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> youyouDetailsService.updateYouyouDetails(updateReqVO), YOUYOU_DETAILS_NOT_EXISTS);
    }

    @Test
    public void testDeleteYouyouDetails_success() {
        // mock 数据
        YouyouDetailsDO dbYouyouDetails = randomPojo(YouyouDetailsDO.class);
        youyouDetailsMapper.insert(dbYouyouDetails);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbYouyouDetails.getId();

        // 调用
        youyouDetailsService.deleteYouyouDetails(id);
       // 校验数据不存在了
       assertNull(youyouDetailsMapper.selectById(id));
    }

    @Test
    public void testDeleteYouyouDetails_notExists() {
        // 准备参数
        Integer id = randomInteger();

        // 调用, 并断言异常
        assertServiceException(() -> youyouDetailsService.deleteYouyouDetails(id), YOUYOU_DETAILS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetYouyouDetailsPage() {
       // mock 数据
       YouyouDetailsDO dbYouyouDetails = randomPojo(YouyouDetailsDO.class, o -> { // 等会查询到
           o.setAppkey(null);
           o.setTimestamp(null);
           o.setSign(null);
           o.setDataType(null);
           o.setStartTime(null);
           o.setEndTime(null);
           o.setCreateTime(null);
           o.setApplyCode(null);
           o.setData(null);
       });
       youyouDetailsMapper.insert(dbYouyouDetails);
       // 测试 appkey 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setAppkey(null)));
       // 测试 timestamp 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setTimestamp(null)));
       // 测试 sign 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setSign(null)));
       // 测试 dataType 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setDataType(null)));
       // 测试 startTime 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setStartTime(null)));
       // 测试 endTime 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setEndTime(null)));
       // 测试 createTime 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setCreateTime(null)));
       // 测试 applyCode 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setApplyCode(null)));
       // 测试 data 不匹配
       youyouDetailsMapper.insert(cloneIgnoreId(dbYouyouDetails, o -> o.setData(null)));
       // 准备参数
       YouyouDetailsPageReqVO reqVO = new YouyouDetailsPageReqVO();
       reqVO.setAppkey(null);
       reqVO.setTimestamp(null);
       reqVO.setSign(null);
       reqVO.setDataType(null);
       reqVO.setStartTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setEndTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setApplyCode(null);
       reqVO.setData(null);

       // 调用
       PageResult<YouyouDetailsDO> pageResult = youyouDetailsService.getYouyouDetailsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouDetails, pageResult.getList().get(0));
    }

}