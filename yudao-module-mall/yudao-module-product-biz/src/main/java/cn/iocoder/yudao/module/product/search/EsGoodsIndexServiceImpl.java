package cn.iocoder.yudao.module.product.search;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.core.BaseElasticsearchService;
import cn.iocoder.yudao.module.product.search.po.EsGoodsIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author whycode
 * @title: EsGoodsIndexServiceImpl
 * @projectName home-care
 * @description: TODO
 * @date 2023/10/1815:53
 */

@Component
public class EsGoodsIndexServiceImpl extends BaseElasticsearchService {



    @PostConstruct
    public void initIndex(){

        /*if(this.indexExist("goods")){
            this.deleteIndexRequest("goods");
        }
        this.createIndexRequest("goods", body);*/

        //this.deleteRequest("goods", "1");

//        this.addDocument("goods", "1", JSONUtil.toJsonStr(new EsGoodsIndex()
//                .setGoodsId("123456")
//                .setId("1")
//                .setName("五环外")
//                .setBrandName("华为")));

    }

}
