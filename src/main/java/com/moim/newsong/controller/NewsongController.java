package com.moim.newsong.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moim.newsong.service.NewsongService;
import com.moim.newsong.service.SongDto;

import lombok.AllArgsConstructor;

/**
 * NewsongController.java
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
@RestController
@RequestMapping
public class NewsongController {

	private NewsongService newsongService;
	
	@PostMapping("/search")
	public Page<SongDto.SearchRes> postSearch(@RequestBody @Valid SongDto.SearchReq dto, Pageable pageable) {
		return newsongService.postSearch(dto, pageable);
	}
	
	@PostMapping("/search/{songNo}")
	public Page<SongDto.SearchRes> postSearchOne(@PathVariable final String songNo, @RequestBody @Valid SongDto.SearchReq dto, Pageable pageable) {
		return newsongService.postSearchOne(songNo, dto, pageable);
	}
}
