package personal.hdproject.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.domain.Member;
import personal.hdproject.member.service.MemberProfileService;
import personal.hdproject.store.dao.StoreRepository;
import personal.hdproject.store.domain.Store;
import personal.hdproject.store.domain.StoreCategory;
import personal.hdproject.store.service.request.CreateStoreServiceRequest;
import personal.hdproject.store.service.request.UpdateStoreServiceRequest;
import personal.hdproject.store.web.response.StoreResponse;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final MemberProfileService memberProfileService;
	private final StoreCategoryService storeCategoryService;

	@Transactional
	public Long createStore(CreateStoreServiceRequest request) {
		Member findMember = memberProfileService.findMemberById(request.getMemberId());
		StoreCategory findStoreCategory = storeCategoryService.findStoreCategoryById(request.getStoreCategoryId());

		Store storeEntity = Store.toEntity(request, findStoreCategory, findMember);
		Store savedStore = storeRepository.save(storeEntity);

		return savedStore.getId();
	}

	public StoreResponse findStoreById(Long storeId) {
		Optional<Store> findStoreOptional = storeRepository.findById(storeId);

		if (findStoreOptional.isPresent()) {
			return StoreResponse.toResponseDto(findStoreOptional.get());
		}

		throw new NoSuchElementException("매장이 존재하지 않습니다.");
	}

	public List<StoreResponse> findStoreByStoreCategoryId(Long storeCategoryId, Long cursorId, Integer offset) {
		List<Store> stores = storeRepository.findByStoreCategoryIdLessThanOrderByIdDesc(storeCategoryId, cursorId,
			offset);

		return stores.stream()
			.map(StoreResponse::toResponseDto)
			.collect(Collectors.toList());
	}

	public Long updateStore(Long storeId, UpdateStoreServiceRequest request) {
		return storeRepository.updateStore(storeId, request.toUpdateDto());
	}

	public void deleteStore(Long storeId) {
		storeRepository.deleteById(storeId);
	}
}
