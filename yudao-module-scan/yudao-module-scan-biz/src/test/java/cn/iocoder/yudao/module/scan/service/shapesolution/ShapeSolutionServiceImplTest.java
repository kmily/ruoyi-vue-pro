package cn.iocoder.yudao.module.scan.service.shapesolution;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import cn.iocoder.yudao.module.scan.dal.mysql.shapesolution.ShapeSolutionMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ShapeSolutionServiceImpl} 的单元测试类
*
* @author lyz
*/
@Import(ShapeSolutionServiceImpl.class)
public class ShapeSolutionServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ShapeSolutionServiceImpl shapeSolutionService;

    @Resource
    private ShapeSolutionMapper shapeSolutionMapper;

    @Test
    public void testCreateShapeSolution_success() {
        // 准备参数
        ShapeSolutionCreateReqVO reqVO = randomPojo(ShapeSolutionCreateReqVO.class);

        // 调用
        Long shapeSolutionId = shapeSolutionService.createShapeSolution(reqVO);
        // 断言
        assertNotNull(shapeSolutionId);
        // 校验记录的属性是否正确
        ShapeSolutionDO shapeSolution = shapeSolutionMapper.selectById(shapeSolutionId);
        assertPojoEquals(reqVO, shapeSolution);
    }

    @Test
    public void testUpdateShapeSolution_success() {
        // mock 数据
        ShapeSolutionDO dbShapeSolution = randomPojo(ShapeSolutionDO.class);
        shapeSolutionMapper.insert(dbShapeSolution);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ShapeSolutionUpdateReqVO reqVO = randomPojo(ShapeSolutionUpdateReqVO.class, o -> {
            o.setId(dbShapeSolution.getId()); // 设置更新的 ID
        });

        // 调用
        shapeSolutionService.updateShapeSolution(reqVO);
        // 校验是否更新正确
        ShapeSolutionDO shapeSolution = shapeSolutionMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, shapeSolution);
    }

    @Test
    public void testUpdateShapeSolution_notExists() {
        // 准备参数
        ShapeSolutionUpdateReqVO reqVO = randomPojo(ShapeSolutionUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> shapeSolutionService.updateShapeSolution(reqVO), SHAPE_SOLUTION_NOT_EXISTS);
    }

    @Test
    public void testDeleteShapeSolution_success() {
        // mock 数据
        ShapeSolutionDO dbShapeSolution = randomPojo(ShapeSolutionDO.class);
        shapeSolutionMapper.insert(dbShapeSolution);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbShapeSolution.getId();

        // 调用
        shapeSolutionService.deleteShapeSolution(id);
       // 校验数据不存在了
       assertNull(shapeSolutionMapper.selectById(id));
    }

    @Test
    public void testDeleteShapeSolution_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> shapeSolutionService.deleteShapeSolution(id), SHAPE_SOLUTION_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShapeSolutionPage() {
       // mock 数据
       ShapeSolutionDO dbShapeSolution = randomPojo(ShapeSolutionDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setTitle(null);
           o.setType(null);
           o.setContent(null);
           o.setShapeShapeId(null);
           o.setSort(null);
       });
       shapeSolutionMapper.insert(dbShapeSolution);
       // 测试 createTime 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setCreateTime(null)));
       // 测试 title 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setTitle(null)));
       // 测试 type 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setType(null)));
       // 测试 content 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setContent(null)));
       // 测试 shapeShapeId 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setShapeShapeId(null)));
       // 测试 sort 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setSort(null)));
       // 准备参数
       ShapeSolutionPageReqVO reqVO = new ShapeSolutionPageReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setTitle(null);
       reqVO.setType(null);
       reqVO.setContent(null);
       reqVO.setShapeShapeId(null);
       reqVO.setSort(null);

       // 调用
       PageResult<ShapeSolutionDO> pageResult = shapeSolutionService.getShapeSolutionPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbShapeSolution, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShapeSolutionList() {
       // mock 数据
       ShapeSolutionDO dbShapeSolution = randomPojo(ShapeSolutionDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setTitle(null);
           o.setType(null);
           o.setContent(null);
           o.setShapeShapeId(null);
           o.setSort(null);
       });
       shapeSolutionMapper.insert(dbShapeSolution);
       // 测试 createTime 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setCreateTime(null)));
       // 测试 title 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setTitle(null)));
       // 测试 type 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setType(null)));
       // 测试 content 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setContent(null)));
       // 测试 shapeShapeId 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setShapeShapeId(null)));
       // 测试 sort 不匹配
       shapeSolutionMapper.insert(cloneIgnoreId(dbShapeSolution, o -> o.setSort(null)));
       // 准备参数
       ShapeSolutionExportReqVO reqVO = new ShapeSolutionExportReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setTitle(null);
       reqVO.setType(null);
       reqVO.setContent(null);
       reqVO.setShapeShapeId(null);
       reqVO.setSort(null);

       // 调用
       List<ShapeSolutionDO> list = shapeSolutionService.getShapeSolutionList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbShapeSolution, list.get(0));
    }

}
