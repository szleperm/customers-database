package pl.szleperm.examples.service;

import java.util.List;

import pl.szleperm.examples.entity.Customer;

public interface CustomerService {
	List<Customer> findAll();
	Customer findById(long id);
	void saveCustomer(Customer customer);
	Customer update(Customer customer);
	void delete(long id);
	void deleteAll();
}
