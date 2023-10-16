package cn.iocoder.yudao.module.member.controller.admin.test;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.test.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.test.TestDO;
import cn.iocoder.yudao.module.member.convert.test.TestConvert;
import cn.iocoder.yudao.module.member.service.test.TestService;

@Tag(name = "管理后台 - 会员标签")
@RestController
@RequestMapping("/member/test")
@Validated
public class TestController {

    @Resource
    private TestService testService;

    @PostMapping("/create")
    @Operation(summary = "创建会员标签")
    @PreAuthorize("@ss.hasPermission('member:test:create')")
    public CommonResult<Long> createTest(@Valid @RequestBody TestCreateReqVO createReqVO) {
        return success(testService.createTest(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员标签")
    @PreAuthorize("@ss.hasPermission('member:test:update')")
    public CommonResult<Boolean> updateTest(@Valid @RequestBody TestUpdateReqVO updateReqVO) {
        testService.updateTest(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员标签")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:test:delete')")
    public CommonResult<Boolean> deleteTest(@RequestParam("id") Long id) {
        testService.deleteTest(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员标签")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:test:query')")
    public CommonResult<TestRespVO> getTest(@RequestParam("id") Long id) {
        TestDO test = testService.getTest(id);
        return success(TestConvert.INSTANCE.convert(test));
    }

    @GetMapping("/list")
    @Operation(summary = "获得会员标签列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:test:query')")
    public CommonResult<List<TestRespVO>> getTestList(@RequestParam("ids") Collection<Long> ids) {
        List<TestDO> list = testService.getTestList(ids);
        return success(TestConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员标签分页")
    @PreAuthorize("@ss.hasPermission('member:test:query')")
    public CommonResult<PageResult<TestRespVO>> getTestPage(@Valid TestPageReqVO pageVO) {
        PageResult<TestDO> pageResult = testService.getTestPage(pageVO);
        return success(TestConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员标签 Excel")
    @PreAuthorize("@ss.hasPermission('member:test:export')")
    @OperateLog(type = EXPORT)
    public void exportTestExcel(@Valid TestExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<TestDO> list = testService.getTestList(exportReqVO);
        // 导出 Excel
        List<TestExcelVO> datas = TestConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "会员标签.xls", "数据", TestExcelVO.class, datas);
    }

}
