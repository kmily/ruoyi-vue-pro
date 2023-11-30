package cn.iocoder.yudao.module.infra.controller.admin.demo.demo04;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datatranslation.core.annotations.DataTranslation;
import cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo.Demo04StudentPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo.Demo04StudentRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04StudentDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 学生")
@RestController
@RequestMapping("/infra/demo04-student")
@Validated
public class Demo04StudentController {

    @Resource
    private Demo04StudentService demo04StudentService;

    @PostMapping("/create")
    @Operation(summary = "创建学生")
    public CommonResult<Long> createDemo03Student() {
        return success(demo04StudentService.createDemo04Student());
    }

    @GetMapping("/list")
    @Operation(summary = "获得学生信息列表")
    @DataTranslation
    public CommonResult<List<Demo04StudentRespVO>> getDemo04StudentList() {
        List<Demo04StudentDO> demo04StudentList = demo04StudentService.getDemo04StudentList();
        return success(BeanUtils.toBean(demo04StudentList, Demo04StudentRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得学生")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @DataTranslation
    public CommonResult<Demo04StudentRespVO> getDemo03Student(@RequestParam("id") Long id) {
        Demo04StudentDO demo04Student = demo04StudentService.getDemo04StudentById(id);
        return success(BeanUtils.toBean(demo04Student, Demo04StudentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学生分页")
    @DataTranslation
    public CommonResult<PageResult<Demo04StudentRespVO>> getDemo03StudentPage(@Valid Demo04StudentPageReqVO pageReqVO) {
        PageResult<Demo04StudentDO> pageResult = demo04StudentService.getDemo04StudentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, Demo04StudentRespVO.class));
    }

}