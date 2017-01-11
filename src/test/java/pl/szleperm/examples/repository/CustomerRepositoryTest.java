package pl.szleperm.examples.repository;

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
		Customer result = customerRepository.findByNipNumber("6103539281");
		//then
		Assert.assertEquals(customer, result);
		Assert.assertEquals(customer.getName(), result.getName());
		Assert.assertEquals(customer.getAddress(), result.getAddress());
	}
}
