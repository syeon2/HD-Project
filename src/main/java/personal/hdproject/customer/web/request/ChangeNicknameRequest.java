package personal.hdproject.customer.web.request;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChangeNicknameRequest {

	@NotBlank(message = "닉네임은 공백을 허용하지 않습니다.")
	private String nickname;
}
