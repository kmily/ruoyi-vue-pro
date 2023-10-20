package cn.iocoder.yudao.module.biz.controller.admin.calc;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.biz.controller.admin.calc.vo.*;
import cn.iocoder.yudao.module.biz.convert.calc.CalcInterestRateDataConvert;
import cn.iocoder.yudao.module.biz.dal.dataobject.calc.CalcInterestRateDataDO;
import cn.iocoder.yudao.module.biz.service.calc.CalcInterestRateDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 利率数据")
@RestController
@RequestMapping("/biz/calc-interest-rate-data")
@Validated
public class CalcInterestRateDataController {

    @Resource
    private CalcInterestRateDataService calcInterestRateDataService;

    @GetMapping("/desc/file")
    @Operation(summary = "测试阶段说明下载")
    @PermitAll
    public void getDescFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置文件路径
        org.springframework.core.io.Resource resource = new ClassPathResource("测试阶段说明.docx");
        if (resource.exists()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/msword");
            String fileName = resource.getURI().toString().substring(resource.getURI().toString().lastIndexOf("/") + 1);
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
            //放弃使用直接获取文件的方式, 改成使用流的方式
            BufferedInputStream bis = new BufferedInputStream(resource.getInputStream());
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            if (bis != null) {
                bis.close();
            }
        }

    }

    @PostMapping("/excel")
    @PermitAll
    public void downloadExcelFile(HttpServletResponse response, @RequestBody List<SectionIndexVO> sectionList) throws IOException {

        List<CalcInterestRateLxDataExcelVO> datas = parseExcel2(sectionList);
        ExcelUtils.write(response, "数据.xlsx", "数据", CalcInterestRateLxDataExcelVO.class, datas);

    }


    public String parseExcel(List<SectionIndexVO> sectionList) throws Exception {
        //读取excel
        org.springframework.core.io.Resource resource = new ClassPathResource("利息数据.xls");
        String filePath = resource.getFile().getParentFile().getPath() + "/" + System.currentTimeMillis() + ".xls";
        File resultfile = FileUtil.copy(resource.getFile().getAbsolutePath(), filePath, true);
        //ping
        {
            //创建Excel工作薄
            Workbook workbookIn = Workbook.getWorkbook(resultfile);
            WritableWorkbook workbook = Workbook.createWorkbook(resultfile, workbookIn);
            if (null == workbook) {
                throw new Exception("创建Excel工作薄为空！");
            }
            WritableSheet sheet = workbook.getSheet(0);
            //遍历Excel中所有的sheet
            for (int i = 0; i < sectionList.size(); i++) {
                SectionIndexVO index = sectionList.get(i);
                //获取第一列
                String column1 = index.getStartDate() + "至" + index.getEndDate();
                Label label1 = new Label(0, i + 1, column1);
                sheet.addCell(label1);
                //获取第二列
                String column2 = index.getDays();
                Label label2 = new Label(1, i + 1, column2);
                sheet.addCell(label2);
                //获取第三列
                String column3 = index.getSuiteRate();
                Label label3 = new Label(2, i + 1, column3);
                sheet.addCell(label3);
                //获取第四列
                String column4 = index.getSectionAmount().toString();
                Label label4 = new Label(3, i + 1, column4);
                sheet.addCell(label4);
            }
            {
                //写入数据
                workbook.write();
                //一定要关闭，不然不会写入文件内。
                workbook.close();
            }
        }
        return filePath;
    }

    public List<CalcInterestRateLxDataExcelVO> parseExcel2(List<SectionIndexVO> sectionList) {

        List<CalcInterestRateLxDataExcelVO> list = new ArrayList();
        //遍历Excel中所有的sheet
        BigDecimal totalDays = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < sectionList.size(); i++) {
            SectionIndexVO index = sectionList.get(i);
            totalDays = totalDays.add(new BigDecimal(index.getDays()));
            totalAmount = totalAmount.add(index.getSectionAmount());
            CalcInterestRateLxDataExcelVO cir = new CalcInterestRateLxDataExcelVO();
            //获取第一列
            String column1 = index.getStartDate() + "至" + index.getEndDate();
            cir.setTimeFrame(column1);
            //获取第二列
            cir.setDays(index.getDays());
            //获取第三列
            cir.setBenchmarkRate(index.getSuiteRate());
            //获取第四列
            cir.setAmount(index.getSectionAmount());
            list.add(cir);
        }
        CalcInterestRateLxDataExcelVO cir = new CalcInterestRateLxDataExcelVO();
        cir.setDays(totalDays.toString());
        cir.setAmount(totalAmount);
        list.add(cir);
        return list;
    }

    @PostMapping("/exec/lx")
    @Operation(summary = "计算利息")
    @PermitAll
    public CommonResult<CalcInterestRateExecResVO> execCalcInterestLxData(@Valid @RequestBody CalcInterestRateExecLxParamVO execVO) {
        return success(calcInterestRateDataService.execCalcInterestLxData(execVO));
    }

    @PostMapping("/exec/fx")
    @Operation(summary = "计算罚息")
    @PermitAll
    public CommonResult<CalcInterestRateExecResVO> execCalcInterestFxData(@Valid @RequestBody CalcInterestRateExecFxParamVO execVO) {
        return success(calcInterestRateDataService.execCalcInterestFxData(execVO));
    }

    @PostMapping("/exec/zxf")
    @Operation(summary = "执行费")
    @PermitAll
    public CommonResult<CalcInterestRateExecZxfResVO> execCalcZxfData(@Valid @RequestBody CalcInterestRateExecZxfParamVO execVO) {
        return success(calcInterestRateDataService.execCalcFeeData(execVO));
    }


    @PostMapping("/create")
    @Operation(summary = "创建利率数据")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:create')")
    public CommonResult<Integer> createCalcInterestRateData(@Valid @RequestBody CalcInterestRateDataCreateReqVO createReqVO) {
        return success(calcInterestRateDataService.createCalcInterestRateData(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新利率数据")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:update')")
    public CommonResult<Boolean> updateCalcInterestRateData(@Valid @RequestBody CalcInterestRateDataUpdateReqVO updateReqVO) {
        calcInterestRateDataService.updateCalcInterestRateData(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除利率数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:delete')")
    public CommonResult<Boolean> deleteCalcInterestRateData(@RequestParam("id") Integer id) {
        calcInterestRateDataService.deleteCalcInterestRateData(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得利率数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<CalcInterestRateDataRespVO> getCalcInterestRateData(@RequestParam("id") Integer id) {
        CalcInterestRateDataDO calcInterestRateData = calcInterestRateDataService.getCalcInterestRateData(id);
        return success(CalcInterestRateDataConvert.INSTANCE.convert(calcInterestRateData));
    }

    @GetMapping("/list")
    @Operation(summary = "获得利率数据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<List<CalcInterestRateDataRespVO>> getCalcInterestRateDataList(@RequestParam("ids") Collection<Integer> ids) {
        List<CalcInterestRateDataDO> list = calcInterestRateDataService.getCalcInterestRateDataList(ids);
        return success(CalcInterestRateDataConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得利率数据分页")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:query')")
    public CommonResult<PageResult<CalcInterestRateDataRespVO>> getCalcInterestRateDataPage(@Valid CalcInterestRateDataPageReqVO pageVO) {
        PageResult<CalcInterestRateDataDO> pageResult = calcInterestRateDataService.getCalcInterestRateDataPage(pageVO);
        return success(CalcInterestRateDataConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出利率数据 Excel")
    @PreAuthorize("@ss.hasPermission('biz:calc-interest-rate-data:export')")
    public void exportCalcInterestRateDataExcel(@Valid CalcInterestRateDataExportReqVO exportReqVO,
                                                HttpServletResponse response) throws IOException {
        List<CalcInterestRateDataDO> list = calcInterestRateDataService.getCalcInterestRateDataList(exportReqVO);
        // 导出 Excel
        List<CalcInterestRateDataExcelVO> datas = CalcInterestRateDataConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "利率数据.xls", "数据", CalcInterestRateDataExcelVO.class, datas);
    }

}
