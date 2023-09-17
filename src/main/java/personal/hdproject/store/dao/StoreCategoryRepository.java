package personal.hdproject.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.store.domain.StoreCategory;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
