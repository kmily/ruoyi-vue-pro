package cn.iocoder.yudao.framework.export.service;

import java.io.OutputStream;

public interface TemplateExportPdfService {

    void  exportPdf(OutputStream outputStream, Object dataModel, String tplName);


}
