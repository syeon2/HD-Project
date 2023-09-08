package personal.hdproject.util.interceptor.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import personal.hdproject.util.jwt.JwtAuthTokenProvider;

@Component
@RequiredArgsConstructor
public class CustomerLoginCheckHandler implements HandlerInterceptor {

	@Value("${jwt.header}")
	private String AUTH_HEADER;

	private final JwtAuthTokenProvider jwtAuthTokenProvider;

	@Override
	public boolean preHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String accessToken = request.getHeader(AUTH_HEADER);

		return jwtAuthTokenProvider.validateAccessToken(accessToken);
	}
}
