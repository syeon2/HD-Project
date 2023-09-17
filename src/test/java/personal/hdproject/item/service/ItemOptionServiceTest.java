package personal.hdproject.item.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.item.dao.ItemOptionRepository;
import personal.hdproject.item.dao.ItemRepository;
import personal.hdproject.item.domain.Item;
import personal.hdproject.item.domain.ItemOption;
import personal.hdproject.item.service.request.CreateItemOptionServiceRequest;
import personal.hdproject.item.service.response.ItemOptionResponse;
import personal.hdproject.store.dao.StoreCategoryRepository;
import personal.hdproject.store.dao.StoreRepository;
import personal.hdproject.store.domain.Store;
import personal.hdproject.store.domain.StoreCategory;

@SpringBootTest
class ItemOptionServiceTest {

	@Autowired
	private ItemOptionService itemOptionService;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private StoreCategoryRepository storeCategoryRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@DisplayName(value = "상품 옵션을 추가합니다.")
	void createItemOption() {
		// given
		Item savedItem = getItem();

		String itemOptionName = "itemOptionA";
		CreateItemOptionServiceRequest request = createItemOptionServiceRequest(itemOptionName, savedItem);

		// when
		Long savedItemOption = itemOptionService.createItemOption(request);

		// then
		Optional<ItemOption> findItemOption = itemOptionRepository.findById(savedItemOption);

		findItemOption.ifPresentOrElse(
			itemOption -> assertThat(itemOption.getName()).isEqualTo(itemOptionName),
			() -> fail("상품 옵션이 저장되지 않았습니다.")
		);
	}

	@Test
	@DisplayName(value = "상품 아이디를 사용하여 상품 옵션을 모두 조회합니다.")
	void finaAllItemOptionByItemId() {
		// given
		Item savedItem = getItem();

		String itemOptionName1 = "itemOptionA";
		String itemOptionName2 = "itemOptionB";

		CreateItemOptionServiceRequest request1 = createItemOptionServiceRequest(itemOptionName1, savedItem);
		CreateItemOptionServiceRequest request2 = createItemOptionServiceRequest(itemOptionName2, savedItem);

		Long savedItemOption1 = itemOptionService.createItemOption(request1);
		Long savedItemOption2 = itemOptionService.createItemOption(request2);

		// when
		List<ItemOptionResponse> findItemOptionResponses = itemOptionService.findItemOptionByItemId(savedItem.getId());

		// then
		assertThat(findItemOptionResponses).hasSize(2);
	}

	@Test
	@DisplayName(value = "상품 옵션을 삭제합니다.")
	void deleteItemOption() {
		// given
		Item savedItem = getItem();

		String itemOptionName = "itemOptionA";
		CreateItemOptionServiceRequest request = createItemOptionServiceRequest(itemOptionName, savedItem);

		Long createdItemOptionId = itemOptionService.createItemOption(request);

		// when
		itemOptionService.deleteOption(createdItemOptionId);

		// then
		Optional<ItemOption> findItemOptionOptional = itemOptionRepository.findById(createdItemOptionId);

		assertThat(findItemOptionOptional).isEmpty();
	}

	private CreateItemOptionServiceRequest createItemOptionServiceRequest(String itemOptionName1, Item savedItem) {
		return CreateItemOptionServiceRequest.builder()
			.name(itemOptionName1)
			.description("good")
			.price(1000)
			.itemId(savedItem.getId())
			.build();
	}

	private Item getItem() {
		StoreCategory savedStoreCategory = storeCategoryRepository.save(new StoreCategory("categoryA"));

		Store store = Store.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategory(savedStoreCategory)
			.build();

		Store savedStore = storeRepository.save(store);

		String itemName = "itemA";
		Item item = Item.builder()
			.name(itemName)
			.description("itemA 입니다.")
			.price(10000)
			.store(savedStore)
			.build();

		return itemRepository.save(item);
	}
}
