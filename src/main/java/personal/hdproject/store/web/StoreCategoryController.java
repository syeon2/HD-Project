package personal.hdproject.store.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.store.service.StoreCategoryService;
import personal.hdproject.store.web.request.StoreCategoryRequest;
import personal.hdproject.store.web.response.StoreCategoryResponse;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class StoreCategoryController {

	private final StoreCategoryService storeCategoryService;

	@PostMapping("/api/v1/store-category")
	public ApiResult<Long> createStoreCategory(@Valid @RequestBody StoreCategoryRequest request) {
		Long createdCategoryId = storeCategoryService.createCategory(request.toServiceRequest());

		return ApiResult.onSuccess(createdCategoryId);
	}

	@GetMapping("/api/v1/store-category")
	public ApiResult<List<StoreCategoryResponse>> findAllStoreCategory() {
		List<StoreCategoryResponse> findAllStoreCategories = storeCategoryService.findAllStoreCategory();

		return ApiResult.onSuccess(findAllStoreCategories);
	}

	@DeleteMapping("/api/v1/store-category/{id}")
	public ApiResult<String> deleteStoreCategory(@PathVariable Long id) {
		storeCategoryService.deleteStoreCategory(id);

		return ApiResult.onSuccess("매장 카테고리가 성공적으로 삭제되었습니다.");
	}
}
