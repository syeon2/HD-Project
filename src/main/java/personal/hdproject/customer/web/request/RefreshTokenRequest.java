package personal.hdproject.customer.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

	@NotNull(message = "아이디는 필수 값입니다.")
	private final Long id;

	@NotBlank(message = "Refresh Token은 필수 값입니다.")
	private final String refreshToken;

	@Builder
	private RefreshTokenRequest(Long id, String refreshToken) {
		this.id = id;
		this.refreshToken = refreshToken;
	}
}
