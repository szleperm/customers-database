package pl.szleperm.examples.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.szleperm.examples.configuration.AppTestConfiguration;
import pl.szleperm.examples.entity.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppTestConfiguration.class})
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
		
	@Transactional
	@Test
	public void findByNipNumberTest(){
		//given
		Customer customer = new Customer(2L , "Greenholt and Sons", "6103539281", "16894 Evergreen Pass");
		//when
		Optional<Customer> result = customerRepository.findByNipNumber("6103539281");
		//then
		Assert.assertEquals(customer, result.get());
		Assert.assertEquals(customer.getName(), result.get().getName());
		Assert.assertEquals(customer.getAddress(), result.get().getAddress());
	}
}
