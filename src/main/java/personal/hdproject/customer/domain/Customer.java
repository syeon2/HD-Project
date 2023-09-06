package personal.hdproject.customer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "customer")
@NoArgsConstructor
public class Customer extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id", columnDefinition = "bigint") // unsigned
	private Long id;

	@Column(columnDefinition = "varchar(255)", unique = true)
	private String email;

	@Column(columnDefinition = "char(60)")
	private String password;

	@Column(columnDefinition = "varchar(40)")
	private String nickname;

	@Column(columnDefinition = "varchar(40)", unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(40)")
	private Grade grade;

	// TODO: 주문 테이블 - 연관관계 Mapping

	@Builder
	private Customer(String email, String password, String nickname, String phone, Grade grade) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
		this.grade = grade;
	}
}