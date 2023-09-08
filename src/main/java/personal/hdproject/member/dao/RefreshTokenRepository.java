package personal.hdproject.member.dao;

public interface RefreshTokenRepository {

	void save(Long id, String refreshToken);

	void delete(String id);

	String getRefreshToken(Long id);
}
