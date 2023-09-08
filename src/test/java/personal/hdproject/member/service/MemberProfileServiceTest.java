package personal.hdproject.member.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.domain.Member;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.util.encryption.Sha256Util;
import personal.hdproject.util.error.exception.DuplicateEmailException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberProfileServiceTest {

	@Autowired
	private MemberProfileService memberProfileService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	public void after() {
		memberRepository.deleteAll();
	}

	@Test
	@DisplayName(value = "이메일이 이미 존재하는지 검사하고, 비밀번호를 SHA-256으로 암호화하여 새로운 계정을 추가합니다.")
	void join() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		// when
		Long joinedMemberId = memberProfileService.join(createMemberDTO);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember)
				.extracting("email", "nickname", "phone")
				.contains(email, nickname, phone),
			() -> fail("Member should be present")
		);
	}

	@Test
	@DisplayName(value = "비밀번호는 암호화되어 저장합니다. - 기존의 비밀번호와 암호화된 비밀번호는 다릅니다.")
	void join_encryptedPassword() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		// when
		Long joinedMemberId = memberProfileService.join(createMemberDTO);

		// then
		String encryptedPassword = Sha256Util.getEncrypt(password);

		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		assertThat(password).isNotEqualTo(encryptedPassword);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getPassword()).isEqualTo(encryptedPassword),
			() -> fail("Member should be present")
		);
	}

	@Test
	@DisplayName(value = "이메일이 중복되어 예외가 발생합니다.")
	void join_duplicateEmail() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		Long joinMemberId = memberProfileService.join(createMemberDTO);

		Optional<Member> findMemberOptional = memberRepository.findById(joinMemberId);
		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getEmail()).isEqualTo(email),
			() -> fail("Member should be present")
		);

		// when // then
		assertThatThrownBy(() -> memberProfileService.join(createMemberDTO))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	@DisplayName(value = "닉네임을 수정합니다.")
	void changeNickname() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		Long joinMemberId = memberProfileService.join(createMemberDTO);

		// when
		String changedNickname = "changed_nickname";
		memberProfileService.changeNickname(joinMemberId, changedNickname);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinMemberId);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getNickname()).isEqualTo(changedNickname),
			() -> fail("Member should be present")
		);
	}

	@Test
	@DisplayName(value = "전화번호를 수정합니다.")
	void changePhone() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		Long joinMemberId = memberProfileService.join(createMemberDTO);

		// when
		String changedPhone = "99988887777";
		memberProfileService.changePhone(joinMemberId, changedPhone);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinMemberId);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getPhone()).isEqualTo(changedPhone),
			() -> fail("Member should be present")
		);
	}

	@Test
	@DisplayName(value = "소비자 아이디를 삭제합니다.")
	void deleteAccount() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateMemberServiceRequest createMemberDTO = getCreateMemberServiceRequest(email, password, nickname,
			phone);

		Long joinMemberId = memberProfileService.join(createMemberDTO);

		// when
		memberProfileService.deleteAccount(joinMemberId);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinMemberId);

		assertThat(findMemberOptional.isPresent()).isFalse();
	}

	private CreateMemberServiceRequest getCreateMemberServiceRequest(String email, String password, String nickname,
		String phone) {
		return CreateMemberServiceRequest.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.phone(phone)
			.build();
	}
}
