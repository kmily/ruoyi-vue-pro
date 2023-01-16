package cn.iocoder.yudao.module.scan.service.shape;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import cn.iocoder.yudao.module.scan.dal.mysql.shape.ShapeMapper;
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
* {@link ShapeServiceImpl} 的单元测试类
*
* @author lyz
*/
@Import(ShapeServiceImpl.class)
public class ShapeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ShapeServiceImpl shapeService;

    @Resource
    private ShapeMapper shapeMapper;

    @Test
    public void testCreateShape_success() {
        // 准备参数
        ShapeCreateReqVO reqVO = randomPojo(ShapeCreateReqVO.class);

        // 调用
        Long shapeId = shapeService.createShape(reqVO);
        // 断言
        assertNotNull(shapeId);
        // 校验记录的属性是否正确
        ShapeDO shape = shapeMapper.selectById(shapeId);
        assertPojoEquals(reqVO, shape);
    }

    @Test
    public void testUpdateShape_success() {
        // mock 数据
        ShapeDO dbShape = randomPojo(ShapeDO.class);
        shapeMapper.insert(dbShape);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ShapeUpdateReqVO reqVO = randomPojo(ShapeUpdateReqVO.class, o -> {
            o.setId(dbShape.getId()); // 设置更新的 ID
        });

        // 调用
        shapeService.updateShape(reqVO);
        // 校验是否更新正确
        ShapeDO shape = shapeMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, shape);
    }

    @Test
    public void testUpdateShape_notExists() {
        // 准备参数
        ShapeUpdateReqVO reqVO = randomPojo(ShapeUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> shapeService.updateShape(reqVO), SHAPE_NOT_EXISTS);
    }

    @Test
    public void testDeleteShape_success() {
        // mock 数据
        ShapeDO dbShape = randomPojo(ShapeDO.class);
        shapeMapper.insert(dbShape);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbShape.getId();

        // 调用
        shapeService.deleteShape(id);
       // 校验数据不存在了
       assertNull(shapeMapper.selectById(id));
    }

    @Test
    public void testDeleteShape_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> shapeService.deleteShape(id), SHAPE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShapePage() {
       // mock 数据
       ShapeDO dbShape = randomPojo(ShapeDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setShapeName(null);
           o.setShapeValue(null);
       });
       shapeMapper.insert(dbShape);
       // 测试 createTime 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setCreateTime(null)));
       // 测试 shapeName 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setShapeName(null)));
       // 测试 shapeValue 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setShapeValue(null)));
       // 准备参数
       ShapePageReqVO reqVO = new ShapePageReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setShapeName(null);
       reqVO.setShapeValue(null);

       // 调用
       PageResult<ShapeDO> pageResult = shapeService.getShapePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbShape, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShapeList() {
       // mock 数据
       ShapeDO dbShape = randomPojo(ShapeDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setShapeName(null);
           o.setShapeValue(null);
       });
       shapeMapper.insert(dbShape);
       // 测试 createTime 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setCreateTime(null)));
       // 测试 shapeName 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setShapeName(null)));
       // 测试 shapeValue 不匹配
       shapeMapper.insert(cloneIgnoreId(dbShape, o -> o.setShapeValue(null)));
       // 准备参数
       ShapeExportReqVO reqVO = new ShapeExportReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setShapeName(null);
       reqVO.setShapeValue(null);

       // 调用
       List<ShapeDO> list = shapeService.getShapeList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbShape, list.get(0));
    }

}
