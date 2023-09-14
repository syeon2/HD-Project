package personal.hdproject.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.item.domain.ItemOption;

public interface OptionRepository extends JpaRepository<ItemOption, Long> {

}
