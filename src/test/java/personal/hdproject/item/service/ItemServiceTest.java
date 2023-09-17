package personal.hdproject.item.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.item.dao.ItemRepository;
import personal.hdproject.item.domain.Item;
import personal.hdproject.item.service.request.CreateItemServiceRequest;
import personal.hdproject.item.service.request.UpdateItemServiceRequest;
import personal.hdproject.item.service.response.ItemResponse;
import personal.hdproject.store.dao.StoreCategoryRepository;
import personal.hdproject.store.dao.StoreRepository;
import personal.hdproject.store.domain.Store;
import personal.hdproject.store.domain.StoreCategory;

@SpringBootTest
class ItemServiceTest {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StoreCategoryRepository storeCategoryRepository;

	@AfterEach
	void after() {
		itemRepository.deleteAll();
		storeRepository.deleteAll();
		storeCategoryRepository.deleteAll();
	}

	@Test
	@DisplayName(value = "상품을 추가합니다.")
	void createItem() {
		// given
		StoreCategory savedStoreCategory = storeCategoryRepository.save(new StoreCategory("categoryA"));

		Store store = Store.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategory(savedStoreCategory)
			.build();

		Store savedStore = storeRepository.save(store);

		String itemName = "itemA";
		CreateItemServiceRequest request = CreateItemServiceRequest.builder()
			.name(itemName)
			.description("itemA 입니다.")
			.price(10000)
			.storeId(savedStore.getId())
			.build();

		// when
		Long savedItemId = itemService.createItem(request);

		// then
		assertThat(savedItemId).isNotNull();

		Optional<Item> findItemOptional = itemRepository.findById(savedItemId);

		findItemOptional.ifPresentOrElse(
			item -> assertThat(item.getName()).isEqualTo(itemName),
			() -> fail("상품 추가에 실패하였습니다.")
		);
	}

	@Test
	@DisplayName(value = "상품 아이디를 사용하여 상품을 조회합니다.")
	void findItemById() {
		// given
		StoreCategory savedStoreCategory = storeCategoryRepository.save(new StoreCategory("categoryA"));

		Store store = Store.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategory(savedStoreCategory)
			.build();

		Store savedStore = storeRepository.save(store);

		String itemName = "itemA";
		String description = "itemA 입니다.";
		int price = 10000;

		CreateItemServiceRequest request = CreateItemServiceRequest.builder()
			.name(itemName)
			.description(description)
			.price(price)
			.storeId(savedStore.getId())
			.build();

		Long savedItemId = itemService.createItem(request);

		// when
		ItemResponse findItemResponse = itemService.findItemById(savedItemId);

		// then
		assertThat(findItemResponse)
			.extracting("name", "description", "price", "storeId")
			.contains(itemName, description, price, savedStore.getId());
	}

	@Test
	@DisplayName(value = "매장 아이디를 사용하여 커서 페이지네이션 방식으로 상품을 조회합니다.")
	void findItemByStoreId() {
		// given
		StoreCategory savedStoreCategory = storeCategoryRepository.save(new StoreCategory("categoryA"));

		Store store = Store.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategory(savedStoreCategory)
			.build();

		Store savedStore = storeRepository.save(store);

		String itemName = "itemA";
		String description = "itemA 입니다.";
		int price = 10000;

		CreateItemServiceRequest request1 = CreateItemServiceRequest.builder()
			.name(itemName)
			.description(description)
			.price(price)
			.storeId(savedStore.getId())
			.build();

		CreateItemServiceRequest request2 = CreateItemServiceRequest.builder()
			.name("itemB")
			.description("itemB 입니다.")
			.price(20000)
			.storeId(savedStore.getId())
			.build();

		Long savedItem1Id = itemService.createItem(request1);
		Long savedItem2Id = itemService.createItem(request2);

		// when
		List<ItemResponse> itemResponses = itemService.findItemByStoreId(savedStore.getId(), savedItem2Id + 1, 10);

		// then
		assertThat(itemResponses).hasSize(2);

		assertThat(itemResponses)
			.extracting("name", "description", "price", "storeId")
			.containsExactlyInAnyOrder(
				tuple("itemA", "itemA 입니다.", 10000, savedStore.getId()),
				tuple("itemB", "itemB 입니다.", 20000, savedStore.getId())
			);
	}

	@Test
	@DisplayName(value = "상품 정보를 수정합니다.")
	void updateItem() {
		// given
		StoreCategory savedStoreCategory = storeCategoryRepository.save(new StoreCategory("categoryA"));

		Store store = Store.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategory(savedStoreCategory)
			.build();

		Store savedStore = storeRepository.save(store);

		String itemName = "itemA";
		String description = "itemA 입니다.";
		int price = 10000;

		CreateItemServiceRequest request = CreateItemServiceRequest.builder()
			.name(itemName)
			.description(description)
			.price(price)
			.storeId(savedStore.getId())
			.build();

		Long savedItem1Id = itemService.createItem(request);

		// when
		UpdateItemServiceRequest updateRequest = UpdateItemServiceRequest.builder()
			.name("itemB")
			.description("itemB 입니다.")
			.price(20000)
			.build();

		itemRepository.updateItem(savedItem1Id, updateRequest.toUpdateDto());

		// then
		ItemResponse findItem = itemService.findItemById(savedItem1Id);

		assertThat(findItem).extracting("name", "description", "price", "storeId")
			.contains("itemB", "itemB 입니다.", 20000, savedStore.getId());
	}

	@Test
	@DisplayName(value = "상품 아이디를 사용하여 상품을 삭제합니다.")
	void deleteItem() {

	}
}
