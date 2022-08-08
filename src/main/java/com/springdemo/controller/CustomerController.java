package com.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springdemo.entity.Customer;
import com.springdemo.service.CustomerService;
import com.springdemo.util.SortUtils;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	// Inject CustomerDAO 
//	@Autowired
//	private CustomerDAO customerDAO;
	// removed them bcz i added them in the service layer
	
	@Autowired
	private CustomerService customerService;
	
	//@RequestMapping("/list")
	@GetMapping("/list") 
	public String listCustomers(Model theModel,
			@RequestParam(required=false) String sort) {
		
		// get customer from DAO
		List<Customer> theCustomers = null;
		
		if(sort != null) {
			int theSortField = Integer.parseInt(sort);
			theCustomers = customerService.getCustomers(theSortField);	
		} 
		else {
			theCustomers = customerService.getCustomers(SortUtils.LAST_NAME);
		}
		
		// add customer to the model
		theModel.addAttribute("customers", theCustomers);
		
		return "list-customers";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		Customer customer = new Customer();
		
		theModel.addAttribute("customer" , customer);
		
		return "customer-form";
	}
	
	@PostMapping("saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		// save the customer using our service
		customerService.saveCustomer(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId,
									Model theModel) {
		
		// Get the customer from service
		Customer theCustomer = customerService.getCustomer(theId);
		
		// set customer as model attribute to pre populate the form
		theModel.addAttribute("customer", theCustomer);
		
		// send over to our form
		
		return "customer-form";
	}
	
	@GetMapping("/delete")
	public String deleteCustomer(@RequestParam("customerId") int theId) {
		
		// Delete the customer
		customerService.deleteCustomer(theId);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/search")
	public String searchCustomer(@RequestParam("theSearchName") String theSearchName, Model theModel) {
		
		// search the customers
		List<Customer> theCustomers = customerService.searchCustomers(theSearchName);
		
		theModel.addAttribute("customers", theCustomers);
		
		return "list-customers";
	}

}
