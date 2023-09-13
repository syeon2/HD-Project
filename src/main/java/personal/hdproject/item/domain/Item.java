package personal.hdproject.item.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.store.domain.Store;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	@Column(columnDefinition = "varchar(255)")
	private String name;

	@Column(columnDefinition = "blob")
	private String description;

	@Column(columnDefinition = "integer")
	private Integer price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<Option> optionList = new ArrayList<>();

	@Builder
	private Item(String name, String description, Integer price, Store store) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.store = store;
	}

	public void addOption(Option option) {
		optionList.add(option);
		option.setItem(this);
	}
}
