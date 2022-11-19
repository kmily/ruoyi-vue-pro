package cn.iocoder.yudao.module.yr.api.dict;



import java.util.Collection;
import java.util.List;


/**
 * 字典数据 API 接口
 *
 * @author alex
 */
public interface QgDictDataApi {

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
     * 通过dictype得到dict数据集合：
     *
     * @param type 字典类型
     * @return List<DictDataRespDTO> 字典数据列表
     */
    List<QgDictDataRespDTO> listDictDatasFromCache(String type);
}
