package cn.iocoder.yudao.framework.export.service.config;

import cn.iocoder.yudao.framework.export.service.TemplateExportPdfService;
import cn.iocoder.yudao.framework.export.service.TemplateExportWordService;
import cn.iocoder.yudao.framework.export.service.XhtmlExportPdfService;
import cn.iocoder.yudao.framework.export.service.XhtmlExportWordService;
import cn.iocoder.yudao.framework.export.service.freemarker.FreemarkerXhtmlExportPdfService;
import cn.iocoder.yudao.framework.export.service.freemarker.FreemarkerXhtmlExportWordService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnClass({freemarker.template.Configuration.class, XhtmlExportPdfService.class, XhtmlExportWordService.class})
@AutoConfigureAfter({ FreeMarkerAutoConfiguration.class})
public class YudaoExportAutoConfiguration {

    private final ApplicationContext applicationContext;

    private final FreeMarkerProperties properties;

    public YudaoExportAutoConfiguration(ApplicationContext applicationContext, FreeMarkerProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnClass({PdfRendererBuilder.class})
    @ConditionalOnMissingBean({TemplateExportPdfService.class})
    @ConditionalOnBean(freemarker.template.Configuration.class)
    TemplateExportPdfService exportPdfService(freemarker.template.Configuration configuration) throws IOException {
        final String[] templateLoaderPath = properties.getTemplateLoaderPath();
        //get the first template loader path
        String path = templateLoaderPath[0];
        String baseDocumentUri = applicationContext.getResource(path).getURI().toString();
        return new FreemarkerXhtmlExportPdfService(configuration,baseDocumentUri);
    }


    @Bean
    @ConditionalOnClass({WordprocessingMLPackage.class, XHTMLImporterImpl.class})
    @ConditionalOnMissingBean({TemplateExportWordService.class})
    @ConditionalOnBean(freemarker.template.Configuration.class)
    TemplateExportWordService exportWordService(freemarker.template.Configuration configuration) throws IOException {
        final String[] templateLoaderPath = properties.getTemplateLoaderPath();
        //get the first template loader path
        String path = templateLoaderPath[0];
        String baseDocumentUri = applicationContext.getResource(path).getURI().toString();
        return  new FreemarkerXhtmlExportWordService(configuration,baseDocumentUri);
    }
}
