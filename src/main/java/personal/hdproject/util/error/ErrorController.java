package personal.hdproject.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import personal.hdproject.util.wrapper.ApiResult;

@RestControllerAdvice
public class ErrorController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ApiResult<Void> handlerBadRequest(RuntimeException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}
}
