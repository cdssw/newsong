package com.moim.newsong.except;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode.java
 * 
 * @author cdssw
 * @since Apr 18, 2020
 * @description 사용자 정의 ErrorCode
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 18, 2020   cdssw            최초 생성
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

	INPUT_VALUE_INVALID("V_00001", "입력값이 올바르지 않습니다."),
	SEARCH_FILED_INVALID("V_00002", "검색에 실패했습니다."),
	;
	
	private final String code;
	private final String message;
}
