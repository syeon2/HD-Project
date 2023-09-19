package personal.hdproject.member.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.domain.Member;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.util.error.exception.DuplicateEmailException;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

	private final MemberRepository memberRepository;

	public Long join(CreateMemberServiceRequest request) {
		if (isEmailAlreadyRegistered(request.getEmail())) {
			throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
		}

		Member savedMember = memberRepository.save(Member.toEntity(request));

		return savedMember.getId();
	}

	public Long changeNickname(Long memberId, String nickname) {
		memberRepository.updateNickname(memberId, nickname);

		return memberId;
	}

	public Long changePhone(Long memberId, String phone) {
		memberRepository.updatePhone(memberId, phone);

		return memberId;
	}

	public void deleteAccount(Long memberId) {
		memberRepository.deleteById(memberId);
	}

	public Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
	}

	private boolean isEmailAlreadyRegistered(String email) {
		Optional<Member> findCustomerOptional = memberRepository.findMemberByEmail(email);

		return findCustomerOptional.isPresent();
	}
}
