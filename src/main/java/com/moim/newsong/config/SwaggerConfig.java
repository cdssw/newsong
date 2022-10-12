package com.moim.newsong.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig.java
 * 
 * @author cdssw
 * @since Apr 10, 2020
 * @description Swagger 설정 
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 10, 2020   cdssw            최초 생성
 * </pre>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	private final TypeResolver typeResolver = null;
	
	@Value("${swagger.host}")
	private final String host = null;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				// pageable custom 처리
				.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(Page.class)))
				.apiInfo(swaggerInfo())
				.host(host)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.moim.newsong.controller"))
				.paths(PathSelectors.any()).build()
				.securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.newArrayList(apiKey())) // authorization header 추가
				.useDefaultResponseMessages(false);
	}
	
	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder()
				.title("Moim API Documentation")
				.description("Newsong Service Document")
				.license("Andrew")
				.licenseUrl("conerule.duckdns.org")
				.version("1")
				.build();
	}
	
	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any())
				.build();
	}
	
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
	}
	
	// pageable custom 처리
	@Getter
	@Setter
	@ApiModel
	private static class Page {
		
		@ApiModelProperty(value = "페이지 번호(0..N)")
		private Integer page;
		
		@ApiModelProperty(value = "페이지 크기", allowableValues = "range[0, 100]")
		private Integer size;
		
		@ApiModelProperty(value = "정렬(사용법: 컬럼명,ASC|DESC)")
		private List<String> sort;
	}
}
