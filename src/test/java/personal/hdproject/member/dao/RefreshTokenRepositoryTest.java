package personal.hdproject.member.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RefreshTokenRepositoryTest {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private final RedisTemplate<String, String> redisTemplate;

	public RefreshTokenRepositoryTest(
		@Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@AfterEach
	void after() {
		Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
	}

	@Test
	@DisplayName("Refresh Token을 저장합니다.")
	void save() {
		// given
		Long memberId = 1L;
		String refreshToken = "hello";

		// when
		refreshTokenRepository.save(memberId, refreshToken);

		// then
		String findRefreshToken = redisTemplate.opsForValue().get(memberId.toString());

		assertThat(refreshToken).isEqualTo(findRefreshToken);
	}

	@Test
	@DisplayName("Refresh Token을 삭제합니다.")
	void delete() {
		// given
		Long memberId = 1L;
		String refreshToken = "hello";
		refreshTokenRepository.save(memberId, refreshToken);

		String findRefreshToken = redisTemplate.opsForValue().get(memberId.toString());

		assertThat(refreshToken).isEqualTo(findRefreshToken);

		// when
		refreshTokenRepository.delete(memberId.toString());

		// then
		String deletedRefreshToken = redisTemplate.opsForValue().get(memberId.toString());

		assertThat(deletedRefreshToken).isNull();
	}

	@Test
	@DisplayName("Refresh Token을 조회합니다.")
	void getRefreshToken() {
		// given
		Long memberId = 1L;
		String refreshToken = "hello";
		refreshTokenRepository.save(memberId, refreshToken);

		// when
		String findRefreshToken = refreshTokenRepository.getRefreshToken(memberId);

		// then
		assertThat(refreshToken).isEqualTo(findRefreshToken);
	}
}
