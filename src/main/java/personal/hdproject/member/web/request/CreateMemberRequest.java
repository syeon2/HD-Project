package personal.hdproject.member.web.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;

@Getter
public class CreateMemberRequest {

	@Email(message = "이메일 형식으로 작성해주세요.(ex. xxxx@xxxx.com")
	@NotBlank(message = "이메일은 필수 값입니다.")
	private final String email;

	@NotBlank(message = "비밀번호는 필수 값입나다.")
	@Length(min = 8, message = "비밀번호는 최소 8자 이상 작성해주세요")
	private final String password;

	@NotBlank(message = "닉네임은 공백을 허용하지 않습니다.")
	private final String nickname;

	@NotBlank(message = "전화번호는 필수 값입니다.")
	@Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11자리의 숫자만 입력 가능합니다.")
	private final String phone;

	@Builder
	private CreateMemberRequest(String email, String password, String nickname, String phone) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
	}

	public CreateMemberServiceRequest toServiceRequest() {
		return CreateMemberServiceRequest.builder()
			.email(this.email)
			.password(this.password)
			.nickname(this.nickname)
			.phone(this.phone)
			.build();
	}
}
