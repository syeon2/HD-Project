package personal.hdproject.item.dao.jpa;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;
import personal.hdproject.item.dao.common.ItemCustomRepository;
import personal.hdproject.item.dao.common.UpdateItemDto;
import personal.hdproject.item.domain.Item;
import personal.hdproject.item.domain.QItem;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QItem item = QItem.item;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updateItem(Long itemId, UpdateItemDto updateItemDto) {
		JPAUpdateClause update = jpaQueryFactory.update(item);

		setUpdateFields(update, updateItemDto);

		return update.where(item.id.eq(itemId)).execute();
	}

	@Override
	public List<Item> findItemByStoreIdLessThanOrderByItemIdDesc(Long storeId, Long cursorId, Integer offset) {
		return jpaQueryFactory.selectFrom(item)
			.where(
				item.store.id.eq(storeId),
				item.id.lt(cursorId)
			)
			.limit(offset)
			.orderBy(item.id.desc())
			.fetch();
	}

	private void setUpdateFields(JPAUpdateClause update, UpdateItemDto updateItemDto) {
		ifNonNull(updateItemDto.getName(), value -> update.set(item.name, value));
		ifNonNull(updateItemDto.getDescription(), value -> update.set(item.description, value));
		ifNonNull(updateItemDto.getPrice(), value -> update.set(item.price, value));
	}

	private <T> void ifNonNull(T value, Consumer<T> action) {
		if (value != null) {
			action.accept(value);
		}
	}
}
