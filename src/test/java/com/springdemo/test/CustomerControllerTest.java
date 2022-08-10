package com.springdemo.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.springdemo.controller.CustomerController;
import com.springdemo.entity.Customer;
import com.springdemo.service.CustomerServiceImpl;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class CustomerControllerTest {
	
	@InjectMocks
	CustomerController customerController;
	
	@Mock
	CustomerServiceImpl customerService;

	@Mock
	Customer customerDao;

	@Test
	public void testFindAll() {
		// given
		Customer customer1 = new Customer(1, "Test", "Tester", "Testing@gmail.com");
		Customer customer2 = new Customer(2, "Test", "Gussin", "example@gmail.com");
		List<Customer> customers = Arrays.asList(customer1, customer2);

		int sort = 0;
		when(customerService.getCustomers(sort)).thenReturn(customers);
		 
        // when
		List<Customer> result = customerService.getCustomers(sort);
		
//		when(customerService.getCustomers(eq(sort))).thenReturn(Arrays.asList(customer1, customer2));
		
		// then
        assertThat(result.size()).isEqualTo(2);
         
        assertThat(result.get(0).getFirstName())
                        .isEqualTo(customer1.getFirstName());
         
        assertThat(result.get(1).getFirstName())
                        .isEqualTo(customer2.getFirstName());
		
	}
	
	@Test
	public void testAddCustomer() {
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         
        when(customerController.saveCustomer(any(Customer.class))).thenReturn("redirect:/customer/list");
         
        Customer customer = new Customer("Tester", "Test", "testingggg@gmail.com");
        String responseEntity = customerController.saveCustomer(customer);
         
        assertThat(responseEntity = "redirect:/customer/list");
	}

}
