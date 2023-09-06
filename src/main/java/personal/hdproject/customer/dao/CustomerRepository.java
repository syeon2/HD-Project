package personal.hdproject.customer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import personal.hdproject.customer.dao.common.CustomeCustomRepository;
import personal.hdproject.customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomeCustomRepository {

	Optional<Customer> findCustomerByEmail(String email);
}
