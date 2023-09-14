package personal.hdproject.item.dao.common;

import java.util.List;

import personal.hdproject.item.domain.Item;

public interface ItemCustomRepository {

	Long updateItem(Long itemId, UpdateItemDto updateItemDto);

	List<Item> findItemByStoreIdLessThanOrderByItemIdDesc(Long storeId, Long cursorId, Integer offset);
}
