package com.moim.newsong.service.newsong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.moim.newsong.repository.NewsongRepository;
import com.moim.newsong.service.NewsongServiceImpl;
import com.moim.newsong.service.SongDto;
import com.moim.newsong.service.SongDto.SearchRes;

/**
 * NewsongServiceImplTest.java
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
@RunWith(MockitoJUnitRunner.class)
public class NewsongServiceImplTest {

	// 테스트 하고자 하는 class
	private NewsongServiceImpl newsongServiceImpl;
	
	@Mock private NewsongRepository newsongRepository;
	private SongDto.SearchReq dto = null;
	
	@Before
	public void setUp() {
		newsongServiceImpl = new NewsongServiceImpl(newsongRepository);
		dto = SongDto.SearchReq.builder()
				.query("우리")
				.searchField(Arrays.asList("songTitle", "songContent"))
				.preTags("<b>")
				.postTags("</b>")
				.build();
	}
	
	@Test
	public void testPostSearch() {
		// given
		SongDto.SearchRes m1 = mock(SongDto.SearchRes.class);
		SongDto.SearchRes m2 = mock(SongDto.SearchRes.class);
		
		List<SearchRes> list = Arrays.asList(m1, m2);
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<SearchRes> pageList = new PageImpl<>(list, pageable, list.size());
		given(newsongRepository.search(any(), any())).willReturn(pageList);
		
		// when
		Page<SongDto.SearchRes> res = newsongServiceImpl.postSearch(dto, pageable);
		
		// then
		assertEquals(res.getTotalElements(), 2L);
	}
	
	@Test
	public void testPostSearchOne() {
		// given
		SongDto.SearchRes m1 = mock(SongDto.SearchRes.class);
		SongDto.SearchRes m2 = mock(SongDto.SearchRes.class);
		
		List<SearchRes> list = Arrays.asList(m1, m2);
		
		String songNo = "001";
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<SearchRes> pageList = new PageImpl<>(list, pageable, list.size());
		given(newsongRepository.searchOne(any(), any(), any())).willReturn(pageList);
		
		// when
		Page<SongDto.SearchRes> res = newsongServiceImpl.postSearchOne(songNo, dto, pageable);
		
		// then
		assertEquals(res.getTotalElements(), 2L);
	}
}
