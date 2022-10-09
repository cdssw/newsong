package com.moim.newsong.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * ElasticsearchConfig.java
 * 
 * @author cdssw
 * @since 2022. 10. 7.
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * 2022. 10. 7.    cdssw            최초 생성
 * </pre>
 */
/**
* 
*/
@EnableElasticsearchRepositories
@Configuration
public class ElasticsearchConfig {

	@Value("${elasticsearch.host}")
	private String host;
	
	@Value("${elasticsearch.port}")
	private int port;
	
	@Value("${elasticsearch.tls}")
	private String tls;
	
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, tls)));
	}
	
	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchRestTemplate(restHighLevelClient());
	}
}
