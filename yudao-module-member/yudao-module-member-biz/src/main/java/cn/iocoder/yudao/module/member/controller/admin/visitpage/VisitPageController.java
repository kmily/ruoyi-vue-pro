package cn.iocoder.yudao.module.member.controller.admin.visitpage;

import cn.iocoder.yudao.framework.common.util.date.DateUtils;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.visitpage.VisitPageDO;
import cn.iocoder.yudao.module.member.convert.visitpage.VisitPageConvert;
import cn.iocoder.yudao.module.member.service.visitpage.VisitPageService;

@Tag(name = "管理后台 - 页面访问数据")
@RestController
@RequestMapping("/member/visit-page")
@Validated
public class VisitPageController {

    @Resource
    private VisitPageService visitPageService;

    @PostMapping("/create")
    @Operation(summary = "创建页面访问数据")
    @PreAuthorize("@ss.hasPermission('member:visit-page:create')")
    public CommonResult<Long> createVisitPage(@Valid @RequestBody VisitPageCreateReqVO createReqVO) {
        return success(visitPageService.createVisitPage(createReqVO));
    }


    @GetMapping("/list")
    @Operation(summary = "获得页面访问数据列表")
    @PreAuthorize("@ss.hasPermission('member:visit-page:query')")
    public CommonResult<List<VisitPageResVO>> getVisitPageList(VisitPagePageReqVO pageReqVO) {
        List<VisitPageDO> list = visitPageService.getVisitPageList(new VisitPageExportReqVO().setStartTime(pageReqVO.getStartTime()));
        List<VisitPageResVO> resVOList = new ArrayList<>();

        Map<String, List<VisitPageDO>> listMap = list.stream().collect(Collectors.groupingBy(VisitPageDO::getPageName));
        int total = list.size();
        listMap.forEach((key, doList) -> {
            int size = doList.size();
            double average = doList.stream().mapToLong(VisitPageDO::getUseTime).summaryStatistics().getAverage();
            String avg = DateUtils.secToTime(Double.valueOf(Double.toString(average)).longValue());
            resVOList.add(new VisitPageResVO()
                    .setAverage(avg).setPageName(key).setCount(size).setPercentage(NumberUtils.getPercent(size, total)));

        });
        return success(resVOList);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出页面访问数据 Excel")
    @PreAuthorize("@ss.hasPermission('member:visit-page:export')")
    @OperateLog(type = EXPORT)
    public void exportVisitPageExcel(@Valid VisitPagePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        CommonResult<List<VisitPageResVO>> result = this.getVisitPageList(pageReqVO);
        // 导出 Excel
        List<VisitPageResVO> datas = result.getData();
        ExcelUtils.write(response, "页面访问数据.xls", "数据", VisitPageResVO.class, datas);
    }

}
