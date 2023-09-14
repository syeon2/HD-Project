package personal.hdproject.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.store.service.request.StoreCategoryServiceRequest;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "store_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCategory extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "store_category_id")
	private Long id;

	@Column(columnDefinition = "varchar(40)")
	private String name;

	public StoreCategory(String name) {
		this.name = name;
	}

	public static StoreCategory toEntity(StoreCategoryServiceRequest request) {
		return new StoreCategory(request.getName());
	}
}
