package cn.iocoder.yudao.module.system.api.notice;

import cn.iocoder.yudao.module.system.api.notice.dto.NoticeDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whycode
 * @title: NoticeApi
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1813:48
 */
public interface NoticeApi {


    /**
     * 查询未阅读的通告
     * @param startTime 开始查询时间
     * @return
     */
    List<NoticeDTO> getNotices(LocalDateTime startTime);

}
