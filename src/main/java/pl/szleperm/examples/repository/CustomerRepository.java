package pl.szleperm.examples.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.szleperm.examples.entity.Customer;



public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Customer findByNipNumber(String nipNumber);
}
