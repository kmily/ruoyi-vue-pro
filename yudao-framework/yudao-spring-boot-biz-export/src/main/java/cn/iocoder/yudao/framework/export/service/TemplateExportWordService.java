package cn.iocoder.yudao.framework.export.service;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import java.io.OutputStream;

public interface TemplateExportWordService {

    void exportWord(OutputStream outputStream, Object dataModel, String tplName);
}
