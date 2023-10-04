package cn.iocoder.yudao.module.mp.dal.mysql.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.mp.controller.admin.message.vo.template.MpTemplatePageReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/3 20:55
 */
@Mapper
public interface MpTemplateMapper extends BaseMapperX<MpTemplateDO> {

    default PageResult<MpTemplateDO> selectPage(MpTemplatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MpTemplateDO>()
                .likeIfPresent(MpTemplateDO::getTemplateId, reqVO.getTemplateId())
                .likeIfPresent(MpTemplateDO::getTitle, reqVO.getTitle())
                .orderByDesc(MpTemplateDO::getId));
    }

    default List<MpTemplateDO> selectListByAccountId(Long accountId){
        return selectList(MpTemplateDO::getAccountId, accountId);
    }

    default MpTemplateDO selectByTemplateId(String templateId){
        return selectOne(MpTemplateDO::getTemplateId, templateId);
    }

    default String selectContent(Long id){
        return selectOne(new LambdaQueryWrapperX<MpTemplateDO>()
                .select(MpTemplateDO::getContent)
                .eq(MpTemplateDO::getId, id)).getContent();
    }
}
