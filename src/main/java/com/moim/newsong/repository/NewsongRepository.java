package com.moim.newsong.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.moim.newsong.except.ErrorCode;
import com.moim.newsong.except.NewsongBusinessException;
import com.moim.newsong.service.SongDto;
import com.moim.newsong.service.SongDto.SearchReq;

import lombok.AllArgsConstructor;

/**
 * NewsongRepository.java
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
@Repository
@AllArgsConstructor
public class NewsongRepository {

	private ModelMapper modelMapper;
	private final RestHighLevelClient client;
	
	private static String index;
	@Value("${elasticsearch.index}")
	private void setIndex(String _index) {
		index = _index;
	}
	
	public Page<SongDto.SearchRes> search(SearchReq dto, Pageable pageable) {
		// create builder
		SearchSourceBuilder searchSourceBuilder = createMultiMatchQueryBuilder(dto, pageable);
		
		// create request
		SearchRequest req = createSearchRequest(index, searchSourceBuilder);
		
		// search to es
		SearchResponse res = null;
		try {
			res = client.search(req, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NewsongBusinessException(ErrorCode.SEARCH_FILED_INVALID);
		}
		
		// processing result
		SearchHits hits = res.getHits();
		List<SongDto.SearchRes> list = Arrays.asList(hits.getHits()).stream().map(m -> {
			// source to dto
			Map<String, Object> s = m.getSourceAsMap();
			SongDto.SearchRes.Source source = modelMapper.map(s, SongDto.SearchRes.Source.class);
			
			// highlight to dto
			Map<String, HighlightField> h = m.getHighlightFields();
			Map<String, String> high = new HashMap<String, String>();
			for(Map.Entry<String, HighlightField> elem : h.entrySet()) {
				high.put(elem.getKey(), h.get(elem.getKey()) != null ? h.get(elem.getKey()).getFragments()[0].string() : null);
			}
			SongDto.SearchRes.Highlight highlight = modelMapper.map(high, SongDto.SearchRes.Highlight.class);
			
			// make res
			SongDto.SearchRes hit = SongDto.SearchRes.builder().source(source).highlight(highlight).build();
			return hit;
		}).collect(Collectors.toList());
		
		return new PageImpl<>(list, pageable, hits.getTotalHits());
	}
	
	public Page<SongDto.SearchRes> searchOne(String songNo, SearchReq dto, Pageable pageable) {
		// create builder
		SearchSourceBuilder searchSourceBuilder = createBoolQueryBuilder(songNo, dto, pageable);
		
		// create request
		SearchRequest req = createSearchRequest(index, searchSourceBuilder);
		
		// search to es
		SearchResponse res = null;
		try {
			res = client.search(req, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NewsongBusinessException(ErrorCode.SEARCH_FILED_INVALID);
		}
		
		// processing result
		SearchHits hits = res.getHits();
		List<SongDto.SearchRes> list = Arrays.asList(hits.getHits()).stream().map(m -> {
			// source to dto
			Map<String, Object> s = m.getSourceAsMap();
			SongDto.SearchRes.Source source = modelMapper.map(s, SongDto.SearchRes.Source.class);
			
			// highlight to dto
			Map<String, HighlightField> h = m.getHighlightFields();
			Map<String, String> high = new HashMap<String, String>();
			for(Map.Entry<String, HighlightField> elem : h.entrySet()) {
				high.put(elem.getKey(), h.get(elem.getKey()) != null ? h.get(elem.getKey()).getFragments()[0].string() : null);
			}
			SongDto.SearchRes.Highlight highlight = modelMapper.map(high, SongDto.SearchRes.Highlight.class);
			
			// make res
			SongDto.SearchRes hit = SongDto.SearchRes.builder().source(source).highlight(highlight).build();
			return hit;
		}).collect(Collectors.toList());
		
		return new PageImpl<>(list, pageable, hits.getTotalHits());
	}
	
	private SearchSourceBuilder createMultiMatchQueryBuilder(SearchReq dto, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		// highlight field add
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		for(String field : dto.getSearchField()) {
			HighlightBuilder.Field f = new HighlightBuilder.Field(field);
			highlightBuilder.field(f);
		}
		
		// custom tag add
		highlightBuilder.preTags(dto.getPreTags());
		highlightBuilder.postTags(dto.getPostTags());
		searchSourceBuilder
			.from(pageable.getPageNumber() * pageable.getPageSize())
			.size(pageable.getPageSize())
			.highlighter(highlightBuilder)
			;

		// sort는 optional
		if(dto.getSort() != null) {
			searchSourceBuilder.sort(SortBuilders.fieldSort(dto.getSort()).order(SortOrder.ASC));
		}
		
		// 수신받은 검색필드를 array로 설정하여 querybuilder 생성
		MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(dto.getQuery(), dto.getSearchField().toArray(new String[dto.getSearchField().size()])).type(Type.PHRASE);
		
		// builder에 query 설정
		searchSourceBuilder.query(multiMatchQueryBuilder);
		
		return searchSourceBuilder;
	}
	
	private SearchSourceBuilder createBoolQueryBuilder(String songNo, SearchReq dto, Pageable pageable) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		// highlight field add
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		for(String field : dto.getSearchField()) {
			HighlightBuilder.Field f = new HighlightBuilder.Field(field);
			highlightBuilder.field(f);
		}
		
		// custom tag add
		highlightBuilder.preTags(dto.getPreTags());
		highlightBuilder.postTags(dto.getPostTags());
		searchSourceBuilder
			.from(pageable.getPageNumber() * pageable.getPageSize())
			.size(pageable.getPageSize())
			.highlighter(highlightBuilder)
			;

		searchSourceBuilder.sort(SortBuilders.fieldSort("seqNo").order(SortOrder.ASC));
		
		// match query 생성
		MatchQueryBuilder match = QueryBuilders.matchQuery("songNo", songNo);
		
		// multi match query 생성
		MultiMatchQueryBuilder multiMatch = QueryBuilders.multiMatchQuery(dto.getQuery(), dto.getSearchField().toArray(new String[dto.getSearchField().size()])).type(Type.PHRASE);
		
		// filter 생성
		TermQueryBuilder filter = QueryBuilders.termQuery("songNo", songNo);
		
		// boolQuery 생성
		BoolQueryBuilder bool = QueryBuilders.boolQuery().should(match).should(multiMatch).filter(filter);
		
		// builder에 query 설정
		searchSourceBuilder.query(bool);
		
		return searchSourceBuilder;
	}
	
	private SearchRequest createSearchRequest(String indexName, SearchSourceBuilder searchSourceBuilder) {
		SearchRequest searchRequest = new SearchRequest("posts");
		searchRequest.indices(indexName).source(searchSourceBuilder);
		return searchRequest;
	}
}
