package cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.*;

/**
 * 悠悠商品模板 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface UUTemplateMapper extends BaseMapperX<YouyouTemplateDO> {

    default PageResult<YouyouTemplateDO> selectPage(YouyouTemplatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<YouyouTemplateDO>()
                .likeIfPresent(YouyouTemplateDO::getName, reqVO.getName())
                .likeIfPresent(YouyouTemplateDO::getHashName, reqVO.getHashName())
                .eqIfPresent(YouyouTemplateDO::getTypeId, reqVO.getTypeId())
                .likeIfPresent(YouyouTemplateDO::getTypeName, reqVO.getTypeName())
                .likeIfPresent(YouyouTemplateDO::getTypeHashName, reqVO.getTypeHashName())
                .eqIfPresent(YouyouTemplateDO::getWeaponId, reqVO.getWeaponId())
                .likeIfPresent(YouyouTemplateDO::getWeaponName, reqVO.getWeaponName())
                .likeIfPresent(YouyouTemplateDO::getWeaponHashName, reqVO.getWeaponHashName())
                .betweenIfPresent(YouyouTemplateDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(YouyouTemplateDO::getTemplateId, reqVO.getTemplateId())
                .orderByDesc(YouyouTemplateDO::getId));
    }

}