package personal.hdproject.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.store.dao.common.StoreCustomRepository;
import personal.hdproject.store.domain.Store;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreCustomRepository {

}
