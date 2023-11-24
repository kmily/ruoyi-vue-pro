package cn.iocoder.yudao.module.mp.service.message;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateFormUserReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplatePageReqVO;
import cn.iocoder.yudao.module.mp.convert.message.MpTemplateConvert;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpTemplateDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.yudao.module.mp.dal.mysql.message.MpTemplateMapper;
import cn.iocoder.yudao.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.yudao.module.mp.service.account.MpAccountService;
import cn.iocoder.yudao.module.mp.service.user.MpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.mp.enums.ErrorCodeConstants.*;

/**
 * 公众号模板 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/10/3 20:38
 */
@Slf4j
@Service
public class MpTemplateServiceImpl implements MpTemplateService {

    @Resource
    @Lazy // 延迟加载，解决循环依赖的问题
    private MpAccountService mpAccountService;

    @Resource
    @Lazy // 延迟加载，解决循环依赖的问题
    private MpServiceFactory mpServiceFactory;

    @Resource
    @Lazy // 延迟加载，解决循环依赖的问题
    private MpUserService mpUserService;

    @Resource
    private MpTemplateMapper mpTemplateMapper;

    @Override
    public PageResult<MpTemplateDO> getTemplatePage(MpTemplatePageReqVO pageReqVO) {
        return mpTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public MpTemplateDO getTemplate(Long id) {
        return mpTemplateMapper.selectById(id);
    }

    @Override
    public MpTemplateDO getTemplate(String templateId) {
        return mpTemplateMapper.selectByTemplateId(templateId);
    }

    @Override
    public void deleteTemplate(Long id) {
        // 校验是否存在账号，如果存在并返回mpTemplate
        MpTemplateDO mpTemplateDO = validateTemplateExists(id);
        WxMpService mpService = mpServiceFactory.getRequiredMpService(mpTemplateDO.getAccountId());
        mpTemplateMapper.deleteById(id);
        try {
            mpService.getTemplateMsgService().delPrivateTemplate(mpTemplateDO.getTemplateId());
        } catch (WxErrorException e) {
            throw exception(TEMPLATE_DELETE_FAIL, e.getError().getErrorMsg());
        }
    }

    @Override
    public void updateTemplate(MpTemplateDO mpTemplateDO) {
        mpTemplateMapper.updateById(mpTemplateDO);
    }

    @Override
    public void sendMsgBatch(MpTemplateFormUserReqVO form) {
        log.info("[MpTemplateServiceImpl](sendMsgBatch)批量发送模板消息任务开始,参数：({})",form.toString());

        WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = templateMessageBuilder(form.getId());
        sendUserList(form).forEach(user -> {
            // 构建用户的openId
            WxMpTemplateMessage msg = builder.toUser(user.getOpenid()).build();
            // 发送
            sendTemplateMsg(form.getAccountId(),msg);
        });
    }

    @Override
    public String getTemplateContent(Long id) {
        return mpTemplateMapper.selectContent(id);
    }

    @Override
    public void syncTemplate(Long accountId) {
        // 存储数据库的时候才会用到
        MpAccountDO account = mpAccountService.getRequiredAccount(accountId);
        // 第一步，从公众号平台获取最新的标签列表
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        List<WxMpTemplate> wxMpTemplateList;
        try {
            wxMpTemplateList = mpService.getTemplateMsgService().getAllPrivateTemplate();
            log.info("[MpTemplateServiceImpl](syncTemplate)成功加载出微信公众号模板数量为：({})，模板详细信息为：({})"
                    ,wxMpTemplateList.size(),wxMpTemplateList);
        } catch (WxErrorException e) {
            throw exception(TEMPLATE_GET_FAIL, e.getError().getErrorMsg());
        }
        // 第二步，公众号中有些模板是被删除的，数据库中则需要和我公众号进行同步
        // 数据库中的数据
        Map<String, MpTemplateDO> mpTemplateDOMap = convertMap(mpTemplateMapper.selectListByAccountId(accountId),
                MpTemplateDO::getTemplateId);
        // 公众号中的数据
        wxMpTemplateList.forEach(wxMpTemplate -> {
            MpTemplateDO removeTemplate = mpTemplateDOMap.remove(wxMpTemplate.getTemplateId());
            // 1.数据库中找不到数据那就，新增
            if(removeTemplate == null){
                MpTemplateDO mpTemplateDO = MpTemplateConvert.INSTANCE.convert(wxMpTemplate, account);
                mpTemplateMapper.insert(mpTemplateDO);
            } else {
                // 2.数据库中找到数据那就，更新
                mpTemplateMapper.updateById(new MpTemplateDO().setId(removeTemplate.getId())
                        .setTitle(wxMpTemplate.getTitle())
                        .setContent(wxMpTemplate.getContent())
                );
            }
        });
        // 剩下如果存在的话，就代表着微信数据库中已经删除，而我的数据库中存在
        if(CollUtil.isNotEmpty(mpTemplateDOMap)){
            mpTemplateMapper.deleteBatchIds(convertList(mpTemplateDOMap.values(), MpTemplateDO::getId));
        }
    }

    private void sendTemplateMsg(Long accountId, WxMpTemplateMessage msg) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        String result = null;
        try {
            result = mpService.getTemplateMsgService().sendTemplateMsg(msg);
            log.info("【成功】公众号模板消息发送成功，返回的结果为：({})",result);
        } catch (WxErrorException e) {
            throw exception(TEMPLATE_SEND_ERROR, e.getError().getErrorMsg());
        }
    }

