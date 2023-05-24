package cn.iocoder.yudao.module.oa.service.projectimpllog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogPageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogUpdateReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import cn.iocoder.yudao.module.oa.dal.mysql.projectimpllog.ProjectImplLogMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.PROJECT_IMPL_LOG_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link ProjectImplLogServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ProjectImplLogServiceImpl.class)
public class ProjectImplLogServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProjectImplLogServiceImpl projectImplLogService;

    @Resource
    private ProjectImplLogMapper projectImplLogMapper;

    @Test
    public void testCreateProjectImplLog_success() {
        // 准备参数
        ProjectImplLogCreateReqVO reqVO = randomPojo(ProjectImplLogCreateReqVO.class);

        // 调用
        Long projectImplLogId = projectImplLogService.createProjectImplLog(reqVO);
        // 断言
        assertNotNull(projectImplLogId);
        // 校验记录的属性是否正确
        ProjectImplLogDO projectImplLog = projectImplLogMapper.selectById(projectImplLogId);
        assertPojoEquals(reqVO, projectImplLog);
    }

    @Test
    public void testUpdateProjectImplLog_success() {
        // mock 数据
        ProjectImplLogDO dbProjectImplLog = randomPojo(ProjectImplLogDO.class);
        projectImplLogMapper.insert(dbProjectImplLog);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProjectImplLogUpdateReqVO reqVO = randomPojo(ProjectImplLogUpdateReqVO.class, o -> {
            o.setId(dbProjectImplLog.getId()); // 设置更新的 ID
        });

        // 调用
        projectImplLogService.updateProjectImplLog(reqVO);
        // 校验是否更新正确
        ProjectImplLogDO projectImplLog = projectImplLogMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, projectImplLog);
    }

    @Test
    public void testUpdateProjectImplLog_notExists() {
        // 准备参数
        ProjectImplLogUpdateReqVO reqVO = randomPojo(ProjectImplLogUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> projectImplLogService.updateProjectImplLog(reqVO), PROJECT_IMPL_LOG_NOT_EXISTS);
    }

    @Test
    public void testDeleteProjectImplLog_success() {
        // mock 数据
        ProjectImplLogDO dbProjectImplLog = randomPojo(ProjectImplLogDO.class);
        projectImplLogMapper.insert(dbProjectImplLog);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProjectImplLog.getId();

        // 调用
        projectImplLogService.deleteProjectImplLog(id);
       // 校验数据不存在了
       assertNull(projectImplLogMapper.selectById(id));
    }

    @Test
    public void testDeleteProjectImplLog_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> projectImplLogService.deleteProjectImplLog(id), PROJECT_IMPL_LOG_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectImplLogPage() {
       // mock 数据
       ProjectImplLogDO dbProjectImplLog = randomPojo(ProjectImplLogDO.class, o -> { // 等会查询到
           o.setContractId(null);
           o.setImplStatus(null);
           o.setCreator(null);
       });
       projectImplLogMapper.insert(dbProjectImplLog);
       // 测试 contractId 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setContractId(null)));
       // 测试 implStatus 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setImplStatus(null)));
       // 测试 creator 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setCreator(null)));
       // 准备参数
       ProjectImplLogPageReqVO reqVO = new ProjectImplLogPageReqVO();
       reqVO.setContractId(null);
       reqVO.setImplStatus(null);
       reqVO.setCreator(null);

       // 调用
       PageResult<ProjectImplLogDO> pageResult = projectImplLogService.getProjectImplLogPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProjectImplLog, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProjectImplLogList() {
       // mock 数据
       ProjectImplLogDO dbProjectImplLog = randomPojo(ProjectImplLogDO.class, o -> { // 等会查询到
           o.setContractId(null);
           o.setImplStatus(null);
           o.setCreator(null);
       });
       projectImplLogMapper.insert(dbProjectImplLog);
       // 测试 contractId 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setContractId(null)));
       // 测试 implStatus 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setImplStatus(null)));
       // 测试 creator 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setCreator(null)));
       // 准备参数
       ProjectImplLogExportReqVO reqVO = new ProjectImplLogExportReqVO();
       reqVO.setContractId(null);
       reqVO.setImplStatus(null);
       reqVO.setCreator(null);

       // 调用
       List<ProjectImplLogDO> list = projectImplLogService.getProjectImplLogList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProjectImplLog, list.get(0));
    }

}
