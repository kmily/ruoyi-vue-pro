package cn.iocoder.dashboard.modules.system.service.dept;

import cn.iocoder.dashboard.BaseDbUnitTest;
import cn.iocoder.dashboard.common.enums.CommonStatusEnum;
import cn.iocoder.dashboard.modules.system.controller.dept.vo.dept.SysDeptCreateReqVO;
import cn.iocoder.dashboard.modules.system.controller.dept.vo.dept.SysDeptListReqVO;
import cn.iocoder.dashboard.modules.system.controller.dept.vo.dept.SysDeptUpdateReqVO;
import cn.iocoder.dashboard.modules.system.dal.dataobject.dept.SysDeptDO;
import cn.iocoder.dashboard.modules.system.dal.mysql.dept.SysDeptMapper;
import cn.iocoder.dashboard.modules.system.enums.dept.DeptIdEnum;
import cn.iocoder.dashboard.modules.system.mq.producer.dept.SysDeptProducer;
import cn.iocoder.dashboard.modules.system.service.dept.impl.SysDeptServiceImpl;
import cn.iocoder.dashboard.util.collection.ArrayUtils;
import cn.iocoder.dashboard.util.object.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static cn.iocoder.dashboard.modules.system.enums.SysErrorCodeConstants.*;
import static cn.iocoder.dashboard.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.dashboard.util.AssertUtils.assertServiceException;
import static cn.iocoder.dashboard.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * {@link SysDeptServiceImpl} 的单元测试类
 *
 * @author niudehua
 * @datetime 2021/3/7 22:39
 */
@Import(SysDeptServiceImpl.class)
class SysDeptServiceTest extends BaseDbUnitTest {
    @Resource
    private SysDeptServiceImpl deptService;

    @Resource
    private SysDeptMapper deptMapper;
    @MockBean
    private SysDeptProducer deptProducer;

    @Test
    void testListDepts() {
        // mock 数据
        SysDeptDO dept = randomPojo(SysDeptDO.class, o -> { // 等会查询到
            o.setName("开发部");
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        deptMapper.insert(dept);
        // 测试 name 不匹配
        deptMapper.insert(ObjectUtils.clone(dept, o -> o.setName("发")));
        // 测试 status 不匹配
        deptMapper.insert(ObjectUtils.clone(dept, o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus())));
        // 准备参数
        SysDeptListReqVO reqVO = new SysDeptListReqVO();
        reqVO.setName("开");
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        // 调用
        List<SysDeptDO> sysDeptDOS = deptService.listDepts(reqVO);
        // 断言
        assertEquals(1, sysDeptDOS.size());
        assertPojoEquals(dept, sysDeptDOS.get(0));
    }

    @Test
    void testCreateDept_success() {
        // 准备参数
        SysDeptCreateReqVO reqVO = randomPojo(SysDeptCreateReqVO.class,
            o -> {
                o.setParentId(DeptIdEnum.ROOT.getId());
                o.setStatus(randomCommonStatus());
            }
        );
        // 调用
        Long deptId = deptService.createDept(reqVO);
        // 断言
        assertNotNull(deptId);
        // 校验记录的属性是否正确
        SysDeptDO deptDO = deptMapper.selectById(deptId);
        assertPojoEquals(reqVO, deptDO);
        // 校验调用
        verify(deptProducer, times(1)).sendDeptRefreshMessage();
    }

    @Test
    void testCreateDept_parentNotExits() {
        SysDeptCreateReqVO reqVO = randomPojo(SysDeptCreateReqVO.class,
            o -> o.setStatus(randomCommonStatus()));
        // 调用,并断言异常
        assertServiceException(() -> deptService.createDept(reqVO), DEPT_PARENT_NOT_EXITS);
    }

    @Test
    void testUpdateDept_success() {
        // mock 数据
        SysDeptDO dbDeptDO = randomPojo(SysDeptDO.class, o -> o.setStatus(randomCommonStatus()));
        deptMapper.insert(dbDeptDO);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SysDeptUpdateReqVO reqVO = randomPojo(SysDeptUpdateReqVO.class, o -> {
            // 设置更新的 ID
            o.setParentId(DeptIdEnum.ROOT.getId());
            o.setId(dbDeptDO.getId());
            o.setStatus(randomCommonStatus());
        });
        // 调用
        deptService.updateDept(reqVO);
        // 校验是否更新正确
        SysDeptDO deptDO = deptMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, deptDO);
    }

    @Test
    void testUpdateDept_notFound() {
        // 准备参数
        SysDeptUpdateReqVO reqVO = randomPojo(SysDeptUpdateReqVO.class);
        // 调用, 并断言异常
        assertServiceException(() -> deptService.updateDept(reqVO), DEPT_NOT_FOUND);
    }

    @Test
    void testUpdateDept_nameDuplicate() {
        // mock 数据
        SysDeptDO deptDO = randomDeptDO();
        // 设置根节点部门
        deptDO.setParentId(DeptIdEnum.ROOT.getId());
        deptMapper.insert(deptDO);
        // mock 数据 稍后模拟重复它的 name
        SysDeptDO nameDeptDO = randomDeptDO();
        // 设置根节点部门
        nameDeptDO.setParentId(DeptIdEnum.ROOT.getId());
        deptMapper.insert(nameDeptDO);
        // 准备参数
        SysDeptUpdateReqVO reqVO = randomPojo(SysDeptUpdateReqVO.class,
            o -> {
                // 设置根节点部门
                o.setParentId(DeptIdEnum.ROOT.getId());
                // 设置更新的 ID
                o.setId(deptDO.getId());
                // 模拟 name 重复
                o.setName(nameDeptDO.getName());
            });
        // 调用, 并断言异常
        assertServiceException(() -> deptService.updateDept(reqVO), DEPT_NAME_DUPLICATE);

    }

    @Test
    void testDeleteDept_success() {
        // mock 数据
        SysDeptDO dbDeptDO = randomPojo(SysDeptDO.class, o -> o.setStatus(randomCommonStatus()));
        deptMapper.insert(dbDeptDO);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDeptDO.getId();
        // 调用
        deptService.deleteDept(id);
        // 校验数据不存在了
        assertNull(deptMapper.selectById(id));
    }

    @Test
    void testDeleteDept_exitsChildren() {
        // mock 数据
        SysDeptDO dbDeptDO = randomPojo(SysDeptDO.class, o -> o.setStatus(randomCommonStatus()));
        deptMapper.insert(dbDeptDO);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SysDeptDO childrenDeptDO = randomPojo(SysDeptDO.class, o -> {
            o.setParentId(dbDeptDO.getId());
            o.setStatus(randomCommonStatus());
        });
        // 插入子部门
        deptMapper.insert(childrenDeptDO);
        // 调用, 并断言异常
        assertServiceException(() -> deptService.deleteDept(dbDeptDO.getId()), DEPT_EXITS_CHILDREN);
    }

    @Test
    void testDeleteDept_notExists() {
        // 准备参数
        Long id = randomLongId();
        // 调用, 并断言异常
        assertServiceException(() -> deptService.deleteDept(id), DEPT_NOT_FOUND);
    }

    @SafeVarargs
    private static SysDeptDO randomDeptDO(Consumer<SysDeptDO>... consumers) {
        Consumer<SysDeptDO> consumer = (o) -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // 保证 status 的范围
        };
        return randomPojo(SysDeptDO.class, ArrayUtils.append(consumer, consumers));
    }
}