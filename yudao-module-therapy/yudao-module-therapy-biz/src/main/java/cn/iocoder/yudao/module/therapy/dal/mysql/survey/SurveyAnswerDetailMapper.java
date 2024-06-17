package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyAnswerDetailMapper extends BaseMapperX<AnswerDetailDO> {

    default List<AnswerDetailDO> getByAnswerId(Long id){
        LambdaQueryWrapper<AnswerDetailDO> queryWrapper= Wrappers.lambdaQuery(AnswerDetailDO.class)
                .eq(AnswerDetailDO::getAnswerId,id);
        return selectList(queryWrapper);
    }

    default List<AnswerDetailDO> getByAnswerIds(List<Long> ids){
        LambdaQueryWrapper<AnswerDetailDO> queryWrapper= Wrappers.lambdaQuery(AnswerDetailDO.class)
                .in(AnswerDetailDO::getAnswerId,ids);
        return selectList(queryWrapper);
    }

}
