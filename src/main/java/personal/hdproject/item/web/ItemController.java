package personal.hdproject.item.web;

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
import personal.hdproject.item.service.ItemService;
import personal.hdproject.item.service.response.ItemResponse;
import personal.hdproject.item.web.request.CreateItemRequest;
import personal.hdproject.item.web.request.UpdateItemRequest;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping("/api/v1/item")
	public ApiResult<Long> createItem(@Valid @RequestBody CreateItemRequest request) {
		Long itemId = itemService.createItem(request.toServiceRequest());

		return ApiResult.onSuccess(itemId);
	}

	@GetMapping("/api/v1/item/{itemId}")
	private ApiResult<ItemResponse> findItemById(@PathVariable Long itemId) {
		ItemResponse itemResponse = itemService.findItemById(itemId);

		return ApiResult.onSuccess(itemResponse);
	}

	@GetMapping("/api/v1/item/store/{storeId}")
	private ApiResult<List<ItemResponse>> findItemByStoreId(
		@PathVariable Long storeId,
		@RequestParam("cursor_id") Long cursorId,
		@RequestParam("page_size") Integer pageSize
	) {
		List<ItemResponse> itemResponses = itemService.findItemByStoreId(storeId, cursorId, pageSize);

		return ApiResult.onSuccess(itemResponses);
	}

	@PostMapping("/api/v1/item/{itemId}")
	public ApiResult<Long> updateItem(@PathVariable Long itemId, @Valid @RequestBody UpdateItemRequest request) {
		Long updatedItemId = itemService.updateItem(itemId, request.toServiceRequest());

		return ApiResult.onSuccess(updatedItemId);
	}

	@DeleteMapping("/api/v1/item/{itemId}")
	public ApiResult<String> deleteItem(@PathVariable Long itemId) {
		itemService.deleteItem(itemId);

		return ApiResult.onSuccess("상품이 성공적으로 삭제되었습니다.");
	}
}
