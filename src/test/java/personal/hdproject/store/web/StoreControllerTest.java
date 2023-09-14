package personal.hdproject.store.web;

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
import personal.hdproject.store.service.StoreService;
import personal.hdproject.store.web.request.CreateStoreRequest;
import personal.hdproject.store.web.response.StoreResponse;

@WebMvcTest(controllers = StoreController.class)
class StoreControllerTest extends BaseTestConfig {

	@MockBean
	private StoreService storeService;

	@Test
	@DisplayName(value = "매장을 생성하는 API를 호출합니다.")
	void createStore() throws Exception {
		// given
		CreateStoreRequest request = CreateStoreRequest.builder()
			.name("good")
			.phone("00011112222")
			.address("서울")
			.storeCategoryId(1L)
			.memberId(1L)
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/store")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
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
				get("/api/v1/store/" + storeId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(storeId))
			.andExpect(jsonPath("$.data.name").value(name))
			.andExpect(jsonPath("$.data.phone").value(phone))
			.andExpect(jsonPath("$.data.address").value(address))
			.andExpect(jsonPath("$.message").doesNotExist());
	}

	@Test
	@DisplayName(value = "매장 카테고리 아이디를 사용하여 매장 리스트를 조회하는 API를 호출합니다.")
	void findStoreByStoreCategoryId() throws Exception {
		// given
		Long storeCategoryId = 1L;
		Long cursorId = 1L;
		Integer pageSize = 10;

		List<StoreResponse> responses = List.of();

		when(storeService.findStoreByStoreCategoryId(storeCategoryId, cursorId, pageSize)).thenReturn(responses);

		// when  // then
		mockMvc.perform(
				get("/api/v1/store/categories/" + storeCategoryId)
					.queryParam("cursorId", String.valueOf(cursorId))
					.queryParam("pageSize", String.valueOf(pageSize))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray());
	}

	@Test
	@DisplayName(value = "매장을 삭제하는 API를 호출합니다.")
	void deleteStore() throws Exception {
		// given
		Long storeId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/store/" + storeId)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
}
