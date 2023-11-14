package cn.iocoder.yudao.framework.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.core.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;

/**
 * @author whycode
 * @title: YudaoElasticsearchAutoConfiguration
 * @projectName home-care
 * @description: TODO
 * @date 2023/10/1810:14
 */

@EnableConfigurationProperties(ElasticsearchProperties.class)
@AutoConfiguration
public class YudaoElasticsearchAutoConfiguration extends ElasticsearchConfiguration {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    @Override
    @Bean
    public ClientConfiguration clientConfiguration() {

        String username = elasticsearchProperties.getAccount().getUsername();
        String password = elasticsearchProperties.getAccount().getPassword();
        CredentialsProvider provider = createCredentialsIfNotNull(username, password);
        return ClientConfiguration.builder()
                .connectedTo(this.getHosts())
                .withSocketTimeout(Duration.ofSeconds(12L))
                .withConnectTimeout(Duration.ofSeconds(1L))
                //.withBasicAuth(username, password)
                .withClientConfigurer(ElasticsearchClients.ElasticsearchClientConfigurationCallback.from(clientBuilder -> configureHttpClientBuilder(clientBuilder, null))).build();
    }

    private CredentialsProvider createCredentialsIfNotNull(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
    }

    private HttpAsyncClientBuilder configureHttpClientBuilder(HttpAsyncClientBuilder httpClientBuilder,
                                                              CredentialsProvider credentialsProvider) {
        httpClientBuilder
                .setKeepAliveStrategy(getConnectionKeepAliveStrategy())
                .setMaxConnPerRoute(10)
                .setDefaultIOReactorConfig(
                        IOReactorConfig
                                .custom()
                                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                                .build());
        if (credentialsProvider != null) {
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
        return httpClientBuilder;
    }

    private ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        return (response, context) -> 2 * 60 * 1000;
    }

    private InetSocketAddress[] getHttpHosts() {
        List<String> clusterNodes = elasticsearchProperties.getClusterNodes();
        InetSocketAddress[] httpHosts = new InetSocketAddress[clusterNodes.size()];
        for (int i = 0; i < clusterNodes.size(); i++) {
            String[] node = clusterNodes.get(i).split(":");
            httpHosts[i] = new InetSocketAddress(node[0], Convert.toInt(node[1]));
        }
        return httpHosts;
    }

    private String[] getHosts(){
        List<String> clusterNodes = elasticsearchProperties.getClusterNodes();
        return ArrayUtil.toArray(clusterNodes, String.class);
    }
}
