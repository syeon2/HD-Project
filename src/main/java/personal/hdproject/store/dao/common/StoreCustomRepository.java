package personal.hdproject.store.dao.common;

import java.util.List;

import personal.hdproject.store.domain.Store;

public interface StoreCustomRepository {

	Long updateStore(Long id, UpdateStoreDto updateStoreDto);

	List<Store> findByStoreCategoryIdLessThanOrderByIdDesc(Long storeCategoryId, Long cursorId, Integer offset);
}
