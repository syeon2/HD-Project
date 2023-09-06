package personal.hdproject.customer.dao.common;

public interface CustomeCustomRepository {

	Long updateNickname(Long customerId, String nickname);

	Long updatePhone(Long customerId, String phone);
}
