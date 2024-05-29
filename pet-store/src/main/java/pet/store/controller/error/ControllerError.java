package pet.store.controller.error;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerError {
	private enum LogStatus {
		STACK_TRACE, MESSAGE_ONLY
	}

	@Data
	private class ExceptionMessage {
		private String message;
		private String statusReason;
		private int statuseCode;
		private String timeStamp;
		private String uri;
	}

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExceptionMessage handleIllegalStateException(IllegalStateException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.BAD_REQUEST, webRequest, LogStatus.MESSAGE_ONLY);
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public ExceptionMessage handleUnsupportedOperationException(UnsupportedOperationException ex,
			WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.METHOD_NOT_ALLOWED, webRequest, LogStatus.MESSAGE_ONLY);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ExceptionMessage handleNoSuchElementException(UnsupportedOperationException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.NOT_FOUND, webRequest, LogStatus.MESSAGE_ONLY);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ExceptionMessage handleDuplicateKeyException(DuplicateKeyException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.CONFLICT, webRequest, LogStatus.MESSAGE_ONLY);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionMessage handleException(Exception ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, webRequest, LogStatus.STACK_TRACE);
	}

	@ResponseBody
	public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
		log.error("Error: {}", ex.getMessage());
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("message", ex.toString());
		return errorResponse;
	}

	@SuppressWarnings("unused")
	private ExceptionMessage buildExceptionMessage(Exception ex, HttpStatus status, WebRequest webRequest,
			LogStatus logStatus) {
		String message = ex.toString();
		String statusReason = status.getReasonPhrase();
		int statusCode = status.value();
		String uri = null;
		String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

		if (webRequest instanceof ServletWebRequest swr) {
			uri = swr.getRequest().getRequestURI();
		}
		return null;
	}
}