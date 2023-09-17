package personal.hdproject.item.dao.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateItemDto {

	private String name;
	private String description;
	private Integer price;

	@Builder
	private UpdateItemDto(String name, String description, Integer price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}
}
