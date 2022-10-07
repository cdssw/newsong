package com.moim.newsong.except;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RestExceptionHandler.java
 * 
 * @author cdssw
 * @since Apr 18, 2020
 * @description 예외처리
 * 
 * <pre>
 * since          author           description
 * ===========    =============    ===========================
 * Apr 18, 2020   cdssw            최초 생성
 * </pre>
 */
@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {

	// 일반적인 Exception 처리 class
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ExceptRes {
		private String timeStamp;
		private String code;
		private String message;
		private String path;
		
		@Builder
		public ExceptRes(String timeStamp, String code, String message, String path) {
			this.timeStamp = timeStamp;
			this.code = code;
			this.message = message;
			this.path = path;
		}
	}
	
	// 입력값 검증 Exception 처리 class
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	private static class ExceptValidRes {
		private String timeStamp;
		private String code;
		private String message;
		private String path;
		private List<FieldError> errors;		
		
		@Builder		
		public ExceptValidRes(String timeStamp, String code, String message, String path, List<FieldError> errors) {
			this.timeStamp = timeStamp;
			this.code = code;
			this.message = message;
			this.path = path;
			this.errors = errors;
		}

		@Getter
		@NoArgsConstructor(access = AccessLevel.PROTECTED)
		private static class FieldError {
			private String field;
			private String value;
			private String reason;
			
			@Builder
			public FieldError(String field, String value, String reason) {
				this.field = field;
				this.value = value;
				this.reason = reason;
			}
		}
	}
	
	// 입력값 검증 오류 전체 리스트 생성
	private ExceptValidRes validError(HttpServletRequest req, List<org.springframework.validation.FieldError> errors) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ExceptValidRes.builder()
				.code(ErrorCode.INPUT_VALUE_INVALID.getCode())
				.message(ErrorCode.INPUT_VALUE_INVALID.getMessage())
				.path(req.getRequestURI())
				.timeStamp(LocalDateTime.now().format(formatter))
				.errors(errors.parallelStream()
						.map(error -> ExceptValidRes.FieldError.builder()
								.field(error.getField())
								.value((String) error.getRejectedValue())
								.reason(error.getDefaultMessage())
								.build())
						.collect(Collectors.toList()))
				.build();
	}
	
	// 입력값 검증 예외처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ExceptValidRes handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
		final BindingResult bindingResult = e.getBindingResult();
		final List<org.springframework.validation.FieldError> errors = bindingResult.getFieldErrors();
		return this.validError(req, errors);
	}
	
	// NotFound (UserNotFound, ElementNotFound) 처리
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ExceptRes handleNotFoundException(HttpServletRequest req, NotFoundException e) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ExceptRes.builder()
				.code(e.getErrorCode().getCode())
				.message(e.getErrorCode().getMessage())
				.path(req.getRequestURI())
				.timeStamp(LocalDateTime.now().format(formatter))
				.build();
	}
	
	// NewsongBusinessException 처리
	@ExceptionHandler(NewsongBusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ExceptRes handleMeetBusinessException(HttpServletRequest req, NewsongBusinessException e) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ExceptRes.builder()
				.code(e.getErrorCode().getCode())
				.message(e.getErrorCode().getMessage())
				.path(req.getRequestURI())
				.timeStamp(LocalDateTime.now().format(formatter))
				.build();
	}
}
