package cn.iocoder.yudao.module.db.service.material;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.db.controller.admin.material.vo.*;
import cn.iocoder.yudao.module.db.dal.dataobject.material.MaterialDO;
import cn.iocoder.yudao.module.db.dal.mysql.material.MaterialMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.db.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link MaterialServiceImpl} 的单元测试类
 *
 * @author Arlen
 */
@Import(MaterialServiceImpl.class)
public class MaterialServiceImplTest extends BaseDbUnitTest {

    @Resource
    private MaterialServiceImpl materialService;

    @Resource
    private MaterialMapper materialMapper;

    @Test
    public void testCreateMaterial_success() {
        // 准备参数
        MaterialSaveReqVO createReqVO = randomPojo(MaterialSaveReqVO.class).setId(null);

        // 调用
        String materialId = materialService.createMaterial(createReqVO);
        // 断言
        assertNotNull(materialId);
        // 校验记录的属性是否正确
        MaterialDO material = materialMapper.selectById(materialId);
        assertPojoEquals(createReqVO, material, "id");
    }

    @Test
    public void testUpdateMaterial_success() {
        // mock 数据
        MaterialDO dbMaterial = randomPojo(MaterialDO.class);
        materialMapper.insert(dbMaterial);// @Sql: 先插入出一条存在的数据
        // 准备参数
        MaterialSaveReqVO updateReqVO = randomPojo(MaterialSaveReqVO.class, o -> {
            o.setId(dbMaterial.getId()); // 设置更新的 ID
        });

        // 调用
        materialService.updateMaterial(updateReqVO);
        // 校验是否更新正确
        MaterialDO material = materialMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, material);
    }

    @Test
    public void testUpdateMaterial_notExists() {
        // 准备参数
        MaterialSaveReqVO updateReqVO = randomPojo(MaterialSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> materialService.updateMaterial(updateReqVO), MATERIAL_NOT_EXISTS);
    }

    @Test
    public void testDeleteMaterial_success() {
        // mock 数据
        MaterialDO dbMaterial = randomPojo(MaterialDO.class);
        materialMapper.insert(dbMaterial);// @Sql: 先插入出一条存在的数据
        // 准备参数
        String id = dbMaterial.getId();

        // 调用
        materialService.deleteMaterial(id);
       // 校验数据不存在了
       assertNull(materialMapper.selectById(id));
    }

    @Test
    public void testDeleteMaterial_notExists() {
        // 准备参数
        String id = randomStringId();

        // 调用, 并断言异常
        assertServiceException(() -> materialService.deleteMaterial(id), MATERIAL_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetMaterialPage() {
       // mock 数据
       MaterialDO dbMaterial = randomPojo(MaterialDO.class, o -> { // 等会查询到
           o.setOrgId(null);
           o.setErpMtrlId(null);
           o.setModel(null);
           o.setStoreId(null);
           o.setStorelocId(null);
           o.setQtySkMin(null);
           o.setQtySkMax(null);
           o.setQtySkWarn(null);
           o.setRem(null);
           o.setStatus(null);
           o.setCheckTime(null);
           o.setUnitId(null);
           o.setChecker(null);
           o.setCreateTime(null);
           o.setCode(null);
           o.setName(null);
           o.setType(null);
           o.setAbc(null);
       });
       materialMapper.insert(dbMaterial);
       // 测试 orgId 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setOrgId(null)));
       // 测试 erpMtrlId 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setErpMtrlId(null)));
       // 测试 model 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setModel(null)));
       // 测试 storeId 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setStoreId(null)));
       // 测试 storelocId 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setStorelocId(null)));
       // 测试 qtySkMin 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setQtySkMin(null)));
       // 测试 qtySkMax 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setQtySkMax(null)));
       // 测试 qtySkWarn 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setQtySkWarn(null)));
       // 测试 rem 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setRem(null)));
       // 测试 status 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setStatus(null)));
       // 测试 checkTime 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setCheckTime(null)));
       // 测试 unitId 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setUnitId(null)));
       // 测试 checker 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setChecker(null)));
       // 测试 createTime 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setCreateTime(null)));
       // 测试 code 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setCode(null)));
       // 测试 name 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setName(null)));
       // 测试 type 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setType(null)));
       // 测试 abc 不匹配
       materialMapper.insert(cloneIgnoreId(dbMaterial, o -> o.setAbc(null)));
       // 准备参数
       MaterialPageReqVO reqVO = new MaterialPageReqVO();
       reqVO.setOrgId(null);
       reqVO.setErpMtrlId(null);
       reqVO.setModel(null);
       reqVO.setStoreId(null);
       reqVO.setStorelocId(null);
       reqVO.setQtySkMin(null);
       reqVO.setQtySkMax(null);
       reqVO.setQtySkWarn(null);
       reqVO.setRem(null);
       reqVO.setStatus(null);
       reqVO.setCheckTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setUnitId(null);
       reqVO.setChecker(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCode(null);
       reqVO.setName(null);
       reqVO.setType(null);
       reqVO.setAbc(null);

       // 调用
       PageResult<MaterialDO> pageResult = materialService.getMaterialPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbMaterial, pageResult.getList().get(0));
    }

}