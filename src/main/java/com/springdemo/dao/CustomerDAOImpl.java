package com.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springdemo.entity.Customer;
import com.springdemo.util.SortUtils;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	// Inject session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	//@Transactional 
	// removed transactional bcz we added it to the service layer
	public List<Customer> getCustomers(int theSortField) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// determine sort field
		String fieldName = null;
		
		switch (theSortField) {
		case SortUtils.FIRST_NAME: 
			fieldName = "firstName";
			break;
			
		case SortUtils.LAST_NAME:
			fieldName = "lastName";
			break;
			
		case SortUtils.EMAIL:
			fieldName = "email";
			break;
			
		default:
			fieldName = "lastName";
		}
		
		// Create a query
		Query<Customer> theQuery = 
				currentSession.createQuery("from Customer order by "+ fieldName, Customer.class);
		
		// execute query and get result
		List<Customer> customers = theQuery.getResultList();
		
		// return results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// Retrieve from db using pk
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with pk
		Query theQuery = 
				currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId" ,theId);
		
		theQuery.executeUpdate();
		
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// Search for customers with search key
		Query theQuery = null;
		
		//
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // search for firstName or lastName ... case insensitive
            theQuery =currentSession.createQuery("from Customer where lower(firstName) "
            		+ "like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // theSearchName is empty ... so just get all customers
            theQuery =currentSession.createQuery("from Customer", Customer.class);            
        }
        
        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();
                
        // return the results
		return customers;
	}

}
