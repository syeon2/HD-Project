package personal.hdproject.store.dao.jpa;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;
import personal.hdproject.store.dao.common.StoreCustomRepository;
import personal.hdproject.store.dao.common.UpdateStoreDto;
import personal.hdproject.store.domain.QStore;
import personal.hdproject.store.domain.Store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QStore store = QStore.store;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updateStore(Long id, UpdateStoreDto updateStoreDto) {
		JPAUpdateClause update = jpaQueryFactory.update(store);

		setUpdateFields(updateStoreDto, update);

		return update.where(store.id.eq(id)).execute();
	}

	@Transactional
	@Override
	public List<Store> findByStoreCategoryIdLessThanOrderByIdDesc(Long storeCategoryId, Long cursorId, Integer offset) {
		return jpaQueryFactory.selectFrom(store)
			.where(
				store.storeCategory.id.eq(storeCategoryId),
				store.id.lt(cursorId)
			)
			.limit(offset)
			.orderBy(store.id.desc())
			.fetch();
	}

	private void setUpdateFields(UpdateStoreDto updateStoreDto, JPAUpdateClause update) {
		ifNonNull(updateStoreDto.getName(), value -> update.set(store.name, value));
		ifNonNull(updateStoreDto.getPhone(), value -> update.set(store.phone, value));
		ifNonNull(updateStoreDto.getAddress(), value -> update.set(store.address, value));
		ifNonNull(updateStoreDto.getStoreCategoryId(), value -> update.set(store.storeCategory.id, value));
	}

	private <T> void ifNonNull(T value, Consumer<T> action) {
		if (value != null) {
			action.accept(value);
		}
	}
}
