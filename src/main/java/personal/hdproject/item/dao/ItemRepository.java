package personal.hdproject.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.item.dao.common.ItemCustomRepository;
import personal.hdproject.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

}
