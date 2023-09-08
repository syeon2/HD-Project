package personal.hdproject.member.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JWTTokenResponse {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	private JWTTokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JWTTokenResponse toTokenResponse(String accessToken, String refreshToken) {
		return JWTTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
