package cn.iocoder.yudao.framework.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.util.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author whycode
 * @title: BaseElasticsearchService
 * @projectName home-care
 * @description: TODO
 * @date 2023/10/1813:55
 */

@Slf4j
public class BaseElasticsearchService {


    protected static final RequestOptions COMMON_OPTIONS;



    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));

        COMMON_OPTIONS = builder.build();
    }


    @Autowired
    protected RestClient restClient;

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;


    /**
     * 创建 DeleteIndexRequest
     * @param index elasticsearch index 名称
     * @return
     */
    private static Request buildDeleteIndexRequest(String index) {

        Request request = new Request(HttpDelete.METHOD_NAME, index);
        request.setOptions(COMMON_OPTIONS);
        return request;
    }

    /**
     * build IndexRequest
     *
     * @param index  elasticsearch index name
     * @param id     request object id
     * @param object request object
     * @return {@link IndexRequest}
     */
    protected static IndexRequest buildIndexRequest(String index, String id, Object object) {
        return new IndexRequest(index).id(id).source(BeanUtil.beanToMap(object), XContentType.JSON);
    }


    /**
     * 创建 elasticsearch index
     * @param index
     * @param body
     */
    protected void createIndexRequest(String index, String body){

        try {
            JSONObject entries = JSONUtil.parseObj(body);
            boolean mapping = entries.containsKey("mappings");
            boolean settings = entries.containsKey("settings");
            Assert.isTrue(mapping || settings, "");
        } catch (Exception e) {
            throw new ElasticsearchException("创建索引 {" + index + "} 传入参数 [body] 不合法：" + e.getMessage());
        }

        Request request = new Request(HttpPut.METHOD_NAME, index);
        request.setOptions(COMMON_OPTIONS);
        request.setJsonEntity(body);

        CountDownLatch latch = new CountDownLatch(1);
        restClient.performRequestAsync(
                request, new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        latch.countDown();
                        log.info("创建索引mapping成功：{}", response);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        latch.countDown();
                        log.error("创建索引mapping失败", exception);
                    }
                });
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new ElasticsearchException("创建索引 {" + index + "} 失败：" + e.getMessage());
        }

    }

    /**
     * create elasticsearch index (asyc)
     *
     * @param index elasticsearch index
     * @author fxbin
     */
    protected void createIndexRequest(String index) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("number_of_shards", elasticsearchProperties.getIndex().getNumberOfShards().toString());
            params.put("number_of_replicas", elasticsearchProperties.getIndex().getNumberOfReplicas().toString());
            params.put("max_result_window", "100000"); //最大查询结果数
            params.put("mapping.total_fields.limit", "100000");

            Map<String, Map<String, String>> indexParams  = new HashMap<>();
            indexParams.put("index", params);

            Map<String, Object> settingMap = new HashMap<>();
            settingMap.put("settings", indexParams);
            this.createIndexRequest(index, JSONUtil.toJsonStr(settingMap));
        } catch (Exception e) {
            log.error("创建索引错误", e);
            throw new ElasticsearchException("创建索引 {" + index + "} 失败：" + e.getMessage());
        }
    }

    /**
     * Description: 判断某个index是否存在
     *
     * @param index index名
     * @return boolean
     * @author fanxb
     * @since 2019/7/24 14:57
     */
    public boolean indexExist(String index) {
        try {
            Request request = new Request(HttpGet.METHOD_NAME, index);
            Response response = restClient.performRequest(request);
            InputStream content = response.getEntity().getContent();
            String read = IoUtil.read(content, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            log.error("获取索引 {" + index + "} 是否存在失败：" + e.getMessage());
        }
        return false;
    }

    /**
     * delete elasticsearch index
     *
     * @param index elasticsearch index name
     * @author fxbin
     */
    protected void deleteIndexRequest(String index) {
        Request deleteIndexRequest = buildDeleteIndexRequest(index);

        restClient.performRequestAsync(deleteIndexRequest, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                log.info("删除索引 {} 成功", index);
            }

            @Override
            public void onFailure(Exception exception) {
                log.error("删除索引 {} 失败", index, exception);
            }
        });
    }

    /**
     * exec updateRequest
     *
     * @param index  elasticsearch index name
     * @param id     Document id
     * @param object request object
     * @author fxbin
     */
    protected void updateRequest(String index, String id, Object object) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, id).doc(BeanUtil.beanToMap(object), XContentType.JSON);
           // client.update(updateRequest, COMMON_OPTIONS);
        } catch (Exception e) {
            throw new ElasticsearchException("更新索引 {" + index + "} 数据 {" + object + "} 失败: " + e.getMessage());
        }
    }

    /**
     * exec deleteRequest
     *
     * @param index elasticsearch index name
     * @param id    Document id
     * @author fxbin
     */
    protected void deleteRequest(String index, String id) {
        try {
            Request request = new Request(HttpDelete.METHOD_NAME, index + "/_doc/" + id);
            request.setOptions(COMMON_OPTIONS);
            //request.addParameter("_doc", id);

            restClient.performRequestAsync(request, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {

                    try {
                        try(InputStream is = response.getEntity().getContent()) {
                            log.info("删除索引[{}] 数据 id [{}] 成功, 返回数据：{}", index, id, IoUtil.read(is, StandardCharsets.UTF_8));
                        }
                    } catch (IOException e) {
                    }
                    log.info("删除索引[{}] 数据 id [{}] 成功", index, id);
                }

                @Override
                public void onFailure(Exception exception) {
                    log.info("删除索引[{}] 数据 id [{}] 失败", index, id, exception);
                }
            });

        } catch (Exception e) {
            throw new ElasticsearchException("删除索引 {" + index + "} 数据id {" + id + "} 失败: " + e.getMessage());
        }
    }

    /**
     * search all
     *
     * @param index elasticsearch index name
     * @return {@link SearchResponse}
     * @author fxbin
     */
    protected SearchResponse search(String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
           // searchResponse = client.search(searchRequest, COMMON_OPTIONS);
        } catch (Exception e) {
            log.error("es 搜索错误", e);
        }
        return searchResponse;
    }


    protected void addDocument(@NotBlank String index, @NotBlank String id, @NotBlank String body){

        Request request = new Request(HttpPost.METHOD_NAME, "/" + index + "/_doc/" + id);
        request.setOptions(COMMON_OPTIONS);
        request.setJsonEntity(body);

        restClient.performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    try(InputStream is = response.getEntity().getContent()) {
                        log.info("添加索引[{}] 数据 id [{}] 成功, 返回数据：{}", index, id, IoUtil.read(is, StandardCharsets.UTF_8));
                    }
                } catch (IOException e) {
                }
                log.info("添加索引[{}] 数据 id [{}] 成功", index, id);
            }

            @Override
            public void onFailure(Exception exception) {
                log.info("添加索引[{}] 数据 id [{}] 失败", index, id, exception);
            }
        });

    }

}
