package personal.hdproject.store.web;

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
import personal.hdproject.store.service.StoreService;
import personal.hdproject.store.service.request.CreateStoreServiceRequest;
import personal.hdproject.store.service.request.UpdateStoreServiceRequest;
import personal.hdproject.store.web.request.CreateStoreRequest;
import personal.hdproject.store.web.request.UpdateStoreRequest;
import personal.hdproject.store.web.response.StoreResponse;

@WebMvcTest(controllers = StoreController.class)
class StoreControllerTest extends BaseTestConfig {

	@MockBean
	private StoreService storeService;

	@Test
	@DisplayName(value = "매장을 생성하는 API를 호출합니다.")
	void createStore() throws Exception {
		// given
		long storeId = 1L;
		CreateStoreRequest request = CreateStoreRequest.builder()
			.name("good")
			.phone("00011112222")
			.address("서울")
			.storeCategoryId(1L)
			.memberId(1L)
			.build();

		given(storeService.createStore(any(CreateStoreServiceRequest.class)))
			.willReturn(storeId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/store")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("store-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING)
						.description("매장 이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("매장 전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("매장 주소"),
					fieldWithPath("storeCategoryId").type(JsonFieldType.NUMBER)
						.description("매장 카테고리 아이디"),
					fieldWithPath("memberId").type(JsonFieldType.NUMBER)
						.description("매장 생성하는 회원 아이디")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("생성된 매장 아이디")
				)));
	}

	@Test
	@DisplayName(value = "매장 아이디를 사용하여 매장 정보를 조회하는 API를 호출합니다.")
	void findStore() throws Exception {
		// given
		long storeId = 1L;
		String name = "good";
		String phone = "00011112222";
		String address = "서울";

		StoreResponse response = StoreResponse.builder()
			.id(storeId)
			.name(name)
			.phone(phone)
			.address(address)
			.build();

		when(storeService.findStoreById(storeId)).thenReturn(response);

		// when   // then
		mockMvc.perform(
				get("/api/v1/store/{storeId}", storeId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(storeId))
			.andExpect(jsonPath("$.data.name").value(name))
			.andExpect(jsonPath("$.data.phone").value(phone))
			.andExpect(jsonPath("$.data.address").value(address))
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("store-by-store-id-select",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("storeId").description("매장 아이디")
				),
				responseFields(
					fieldWithPath("data.id").type(JsonFieldType.NUMBER)
						.description("매장 아이디"),
					fieldWithPath("data.name").type(JsonFieldType.STRING)
						.description("매장 이름"),
					fieldWithPath("data.phone").type(JsonFieldType.STRING)
						.description("매장 전화번호"),
					fieldWithPath("data.address").type(JsonFieldType.STRING)
						.description("매장 주소")
				)
			));
	}

	@Test
	@DisplayName(value = "매장 카테고리 아이디를 사용하여 매장 리스트를 조회하는 API를 호출합니다.")
	void findStoreByStoreCategoryId() throws Exception {
		// given
		Long storeCategoryId = 1L;
		Long cursorId = 1L;
		Integer pageSize = 10;

		List<StoreResponse> responses = List.of(StoreResponse.builder()
			.id(1L).name("storeA").phone("00011112222").address("seoul").build());

		when(storeService.findStoreByStoreCategoryId(storeCategoryId, cursorId, pageSize)).thenReturn(responses);

		// when  // then
		mockMvc.perform(
				get("/api/v1/store/categories/{store_category_id}", storeCategoryId)
					.queryParam("cursor_id", String.valueOf(cursorId))
					.queryParam("page_size", String.valueOf(pageSize))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("store-select-by-store-category-id",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("store_category_id").description("매장 카테고리 아이디")
				),
				requestParameters(
					parameterWithName("cursor_id").description("커서(상품) 아이디"),
					parameterWithName("page_size").description("페이지 오프셋 사이즈")
				),
				responseFields(
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description("매장 아이디"),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description("매장 이름"),
					fieldWithPath("data[].phone").type(JsonFieldType.STRING)
						.description("매장 전화번호"),
					fieldWithPath("data[].address").type(JsonFieldType.STRING)
						.description("매장 주소")
				)
			));
	}

	@Test
	@DisplayName(value = "매장 정보를 업데이트하는 API를 호출합니다.")
	void updateStoreInfo() throws Exception {
		// given
		long storeId = 1L;
		UpdateStoreRequest request = UpdateStoreRequest.builder()
			.name("name")
			.phone("00011112222")
			.address("서울")
			.storeCategoryId(1L)
			.build();

		given(storeService.updateStore(anyLong(), any(UpdateStoreServiceRequest.class)))
			.willReturn(storeId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/store/{store_id}", storeId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(storeId))
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("store-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("store_id").description("매장 아이디")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING)
						.optional()
						.description("매장 이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.optional()
						.description("매장 전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.optional()
						.description("매장 주소"),
					fieldWithPath("storeCategoryId").type(JsonFieldType.NUMBER)
						.optional()
						.description("매장 카테고리 아이디")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("수정된 매장 아이디")
				)));
	}

	@Test
	@DisplayName(value = "매장을 삭제하는 API를 호출합니다.")
	void deleteStore() throws Exception {
		// given
		Long storeId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/store/{store_id}", storeId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("store-delete",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("store_id").description("매장 아이디")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING)
						.description("삭제 시 성공 메시지")
				)));
	}
}
