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
import personal.hdproject.util.encryption.EncryptedSourceDto;
import personal.hdproject.util.encryption.Sha256Util;
import personal.hdproject.util.error.exception.LoginException;
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

	@Column(columnDefinition = "varchar(20)")
	private String salt;

	// TODO: 주문 테이블 - 연관관계 Mapping

	@Builder
	private Member(String email, String password, String nickname, String phone, String address, Grade grade,
		String salt) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.phone = phone;
		this.address = address;
		this.grade = grade;
		this.salt = salt;
	}

	public static Member toEntity(CreateMemberServiceRequest request) {
		EncryptedSourceDto encryptDto = Sha256Util.getEncryptDto(request.getPassword());

		return Member.builder()
			.email(request.getEmail())
			.password(encryptDto.getEncryptedSource())
			.nickname(request.getNickname())
			.phone(request.getPhone())
			.grade(Grade.BASIC)
			.address(request.getAddress())
			.salt(encryptDto.getSalt())
			.build();
	}

	public void checkPassword(Member member, String password) {
		String encryptedSource = Sha256Util.getEncryptedSourceWithSalt(password, this.salt);

		if (!member.getPassword().equals(encryptedSource)) {
			throw new LoginException("비밀번호가 일치하지 않습니다.");
		}
	}
}
