package pl.szleperm.examples.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.repository.CustomerRepository;

public class CustomerServiceTest {

	@Mock
	CustomerRepository customerRepository;
	
	@Spy
	List<Customer> customers = new ArrayList<>();
	
	CustomerServiceImpl service;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		service = new CustomerServiceImpl(customerRepository);
		this.customers = getCustomers();
	}
	
	@Test
	public void shouldReturnAllCustoers(){
		//given
		//when
		when(customerRepository.findAll()).thenReturn(customers);
		List<Customer> result = service.findAll();
		//then
		Assert.assertEquals(customers, result);
		verify(customerRepository, atLeastOnce()).findAll();
	}
	
	@Test
	public void shouldReturnCustomerById(){
		//given
		long id = 1L;
		Customer customer = customers.get(0);
		//when
		when(customerRepository.findOne(id)).thenReturn(customer);
		Customer result = service.findById(id);
		//then
		Assert.assertEquals(customer, result);
		verify(customerRepository, atLeastOnce()).findOne(id);
	}
	
	@Test
	public void shouldUpdateCustomer(){
		//given
		Customer customer = customers.get(0);
		customer.setId(5L);
		//when
		when(customerRepository.findOne(anyLong())).thenReturn(customers.get(0));
		when(customerRepository.save(customers.get(0))).thenReturn(customers.get(0));
		Customer result = service.update(customer);
		//then
		assertEquals(result, customers.get(0));
		verify(customerRepository, atLeastOnce()).save(customers.get(0));
	}
	
	@Test
	public void shouldDeleteCustomer(){
		//given
		long id = 1L;
		//when
		service.delete(id);
		//then
		verify(customerRepository, atLeastOnce()).delete(id);
	}
	
	@Test
	public void shouldSaveCustomer(){
		//given
		Customer customer = customers.get(0);
		//when
		service.saveCustomer(customer);
		//then
		verify(customerRepository, atLeastOnce()).save(customer);
	}
	
	@Test
	public void shouldDeleteAllCustomers(){
		//given
		//when
		service.deleteAll();
		//then
		verify(customerRepository, atLeastOnce()).deleteAll();
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
