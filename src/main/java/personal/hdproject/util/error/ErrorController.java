package personal.hdproject.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import personal.hdproject.util.error.exception.DuplicateEmailException;
import personal.hdproject.util.error.exception.ExpiredAccessTokenException;
import personal.hdproject.util.error.exception.LoginException;
import personal.hdproject.util.error.exception.TokenValidationException;
import personal.hdproject.util.wrapper.ApiResult;

@RestControllerAdvice
public class ErrorController {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ExpiredAccessTokenException.class)
	public ApiResult<Void> handlerExpiredAccessTokenRequest(ExpiredAccessTokenException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(TokenValidationException.class)
	public ApiResult<Void> handlerTokenValidationRequest(TokenValidationException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateEmailException.class)
	public ApiResult<Void> handlerDuplicateEmailRequest(DuplicateEmailException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(LoginException.class)
	public ApiResult<Void> handlerLoginException(LoginException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResult<Void> handlerValidationRequest(MethodArgumentNotValidException exception) {
		return ApiResult.onFailure(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ApiResult<Void> handlerBadRequest(RuntimeException exception) {
		return ApiResult.onFailure(exception.getLocalizedMessage());
	}
}
