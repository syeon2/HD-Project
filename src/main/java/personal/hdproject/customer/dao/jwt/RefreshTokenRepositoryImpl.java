package personal.hdproject.customer.dao.jwt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import personal.hdproject.customer.dao.RefreshTokenRepository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public RefreshTokenRepositoryImpl(
		@Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void save(Long id, String refreshToken) {
		redisTemplate.opsForValue().set(id.toString(), refreshToken);
	}

	@Override
	public void delete(String id) {
		redisTemplate.opsForValue().getAndDelete(id);
	}

	public String getRefreshToken(Long id) {
		return redisTemplate.opsForValue().get(id.toString());
	}
}
