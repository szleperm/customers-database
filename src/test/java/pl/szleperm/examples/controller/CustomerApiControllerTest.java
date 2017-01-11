package pl.szleperm.examples.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.service.CustomerService;
import pl.szleperm.examples.service.CustomerValidationService;

public class CustomerApiControllerTest {

	@Mock
	CustomerService customerService;
	
	@Mock
	CustomerValidationService validationService;
	
	CustomerApiController apiController;

	@Spy
	List<Customer> customers = new ArrayList<>();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		apiController = new CustomerApiController(customerService, validationService);
		customers = getCustomers();
	}

	@Test
	public void shouldGiveAllCustomers() {
		// given
		// when
		when(customerService.findAll()).thenReturn(customers);
		ResponseEntity<List<Customer>> responseEntity = apiController.getAllCustomers();
		// then
		Assert.assertEquals(responseEntity.getBody(), customers);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void shouldGiveNoContentStatusWhenCustomersListIsEmpty() {
		// given
		// when
		when(customerService.findAll()).thenReturn(new ArrayList<>());
		ResponseEntity<List<Customer>> responseEntity = apiController.getAllCustomers();
		// then
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}

	@Test
	public void shouldGiveOneCustomer() {
		// given
		long id = 1;
		Customer customer = customers.get(0);
		// when
		when(customerService.findById(id)).thenReturn(customer);
		ResponseEntity<Customer> responseEntity = apiController.getCustomer(id);
		// then
		Assert.assertEquals(responseEntity.getBody(), customer);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void shouldGiveNotFoundStatusWhenCustomerNotExist() {
		// given
		long id = 1;
		// when
		when(customerService.findById(id)).thenReturn(null);
		ResponseEntity<Customer> responseEntity = apiController.getCustomer(id);
		// then
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldCreateCustomer() {
		// given
		Customer customer = customers.get(0);
		customer.setId(null);
		// when
		doNothing().when(customerService).saveCustomer(any(Customer.class));
		when(validationService.isNewCustomerValid(any(Customer.class))).thenReturn(true);
		ResponseEntity<Void> responseEntity = apiController.createCustomer(customer,
				UriComponentsBuilder.newInstance());
		// then
		verify(customerService, atLeastOnce()).saveCustomer(any(Customer.class));
		Assert.assertEquals(responseEntity.getHeaders().getLocation().toString(), "api/customers/");
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void shouldNotCreateCustomerWhenIsNotValid() {
		// given
		Customer customer = customers.get(0);
		// when
		when(validationService.isNewCustomerValid(any(Customer.class))).thenReturn(false);
		ResponseEntity<Void> responseEntity = apiController.createCustomer(customer,
				UriComponentsBuilder.newInstance());
		// then
		verify(customerService, never()).saveCustomer(any(Customer.class));
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);
	}

	@Test
	public void shouldUpdateCustomer() {
		// given
		Customer customer = customers.get(0);
		long id = 1L;
		// when
		when(validationService.isCustomerExist(id)).thenReturn(true);
		when(validationService.isUpdatedCustomerValid(customer)).thenReturn(true);
		when(customerService.update(any(Customer.class))).thenReturn(customer);
		ResponseEntity<Customer> responseEntity = apiController.updateCustomer(id, customer);
		// then
		verify(customerService, atLeastOnce()).update(customer);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void shouldNotUpdateCustomerWhenNotFound() {
		// given
		Customer customer = customers.get(0);
		long id = 1L;
		// when
		when(validationService.isCustomerExist(id)).thenReturn(false);
		ResponseEntity<Customer> responseEntity = apiController.updateCustomer(id, customer);
		// then
		verify(customerService, never()).update(any(Customer.class));
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldNotUpdateCustomerWhenIsNotValid() {
		// given
		Customer customer = customers.get(0);
		long id = 1L;
		// when
		when(validationService.isCustomerExist(id)).thenReturn(true);
		when(validationService.isUpdatedCustomerValid(customer)).thenReturn(false);
		ResponseEntity<Customer> responseEntity = apiController.updateCustomer(id, customer);
		// then
		verify(customerService, never()).update(any(Customer.class));
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CONFLICT);
	}

	@Test
	public void shouldDeleteCustomer() {
		// given
		Customer customer = customers.get(0);
		long id = 1L;
		// when
		doNothing().when(customerService).delete(anyLong());
		when(customerService.findById(anyLong())).thenReturn(customer);
		ResponseEntity<Customer> responseEntity = apiController.deleteCustomer(id);
		// then
		verify(customerService, atLeastOnce()).delete(id);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}

	@Test
	public void shouldNotDeleteCustomerWhenNotExist() {
		// given
		long id = 1L;
		// when
		doNothing().when(customerService).delete(anyLong());
		when(customerService.findById(anyLong())).thenReturn(null);
		ResponseEntity<Customer> responseEntity = apiController.deleteCustomer(id);
		// then
		verify(customerService, never()).delete(anyLong());
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void shouldDeleteAllCustomers() {
		// given
		// when
		doNothing().when(customerService).deleteAll();
		ResponseEntity<Customer> responseEntity = apiController.deleteAll();
		// then
		verify(customerService, atLeastOnce()).deleteAll();
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
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
