package cn.iocoder.yudao.module.db.service.material;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.db.controller.admin.material.vo.*;
import cn.iocoder.yudao.module.db.dal.dataobject.material.MaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料 Service 接口
 *
 * @author Arlen
 */
public interface MaterialService {

    /**
     * 创建物料
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterial(@Valid MaterialSaveReqVO createReqVO);

    /**
     * 更新物料
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterial(@Valid MaterialSaveReqVO updateReqVO);

    /**
     * 删除物料
     *
     * @param id 编号
     */
    void deleteMaterial(String id);

    /**
     * 获得物料
     *
     * @param id 编号
     * @return 物料
     */
    MaterialDO getMaterial(String id);

    /**
     * 获得物料分页
     *
     * @param pageReqVO 分页查询
     * @return 物料分页
     */
    PageResult<MaterialDO> getMaterialPage(MaterialPageReqVO pageReqVO);

}