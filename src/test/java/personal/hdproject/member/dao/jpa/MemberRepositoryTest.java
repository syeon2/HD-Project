package personal.hdproject.member.dao.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.domain.Grade;
import personal.hdproject.member.domain.Member;

@SpringBootTest
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName(value = "소비자의 닉네임을 수정합니다.")
	void updateNickname() {
		// given
		String nickname = "nickname1";

		Member member = createMember("1235@gmail.com", "12345678", nickname, "00011112222");
		Member savedMember = memberRepository.save(member);

		assertThat(savedMember.getNickname()).isEqualTo(nickname);

		// when
		Long memberId = savedMember.getId();

		String fixedNickname = "fixNickname";
		memberRepository.updateNickname(memberId, fixedNickname);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(memberId);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getNickname()).isEqualTo(fixedNickname),
			() -> fail("Member should be present")
		);
	}

	@Test
	@DisplayName(value = "소비자의 전화번호를 수정합니다.")
	void updatePhone() {
		// given
		String phone = "00011112222";

		Member member = createMember("1234@gmail.com", "1234", "nickname", phone);
		Member savedMember = memberRepository.save(member);

		assertThat(savedMember.getPhone()).isEqualTo(phone);

		// when
		Long memberId = savedMember.getId();

		String fixPhone = "22233334444";
		memberRepository.updatePhone(memberId, fixPhone);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(memberId);

		findMemberOptional.ifPresentOrElse(
			findMember -> assertThat(findMember.getPhone()).isEqualTo(fixPhone),
			() -> fail("Member should be present")
		);
	}

	private Member createMember(String mail, String password, String nickname1, String phone) {
		return Member.builder()
			.email(mail)
			.password(password)
			.nickname(nickname1)
			.phone(phone)
			.grade(Grade.BASIC)
			.build();
	}
}
