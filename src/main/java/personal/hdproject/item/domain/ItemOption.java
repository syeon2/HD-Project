package personal.hdproject.item.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.item.service.request.CreateItemOptionServiceRequest;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "item_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "item_option_id")
	private Long id;

	@Column(columnDefinition = "varchar(255)")
	private String name;

	@Column(columnDefinition = "blob")
	private String description;

	@Column(columnDefinition = "integer")
	private Integer price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Builder
	private ItemOption(String name, String description, Integer price, Item item) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.item = item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public static ItemOption toEntity(CreateItemOptionServiceRequest request, Item item) {
		return ItemOption.builder()
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.item(item)
			.build();
	}
}
