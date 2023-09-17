package personal.hdproject.store.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.service.MemberProfileService;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.store.dao.StoreCategoryRepository;
import personal.hdproject.store.dao.StoreRepository;
import personal.hdproject.store.domain.Store;
import personal.hdproject.store.service.request.CreateStoreServiceRequest;
import personal.hdproject.store.service.request.StoreCategoryServiceRequest;
import personal.hdproject.store.service.request.UpdateStoreServiceRequest;
import personal.hdproject.store.web.response.StoreResponse;

@SpringBootTest
class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StoreCategoryService storeCategoryService;

	@Autowired
	private StoreCategoryRepository storeCategoryRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberProfileService memberProfileService;

	@AfterEach
	void after() {
		storeRepository.deleteAll();
		memberRepository.deleteAll();
		storeCategoryRepository.deleteAll();
	}

	@Test
	@DisplayName(value = "매장을 생성합니다.")
	void createMember() {
		// given
		StoreCategoryServiceRequest storeCategoryRequest = new StoreCategoryServiceRequest("category1");
		CreateMemberServiceRequest memberRequest = getCreateMemberServiceRequest(
			"00011112222", "12345@gmail.com");

		Long categoryId = storeCategoryService.createCategory(storeCategoryRequest);
		Long joinedMemberId = memberProfileService.join(memberRequest);

		String storeName = "good";
		CreateStoreServiceRequest storeRequest = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone("01022234455")
			.address("경기도 광주시")
			.memberId(joinedMemberId)
			.storeCategoryId(categoryId)
			.build();

		// when
		Long storeId = storeService.createStore(storeRequest);

		// then
		Optional<Store> findStore = storeRepository.findById(storeId);

		findStore.ifPresentOrElse(
			store -> {
				assertThat(store.getMember().getId()).isSameAs(joinedMemberId);
				assertThat(store.getStoreCategory().getId()).isSameAs(categoryId);

				assertThat(store.getName()).isEqualTo(storeName);
			},
			() -> fail("매장 생성이 실패하였습니다.")
		);
	}

	@Test
	@DisplayName(value = "매장 아이디를 사용하여 매장을 조회합니다.")
	void findStoreById() {
		// given
		StoreCategoryServiceRequest storeCategoryRequest = new StoreCategoryServiceRequest("category1");
		CreateMemberServiceRequest memberRequest = getCreateMemberServiceRequest(
			"00011112412", "1234@gmail.com");

		Long storeCategoryId = storeCategoryService.createCategory(storeCategoryRequest);
		Long joinedMemberId = memberProfileService.join(memberRequest);

		String storeName = "good";
		String phone = "01022234455";
		String address = "경기도 광주시";
		CreateStoreServiceRequest storeRequest = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone(phone)
			.address(address)
			.memberId(joinedMemberId)
			.storeCategoryId(storeCategoryId)
			.build();

		Long storeId = storeService.createStore(storeRequest);

		// when
		StoreResponse findStore = storeService.findStoreById(storeId);

		// then
		assertThat(findStore).extracting("name", "phone", "address")
			.contains(storeName, phone, address);
	}

	@Test
	@DisplayName(value = "매장 카테고리 아이디를 사용하여 매장을 조회합니다.")
	void findStoresByStoreCategoryId() {
		// given
		StoreCategoryServiceRequest storeCategoryRequest = new StoreCategoryServiceRequest("category1");
		CreateMemberServiceRequest memberRequest1 = getCreateMemberServiceRequest("00022223331", "1@gmail.com");
		CreateMemberServiceRequest memberRequest2 = getCreateMemberServiceRequest("00022223332", "12@gmail.com");

		Long storeCategoryId = storeCategoryService.createCategory(storeCategoryRequest);
		Long joinedMember1Id = memberProfileService.join(memberRequest1);
		Long joinedMember2Id = memberProfileService.join(memberRequest2);

		String storeName = "good";
		String phone = "01022234455";
		String address = "경기도 광주시";
		CreateStoreServiceRequest storeRequest1 = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone(phone)
			.address(address)
			.memberId(joinedMember1Id)
			.storeCategoryId(storeCategoryId)
			.build();

		CreateStoreServiceRequest storeRequest2 = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone(phone)
			.address(address)
			.memberId(joinedMember2Id)
			.storeCategoryId(storeCategoryId)
			.build();

		Long store1Id = storeService.createStore(storeRequest1);
		Long store2Id = storeService.createStore(storeRequest2);

		// when
		List<StoreResponse> storeResponses =
			storeService.findStoreByStoreCategoryId(storeCategoryId, store2Id + 1, 10);

		// then
		assertThat(storeResponses).hasSize(2);
	}

	@Test
	@DisplayName(value = "매장 정보를 수정합니다.")
	void updateStore() {
		// given
		StoreCategoryServiceRequest storeCategoryRequest = new StoreCategoryServiceRequest("category1");
		CreateMemberServiceRequest memberRequest = getCreateMemberServiceRequest(
			"00011112414", "12345@gmail.com");

		Long categoryId = storeCategoryService.createCategory(storeCategoryRequest);
		Long joinedMemberId = memberProfileService.join(memberRequest);

		String storeName = "good";
		CreateStoreServiceRequest storeRequest = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone("01022234455")
			.address("경기도 광주시")
			.memberId(joinedMemberId)
			.storeCategoryId(categoryId)
			.build();

		Long storeId = storeService.createStore(storeRequest);

		// when
		String changedName = "changeGood";
		String changedPhone = "01055556666";
		String changedAddress = "수정된 광주시";
		UpdateStoreServiceRequest request = UpdateStoreServiceRequest.builder()
			.name(changedName)
			.phone(changedPhone)
			.address(changedAddress)
			.build();

		storeService.updateStore(storeId, request);

		// then
		Optional<Store> findStore = storeRepository.findById(storeId);

		findStore.ifPresentOrElse(
			store -> {
				assertThat(store.getName()).isEqualTo(changedName);
				assertThat(store.getPhone()).isEqualTo(changedPhone);
				assertThat(store.getAddress()).isEqualTo(changedAddress);

				assertThat(store.getStoreCategory().getId()).isSameAs(categoryId);
				assertThat(store.getMember().getId()).isSameAs(joinedMemberId);
			},
			() -> fail("매장 정보 수정에 실패하였습니다.")
		);
	}

	@Test
	@DisplayName(value = "매장을 삭제합니다.")
	void name() {
		// given
		StoreCategoryServiceRequest storeCategoryRequest = new StoreCategoryServiceRequest("category1");
		CreateMemberServiceRequest memberRequest = getCreateMemberServiceRequest(
			"00011112412", "1234@gmail.com");

		Long categoryId = storeCategoryService.createCategory(storeCategoryRequest);
		Long joinedMemberId = memberProfileService.join(memberRequest);

		String storeName = "good";
		CreateStoreServiceRequest storeRequest = CreateStoreServiceRequest.builder()
			.name(storeName)
			.phone("01022234455")
			.address("경기도 광주시")
			.memberId(joinedMemberId)
			.storeCategoryId(categoryId)
			.build();

		Long storeId = storeService.createStore(storeRequest);
		assertThat(storeService.findStoreById(storeId).getName()).isEqualTo(storeName);

		// when
		storeService.deleteStore(storeId);

		// then
		assertThatThrownBy(() -> storeService.findStoreById(storeId))
			.isInstanceOf(NoSuchElementException.class);
	}

	private CreateMemberServiceRequest getCreateMemberServiceRequest(String number, String mail) {
		return CreateMemberServiceRequest.builder()
			.nickname("nickname")
			.phone(number)
			.email(mail)
			.password("123456778")
			.address("경기도 성남시")
			.build();
	}
}
