package personal.hdproject.item.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.item.service.ItemService;
import personal.hdproject.item.service.request.CreateItemServiceRequest;
import personal.hdproject.item.service.request.UpdateItemServiceRequest;
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
		long itemId = 1L;
		CreateItemRequest request = CreateItemRequest.builder()
			.name("itemA")
			.description("good!!")
			.price(10000)
			.storeId(1L)
			.build();

		given(itemService.createItem(any(CreateItemServiceRequest.class)))
			.willReturn(itemId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/item")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(itemId))
			.andDo(document("item-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING)
						.description("상품 이름"),
					fieldWithPath("description").type(JsonFieldType.STRING)
						.description("상품 설명"),
					fieldWithPath("price").type(JsonFieldType.NUMBER)
						.description("가격"),
					fieldWithPath("storeId").type(JsonFieldType.NUMBER)
						.description("매장 아이디")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("생성된 상품의 아이디")
				)));
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

		given(itemService.findItemById(anyLong())).willReturn(response);

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
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("item-select",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("data.id").type(JsonFieldType.NUMBER)
						.description("상품 아이디"),
					fieldWithPath("data.name").type(JsonFieldType.STRING)
						.description("상품 이름"),
					fieldWithPath("data.description").type(JsonFieldType.STRING)
						.description("상품 설명"),
					fieldWithPath("data.price").type(JsonFieldType.NUMBER)
						.description("상품 가격"),
					fieldWithPath("data.storeId").type(JsonFieldType.NUMBER)
						.description("매장 아이디")
				)));
	}

	@Test
	@DisplayName(value = "매장 아이디를 사용하여 매장의 모든 상품을 조회하는 API를 호출합니다.")
	void findItemsByStoreId() throws Exception {
		// given
		long storeId = 1L;
		long cursorId = 1L;
		int pageSize = 10;

		List<ItemResponse> responses = List.of(
			ItemResponse.builder()
				.id(1L)
				.name("itemA")
				.description("good")
				.price(1000)
				.storeId(storeId)
				.build());

		when(itemService.findItemByStoreId(storeId, cursorId, pageSize)).thenReturn(responses);

		// when  // then
		mockMvc.perform(
				get("/api/v1/item/store/{store_id}", storeId)
					.queryParam("cursor_id", String.valueOf(cursorId))
					.queryParam("page_size", String.valueOf(pageSize))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("item-select-store-id",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("store_id").description("매장 아이디")
				),
				requestParameters(
					parameterWithName("cursor_id").description("페이징 기준 상품 아이디"),
					parameterWithName("page_size").description("페이징 오프셋")
				),
				responseFields(
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("상품 아이디"),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description("상품 이름"),
					fieldWithPath("data[].description").type(JsonFieldType.STRING)
						.description("상품 설명"),
					fieldWithPath("data[].price").type(JsonFieldType.NUMBER)
						.description("상품 가격"),
					fieldWithPath("data[].storeId").type(JsonFieldType.NUMBER)
						.description("상품이 포함되는 매장 아이디")
				)));
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

		given(itemService.updateItem(anyLong(), any(UpdateItemServiceRequest.class)))
			.willReturn(itemId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/item/{item_id}", itemId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(itemId))
			.andDo(document("item-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("item_id").description("상품 아이디")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING)
						.optional()
						.description("상품 이름"),
					fieldWithPath("description").type(JsonFieldType.STRING)
						.optional()
						.description("상품 설명"),
					fieldWithPath("price").type(JsonFieldType.NUMBER)
						.optional()
						.description("상품 가격")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("수정된 상품 아이디")
				)
			));
	}

	@Test
	@DisplayName(value = "상품을 삭제하는 API를 호출합니다.")
	void deleteItem() throws Exception {
		// given
		long itemId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/item/{item_id}", itemId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isNotEmpty())
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("item-delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING)
						.description("삭제 성공 메시지")
				)
			));
	}
}
