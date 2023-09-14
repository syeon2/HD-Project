package personal.hdproject.store.web.request;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.store.service.request.StoreCategoryServiceRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCategoryRequest {

	@NotBlank(message = "카테고리 이름은 필수 값입니다.")
	private String name;

	public StoreCategoryRequest(String name) {
		this.name = name;
	}

	public StoreCategoryServiceRequest toServiceRequest() {
		return new StoreCategoryServiceRequest(this.name);
	}
}
