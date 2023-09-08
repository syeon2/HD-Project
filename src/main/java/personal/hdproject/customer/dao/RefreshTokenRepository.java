package personal.hdproject.customer.dao;

public interface RefreshTokenRepository {

	void save(Long id, String refreshToken);

	void delete(String id);

	String getRefreshToken(Long id);
}
