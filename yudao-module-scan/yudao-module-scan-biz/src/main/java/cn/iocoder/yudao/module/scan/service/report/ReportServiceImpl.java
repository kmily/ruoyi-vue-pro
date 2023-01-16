package cn.iocoder.yudao.module.scan.service.report;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.report.ReportConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.report.ReportMapper;
import org.springframework.web.client.RestTemplate;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 报告 Service 实现类
 *
 * @author lyz
 */
@Service
@Validated
public class ReportServiceImpl implements ReportService {

    @Value("${scan.upload-path}")
    private String uploadPath;
    @Value("${scan.excel-config-id}")
    private String excelConfigId;
    @Value("${scan.file-type}")
    private String fileType;
    @Value("${scan.url}")
    private String url;
    @Value("${scan.hip-width-hipline-ratio}")
    private String hipWidthHiplineRatio;
    @Value("${scan.waist-hip-ratio}")
    private String waistPipRatio;
    @Resource
    private ReportMapper reportMapper;

    @Override
    public Long createReport(ReportCreateReqVO createReqVO) {
        // 插入
        ReportDO report = ReportConvert.INSTANCE.convert(createReqVO);
        reportMapper.insert(report);
        // 返回
        return report.getId();
    }

    @Override
    public void updateReport(ReportUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateReportExists(updateReqVO.getId());
        // 更新
        ReportDO updateObj = ReportConvert.INSTANCE.convert(updateReqVO);
        reportMapper.updateById(updateObj);
    }

    @Override
    public void deleteReport(Long id) {
        // 校验存在
        this.validateReportExists(id);
        // 删除
        reportMapper.deleteById(id);
    }

    private void validateReportExists(Long id) {
        if (reportMapper.selectById(id) == null) {
            throw exception(REPORT_NOT_EXISTS);
        }
    }

    @Override
    public ReportDO getReport(Long id) {
        return reportMapper.selectById(id);
    }

    @Override
    public List<ReportDO> getReportList(Collection<Long> ids) {
        return reportMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ReportDO> getReportPage(ReportPageReqVO pageReqVO) {
        return reportMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ReportDO> getReportList(ReportExportReqVO exportReqVO) {
        return reportMapper.selectList(exportReqVO);
    }

    //输入参数 肩围 胸围 臀围 腰围 肩宽 臀宽（臀宽参数，暂时以臀围乘以比例值获得—比例值暂定为0.65）
    private String CalculateBodyShape(ReportCreateReqVO createReqVO){
        int hipline=createReqVO.getLeftHipline()+createReqVO.getRightHipline();
        int bust=createReqVO.getBust();
        int d=createReqVO.getHipline();
        int max;
        int min;
//        int temp=x>y?x:y;
//        max=temp>z?temp:z;

        return "";
    }

    @Override
    public String generateReportFile(Long id){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject result=new JSONObject();
        //创建url路径
        Map<String,Object> map = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("id",id);
        map.put("excelConfigId",excelConfigId);
        map.put("queryParam",object);
        HttpHeaders headers = new HttpHeaders();
        //如果有字典需要传token
        String filePath =uploadPath+ id+fileType;
        HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(map,headers);
        ResponseEntity<byte[]> bytes=restTemplate.exchange(url, HttpMethod.POST, httpEntity, byte[].class);
        FileUtil.writeBytes(bytes.getBody(), filePath);
//        try {
//            ResponseEntity<byte[]> bytes=restTemplate.exchange(url, HttpMethod.POST, httpEntity, byte[].class);
//            FileUtil.writeBytes(bytes.getBody(), filePath);
//            File f = new File(filePath);
//            if (!f.exists()) {
//                try {
//                    f.createNewFile();
//                } catch (Exception e) {
//                    f.delete();
//                    throw exception(REPORT_GENERTOR_FAILURE);
//                }
//            }
//            try (FileOutputStream out = new FileOutputStream(f);) {
//                out.write(bytes.getBody(), 0, bytes.getBody().length);
//                out.flush();
//            } catch (Exception e) {
//                throw exception(REPORT_GENERTOR_FAILURE);
//            }
//
//        } catch (Exception e) {
//            throw exception(REPORT_GENERTOR_FAILURE);
//        }
        return filePath;
    }
}
