package personal.hdproject.customer.dao.jpa;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import personal.hdproject.customer.dao.common.CustomerCustomRepository;
import personal.hdproject.customer.domain.QCustomer;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QCustomer customer = QCustomer.customer;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updateNickname(Long customerId, String nickname) {
		return jpaQueryFactory.update(customer)
			.set(customer.nickname, nickname)
			.set(customer.updatedAt, LocalDateTime.now())
			.where(customer.id.eq(customerId)).execute();
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updatePhone(Long customerId, String phone) {
		return jpaQueryFactory.update(customer)
			.set(customer.phone, phone)
			.set(customer.updatedAt, LocalDateTime.now())
			.where(customer.id.eq(customerId)).execute();
	}
}
