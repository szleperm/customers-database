package pl.szleperm.examples.service;

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
		Customer result = customerRepository.findByNipNumber(customer.getNipNumber());
		if (customer.getId() == null && result == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isCustomerExist(long id) {
		if (customerRepository.findOne(id) == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isUpdatedCustomerValid(Customer customer) {
		Customer result = customerRepository.findByNipNumber(customer.getNipNumber());
		if (result == null) {
			return true;
		}else if(customer.getId() == result.getId()){
			return true;
		}else{
			return false;
		}
	}

}
