package pl.szleperm.examples.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.repository.CustomerRepository;

@Transactional
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	protected CustomerRepository customerRepository;
	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Customer findById(long id) {
		return customerRepository.findOne(id);
	}

	@Override
	public Customer update(Customer customer) {
		return customerRepository.save(customer);
	}	
	
	@Override
	public void delete(long id) {
		customerRepository.delete(id);
	}

	@Override
	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public void deleteAll() {
		customerRepository.deleteAll();
	}

}
