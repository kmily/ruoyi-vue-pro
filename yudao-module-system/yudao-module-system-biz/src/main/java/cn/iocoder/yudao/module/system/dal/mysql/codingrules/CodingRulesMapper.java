package cn.iocoder.yudao.module.system.dal.mysql.codingrules;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrules.CodingRulesDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.codingrules.vo.*;

/**
 * 编号规则表头 Mapper
 *
 * @author panjiabao
 */
@Mapper
public interface CodingRulesMapper extends BaseMapperX<CodingRulesDO> {

    default PageResult<CodingRulesDO> selectPage(CodingRulesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CodingRulesDO>()
                .eqIfPresent(CodingRulesDO::getCode, reqVO.getCode())
                .likeIfPresent(CodingRulesDO::getName, reqVO.getName())
                .eqIfPresent(CodingRulesDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CodingRulesDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CodingRulesDO::getId));
    }

}
