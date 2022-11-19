package cn.iocoder.yudao.module.yr.service.sys.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypePageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.QgDictTypeUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictTypeDO;

import java.util.List;

/**
 * 字典类型 Service 接口
 *
 * @author 芋道源码
 */
public interface QgDictTypeService {

    /**
     * 创建字典类型
     *
     * @param reqVO 字典类型信息
     * @return 字典类型编号
     */
    Long createDictType(QgDictTypeCreateReqVO reqVO);

    /**
     * 更新字典类型
     *
     * @param reqVO 字典类型信息
     */
    void updateDictType(QgDictTypeUpdateReqVO reqVO);

    /**
     * 删除字典类型
     *
     * @param id 字典类型编号
     */
    void deleteDictType(Long id);

    /**
     * 获得字典类型分页列表
     *
     * @param reqVO 分页请求
     * @return 字典类型分页列表
     */
    PageResult<QgDictTypeDO> getDictTypePage(QgDictTypePageReqVO reqVO);

    /**
     * 获得字典类型列表
     *
     * @param reqVO 列表请求
     * @return 字典类型列表
     */
    List<QgDictTypeDO> getDictTypeList(QgDictTypeExportReqVO reqVO);

    /**
     * 获得字典类型详情
     *
     * @param id 字典类型编号
     * @return 字典类型
     */
    QgDictTypeDO getDictType(Long id);

    /**
     * 获得字典类型详情
     *
     * @param type 字典类型
     * @return 字典类型详情
     */
    QgDictTypeDO getDictType(String type);

    /**
     * 获得全部字典类型列表
     *
     * @return 字典类型列表
     */
    List<QgDictTypeDO> getDictTypeList();

}
