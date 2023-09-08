package personal.hdproject.customer.dao;

public interface RefreshTokenRepository {

	void save(Long id, String refreshToken);

	String getRefreshToken(Long id);
}
