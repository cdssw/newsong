package com.moim.newsong.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moim.newsong.repository.NewsongRepository;
import com.moim.newsong.service.SongDto.SearchReq;
import com.moim.newsong.service.SongDto.SearchRes;

import lombok.AllArgsConstructor;

/**
 * NewsongServiceImpl.java
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
@AllArgsConstructor
@Service
public class NewsongServiceImpl implements NewsongService {

	private NewsongRepository newsongRepository;
	
	@Override
	public Page<SearchRes> postSearch(SearchReq req, Pageable pageable) {
		return newsongRepository.search(req, pageable);
	}

	@Override
	public Page<SearchRes> postSearchOne(String songNo, SearchReq req, Pageable pageable) {
		return newsongRepository.searchOne(songNo, req, pageable);
	}

}
