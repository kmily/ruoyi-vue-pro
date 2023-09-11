package cn.iocoder.yudao.module.member.controller.admin.bootup;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.bootup.BootUpDO;
import cn.iocoder.yudao.module.member.convert.bootup.BootUpConvert;
import cn.iocoder.yudao.module.member.service.bootup.BootUpService;

@Tag(name = "管理后台 - 用户启动数据")
@RestController
@RequestMapping("/member/boot-up")
@Validated
public class BootUpController {

    @Resource
    private BootUpService bootUpService;

    @PostMapping("/create")
    @Operation(summary = "创建用户启动数据")
    @PreAuthorize("@ss.hasPermission('member:boot-up:create')")
    public CommonResult<Long> createBootUp(@Valid @RequestBody BootUpCreateReqVO createReqVO) {
        return success(bootUpService.createBootUp(createReqVO));
    }


    @GetMapping("/get")
    @Operation(summary = "获得用户启动数据")
    @PreAuthorize("@ss.hasPermission('member:boot-up:query')")
    public CommonResult<BootUpResVO> getBootUp(BootUpReqVO bootUpReqVO) {
        List<BootUpDO> bootUps = bootUpService.getBootUpList(bootUpReqVO);
        BootUpResVO bootUpResVO = new BootUpResVO();

        bootUpResVO.setTotal(bootUps.size());

        Map<Long, List<BootUpDO>> collect = bootUps.stream().collect(Collectors.groupingBy(BootUpDO::getUserId));
        AtomicInteger once = new AtomicInteger(0);
        collect.forEach((key, list) -> {
            if(list.size() == 1){
                once.incrementAndGet();
            }
        });
        bootUpResVO.setOnce(once.get());
        bootUpResVO.setPercentage(NumberUtils.getPercent(once.get(), bootUps.size()));

        Map<String, List<BootUpDO>> dateGroupMap = bootUps.stream().collect(Collectors.groupingBy((item) -> LocalDateTimeUtil.format(item.getCreateTime(), DateUtils.FORMAT_YEAR_MONTH_DAY)));


        List<BootUpListResVO> listResVOS = new ArrayList<>();

        dateGroupMap.forEach((key, list) ->{
            String percent = NumberUtils.getPercent(list.size(), bootUps.size());
            listResVOS.add(new BootUpListResVO().setDate(key).setTimes(list.size()).setPercentage(percent));
        });

        Collections.sort(listResVOS);

        bootUpResVO.setResVOList(listResVOS);
        return success(bootUpResVO);
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出用户启动数据 Excel")
    @PreAuthorize("@ss.hasPermission('member:boot-up:export')")
    @OperateLog(type = EXPORT)
    public void exportBootUpExcel(@Valid BootUpReqVO bootUpReqVO,
              HttpServletResponse response) throws IOException {
        CommonResult<BootUpResVO> bootUp = this.getBootUp(bootUpReqVO);

        List<BootUpListResVO> resVOList = bootUp.getData().getResVOList();

        // 导出 Excel
        List<BootUpExcelVO> datas = BootUpConvert.INSTANCE.convertList03(resVOList);
        ExcelUtils.write(response, "用户启动数据.xls", "数据", BootUpExcelVO.class, datas);
    }

}
