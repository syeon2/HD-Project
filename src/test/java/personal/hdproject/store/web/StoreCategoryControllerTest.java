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
import personal.hdproject.store.service.StoreCategoryService;
import personal.hdproject.store.web.request.StoreCategoryRequest;
import personal.hdproject.store.web.response.StoreCategoryResponse;

@WebMvcTest(controllers = StoreCategoryController.class)
class StoreCategoryControllerTest extends BaseTestConfig {

	@MockBean
	private StoreCategoryService storeCategoryService;

	@Test
	@DisplayName(value = "매장 카테고리를 추가하는 API를 요청합니다.")
	void createStoreCategory() throws Exception {
		// given
		String category1 = "category1";
		StoreCategoryRequest request = new StoreCategoryRequest(category1);

		// when  // then
		mockMvc.perform(
				post("/api/v1/store-category")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "매장 카테고리를 모두 조회합니다.")
	void findAllStoreCategory() throws Exception {
		// given
		List<StoreCategoryResponse> responses = List.of();

		when(storeCategoryService.findAllStoreCategory()).thenReturn(responses);

		// when  // then
		mockMvc.perform(
				get("/api/v1/store-category")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.message").doesNotExist());
	}

	@Test
	@DisplayName(value = "매장 카테고리를 삭제합니다.")
	void deleteStoreCategory() throws Exception {
		// given  // when  // then
		long categoryId = 1L;

		mockMvc.perform(
				delete("/api/v1/store-category/" + categoryId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value("매장 카테고리가 성공적으로 삭제되었습니다."));
	}
}
