package personal.hdproject.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", columnDefinition = "bigint") // unsigned
	private Long id;

	@Column(columnDefinition = "varchar(255)", unique = true)
	private String email;

	@Column(columnDefinition = "char(64)")
	private String password;

	@Column(columnDefinition = "varchar(40)")
	private String nickname;

	@Column(columnDefinition = "varchar(40)", unique = true)
	private String phone;

	@Column(columnDefinition = "varchar(255)")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(40)")
	private Grade grade;

	// TODO: 주문 테이블 - 연관관계 Mapping

	@Builder
	private Member(String email, String password, String nickname, String phone, String address, Grade grade) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
		this.address = address;
		this.grade = grade;
	}

	public static Member toEntity(CreateMemberServiceRequest request, String encryptedPassword) {
		return Member.builder()
			.email(request.getEmail())
			.password(encryptedPassword)
			.nickname(request.getNickname())
			.phone(request.getPhone())
			.grade(Grade.BASIC)
			.build();
	}
}
