package personal.hdproject.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.store.dao.StoreCategoryRepository;
import personal.hdproject.store.domain.StoreCategory;
import personal.hdproject.store.service.request.StoreCategoryServiceRequest;
import personal.hdproject.store.web.response.StoreCategoryResponse;

@Service
@RequiredArgsConstructor
public class StoreCategoryService {

	private final StoreCategoryRepository storeCategoryRepository;

	public Long createCategory(StoreCategoryServiceRequest request) {
		StoreCategory savedStoreCategory = storeCategoryRepository.save(StoreCategory.toEntity(request));

		return savedStoreCategory.getId();
	}

	public List<StoreCategoryResponse> findAllStoreCategory() {
		return storeCategoryRepository.findAll().stream()
			.map(StoreCategoryResponse::toResponseDto)
			.collect(Collectors.toList());
	}

	public void deleteStoreCategory(Long storeCategoryId) {
		storeCategoryRepository.deleteById(storeCategoryId);
	}
}
