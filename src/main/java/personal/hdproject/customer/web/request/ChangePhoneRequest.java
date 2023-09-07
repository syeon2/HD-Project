package personal.hdproject.customer.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChangePhoneRequest {

	@NotBlank(message = "전화번호는 필수 값입니다.")
	@Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11자리의 숫자만 입력 가능합니다.")
	private String phone;
}
