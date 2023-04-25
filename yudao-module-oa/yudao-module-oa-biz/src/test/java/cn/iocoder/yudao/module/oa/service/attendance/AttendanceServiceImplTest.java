package cn.iocoder.yudao.module.oa.service.attendance;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.module.oa.dal.mysql.attendance.AttendanceMapper;
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
* {@link AttendanceServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(AttendanceServiceImpl.class)
public class AttendanceServiceImplTest extends BaseDbUnitTest {

    @Resource
    private AttendanceServiceImpl attendanceService;

    @Resource
    private AttendanceMapper attendanceMapper;

    @Test
    public void testCreateAttendance_success() {
        // 准备参数
        AttendanceCreateReqVO reqVO = randomPojo(AttendanceCreateReqVO.class);

        // 调用
        Long attendanceId = attendanceService.createAttendance(reqVO);
        // 断言
        assertNotNull(attendanceId);
        // 校验记录的属性是否正确
        AttendanceDO attendance = attendanceMapper.selectById(attendanceId);
        assertPojoEquals(reqVO, attendance);
    }

    @Test
    public void testUpdateAttendance_success() {
        // mock 数据
        AttendanceDO dbAttendance = randomPojo(AttendanceDO.class);
        attendanceMapper.insert(dbAttendance);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AttendanceUpdateReqVO reqVO = randomPojo(AttendanceUpdateReqVO.class, o -> {
            o.setId(dbAttendance.getId()); // 设置更新的 ID
        });

        // 调用
        attendanceService.updateAttendance(reqVO);
        // 校验是否更新正确
        AttendanceDO attendance = attendanceMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, attendance);
    }

    @Test
    public void testUpdateAttendance_notExists() {
        // 准备参数
        AttendanceUpdateReqVO reqVO = randomPojo(AttendanceUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> attendanceService.updateAttendance(reqVO), ATTENDANCE_NOT_EXISTS);
    }

    @Test
    public void testDeleteAttendance_success() {
        // mock 数据
        AttendanceDO dbAttendance = randomPojo(AttendanceDO.class);
        attendanceMapper.insert(dbAttendance);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbAttendance.getId();

        // 调用
        attendanceService.deleteAttendance(id);
       // 校验数据不存在了
       assertNull(attendanceMapper.selectById(id));
    }

    @Test
    public void testDeleteAttendance_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> attendanceService.deleteAttendance(id), ATTENDANCE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetAttendancePage() {
       // mock 数据
       AttendanceDO dbAttendance = randomPojo(AttendanceDO.class, o -> { // 等会查询到
           o.setAttendanceType(null);
           o.setCreateBy(null);
       });
       attendanceMapper.insert(dbAttendance);
       // 测试 attendanceType 不匹配
       attendanceMapper.insert(cloneIgnoreId(dbAttendance, o -> o.setAttendanceType(null)));
       // 测试 createBy 不匹配
       attendanceMapper.insert(cloneIgnoreId(dbAttendance, o -> o.setCreateBy(null)));
       // 准备参数
       AttendancePageReqVO reqVO = new AttendancePageReqVO();
       reqVO.setAttendanceType(null);
       reqVO.setCreateBy(null);

       // 调用
       PageResult<AttendanceDO> pageResult = attendanceService.getAttendancePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbAttendance, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetAttendanceList() {
       // mock 数据
       AttendanceDO dbAttendance = randomPojo(AttendanceDO.class, o -> { // 等会查询到
           o.setAttendanceType(null);
           o.setCreateBy(null);
       });
       attendanceMapper.insert(dbAttendance);
       // 测试 attendanceType 不匹配
       attendanceMapper.insert(cloneIgnoreId(dbAttendance, o -> o.setAttendanceType(null)));
       // 测试 createBy 不匹配
       attendanceMapper.insert(cloneIgnoreId(dbAttendance, o -> o.setCreateBy(null)));
       // 准备参数
       AttendanceExportReqVO reqVO = new AttendanceExportReqVO();
       reqVO.setAttendanceType(null);
       reqVO.setCreateBy(null);

       // 调用
       List<AttendanceDO> list = attendanceService.getAttendanceList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbAttendance, list.get(0));
    }

}
