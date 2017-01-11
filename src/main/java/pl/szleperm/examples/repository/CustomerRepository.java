package pl.szleperm.examples.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.szleperm.examples.entity.Customer;



public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Optional<Customer> findByNipNumber(String nipNumber);
}
