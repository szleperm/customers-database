package pl.szleperm.examples.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
		debugLog("Get all customers");
		List<Customer> customers = customerService.findAll();
		return debugLog(new ResponseEntity<List<Customer>>(customers, HttpStatus.OK));
	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
		debugLog("Get customer with id: " + id);
		return Optional.ofNullable(customerService.findById(id))
				.map(c -> debugLog(new ResponseEntity<Customer>(c, HttpStatus.OK)))
				.orElseGet(() -> debugLog(new ResponseEntity<Customer>(HttpStatus.NOT_FOUND)));
	}

	@RequestMapping(value = "/customers", method = RequestMethod.POST)
	public ResponseEntity<Void> createCustomer(@Valid @RequestBody Customer customer, UriComponentsBuilder builder) {
		debugLog("Create customer with NIP " + customer.getNipNumber());
		if (!customerValidationService.isNewCustomerValid(customer)) {
			return debugLog(new ResponseEntity<Void>(HttpStatus.CONFLICT));
		}
		customerService.saveCustomer(customer);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("api/customers/{id}").buildAndExpand(customer.getId()).toUri());
		return debugLog(new ResponseEntity<Void>(headers,HttpStatus.CREATED));
	}
	
	@RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id,@Valid @RequestBody Customer customer){
		debugLog("Update customer with id " + id);
		if (!customerValidationService.isCustomerExist(id)){
			return debugLog(new ResponseEntity<Customer>(HttpStatus.NOT_FOUND));
		}
		if (!customerValidationService.isUpdatedCustomerValid(customer)){
			return debugLog(new ResponseEntity<Customer>(HttpStatus.CONFLICT));
		}
		Customer currentCustomer = customerService.update(customer);
		return debugLog(new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK));
	}
	
	@RequestMapping(value="/customers/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id){
		debugLog("Delete customer with id " + id);
		return Optional.ofNullable(customerService.findById(id))
					.map(c -> {
						customerService.delete(id);
						return debugLog(new ResponseEntity<Customer>(HttpStatus.NO_CONTENT));
					})
					.orElseGet(() -> debugLog(new ResponseEntity<Customer>(HttpStatus.NOT_FOUND)));
	}
	
	@RequestMapping(value="/customers", method=RequestMethod.DELETE)
	public ResponseEntity<Customer> deleteAll(){
		debugLog("Delete all customers");
		customerService.deleteAll();
		return debugLog(new ResponseEntity<Customer>(HttpStatus.NO_CONTENT));
	}
	
	private <T> ResponseEntity<T> debugLog(ResponseEntity<T> responseEntity){
		HttpStatus statusCode = responseEntity.getStatusCode();
		debugLog("Returned status code: " + statusCode + " " + statusCode.getReasonPhrase());
		return responseEntity;
	}
	
	private void debugLog(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
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