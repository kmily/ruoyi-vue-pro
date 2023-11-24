package cn.iocoder.yudao.module.mp.convert.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateFormUserReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplateUpdateReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserPageEnhanceReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpTemplateDO;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/3 21:06
 */
@Mapper
public interface MpTemplateConvert {

    MpTemplateConvert INSTANCE = Mappers.getMapper(MpTemplateConvert.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "account.id", target = "accountId"),
            @Mapping(source = "account.appId", target = "appId"),
    })
    MpTemplateDO convert(WxMpTemplate wxMpTemplate, MpAccountDO account);

    /**
     * convertMap给其他resp类字段使用
     * @param source
     * @return
     */
    Map<String, String> convert(Map<String, String> source);

    MpTemplateRespVO convert(MpTemplateDO mpTemplateDO);

    MpUserPageEnhanceReqVO convert(MpTemplateFormUserReqVO mpTemplateDO);

    MpTemplateDO convert(MpTemplateUpdateReqVO mpTemplateUpdateReqVO);

    List<MpTemplateDO> convertList(List<WxMpTemplate> wxMpTemplateList);

    PageResult<MpTemplateRespVO> convertPage(PageResult<MpTemplateDO> page);
}
