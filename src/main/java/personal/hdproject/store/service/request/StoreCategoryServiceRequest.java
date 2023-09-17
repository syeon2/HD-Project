package personal.hdproject.store.service.request;

import lombok.Getter;

@Getter
public class StoreCategoryServiceRequest {

	private final String name;

	public StoreCategoryServiceRequest(String name) {
		this.name = name;
	}
}
