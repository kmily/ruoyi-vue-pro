package cn.iocoder.yudao.module.oa.service.projectimpl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;
import cn.iocoder.yudao.module.oa.dal.mysql.projectimpl.ProjectImplMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ProjectImplServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ProjectImplServiceImpl.class)
public class ProjectImplServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProjectImplServiceImpl projectImplService;

    @Resource
    private ProjectImplMapper projectImplMapper;

    @Test
    public void testCreateProjectImpl_success() {
        // 准备参数
        ProjectImplCreateReqVO reqVO = randomPojo(ProjectImplCreateReqVO.class);

        // 调用
        Long projectImplId = projectImplService.createProjectImpl(reqVO);
        // 断言
        assertNotNull(projectImplId);
        // 校验记录的属性是否正确
        ProjectImplDO projectImpl = projectImplMapper.selectById(projectImplId);
        assertPojoEquals(reqVO, projectImpl);
    }

    @Test
    public void testUpdateProjectImpl_success() {
        // mock 数据
        ProjectImplDO dbProjectImpl = randomPojo(ProjectImplDO.class);
        projectImplMapper.insert(dbProjectImpl);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProjectImplUpdateReqVO reqVO = randomPojo(ProjectImplUpdateReqVO.class, o -> {
            o.setId(dbProjectImpl.getId()); // 设置更新的 ID
        });

        // 调用
        projectImplService.updateProjectImpl(reqVO);
        // 校验是否更新正确
        ProjectImplDO projectImpl = projectImplMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, projectImpl);
    }

    @Test
    public void testUpdateProjectImpl_notExists() {
        // 准备参数
        ProjectImplUpdateReqVO reqVO = randomPojo(ProjectImplUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> projectImplService.updateProjectImpl(reqVO), PROJECT_IMPL_NOT_EXISTS);
    }

    @Test
    public void testDeleteProjectImpl_success() {
        // mock 数据
        ProjectImplDO dbProjectImpl = randomPojo(ProjectImplDO.class);
        projectImplMapper.insert(dbProjectImpl);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProjectImpl.getId();

        // 调用
        projectImplService.deleteProjectImpl(id);
       // 校验数据不存在了
       assertNull(projectImplMapper.selectById(id));
    }

    @Test
    public void testDeleteProjectImpl_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> projectImplService.deleteProjectImpl(id), PROJECT_IMPL_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectImplPage() {
       // mock 数据
       ProjectImplDO dbProjectImpl = randomPojo(ProjectImplDO.class, o -> { // 等会查询到
           o.setContractId(null);
       });
       projectImplMapper.insert(dbProjectImpl);
       // 测试 contractId 不匹配
       projectImplMapper.insert(cloneIgnoreId(dbProjectImpl, o -> o.setContractId(null)));
       // 准备参数
       ProjectImplPageReqVO reqVO = new ProjectImplPageReqVO();
       reqVO.setContractId(null);

       // 调用
       PageResult<ProjectImplDO> pageResult = projectImplService.getProjectImplPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProjectImpl, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectImplList() {
       // mock 数据
       ProjectImplDO dbProjectImpl = randomPojo(ProjectImplDO.class, o -> { // 等会查询到
           o.setContractId(null);
       });
       projectImplMapper.insert(dbProjectImpl);
       // 测试 contractId 不匹配
       projectImplMapper.insert(cloneIgnoreId(dbProjectImpl, o -> o.setContractId(null)));
       // 准备参数
       ProjectImplExportReqVO reqVO = new ProjectImplExportReqVO();
       reqVO.setContractId(null);

       // 调用
       List<ProjectImplDO> list = projectImplService.getProjectImplList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProjectImpl, list.get(0));
    }

}
