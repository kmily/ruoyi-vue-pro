package cn.iocoder.yudao.module.member.controller.app.serverperson;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

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

import cn.iocoder.yudao.module.member.controller.app.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonConvert;
import cn.iocoder.yudao.module.member.service.serverperson.ServerPersonService;

@Tag(name = "用户 APP - 被服务人")
@RestController
@RequestMapping("/member/server-person")
@Validated
public class AppServerPersonController {

    @Resource
    private ServerPersonService serverPersonService;

    @PostMapping("/create")
    @Operation(summary = "创建被服务人")
    @PreAuthenticated
    public CommonResult<Long> createServerPerson(@Valid @RequestBody AppServerPersonCreateReqVO createReqVO) {
        return success(serverPersonService.createServerPerson(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新被服务人")
    @PreAuthenticated
    public CommonResult<Boolean> updateServerPerson(@Valid @RequestBody AppServerPersonUpdateReqVO updateReqVO) {
        serverPersonService.updateServerPerson(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除被服务人")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteServerPerson(@RequestParam("id") Long id) {
        serverPersonService.deleteServerPerson(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得被服务人")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppServerPersonRespVO> getServerPerson(@RequestParam("id") Long id) {
        ServerPersonDO serverPerson = serverPersonService.getServerPerson(id);
        AppServerPersonRespVO personRespVO = ServerPersonConvert.INSTANCE.convert(serverPerson);
        if(StrUtil.isNotBlank(personRespVO.getIdCard())){
            personRespVO.setAge(IdcardUtil.getAgeByIdCard(personRespVO.getIdCard()));
        }
        return success(personRespVO);
    }

    @GetMapping("/list")
    @Operation(summary = "获得被服务人列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<AppServerPersonRespVO>> getServerPersonList(@RequestParam("ids") Collection<Long> ids) {
        List<ServerPersonDO> list = serverPersonService.getServerPersonList(ids);
        return success(ServerPersonConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得被服务人分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppServerPersonRespVO>> getServerPersonPage(@Valid AppServerPersonPageReqVO pageVO) {
        PageResult<ServerPersonDO> pageResult = serverPersonService.getServerPersonPage(pageVO);

        PageResult<AppServerPersonRespVO> voPageResult = ServerPersonConvert.INSTANCE.convertPage(pageResult);
        List<AppServerPersonRespVO> list = voPageResult.getList();
        if(CollUtil.isNotEmpty(list)){
            list.forEach(item -> {
                if(StrUtil.isNotBlank(item.getIdCard())){
                    item.setAge(IdcardUtil.getAgeByIdCard(item.getIdCard()));
                }
            });
        }
        return success(voPageResult);
    }

}
