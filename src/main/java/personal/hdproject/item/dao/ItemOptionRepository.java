package personal.hdproject.item.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.item.domain.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

	List<ItemOption> findItemOptionByItemId(Long itemId);
}
