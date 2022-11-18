package cn.iocoder.yudao.module.yr.service.sys.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.api.dict.QgDictDataRespDTO;
import cn.iocoder.yudao.module.yr.common.vo.UpdateStatusVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataPageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.QgDictDataUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictDataDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 字典数据 Service 接口
 *
 * @author ruoyi
 */
public interface QgDictDataService {
    /**
     * 初始化业务字典值的本地缓存
     */
    void initLocalCache();
    /**
     * 创建字典数据
     *
     * @param reqVO 字典数据信息
     * @return 字典数据编号
     */
    Long createDictData(QgDictDataCreateReqVO reqVO);

    /**
     * 更新字典数据
     *
     * @param reqVO 字典数据信息
     */
    void updateDictData(QgDictDataUpdateReqVO reqVO);

    /**
     * 删除字典数据
     *
     * @param id 字典数据编号
     */
    void deleteDictData(Long id);

    /**
     * 获得字典数据列表
     *
     * @return 字典数据全列表
     */
    List<QgDictDataDO> getDictDatas();

    /**
     * 获得字典数据分页列表
     *
     * @param reqVO 分页请求
     * @return 字典数据分页列表
     */
    PageResult<QgDictDataDO> getDictDataPage(QgDictDataPageReqVO reqVO);

    /**
     * 获得字典数据列表
     *
     * @param reqVO 列表请求
     * @return 字典数据列表
     */
    List<QgDictDataDO> getDictDatas(QgDictDataExportReqVO reqVO);

    /**
     * 获得字典数据详情
     *
     * @param id 字典数据编号
     * @return 字典数据
     */
    QgDictDataDO getDictData(Long id);

    /**
     * 获得指定字典类型的数据数量
     *
     * @param dictType 字典类型
     * @return 数据数量
     */
    long countByDictType(String dictType);

    /**
     * 校验字典数据们是否有效。如下情况，视为无效：
     * 1. 字典数据不存在
     * 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values 字典数据值的数组
     */
    void validDictDatas(String dictType, Collection<String> values);

    /**
     * 获得指定的字典数据
     *
     * @param dictType 字典类型
     * @param value 字典数据值
     * @return 字典数据
     */
    QgDictDataDO getDictData(String dictType, String value);

    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param dictType 字典类型
     * @param label 字典数据标签
     * @return 字典数据
     */
    QgDictDataDO parseDictData(String dictType, String label);
    /**
     * 获得指定类型的字典数据，从缓存中
     *
     * @param type 字典类型
     * @return 字典数据列表
     */
    List<QgDictDataRespDTO> listDictDatasFromCache(String type);
    /**
     * 更新状态
     *
     * @param updateStatusVO 更新信息
     */
    void updateStatus(@Valid UpdateStatusVO updateStatusVO);
}
