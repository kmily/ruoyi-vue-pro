package cn.iocoder.yudao.adminserver.modules.system.service.notice.impl;

import cn.iocoder.yudao.adminserver.modules.system.controller.notice.vo.SysNoticeCreateReqVO;
import cn.iocoder.yudao.adminserver.modules.system.controller.notice.vo.SysNoticePageReqVO;
import cn.iocoder.yudao.adminserver.modules.system.controller.notice.vo.SysNoticeUpdateReqVO;
import cn.iocoder.yudao.adminserver.modules.system.convert.notice.SysNoticeConvert;
import cn.iocoder.yudao.adminserver.modules.system.dal.dataobject.notice.SysNoticeDO;
import cn.iocoder.yudao.adminserver.modules.system.dal.mysql.notice.SysNoticeMapper;
import cn.iocoder.yudao.adminserver.modules.system.service.notice.SysNoticeService;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.export.service.TemplateExportPdfService;
import cn.iocoder.yudao.framework.export.service.TemplateExportWordService;
import com.google.common.annotations.VisibleForTesting;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;

import static cn.iocoder.yudao.adminserver.modules.system.enums.SysErrorCodeConstants.NOTICE_NOT_FOUND;

/**
 * 通知公告 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Resource
    private SysNoticeMapper noticeMapper;

    @Resource
    private TemplateExportPdfService exportPdfService;

    @Resource
    private TemplateExportWordService exportWordService;

    @Override
    public Long createNotice(SysNoticeCreateReqVO reqVO) {
        SysNoticeDO notice = SysNoticeConvert.INSTANCE.convert(reqVO);
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(SysNoticeUpdateReqVO reqVO) {
        // 校验是否存在
        this.checkNoticeExists(reqVO.getId());
        // 更新通知公告
        SysNoticeDO updateObj = SysNoticeConvert.INSTANCE.convert(reqVO);
        noticeMapper.updateById(updateObj);
    }

    @Override
    public void deleteNotice(Long id) {
        // 校验是否存在
        this.checkNoticeExists(id);
        // 删除通知公告
        noticeMapper.deleteById(id);
    }

    @Override
    public PageResult<SysNoticeDO> pageNotices(SysNoticePageReqVO reqVO) {
        return noticeMapper.selectPage(reqVO);
    }

    @Override
    public SysNoticeDO getNotice(Long id) {
        return noticeMapper.selectById(id);
    }


    @Override
    public void exportPdf(Long id, OutputStream outputStream) {
        // 校验是否存在
        checkNoticeExists(id);
        final SysNoticeDO sysNoticeDO = noticeMapper.selectById(id);
        final String content = sysNoticeDO.getContent();
        Document doc = Jsoup.parse(content);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        //html 转化 xhtml
        sysNoticeDO.setContent(doc.getElementsByTag("body").html());
        exportPdfService.exportPdf(outputStream, sysNoticeDO, "notice.ftl");
    }

    @Override
    public void exportWord(Long id, OutputStream outputStream) {
        // 校验是否存在
        checkNoticeExists(id);
        final SysNoticeDO sysNoticeDO = noticeMapper.selectById(id);
        final String content = sysNoticeDO.getContent();
        Document doc = Jsoup.parse(content);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        //html 转化 xhtml
        sysNoticeDO.setContent(doc.getElementsByTag("body").html());
        exportWordService.exportWord(outputStream, sysNoticeDO,"notice.ftl");
    }

    @VisibleForTesting
    public void checkNoticeExists(Long id) {
        if (id == null) {
            return;
        }
        SysNoticeDO notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw ServiceExceptionUtil.exception(NOTICE_NOT_FOUND);
        }
    }

}
