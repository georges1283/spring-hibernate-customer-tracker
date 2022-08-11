package com.springdemo.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
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

	@Mock
	Model theModel;

	@Test
	public void testFindAll() {
		// given
		Customer customer1 = new Customer(1, "Test", "Tester", "Testing@gmail.com");
		Customer customer2 = new Customer(2, "Test", "Gussin", "example@gmail.com");
		List<Customer> customers = Arrays.asList(customer1, customer2);
		//theModel.addAttribute("customers", customer1);
		//theModel.addAttribute("customers", customer2);
		String sort = "0";
		when(customerService.getCustomers(eq(Integer.parseInt(sort)))).thenReturn(customers);


		// when
		String responseEntity = customerController.listCustomers(theModel, sort);

		verify(customerService).getCustomers(eq(Integer.parseInt(sort)));
		verify(theModel).addAttribute(eq("customers"), eq(customers));

		assertEquals(responseEntity, "list-customers");


	}

	@Test
	public void testShowFormForAdd() {
		// Customer customer = new Customer("Tester", "Test", "testingggg@gmail.com");
		String responseEntity = customerController.showFormForAdd(theModel);

		assertEquals(responseEntity, "customer-form");
	}

	@Test
	public void testSaveCustomer() {
		Customer customer = new Customer("Tester", "Test", "testingggg@gmail.com");
		
		String responseEntity = customerController.saveCustomer(customer);
		
		assertEquals(responseEntity, "redirect:/customer/list");
	}

	@Test
	public void testShowFormForUpdate() {
		int customerId = 1;
		String responseEntity = customerController.showFormForUpdate(customerId, theModel);

		assertEquals(responseEntity, "customer-form");
	}
	
	@Test
	public void TestDeleteCustomer() {
		int customerId = 1;
		
		String responseEntity =  customerController.deleteCustomer(customerId);
		
		assertEquals(responseEntity, "redirect:/customer/list");
	}
	
	@Test
	public void TestSearchCustomer() {
		String customerSearchName = "Test";
		
		String responseEntity =  customerController.searchCustomer(customerSearchName, theModel);
		
		assertEquals(responseEntity, "list-customers");
	}

}
