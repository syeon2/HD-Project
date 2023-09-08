package personal.hdproject.member.dao.jpa;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.dao.common.MemberCustomRepository;
import personal.hdproject.member.domain.QMember;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QMember member = QMember.member;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updateNickname(Long memberId, String nickname) {
		return jpaQueryFactory.update(member)
			.set(member.nickname, nickname)
			.set(member.updatedAt, LocalDateTime.now())
			.where(member.id.eq(memberId)).execute();
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updatePhone(Long memberId, String phone) {
		return jpaQueryFactory.update(member)
			.set(member.phone, phone)
			.set(member.updatedAt, LocalDateTime.now())
			.where(member.id.eq(memberId)).execute();
	}
}
