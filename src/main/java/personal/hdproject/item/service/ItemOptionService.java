package personal.hdproject.item.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.item.dao.ItemOptionRepository;
import personal.hdproject.item.dao.ItemRepository;
import personal.hdproject.item.domain.Item;
import personal.hdproject.item.domain.ItemOption;
import personal.hdproject.item.service.request.CreateItemOptionServiceRequest;
import personal.hdproject.item.service.response.ItemOptionResponse;

@Service
@RequiredArgsConstructor
public class ItemOptionService {

	private final ItemOptionRepository itemOptionRepository;
	private final ItemRepository itemRepository;

	public Long createItemOption(CreateItemOptionServiceRequest request) {
		Optional<Item> findItemOptional = itemRepository.findById(request.getItemId());

		if (findItemOptional.isEmpty()) {
			throw new NoSuchElementException("아이디에 일치하는 상품이 존재하지 않습니다.");
		}

		ItemOption itemOption = itemOptionRepository.save(ItemOption.toEntity(request, findItemOptional.get()));

		return itemOption.getId();
	}

	public List<ItemOptionResponse> findItemOptionByItemId(Long itemId) {
		List<ItemOption> itemOptions = itemOptionRepository.findItemOptionByItemId(itemId);

		return itemOptions.stream()
			.map(ItemOptionResponse::of)
			.collect(Collectors.toList());
	}

	public void deleteOption(Long itemOptionId) {
		itemOptionRepository.deleteById(itemOptionId);
	}
}
