package personal.hdproject.util.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisRefreshTokenConfig {

	@Value("${redis.refresh_token.host}")
	private String host;

	@Value("${redis.refresh_token.port}")
	private Integer port;

	@Bean
	@Qualifier("refreshTokenRedisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier("refreshTokenRedisTemplate")
	public RedisTemplate<String, String> refreshTokenRedisTemplate(
		@Qualifier("refreshTokenRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory
	) {
		RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		return redisTemplate;
	}
}
