package personal.hdproject.store.domain;

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
import personal.hdproject.member.domain.Member;
import personal.hdproject.store.service.request.CreateStoreServiceRequest;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "store_id")
	private Long id;

	@Column(columnDefinition = "varchar(40)")
	private String name;

	@Column(columnDefinition = "varchar(40)")
	private String phone;

	@Column(columnDefinition = "varchar(255)")
	private String address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_category_id")
	private StoreCategory storeCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	private Store(String name, String phone, String address, StoreCategory storeCategory, Member member) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategory = storeCategory;
		this.member = member;
	}

	public static Store toEntity(CreateStoreServiceRequest request, StoreCategory storeCategory, Member member) {
		return Store.builder()
			.name(request.getName())
			.phone(request.getPhone())
			.address(request.getAddress())
			.storeCategory(storeCategory)
			.member(member)
			.build();
	}
}
