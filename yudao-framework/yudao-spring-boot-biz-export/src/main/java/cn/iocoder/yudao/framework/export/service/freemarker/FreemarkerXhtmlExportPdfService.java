package cn.iocoder.yudao.framework.export.service.freemarker;

import cn.iocoder.yudao.framework.export.service.XhtmlExportPdfService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;


public class FreemarkerXhtmlExportPdfService extends XhtmlExportPdfService {

    private final Configuration configuration;


    public FreemarkerXhtmlExportPdfService(Configuration configuration) {
        this.configuration = configuration;
    }


    public FreemarkerXhtmlExportPdfService(Configuration configuration,
                                           String baseDocumentUri) {
        super(baseDocumentUri);
        this.configuration = configuration;
    }


    @Override
    protected String getXHtmlContent(Object dataModel, String tplName) throws IOException, TemplateException {

        Template template = configuration.getTemplate(tplName, StandardCharsets.UTF_8.name());

        StringWriter stringWriter = new StringWriter(256);

        template.process(dataModel, stringWriter);

        return stringWriter.toString();
    }
}
