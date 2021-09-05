package cn.iocoder.yudao.framework.export.service.freemarker;

import cn.iocoder.yudao.framework.export.service.XhtmlExportWordService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class FreemarkerXhtmlExportWordService extends XhtmlExportWordService {

    private final Configuration configuration;


    public FreemarkerXhtmlExportWordService(Configuration configuration, String baseDocumentUri) {
        super(baseDocumentUri);
        this.configuration = configuration;
    }

    public FreemarkerXhtmlExportWordService(Configuration configuration) {
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
