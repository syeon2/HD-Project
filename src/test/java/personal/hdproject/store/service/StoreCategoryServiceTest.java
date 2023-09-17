package personal.hdproject.store.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.store.dao.StoreCategoryRepository;
import personal.hdproject.store.domain.StoreCategory;
import personal.hdproject.store.service.request.StoreCategoryServiceRequest;

@SpringBootTest
class StoreCategoryServiceTest {

	@Autowired
	private StoreCategoryService storeCategoryService;

	@Autowired
	private StoreCategoryRepository storeCategoryRepository;

	@AfterEach
	void after() {
		storeCategoryRepository.deleteAll();
	}

	@Test
	@DisplayName(value = "매장 카테고리를 생성합니다.")
	void createStoreCategory() {
		// given
		String category = "category1";
		StoreCategoryServiceRequest request = new StoreCategoryServiceRequest(category);

		// when
		Long createdCategoryId = storeCategoryService.createCategory(request);

		// then
		Optional<StoreCategory> findStoreCategory = storeCategoryRepository.findById(createdCategoryId);

		findStoreCategory.ifPresentOrElse(
			storeCategory -> assertThat(storeCategory.getName()).isEqualTo(category),
			() -> fail("Store Category is not exist")
		);
	}

	@Test
	@DisplayName(value = "모든 매장 카테고리를 조회합니다.")
	void findAllStoreCategory() {
		// given
		String category1 = "category1";
		String category2 = "category2";
		StoreCategoryServiceRequest request1 = new StoreCategoryServiceRequest(category1);
		StoreCategoryServiceRequest request2 = new StoreCategoryServiceRequest(category2);

		// when
		Long createdCategory1Id = storeCategoryService.createCategory(request1);
		Long createdCategory2Id = storeCategoryService.createCategory(request2);

		// then
		List<StoreCategory> findCategories = storeCategoryRepository.findAll();

		assertThat(findCategories).hasSize(2);
		assertThat(findCategories).extracting("name")
			.containsExactlyInAnyOrder(
				category1,
				category2
			);
	}

	@Test
	@DisplayName(value = "매장 카테고리를 삭제합니다.")
	void deleteStoreCategory() {
		// given
		String category = "category1";
		StoreCategory storeCategory = new StoreCategory(category);

		StoreCategory saveStoreCategory = storeCategoryRepository.save(storeCategory);

		assertThat(storeCategoryRepository.findAll()).hasSize(1);

		// when
		storeCategoryService.deleteStoreCategory(saveStoreCategory.getId());

		// then
		assertThat(storeCategoryRepository.findAll()).hasSize(0);
	}
}
