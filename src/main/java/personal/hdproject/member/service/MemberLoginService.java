package personal.hdproject.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.domain.Member;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.member.service.response.JWTTokenResponse;
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
		Member member = checkAccountAndGetMember(request.getEmail(), request.getEncryptedPassword());
		JWTTokenResponse jwtToken = jwtAuthTokenProvider.generateToken(member.getId());

		return SignInMemberResponse.toResponse(member, jwtToken);
	}

	public void logout(String accessToken) {
		jwtAuthTokenProvider.removeRefreshTokenInStorage(accessToken);
	}

	public JWTTokenResponse validateAndRenewToken(RefreshTokenRequest request) {
		return jwtAuthTokenProvider.generateRenewToken(request.getId(), request.getRefreshToken());
	}

	private Member checkAccountAndGetMember(String email, String encryptedPassword) {
		Optional<Member> findMemberByEmailOptional = memberRepository.findMemberByEmail(email);

		Member member = checkEmailAndGetMember(findMemberByEmailOptional);
		checkPassword(member, encryptedPassword);

		return member;
	}

	private Member checkEmailAndGetMember(Optional<Member> findMemberByEmailOptional) {
		return findMemberByEmailOptional
			.orElseThrow(() -> new LoginException("아이디가 존재하지 않습니다."));
	}

	private void checkPassword(Member member, String encryptedPassword) {
		if (!member.getPassword().equals(encryptedPassword)) {
			throw new LoginException("비밀번호가 일치하지 않습니다.");
		}
	}
}
