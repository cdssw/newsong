package com.moim.newsong.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CommonConfig.java
 * 
 * @author cdssw
 * @since Apr 12, 2020
 * @description 공통 Config
 *  
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 12, 2020   cdssw            최초 생성
 * </pre>
 */
@Configuration
public class CommonConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