    private MpTemplateDO validateTemplateExists(Long id) {
        MpTemplateDO mpTemplateDO = mpTemplateMapper.selectById(id);
        if (ObjUtil.isEmpty(mpTemplateDO)) {
            throw exception(TEMPLATE_ID_NOT_EXISTS);
        }
        return mpTemplateDO;
    }

    private WxMpTemplateMessage.WxMpTemplateMessageBuilder templateMessageBuilder(Long id) {
        MpTemplateDO template = getTemplate(id);
        if(ObjUtil.isEmpty(template)){
            throw exception(TEMPLATE_ID_NOT_EXISTS);
        }
        if(!CommonStatusEnum.ENABLE.equals(template.getStatus())){
            throw exception(TEMPLATE_DISABLE);
        }
        // 构建所需要的【五大参数】 templateId url miniProgram data toUser（toUser不会在这里给）
        WxMpTemplateMessage.WxMpTemplateMessageBuilder wxMpTemplateMessageBuilder = WxMpTemplateMessage.builder()
                .templateId(template.getTemplateId())
                .data(convert(template.getMessageData()));
        if(StrUtil.isNotEmpty(template.getUrl())){
            wxMpTemplateMessageBuilder.url(template.getUrl());
        }
        if(StrUtil.isNotEmpty(template.getAppId()) && StrUtil.isNotEmpty(template.getAppPath())){
            wxMpTemplateMessageBuilder.miniProgram(new WxMpTemplateMessage.MiniProgram(template.getAppId(),
                    template.getAppPath(),false));
        }
        return wxMpTemplateMessageBuilder;
    }

    private List<MpUserDO> sendUserList(MpTemplateFormUserReqVO form){
        PageResult<MpUserDO> userPageEnhance = mpUserService.getUserPageEnhance(MpTemplateConvert.INSTANCE.convert(form));
        return userPageEnhance.getList();
    }

    private List<WxMpTemplateData> convert(List<Map<String,String>> originalList){
        List<WxMpTemplateData> wxMpTemplateDataList = new ArrayList<>();
        for (Map<String, String> map : originalList) {
            // 创建一个新的 WxMpTemplateData 对象
            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();

            // 遍历 map 的键值对，将其转换为 WxMpTemplateData 的属性
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // 根据具体的 WxMpTemplateData 对象属性，设置对应的值
                if ("color".equals(key)) {
                    wxMpTemplateData.setColor(value);
                } else if ("name".equals(key)) {
                    wxMpTemplateData.setName(value);
                }else if ("value".equals(key)){
                    wxMpTemplateData.setValue(value);
                }
            }
            // 将转换后的 WxMpTemplateData 对象添加到结果列表中
            wxMpTemplateDataList.add(wxMpTemplateData);
        }
        return wxMpTemplateDataList;
    }

}
