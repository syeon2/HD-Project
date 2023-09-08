package personal.hdproject.member.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMemberServiceRequest {

	private final String email;
	private final String password;
	private final String nickname;
	private final String address;
	private final String phone;

	@Builder
	private CreateMemberServiceRequest(String email, String password, String nickname, String address, String phone) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.address = address;
		this.phone = phone;
	}
}
