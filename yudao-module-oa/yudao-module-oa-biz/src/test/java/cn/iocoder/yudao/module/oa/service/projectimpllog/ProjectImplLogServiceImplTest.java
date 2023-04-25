package cn.iocoder.yudao.module.oa.service.projectimpllog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import cn.iocoder.yudao.module.oa.dal.mysql.projectimpllog.ProjectImplLogMapper;
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
           o.setCreateBy(null);
       });
       projectImplLogMapper.insert(dbProjectImplLog);
       // 测试 contractId 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setContractId(null)));
       // 测试 implStatus 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setImplStatus(null)));
       // 测试 createBy 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setCreateBy(null)));
       // 准备参数
       ProjectImplLogPageReqVO reqVO = new ProjectImplLogPageReqVO();
       reqVO.setContractId(null);
       reqVO.setImplStatus(null);
       reqVO.setCreateBy(null);

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
           o.setCreateBy(null);
       });
       projectImplLogMapper.insert(dbProjectImplLog);
       // 测试 contractId 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setContractId(null)));
       // 测试 implStatus 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setImplStatus(null)));
       // 测试 createBy 不匹配
       projectImplLogMapper.insert(cloneIgnoreId(dbProjectImplLog, o -> o.setCreateBy(null)));
       // 准备参数
       ProjectImplLogExportReqVO reqVO = new ProjectImplLogExportReqVO();
       reqVO.setContractId(null);
       reqVO.setImplStatus(null);
       reqVO.setCreateBy(null);

       // 调用
       List<ProjectImplLogDO> list = projectImplLogService.getProjectImplLogList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbProjectImplLog, list.get(0));
    }

}
