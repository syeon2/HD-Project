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
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "option_id")
	private Long id;

	@Column(columnDefinition = "varchar(255)")
	private String name;

	@Column(columnDefinition = "blob")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Builder
	private Option(String name, String description, Item item) {
		this.name = name;
		this.description = description;
		this.item = item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
