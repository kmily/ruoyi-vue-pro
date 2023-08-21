package cn.iocoder.yudao.module.system.api.notice;

import cn.iocoder.yudao.module.system.api.notice.dto.NoticeDTO;
import cn.iocoder.yudao.module.system.convert.notice.NoticeConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.notice.NoticeDO;
import cn.iocoder.yudao.module.system.service.notice.NoticeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whycode
 * @title: NoticeApiImpl
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1813:52
 */

@Component
public class NoticeApiImpl implements NoticeApi{

    @Resource
    private NoticeService noticeService;

    @Override
    public List<NoticeDTO> getNotices(LocalDateTime startTime) {

        List<NoticeDO> noticeDOS = noticeService.getNoticeList(startTime);

        return NoticeConvert.INSTANCE.convertList(noticeDOS);
    }
}
