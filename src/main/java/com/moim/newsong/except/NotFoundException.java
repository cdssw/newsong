package com.moim.newsong.except;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NotFoundException.java
 * 
 * @author cdssw
 * @since Apr 21, 2020
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 21, 2020   cdssw            최초 생성
 * </pre>
 */
@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8333470763693361638L;

	private ErrorCode errorCode; // rest 결과처리를 위한 ErrorCode Enum
}
