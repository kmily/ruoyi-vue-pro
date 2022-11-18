package cn.iocoder.yudao.module.yr.dal.mysql.sys.dictTree;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeKindTopVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreePageReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 业务字典分类 Mapper
 *
 * @author alex
 */
@Mapper
public interface QgDictTreeMapper extends BaseMapperX<QgDictTreeDO> {

    default PageResult<QgDictTreeDO> selectPage(QgDictTreePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QgDictTreeDO>()
                .likeIfPresent(QgDictTreeDO::getName, reqVO.getName())
                .eqIfPresent(QgDictTreeDO::getParentId, reqVO.getParentId())
                .eqIfPresent(QgDictTreeDO::getSort, reqVO.getSort())
                .eqIfPresent(QgDictTreeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(QgDictTreeDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByAsc(QgDictTreeDO::getId));
    }

    default List<QgDictTreeDO> selectList(QgDictTreeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<QgDictTreeDO>()
                .likeIfPresent(QgDictTreeDO::getName, reqVO.getName())
                .eqIfPresent(QgDictTreeDO::getParentId, reqVO.getParentId())
                .eqIfPresent(QgDictTreeDO::getSort, reqVO.getSort())
                .eqIfPresent(QgDictTreeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(QgDictTreeDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByAsc(QgDictTreeDO::getId));
    }

    default List<QgDictTreeDO> selectListTop(QgDictTreeKindTopVO reqVO) {
        return selectList(new LambdaQueryWrapperX<QgDictTreeDO>()
                .eqIfPresent(QgDictTreeDO::getParentId, reqVO.getParentId())
                .likeIfPresent(QgDictTreeDO::getName, reqVO.getName())
                .orderByAsc(QgDictTreeDO::getId));
    }

    default List<QgDictTreeDO> selectListByType(Integer type, Long parentId) {
        return selectList(new LambdaQueryWrapperX<QgDictTreeDO>()
                .eqIfPresent(QgDictTreeDO::getParentId, parentId)
                .eqIfPresent(QgDictTreeDO::getType, type)
                .orderByAsc(QgDictTreeDO::getId));
    }

    @Select("SELECT id FROM qg_sys_dict_tree WHERE update_time > #{maxUpdateTime} LIMIT 1")
    Long selectExistsByUpdateTimeAfter(LocalDateTime maxUpdateTime);
}
