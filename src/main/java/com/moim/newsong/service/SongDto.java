package com.moim.newsong.service;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SongDto.java
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
public class SongDto {

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class SearchReq {
		
		@NotBlank
		private String query;
		
		@NotEmpty
		private List<String> searchField;
		
		@NotBlank
		private String preTags;
		@NotBlank
		private String postTags;
		@NotBlank
		private String sort;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchRes {
		private Source source;
		private Highlight highlight;
	
		@Getter
		@Setter
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Source {
			private String songNo;
			private String songTitle;
			private String songContent;
		}
		
		@Getter
		@Setter
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Highlight {
			private String songTitle;
			private String songContent;
		}
	}
}
