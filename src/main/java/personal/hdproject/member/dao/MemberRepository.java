package personal.hdproject.member.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.member.dao.common.MemberCustomRepository;
import personal.hdproject.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

	Optional<Member> findMemberByEmail(String email);
}
