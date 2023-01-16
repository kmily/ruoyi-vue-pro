package cn.iocoder.yudao.module.scan.controller.app.report;//package cn.iocoder.yudao.module.scan.controller.app.report;

import cn.hutool.core.io.FileUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.module.scan.controller.app.report.vo.*;
import cn.iocoder.yudao.module.scan.convert.report.ReportConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.module.scan.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "扫描APP - 报告")
@RestController
@RequestMapping("/scan/report")
@Validated
public class AppReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/create")
    @ApiOperation("创建报告")
    @Idempotent(timeout = 5, message = "重复请求，请稍后重试")
    public CommonResult<Long> createReport(@Valid @RequestBody AppReportCreateReqVO createReqVO) {
        return success(reportService.createReport(ReportConvert.INSTANCE.convert(createReqVO)));
    }


    @GetMapping("/get")
    @ApiOperation("获得报告")
    @ApiImplicitParam(name = "id", value = "编号", required = true,  dataTypeClass = Long.class)
    public CommonResult<AppReportRespVO> getReport(@RequestParam("id") Long id) {
        ReportDO report = reportService.getReport(id);
        return success(ReportConvert.INSTANCE.convert02(report));
    }


    @GetMapping("/page")
    @ApiOperation("获得报告分页")
    public CommonResult<PageResult<AppReportRespVO>> getReportPage(@Valid AppReportPageReqVO pageVO) {
        PageResult<ReportDO> pageResult = reportService.getReportPage(ReportConvert.INSTANCE.convert(pageVO));
        return success(ReportConvert.INSTANCE.convertPage02(pageResult));
    }



    @GetMapping("/exportpdf")
    @ApiOperation("exportPdf")
    @PermitAll
    @ApiImplicitParam(name = "id", value = "报告id", required = true, dataTypeClass = Integer.class)
    public void exportPdf(HttpServletResponse response,@RequestParam("id") Long id) throws Exception {
        String filePath = reportService.generateReportFile(id);

        // 读取内容
        byte[] content = FileUtil.readBytes(filePath);
        if (content == null) {
//            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return ;
        }
        ServletUtils.writeAttachment(response, "1.pdf", content);
//        return success("");
    }
}
