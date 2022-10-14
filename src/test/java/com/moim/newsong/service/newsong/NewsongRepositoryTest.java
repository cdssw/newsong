package com.moim.newsong.service.newsong;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.moim.newsong.repository.NewsongRepository;
import com.moim.newsong.service.SongDto;

/**
 * NewsongRepositoryTest.java
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
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NewsongRepositoryTest {

	@Autowired
	private RestHighLevelClient client;
	
	private NewsongRepository newsongRepository;
	private SongDto.SearchReq dto = null;
	
	@Before
	public void setUp() {
		ModelMapper modelMapper = new ModelMapper();
		newsongRepository = new NewsongRepository(modelMapper, client);
		dto = SongDto.SearchReq.builder()
				.query("우리")
				.searchField(Arrays.asList("songTitle", "songContent"))
				.preTags("<b>")
				.postTags("</b>")
				.build();
	}
	
	@Test
	public void testSearch() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		
		// when
		Page<SongDto.SearchRes> res = newsongRepository.search(dto, pageable);
		
		// then
		assertEquals(res.getTotalElements(), 991L);
	}
	
	@Test
	public void testSearchOne() {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		String songNo = "001";
		
		// when
		Page<SongDto.SearchRes> res = newsongRepository.searchOne(songNo, dto, pageable);
		
		// then
		assertEquals(res.getTotalElements(), 12L);
	}
}
