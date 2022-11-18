package cn.iocoder.yudao.module.yr.api.dic;


import cn.iocoder.yudao.module.yr.api.dict.QgDictDataApi;
import cn.iocoder.yudao.module.yr.api.dict.QgDictDataRespDTO;
import cn.iocoder.yudao.module.yr.service.sys.dict.QgDictDataService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 字典数据 API 实现类
 *
 * @author alex
 */
@Service
public class QgDictDataApiImpl implements QgDictDataApi {

    @Resource
    private QgDictDataService dictDataService;

    @Override
    public void validDictDatas(String dictType, Collection<String> values) {
        dictDataService.validDictDatas(dictType, values);
    }

    @Override
    public List<QgDictDataRespDTO> listDictDatasFromCache(String type) {
        return dictDataService.listDictDatasFromCache(type);
    }
}
