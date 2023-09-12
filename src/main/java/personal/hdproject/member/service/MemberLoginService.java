package personal.hdproject.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.domain.Member;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.member.service.response.JwtTokenResponse;
import personal.hdproject.member.service.response.SignInMemberResponse;
import personal.hdproject.member.web.request.RefreshTokenRequest;
import personal.hdproject.util.error.exception.LoginException;
import personal.hdproject.util.jwt.JwtAuthTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

	private final MemberRepository memberRepository;
	private final JwtAuthTokenProvider jwtAuthTokenProvider;

	public SignInMemberResponse login(SignInMemberServiceRequest request) {
		Member member = checkAccountAndGetMember(request.getEmail(), request.getPassword());
		JwtTokenResponse jwtToken = jwtAuthTokenProvider.generateToken(member.getId());

		return SignInMemberResponse.toResponse(member, jwtToken);
	}

	public void logout(String accessToken) {
		jwtAuthTokenProvider.removeRefreshTokenInStorage(accessToken);
	}

	public JwtTokenResponse validateAndRenewToken(RefreshTokenRequest request) {
		return jwtAuthTokenProvider.generateRenewToken(request.getId(), request.getRefreshToken());
	}

	private Member checkAccountAndGetMember(String email, String password) {
		Member member = checkEmailAndGetMember(email);
		member.checkPassword(member, password);

		return member;
	}

	private Member checkEmailAndGetMember(String email) {
		Optional<Member> findMemberByEmailOptional = memberRepository.findMemberByEmail(email);

		return findMemberByEmailOptional
			.orElseThrow(() -> new LoginException("아이디가 존재하지 않습니다."));
	}
}
