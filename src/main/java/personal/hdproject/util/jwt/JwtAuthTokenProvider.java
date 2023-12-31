package personal.hdproject.util.jwt;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import personal.hdproject.member.dao.jwt.RefreshTokenRepositoryImpl;
import personal.hdproject.member.service.response.JwtTokenResponse;
import personal.hdproject.util.error.exception.ExpiredAccessTokenException;
import personal.hdproject.util.error.exception.TokenValidationException;
import personal.hdproject.util.generator.TimeGenerator;

@Component
@RequiredArgsConstructor
public class JwtAuthTokenProvider {

	@Value("${jwt.secret_key}")
	private String secretKey;

	@Value("${jwt.at-expired_minutes}")
	private Integer accessTokenExpiredMinutes;

	@Value("${jwt.rt-expired_days}")
	private Integer refreshTokenExpiredDays;

	private static final String PAYLOAD_KEY = "id";

	private final RefreshTokenRepositoryImpl refreshTokenRepository;

	public JwtTokenResponse generateToken(Long memberId) {
		return JwtTokenResponse.builder()
			.accessToken(createAccessToken(memberId))
			.refreshToken(createRefreshToken(memberId))
			.build();
	}

	public JwtTokenResponse generateRenewToken(Long memberId, String refreshToken) {
		try {
			validateRefreshToken(memberId, refreshToken);

			return JwtTokenResponse.builder()
				.accessToken(createAccessToken(memberId))
				.build();
		} catch (ExpiredJwtException exception) {
			String newAccessToken = createAccessToken(memberId);
			String newRefreshToken = createRefreshToken(memberId);

			return JwtTokenResponse.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
		} catch (Exception exception) {
			throw new TokenValidationException("유효하지 않은 토큰입니다.");
		}
	}

	public void removeRefreshTokenInStorage(String accessToken) {
		try {
			Claims body = Jwts.parserBuilder()
				.setSigningKey(generateHashingKey())
				.build()
				.parseClaimsJws(removeBearer(accessToken))
				.getBody();

			String id = body.get(PAYLOAD_KEY, String.class);
			refreshTokenRepository.delete(id);
		} catch (Exception exception) {
			throw new TokenValidationException("유효하지 않은 토큰입니다.");
		}
	}

	public boolean validateAccessToken(String accessToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(generateHashingKey())
				.build()
				.parseClaimsJws(removeBearer(accessToken))
				.getBody();

			return true;
		} catch (ExpiredJwtException exception) {
			throw new ExpiredAccessTokenException("Access Token 토큰 만료");
		} catch (Exception exception) {
			throw new TokenValidationException("유효하지 않은 토큰입니다.");
		}
	}

	private String createAccessToken(Long id) {
		Claims claims = Jwts.claims();
		claims.put(PAYLOAD_KEY, id.toString());

		return Jwts.builder()
			.setHeaderParam("type", "JWT")
			.setClaims(claims)
			.signWith(generateHashingKey(), SignatureAlgorithm.HS256)
			.setExpiration(TimeGenerator.getMinuteInFuture(accessTokenExpiredMinutes))
			.compact();
	}

	private String createRefreshToken(Long id) {
		Claims claims = Jwts.claims();
		claims.put(PAYLOAD_KEY, id.toString());

		String refreshToken = Jwts.builder()
			.setHeaderParam("type", "JWT")
			.setClaims(claims)
			.signWith(generateHashingKey(), SignatureAlgorithm.HS256)
			.setExpiration(TimeGenerator.getDayInFuture(refreshTokenExpiredDays))
			.compact();

		refreshTokenRepository.save(id, refreshToken);

		return refreshToken;
	}

	private void validateRefreshToken(Long memberId, String refreshToken) {
		String originRefreshToken = refreshTokenRepository.getRefreshToken(memberId);

		if (!originRefreshToken.equals(refreshToken)) {
			throw new TokenValidationException("유효하지 않는 Refresh Token입니다..");
		}

		Claims body = Jwts.parserBuilder()
			.setSigningKey(generateHashingKey())
			.build()
			.parseClaimsJws(refreshToken)
			.getBody();

		String id = body.get(PAYLOAD_KEY, String.class);

		if (!memberId.toString().equals(id)) {
			throw new TokenValidationException("유효하지 않는 Refresh Token입니다.");
		}
	}

	private String removeBearer(String token) {
		return token.replaceFirst("Bearer ", "").trim();
	}

	private SecretKey generateHashingKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
}
