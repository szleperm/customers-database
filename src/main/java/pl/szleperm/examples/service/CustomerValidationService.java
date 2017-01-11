package pl.szleperm.examples.service;

import pl.szleperm.examples.entity.Customer;

public interface CustomerValidationService {

	boolean isNewCustomerValid(Customer customer);

	boolean isCustomerExist(long id);

	boolean isUpdatedCustomerValid(Customer customer);

}
