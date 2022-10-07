package com.moim.newsong.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moim.newsong.service.SongDto;

import lombok.extern.slf4j.Slf4j;

/**
 * MeetControllerTest.java
 * 
 * @author cdssw
 * @since Apr 18, 2020
 * @description  
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 18, 2020   cdssw            최초 생성
 * </pre>
 */
@RunWith(SpringRunner.class)
@WebMvcTest // controller 관련 bean만 로딩
@Slf4j
public class NewsongControllerTest extends BaseControllerTest {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext ctx;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private SongDto.SearchReq dto = null;
	private SongDto.SearchRes res1;
	private SongDto.SearchRes res2;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글깨짐 방지 처리
				.alwaysDo(print()) // 항상 결과 print
				.build();
		
		dto = SongDto.SearchReq.builder()
				.query("우리")
				.searchField(Arrays.asList("songTitle", "songContent"))
				.preTags("<b>")
				.postTags("</b>")
				.sort("songNo")
				.build();
		
		res1 = SongDto.SearchRes.builder()
				.source(SongDto.SearchRes.Source.builder().songNo("001").songTitle("Title1").songContent("Content1").build())
				.highlight(SongDto.SearchRes.Highlight.builder().songTitle("<b>Title1</b>").songContent("<b>Content1</b>").build())
				.build();
		
		res2 = SongDto.SearchRes.builder()
				.source(SongDto.SearchRes.Source.builder().songNo("001").songTitle("Title2").songContent("Content2").build())
				.highlight(SongDto.SearchRes.Highlight.builder().songTitle("<b>Title2</b>").songContent("<b>Content2</b>").build())
				.build();
	}
	
	// 테스트 하는것은 dto를 가지고 controller 호출이 잘 되는지 확인
	@Test
	public void testPostMultiMatchQuery() throws Exception {
		// given
		List<SongDto.SearchRes> list = Arrays.asList(res1, res2);
		Pageable pageable = PageRequest.of(0, 10);
		Page<SongDto.SearchRes> pageList = new PageImpl<>(list, pageable, list.size());
		given(newsongService.postMultiMatchQuery(any(), any())).willReturn(pageList);
		
		// when
		final MvcResult result = mvc.perform(post("/search")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andReturn();
		
		// assert
		log.info(result.getRequest().getContentAsString());		
	}
}
