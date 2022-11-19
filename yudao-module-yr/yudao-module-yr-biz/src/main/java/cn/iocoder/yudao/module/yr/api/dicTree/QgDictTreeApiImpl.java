package cn.iocoder.yudao.module.yr.api.dicTree;


import cn.iocoder.yudao.module.yr.api.dictTree.QgDictTreeApi;
import cn.iocoder.yudao.module.yr.service.sys.dictTree.QgDictTreeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 职员手机号关联租户api，用于创建和修改职员时添加数据
 *
 * @author tml
 */
@Service
@Validated
public class QgDictTreeApiImpl implements QgDictTreeApi {

    @Resource
    private QgDictTreeService qgDictTreeService;



    @Override
    public void initTop() {
        qgDictTreeService.initTop();
    }


}
