package personal.hdproject.item.web;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.item.service.ItemService;
import personal.hdproject.item.service.response.ItemResponse;
import personal.hdproject.item.web.request.CreateItemRequest;
import personal.hdproject.item.web.request.UpdateItemRequest;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest extends BaseTestConfig {

	@MockBean
	private ItemService itemService;

	@Test
	@DisplayName(value = "상품을 생성하는 API를 호출합니다.")
	void createItem() throws Exception {
		// given
		CreateItemRequest request = CreateItemRequest.builder()
			.name("itemA")
			.description("good!!")
			.price(10000)
			.storeId(1L)
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/item")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "상품 아이디를 사용하여 상품을 조회하는 API를 호출합니다.")
	void findItemById() throws Exception {
		// given
		long itemId = 1L;
		String itemName = "itemA";
		String description = "good";
		int price = 10000;
		long storeId = 1L;

		ItemResponse response = ItemResponse.builder()
			.id(1L)
			.name(itemName)
			.description(description)
			.price(price)
			.storeId(storeId)
			.build();

		when(itemService.findItemById(itemId)).thenReturn(response);

		// when  // then
		mockMvc.perform(
				get("/api/v1/item/" + itemId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name").value(itemName))
			.andExpect(jsonPath("$.data.description").value(description))
			.andExpect(jsonPath("$.data.price").value(price))
			.andExpect(jsonPath("$.data.storeId").value(storeId))
			.andExpect(jsonPath("$.message").doesNotExist());
	}

	@Test
	@DisplayName(value = "매장 아이디를 사용하여 매장의 모든 상품을 조회하는 API를 호출합니다.")
	void findItemsByStoreId() throws Exception {
		// given
		long storeId = 1L;
		long cursorId = 1L;
		int pageSize = 10;

		List<ItemResponse> responses = List.of();

		when(itemService.findItemByStoreId(storeId, cursorId, pageSize)).thenReturn(responses);

		// when  // then
		mockMvc.perform(
				get("/api/v1/item/store/" + storeId)
					.queryParam("cursor_id", String.valueOf(cursorId))
					.queryParam("page_size", String.valueOf(pageSize))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.message").doesNotExist());
	}

	@Test
	@DisplayName(value = "상품 정보를 수정하는 API를 호출합니다.")
	void updateItem() throws Exception {
		// given
		long itemId = 1L;
		UpdateItemRequest request = UpdateItemRequest.builder()
			.name("itemB")
			.description("bad")
			.price(10000)
			.build();

		when(itemService.updateItem(itemId, request.toServiceRequest())).thenReturn(itemId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/item/" + itemId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "상품을 삭제하는 API를 호출합니다.")
	void deleteItem() throws Exception {
		// given
		long itemId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/item/" + itemId)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
}
