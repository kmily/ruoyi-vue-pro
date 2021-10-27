package cn.iocoder.yudao.framework.export.service;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.export.service.exception.ExportException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public abstract  class XhtmlExportPdfService implements TemplateExportPdfService {

    private String baseDocumentUri;


    public XhtmlExportPdfService(){

    }

    public XhtmlExportPdfService(String baseDocumentUri) {
        this.baseDocumentUri = baseDocumentUri;
    }

    @Override
    public void  exportPdf(OutputStream outputStream, Object dataModel, String tplName){
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            String html = getXHtmlContent(dataModel, tplName);
            builder.withHtmlContent(html, baseDocumentUri);
            builder.toStream(outputStream);
            builder.run();
        }catch (IOException | TemplateException ex){
            log.error(" 导出 pdf 报错", ex);
            throw new ExportException("export pdf error", ex);
        }
    }

    public void setBaseDocumentUri(String baseDocumentUri) {
        this.baseDocumentUri = baseDocumentUri;
    }

    protected abstract String getXHtmlContent(Object dataModel, String tplName) throws IOException, TemplateException;
}
