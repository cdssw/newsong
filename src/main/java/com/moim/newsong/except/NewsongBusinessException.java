package com.moim.newsong.except;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NewsongBusinessException.java
 * 
 * @author cdssw
 * @since Apr 29, 2020
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 29, 2020   cdssw            최초 생성
 * </pre>
 */
@AllArgsConstructor
@Getter
public class NewsongBusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2891950653809573137L;
	private ErrorCode errorCode; // rest 결과처리를 위한 ErrorCode Enum
}
