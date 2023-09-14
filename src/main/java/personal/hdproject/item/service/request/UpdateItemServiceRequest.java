package personal.hdproject.item.service.request;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.item.dao.common.UpdateItemDto;

@Getter
public class UpdateItemServiceRequest {

	private String name;
	private String description;
	private Integer price;

	@Builder
	private UpdateItemServiceRequest(String name, String description, Integer price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public UpdateItemDto toUpdateDto() {
		return UpdateItemDto.builder()
			.name(this.name)
			.description(this.description)
			.price(this.price)
			.build();
	}
}
