package personal.hdproject.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.item.domain.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {

}
