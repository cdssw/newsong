package com.moim.newsong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moim.newsong.service.SongDto;

/**
 * MeetIntegrationTest.java
 * 
 * @author cdssw
 * @since Apr 28, 2020
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 28, 2020   cdssw            최초 생성
 * </pre>
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class NewsongIntegrationMockTest {

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext ctx;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
				.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글깨짐 방지 처리
				.alwaysDo(print()) // 결과 항상 print
				.build();
	}
	
	@Test
	public void testPostMultiMatchQuery() throws Exception {
		// given
		SongDto.SearchReq dto = SongDto.SearchReq.builder()
				.query("우리")
				.searchField(Arrays.asList("songTitle", "songContent"))
				.preTags("<b>")
				.postTags("</b>")
				.sort("songNo")
				.build();
		
		// when
		final MvcResult result = mvc.perform(post("/search?page=5&size=10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andReturn();
		
		// then
		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
	}
}
