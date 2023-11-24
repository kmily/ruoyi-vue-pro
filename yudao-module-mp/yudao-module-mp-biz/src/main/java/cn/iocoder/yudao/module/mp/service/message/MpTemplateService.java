package cn.iocoder.yudao.module.mp.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateFormUserReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplatePageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateUpdateReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserPageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpTemplateDO;

/**
 * 公众号模板 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/10/3 20:38
 */
public interface MpTemplateService {

    /**
     * 同步一个公众号的模板
     *
     * @param accountId 公众号账号的编号
     */
    void syncTemplate(Long accountId);

    /**
     * 获得公众号粉丝分页
     *
     * @param pageReqVO 分页查询
     * @return 公众号粉丝分页
     */
    PageResult<MpTemplateDO> getTemplatePage(MpTemplatePageReqVO pageReqVO);

    /**
     * 获得公众号模板
     *
     * @param id 编号
     * @return 公众号模板
     */
    MpTemplateDO getTemplate(Long id);

    /**
     * 获取公众号模板id
     *
     * @param templateId 模板id
     * @return
     */
    MpTemplateDO getTemplate(String templateId);

    /**
     * 删除公众号模板
     *
     * @param id 主键id
     */
    void deleteTemplate(Long id);

    /**
     * 更新公众号模板
     *
     * @param mpTemplateDO 更新vo
     */
    void updateTemplate(MpTemplateDO mpTemplateDO);

    /**
     * 向用户发送公众号模板
     *
     * @param form 发送模板信息vo
     */
    void sendMsgBatch(MpTemplateFormUserReqVO form);

    /**
     * 根据id获取公众号模板
     *
     * @param id 模板id
     * @return
     */
    String getTemplateContent(Long id);
}
