package personal.hdproject.member.dao.common;

public interface MemberCustomRepository {

	Long updateNickname(Long memberId, String nickname);

	Long updatePhone(Long memberId, String phone);
}
