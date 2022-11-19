package cn.iocoder.yudao.module.yr.service.sys.dictTree;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.*;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 业务字典分类 Service 接口
 *
 * @author alex
 */
public interface QgDictTreeService {
    /**
     * 初始化业务字典分类的本地缓存
     */
    void initLocalCache();
    /**
     * 获得列表
     *
     * @param  reqVO 筛选条件请求 VO
     * @return 列表
     */
    List<QgDictTreeDO> getListTop(QgDictTreeKindTopVO reqVO);
    /**
     * 创建业务字典分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQgDictTree(@Valid QgDictTreeCreateReqVO createReqVO);

    /**
     * 更新业务字典分类
     *
     * @param updateReqVO 更新信息
     */
    void updateQgDictTree(@Valid QgDictTreeUpdateReqVO updateReqVO);

    /**
     * 删除业务字典分类
     *
     * @param id 编号
     */
    void deleteQgDictTree(Long id);
    /**
     * 获得所有子业务字典分类，从缓存中
     *
     * @param parentId 业务字典分类编号
     * @param recursive 是否递归获取所有
     * @return 子业务字典分类列表
     */
    List<QgDictTreeDO> getByParentIdFromCache(Long parentId, boolean recursive);
    /**
     * 获得业务字典分类
     *
     * @param id 编号
     * @return 业务字典分类
     */
    QgDictTreeDO getQgDictTree(Long id);

    /**
     * 获得业务字典分类列表
     *
     * @param ids 编号
     * @return 业务字典分类列表
     */
    List<QgDictTreeDO> getQgDictTreeList(Collection<Long> ids);

    /**
     * 获得业务字典分类分页
     *
     * @param pageReqVO 分页查询
     * @return 业务字典分类分页
     */
    PageResult<QgDictTreeDO> getQgDictTreePage(QgDictTreePageReqVO pageReqVO);

    /**
     * 获得业务字典分类列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 业务字典分类列表
     */
    List<QgDictTreeDO> getQgDictTreeList(QgDictTreeExportReqVO exportReqVO);
    /**
     * 获得业务字典分类条件：查询指定节点的业务字典分类子编号们，包括自身
     *
     * @param QgDictTreeId 业务字典分类编号
     * @return 业务字典分类编号集合
     */
    Set<Long> getCondition(Long QgDictTreeId);
    /**
     * 第一次查询初始化
     */
    void initTop();
    /**
     * 获得业务字典分类列表：
     * @param parentId 父节点ID
     * @return 业务字典分类列表
     */
    List<QgDictTreeDO> getQgDictTreeListByType(Long parentId);
    /**
     * 获得指定编号的业务字典分类列表
     *
     * @param ids 业务字典分类编号数组
     * @return 业务字典分类列表
     */
    List<QgDictTreeDO> getSimpleQgDictTrees(Collection<Long> ids);
    /**
     * 获得指定编号的业务字典分类 Map
     *
     * @param ids 业务字典分类编号数组
     * @return 业务字典分类 Map
     */
    default String getQgDictTreeMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return "";
        }
        List<QgDictTreeDO> list = getSimpleQgDictTrees(ids);
        return list.stream().map(QgDictTreeDO::getName).collect(Collectors.joining());
    }
    /**
     * 通过缓存获取id对应的name集合
     *
     * @param ids 业务字典ID集合
     * @return name集合
     */
    String getTreeNamesFromCache(Collection<Long> ids);
    /**
     * 通过缓存获取id对应的name集合
     *
     * @param id 业务字典ID
     * @return name
     */
    String getSignTreeNamesFromCache(Long id);
}
