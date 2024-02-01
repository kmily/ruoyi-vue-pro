package cn.iocoder.yudao.module.steam.dal.mysql.selitemset;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.*;

/**
 * 收藏品选择 Mapper
 *
 * @author glzaboy
 */
@Mapper
public interface SelItemsetMapper extends BaseMapperX<SelItemsetDO> {

    default List<SelItemsetDO> selectList(SelItemsetListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SelItemsetDO>()
                .eqIfPresent(SelItemsetDO::getParentId, reqVO.getParentId())
                .likeIfPresent(SelItemsetDO::getInternalName, reqVO.getInternalName())
                .likeIfPresent(SelItemsetDO::getLocalizedTagName, reqVO.getLocalizedTagName())
                .orderByDesc(SelItemsetDO::getId));
    }

	default SelItemsetDO selectByParentIdAndInternalName(Long parentId, String internalName) {
	    return selectOne(SelItemsetDO::getParentId, parentId, SelItemsetDO::getInternalName, internalName);
	}

    default Long selectCountByParentId(Long parentId) {
        return selectCount(SelItemsetDO::getParentId, parentId);
    }

}