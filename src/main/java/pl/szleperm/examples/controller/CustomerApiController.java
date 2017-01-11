package pl.szleperm.examples.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import pl.szleperm.examples.entity.Customer;
import pl.szleperm.examples.service.CustomerService;
import pl.szleperm.examples.service.CustomerValidationService;

@RestController
@RequestMapping("/api")
public class CustomerApiController {

	private static final Logger logger = Logger.getLogger(CustomerApiController.class);

	
	protected CustomerService customerService;
	protected CustomerValidationService customerValidationService;
	
	@Autowired
	public CustomerApiController(CustomerService customerService, CustomerValidationService customerValidationService) {
		super();
		this.customerService = customerService;
		this.customerValidationService = customerValidationService;
	}

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getAllCustomers() {
		infoLog("Get all customers");
		List<Customer> customers = customerService.findAll();
		if (customers.isEmpty()) {
			infoLog("Customers list is empty");
			return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
		}
		infoLog("Found " + customers.size() + " customer(s)");
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
		infoLog("Get customer with id: " + id);
		Customer customer = customerService.findById(id);
		if (customer == null) {
			infoLog("Customer with id: " + id + " not found");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		infoLog("Found customer with id: " + id);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@RequestMapping(value = "/customers", method = RequestMethod.POST)
	public ResponseEntity<Void> createCustomer(@Valid @RequestBody Customer customer, UriComponentsBuilder builder) {
		infoLog("Create customer with NIP " + customer.getNipNumber());
		if (!customerValidationService.isNewCustomerValid(customer)) {
			infoLog("Customer with NIP " + customer.getNipNumber()+ " exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		customerService.saveCustomer(customer);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("api/customers/{id}").buildAndExpand(customer.getId()).toUri());
		return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id,@Valid @RequestBody Customer customer){
		infoLog("Update customer with id " + id);
		if (!customerValidationService.isCustomerExist(id)){
			infoLog("Customer with id " + id + " not found");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		if (!customerValidationService.isUpdatedCustomerValid(customer)){
			infoLog("Not unique customer NIP");
			return new ResponseEntity<Customer>(HttpStatus.CONFLICT);
		}
		Customer currentCustomer = customerService.update(customer);
		return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
	}
	
	@RequestMapping(value="/customers/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id){
		infoLog("Delete customer with id " + id);
		Customer customer = customerService.findById(id);
		if (customer==null){
			infoLog("Customer with id " + id +" not found");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		customerService.delete(id);
		return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/customers", method=RequestMethod.DELETE)
	public ResponseEntity<Customer> deleteAll(){
		infoLog("Delete all customers");
		customerService.deleteAll();
		return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
	}
	
	private void infoLog(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {
		Map<String, String> errors = exception
				.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
	     return errors;
	}
}
