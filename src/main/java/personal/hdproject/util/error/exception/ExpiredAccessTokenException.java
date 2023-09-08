package personal.hdproject.util.error.exception;

public class ExpiredAccessTokenException extends RuntimeException {
	public ExpiredAccessTokenException(String message) {
		super(message);
	}
}
