package pl.szleperm.examples.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.repository.CustomerRepository;

public class CustomerValidationServiceTest {

	@Mock
	CustomerRepository customerRepository;
	
	@Spy
	List<Customer> customers = new ArrayList<>();
	
	CustomerValidationServiceImpl validationService;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		validationService = new CustomerValidationServiceImpl(customerRepository);
		this.customers = getCustomers();
	}
	
	@Test
	public void shouldReturnTrueIfNewCustomerIsValid(){
		//given
		Customer customer = new Customer();
		customer.setNipNumber("nip");
		//when
		when(customerRepository.findByNipNumber("nip")).thenReturn(Optional.ofNullable(null));
		boolean result = validationService.isNewCustomerValid(customer);
		//then
		Assert.assertEquals(true, result);
		verify(customerRepository, atLeastOnce()).findByNipNumber("nip");
	}
	
	@Test
	public void shouldReturnFalseIfNewCustomerIdIsNotNull(){
		//given
		Customer customer = new Customer();
		customer.setNipNumber("nip");
		customer.setId(1L);
		//when
		when(customerRepository.findByNipNumber("nip")).thenReturn(Optional.ofNullable(null));
		boolean result = validationService.isNewCustomerValid(customer);
		//then
		Assert.assertEquals(false, result);
	}
	
	@Test
	public void shouldReturnFalseIfNewCustomerNipIsNotUnique(){
		//given
		Customer customer = new Customer();
		customer.setNipNumber("nip");
		//when
		when(customerRepository.findByNipNumber("nip")).thenReturn(Optional.of(customers.get(0)));
		boolean result = validationService.isNewCustomerValid(customer);
		//then
		Assert.assertEquals(false, result);
		verify(customerRepository, atLeastOnce()).findByNipNumber("nip");
	}
	
	@Test
	public void shouldReturnTrueIfCustomerExist(){
		//given
		Customer customer = new Customer();
		long id = 1L;
		//when
		when(customerRepository.findOne(id)).thenReturn(customer);
		boolean result = validationService.isCustomerExist(id);
		//then
		Assert.assertEquals(true, result);
		verify(customerRepository, atLeastOnce()).findOne(id);
	}
	
	@Test
	public void shouldReturnFalseIfCustomerNotExist(){
		//given
		long id = 1L;
		//when
		when(customerRepository.findOne(id)).thenReturn(null);
		boolean result = validationService.isCustomerExist(id);
		//then
		Assert.assertEquals(false, result);
		verify(customerRepository, atLeastOnce()).findOne(id);
	}
	
	@Test
	public void shouldReturnFalseIfUpdatedCustomerIsNotValid(){
		//given
		Customer customer1 = customers.get(0);
		Customer customer2 = customers.get(1);
		//when
		when(customerRepository.findByNipNumber(customer1.getNipNumber())).thenReturn(Optional.of(customer2));
		boolean result = validationService.isUpdatedCustomerValid(customer1);
		//then
		Assert.assertEquals(false, result);
		verify(customerRepository, atLeastOnce()).findByNipNumber(customer1.getNipNumber());
	}
	
	@Test
	public void shouldReturnTrueIfUpdatedCustomerIsValid(){
		//given
		Customer customer1 = customers.get(0);
		Customer customer2 = customers.get(1);
		//when
		when(customerRepository.findByNipNumber(customer1.getNipNumber())).thenReturn(Optional.of(customer1));
		when(customerRepository.findByNipNumber(customer2.getNipNumber())).thenReturn(Optional.ofNullable(null));
		boolean result1 = validationService.isUpdatedCustomerValid(customer1);
		boolean result2 = validationService.isUpdatedCustomerValid(customer2);
		//then
		Assert.assertEquals(true, result1);
		Assert.assertEquals(true, result2);
		verify(customerRepository, atLeastOnce()).findByNipNumber(customer1.getNipNumber());
	}
		
	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<>();
		Customer c1 = new Customer();
		c1.setId(1L);
		c1.setName("Name1");
		c1.setNipNumber("Nip1");
		c1.setAddress("Address1");
		Customer c2 = new Customer();
		c2.setId(2L);
		c2.setName("Name2");
		c2.setNipNumber("Nip2");
		c2.setAddress("Address2");
		customers.add(c1);
		customers.add(c2);
		return customers;
	}
}
