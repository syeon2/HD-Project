package personal.hdproject.store.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.store.service.StoreService;
import personal.hdproject.store.web.request.CreateStoreRequest;
import personal.hdproject.store.web.response.StoreResponse;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping("/api/v1/store")
	public ApiResult<Long> createStore(@Valid @RequestBody CreateStoreRequest request) {
		Long createdStoreId = storeService.createStore(request.toServiceRequest());

		return ApiResult.onSuccess(createdStoreId);
	}

	@GetMapping("/api/v1/store/{id}")
	public ApiResult<StoreResponse> findStore(@PathVariable Long id) {
		StoreResponse response = storeService.findStoreById(id);

		return ApiResult.onSuccess(response);
	}

	@GetMapping("/api/v1/store/categories/{storeCategoryId}")
	public ApiResult<List<StoreResponse>> findStoresByCategoryId(
		@PathVariable Long storeCategoryId,
		@RequestParam Long cursorId,
		@RequestParam Integer pageSize
	) {
		List<StoreResponse> storeResponses = storeService.findStoreByStoreCategoryId(storeCategoryId, cursorId,
			pageSize);

		return ApiResult.onSuccess(storeResponses);
	}

	@DeleteMapping("/api/v1/store/{id}")
	public ApiResult<String> deleteStore(@PathVariable Long id) {
		storeService.deleteStore(id);

		return ApiResult.onSuccess("매장이 성공적으로 삭제되었습니다.");
	}
}
