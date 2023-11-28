package cn.iocoder.yudao.module.infra.controller.admin.demo.demo04;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.easytrans.core.annotations.DataTranslation;
import cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo.Demo04StudentRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04StudentDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

}