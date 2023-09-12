package personal.hdproject.util.interceptor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import personal.hdproject.util.interceptor.handler.MemberLoginCheckHandler;

@Profile("product")
@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final MemberLoginCheckHandler memberLoginCheckHandler;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(memberLoginCheckHandler)
			.excludePathPatterns("/api/v1/auth/sign-in")
			.excludePathPatterns("/api/v1/auth/refresh-token")
			.excludePathPatterns("/api/v1/member")
			.addPathPatterns("/**");
	}
}
