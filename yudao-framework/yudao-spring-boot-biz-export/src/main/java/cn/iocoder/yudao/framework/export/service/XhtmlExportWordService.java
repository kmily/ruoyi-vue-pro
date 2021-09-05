package cn.iocoder.yudao.framework.export.service;

import cn.iocoder.yudao.framework.export.service.exception.ExportException;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public abstract class XhtmlExportWordService implements TemplateExportWordService {

    private String baseDocumentUri;


    public XhtmlExportWordService() {

    }

    public XhtmlExportWordService(String baseDocumentUri) {
        this.baseDocumentUri = baseDocumentUri;
    }

    @Override
    public void exportWord(OutputStream outputStream, Object dataModel, String tplName) {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            String content = getXHtmlContent(dataModel, tplName);

            XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
            wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(content,baseDocumentUri));
            wordMLPackage.save(outputStream);

        } catch (Docx4JException | IOException | TemplateException ex) {
            log.error(" 导出 word 报错", ex);
            throw new ExportException("export word error", ex);
        }

    }

    public void setBaseDocumentUri(String baseDocumentUri) {
        this.baseDocumentUri = baseDocumentUri;
    }


    protected abstract String getXHtmlContent(Object dataModel, String tplName) throws IOException, TemplateException;
}
