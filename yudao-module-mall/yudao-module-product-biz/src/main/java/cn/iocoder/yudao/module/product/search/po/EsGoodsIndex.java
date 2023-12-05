package cn.iocoder.yudao.module.product.search.po;

import lombok.Data;
import lombok.experimental.Accessors;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
import java.io.Serializable;

/**
 * @author whycode
 * @title: EsGoodsIndex
 * @projectName home-care
 * @description: TODO
 * @date 2023/10/2011:01
 */

@Data
//@Document(indexName = "goods", createIndex = false)
@Accessors(chain = true)
public class EsGoodsIndex implements Serializable {

    //@Id
    private String id;

   // @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;
  //  @Field(type = FieldType.Text)
    private String goodsId;
   // @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brandName;

}
