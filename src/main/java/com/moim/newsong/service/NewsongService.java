package com.moim.newsong.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * NewsongService.java
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
public interface NewsongService {
	
	public Page<SongDto.SearchRes> postMultiMatchQuery(SongDto.SearchReq req, Pageable pageable);

}
