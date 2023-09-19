package cn.iocoder.yudao.module.mp.convert.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.message.MpMessageRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.yudao.module.mp.service.message.bo.MpMessageSendOutReqBO;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.builder.outxml.BaseBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MpMessageConvert {

    MpMessageConvert INSTANCE = Mappers.getMapper(MpMessageConvert.class);

    MpMessageRespVO convert(MpMessageDO bean);

    List<MpMessageRespVO> convertList(List<MpMessageDO> list);

    PageResult<MpMessageRespVO> convertPage(PageResult<MpMessageDO> page);

    default MpMessageDO convert(WxMpXmlMessage wxMessage, MpAccountDO account, MpUserDO user) {
        MpMessageDO message = convert(wxMessage);
        if (account != null) {
            message.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        if (user != null) {
            message.setUserId(user.getId()).setOpenid(user.getOpenid());
        }
        return message;
    }
    @Mappings(value = {
            @Mapping(source = "msgType", target = "type"),
            @Mapping(target = "createTime", ignore = true),
    })
    MpMessageDO convert(WxMpXmlMessage bean);

    default MpMessageDO convert(MpMessageSendOutReqBO sendReqBO, MpAccountDO account, MpUserDO user) {
        // 构建消息
        MpMessageDO message = new MpMessageDO();
        message.setType(sendReqBO.getType());
        switch (sendReqBO.getType()) {
            // 1. 文本
            case WxConsts.XmlMsgType.TEXT -> message.setContent(sendReqBO.getContent());
            // 2. 图片 3. 语音
            case WxConsts.XmlMsgType.IMAGE,WxConsts.XmlMsgType.VOICE -> message.setMediaId(sendReqBO.getMediaId());
            // 4. 视频
            case WxConsts.XmlMsgType.VIDEO -> message.setMediaId(sendReqBO.getMediaId())
                        .setTitle(sendReqBO.getTitle()).setDescription(sendReqBO.getDescription());
            // 5. 图文
            case WxConsts.XmlMsgType.NEWS -> message.setArticles(sendReqBO.getArticles());
            // 6. 音乐
            case WxConsts.XmlMsgType.MUSIC -> message.setTitle(sendReqBO.getTitle()).setDescription(sendReqBO.getDescription())
                    .setMusicUrl(sendReqBO.getMusicUrl()).setHqMusicUrl(sendReqBO.getHqMusicUrl())
                    .setThumbMediaId(sendReqBO.getThumbMediaId());
            default -> throw new IllegalArgumentException("不支持的消息类型：" + message.getType());
        }

        // 其它字段
        if (account != null) {
            message.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        if (user != null) {
            message.setUserId(user.getId()).setOpenid(user.getOpenid());
        }
        return message;
    }

    default WxMpXmlOutMessage convert02(MpMessageDO message, MpAccountDO account) {
        // 个性化字段
        BaseBuilder<?, ? extends WxMpXmlOutMessage> builder = switch (message.getType()) {
            case WxConsts.XmlMsgType.TEXT -> WxMpXmlOutMessage.TEXT().content(message.getContent());
            case WxConsts.XmlMsgType.IMAGE -> WxMpXmlOutMessage.IMAGE().mediaId(message.getMediaId());
            case WxConsts.XmlMsgType.VOICE -> WxMpXmlOutMessage.VOICE().mediaId(message.getMediaId());
            case WxConsts.XmlMsgType.VIDEO -> WxMpXmlOutMessage.VIDEO().mediaId(message.getMediaId())
                    .title(message.getTitle()).description(message.getDescription());
            case WxConsts.XmlMsgType.NEWS -> WxMpXmlOutMessage.NEWS().articles(convertList02(message.getArticles()));
            case WxConsts.XmlMsgType.MUSIC ->
                    WxMpXmlOutMessage.MUSIC().title(message.getTitle()).description(message.getDescription())
                            .musicUrl(message.getMusicUrl()).hqMusicUrl(message.getHqMusicUrl())
                            .thumbMediaId(message.getThumbMediaId());
            default -> throw new IllegalArgumentException("不支持的消息类型：" + message.getType());
        };
        // 通用字段
        builder.fromUser(account.getAccount());
        builder.toUser(message.getOpenid());
        return builder.build();
    }
    List<WxMpXmlOutNewsMessage.Item> convertList02(List<MpMessageDO.Article> list);

    default WxMpKefuMessage convert(MpMessageSendReqVO sendReqVO, MpUserDO user) {
        // 个性化字段
        me.chanjar.weixin.mp.builder.kefu.BaseBuilder<?> builder = switch (sendReqVO.getType()) {
            case WxConsts.KefuMsgType.TEXT -> WxMpKefuMessage.TEXT().content(sendReqVO.getContent());
            case WxConsts.KefuMsgType.IMAGE -> WxMpKefuMessage.IMAGE().mediaId(sendReqVO.getMediaId());
            case WxConsts.KefuMsgType.VOICE -> WxMpKefuMessage.VOICE().mediaId(sendReqVO.getMediaId());
            case WxConsts.KefuMsgType.VIDEO -> WxMpKefuMessage.VIDEO().mediaId(sendReqVO.getMediaId())
                    .title(sendReqVO.getTitle()).description(sendReqVO.getDescription());
            case WxConsts.KefuMsgType.NEWS -> WxMpKefuMessage.NEWS().articles(convertList03(sendReqVO.getArticles()));
            case WxConsts.KefuMsgType.MUSIC ->
                    WxMpKefuMessage.MUSIC().title(sendReqVO.getTitle()).description(sendReqVO.getDescription())
                            .thumbMediaId(sendReqVO.getThumbMediaId())
                            .musicUrl(sendReqVO.getMusicUrl()).hqMusicUrl(sendReqVO.getHqMusicUrl());
            default -> throw new IllegalArgumentException("不支持的消息类型：" + sendReqVO.getType());
        };
        // 通用字段
        builder.toUser(user.getOpenid());
        return builder.build();
    }
    List<WxMpKefuMessage.WxArticle> convertList03(List<MpMessageDO.Article> list);

    default MpMessageDO convert(WxMpKefuMessage wxMessage, MpAccountDO account, MpUserDO user) {
        MpMessageDO message = convert(wxMessage);
        if (account != null) {
            message.setAccountId(account.getId()).setAppId(account.getAppId());
        }
        if (user != null) {
            message.setUserId(user.getId()).setOpenid(user.getOpenid());
        }
        return message;
    }
    @Mappings(value = {
            @Mapping(source = "msgType", target = "type"),
            @Mapping(target = "createTime", ignore = true),
    })
    MpMessageDO convert(WxMpKefuMessage bean);

}
