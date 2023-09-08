package personal.hdproject.util.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import personal.hdproject.util.interceptor.handler.CustomerLoginCheckHandler;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final CustomerLoginCheckHandler customerLoginCheckHandler;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customerLoginCheckHandler)
			.excludePathPatterns("/api/v1/customer/sign-in")
			.excludePathPatterns("/api/v1/customer")
			.addPathPatterns("/**");
	}
}
