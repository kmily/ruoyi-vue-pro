package cn.iocoder.yudao.module.oa.service.borrow;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowPageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowUpdateReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import cn.iocoder.yudao.module.oa.dal.mysql.borrow.BorrowMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.BORROW_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link BorrowServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(BorrowServiceImpl.class)
public class BorrowServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BorrowServiceImpl borrowService;

    @Resource
    private BorrowMapper borrowMapper;

    @Test
    public void testCreateBorrow_success() {
        // 准备参数
        BorrowCreateReqVO reqVO = randomPojo(BorrowCreateReqVO.class);

        // 调用
        Long borrowId = borrowService.createBorrow(reqVO);
        // 断言
        assertNotNull(borrowId);
        // 校验记录的属性是否正确
        BorrowDO borrow = borrowMapper.selectById(borrowId);
        assertPojoEquals(reqVO, borrow);
    }

    @Test
    public void testUpdateBorrow_success() {
        // mock 数据
        BorrowDO dbBorrow = randomPojo(BorrowDO.class);
        borrowMapper.insert(dbBorrow);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BorrowUpdateReqVO reqVO = randomPojo(BorrowUpdateReqVO.class, o -> {
            o.setId(dbBorrow.getId()); // 设置更新的 ID
        });

        // 调用
        borrowService.updateBorrow(reqVO);
        // 校验是否更新正确
        BorrowDO borrow = borrowMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, borrow);
    }

    @Test
    public void testUpdateBorrow_notExists() {
        // 准备参数
        BorrowUpdateReqVO reqVO = randomPojo(BorrowUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> borrowService.updateBorrow(reqVO), BORROW_NOT_EXISTS);
    }

    @Test
    public void testDeleteBorrow_success() {
        // mock 数据
        BorrowDO dbBorrow = randomPojo(BorrowDO.class);
        borrowMapper.insert(dbBorrow);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBorrow.getId();

        // 调用
        borrowService.deleteBorrow(id);
       // 校验数据不存在了
       assertNull(borrowMapper.selectById(id));
    }

    @Test
    public void testDeleteBorrow_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> borrowService.deleteBorrow(id), BORROW_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBorrowPage() {
       // mock 数据
       BorrowDO dbBorrow = randomPojo(BorrowDO.class, o -> { // 等会查询到
           o.setBorrowReason(null);
           o.setBorrowFee(null);
           o.setRepaymentFee(null);
           o.setRemark(null);
           o.setCreator(null);
           o.setCreateTime(null);
           o.setUpdater(null);
       });
       borrowMapper.insert(dbBorrow);
       // 测试 borrowReason 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setBorrowReason(null)));
       // 测试 borrowFee 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setBorrowFee(null)));
       // 测试 repaymentFee 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setRepaymentFee(null)));
       // 测试 remark 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setRemark(null)));
       // 测试 creator 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setCreator(null)));
       // 测试 createTime 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setCreateTime(null)));
       // 测试 updater 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setUpdater(null)));
       // 准备参数
       BorrowPageReqVO reqVO = new BorrowPageReqVO();
       reqVO.setBorrowReason(null);
       reqVO.setBorrowFee(null);
       reqVO.setRepaymentFee(null);
       reqVO.setRemark(null);
       reqVO.setCreator(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setUpdater(null);

       // 调用
       PageResult<BorrowDO> pageResult = borrowService.getBorrowPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBorrow, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBorrowList() {
       // mock 数据
       BorrowDO dbBorrow = randomPojo(BorrowDO.class, o -> { // 等会查询到
           o.setBorrowReason(null);
           o.setBorrowFee(null);
           o.setRepaymentFee(null);
           o.setRemark(null);
           o.setCreator(null);
           o.setCreateTime(null);
           o.setUpdater(null);
       });
       borrowMapper.insert(dbBorrow);
       // 测试 borrowReason 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setBorrowReason(null)));
       // 测试 borrowFee 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setBorrowFee(null)));
       // 测试 repaymentFee 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setRepaymentFee(null)));
       // 测试 remark 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setRemark(null)));
       // 测试 creator 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setCreator(null)));
       // 测试 createTime 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setCreateTime(null)));
       // 测试 updater 不匹配
       borrowMapper.insert(cloneIgnoreId(dbBorrow, o -> o.setUpdater(null)));
       // 准备参数
       BorrowExportReqVO reqVO = new BorrowExportReqVO();
       reqVO.setBorrowReason(null);
       reqVO.setBorrowFee(null);
       reqVO.setRepaymentFee(null);
       reqVO.setRemark(null);
       reqVO.setCreator(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setUpdater(null);

       // 调用
       List<BorrowDO> list = borrowService.getBorrowList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbBorrow, list.get(0));
    }

}
