package personal.hdproject.member.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtTokenResponse {

	private final String accessToken;
	private final String refreshToken;

	@Builder
	private JwtTokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JwtTokenResponse toTokenResponse(String accessToken, String refreshToken) {
		return JwtTokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
