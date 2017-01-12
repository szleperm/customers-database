package pl.szleperm.examples.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.repository.CustomerRepository;

@Transactional
@Service("customerValidationService")
public class CustomerValidationServiceImpl implements CustomerValidationService {

	protected CustomerRepository customerRepository;
	
	@Autowired
	public CustomerValidationServiceImpl(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}

	@Override
	public boolean isNewCustomerValid(Customer customer) {	
		return customerRepository.findByNipNumber(customer.getNipNumber())
							.map(c -> !(customer.getId() == null))
							.orElse(customer.getId()== null);	
	}

	@Override
	public boolean isCustomerExist(long id) {
		return Optional.ofNullable(customerRepository.findOne(id))
					.map(c -> true)
					.orElse(false);
	}

	@Override
	public boolean isUpdatedCustomerValid(Customer customer) {
		return customerRepository.findByNipNumber(customer.getNipNumber())
						.map(c -> c.getId() == customer.getId())
						.orElse(true);		
	}

}
