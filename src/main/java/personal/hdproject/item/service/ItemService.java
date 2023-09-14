package personal.hdproject.item.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.item.dao.ItemRepository;
import personal.hdproject.item.domain.Item;
import personal.hdproject.item.service.request.CreateItemServiceRequest;
import personal.hdproject.item.service.request.UpdateItemServiceRequest;
import personal.hdproject.item.service.response.ItemResponse;
import personal.hdproject.store.dao.StoreRepository;
import personal.hdproject.store.domain.Store;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final StoreRepository storeRepository;

	public Long createItem(CreateItemServiceRequest request) {
		Optional<Store> findStoreOption = storeRepository.findById(request.getStoreId());

		if (findStoreOption.isEmpty()) {
			throw new NoSuchElementException("매장을 찾을 수 없습니다.");
		}

		Item savedItem = itemRepository.save(Item.toEntity(request, findStoreOption.get()));

		return savedItem.getId();
	}

	public ItemResponse findItemById(Long itemId) {
		Optional<Item> findItemOptional = itemRepository.findById(itemId);

		if (findItemOptional.isEmpty()) {
			throw new NoSuchElementException("상품이 존재하지 않습니다.");
		}

		return ItemResponse.toResponseDto(findItemOptional.get());
	}

	public List<ItemResponse> findItemByStoreId(Long storeId, Long cursorId, Integer offset) {
		List<Item> items = itemRepository.findItemByStoreIdLessThanOrderByItemIdDesc(storeId, cursorId, offset);

		return items.stream()
			.map(ItemResponse::toResponseDto)
			.collect(Collectors.toList());
	}

	public Long updateItem(Long itemId, UpdateItemServiceRequest request) {
		return itemRepository.updateItem(itemId, request.toUpdateDto());
	}

	public void deleteItem(Long itemId) {
		itemRepository.deleteById(itemId);
	}
}
