package com.bethsaida.org.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.CustomerDTO;
import com.bethsaida.org.models.User;


@Repository
public interface CustomerAccountRepo extends JpaRepository <Customer, Long  > 

{    
	/*
	 * @Query("select c.firstName, c.lastName, i.principal, i.maturityDate from Customer c join c.investment i on c.id = i.customer_id where c.userName = :username "
	 * ) List <Customer> findByUserName(String username);
	 */
	
	 Customer getById(Long id);
	 
	 boolean existsByEmail(String email);
	 
	 Customer findByUserName(String username);
	
	 Optional<Customer> findById(Long id);
	 
	 @Query(value="SELECT new com.bethsaida.org.models.CustomerDTO(c.id, c.firstName, c.lastName) " +
		        "FROM Customer c")
	 List<CustomerDTO> findByFirstNameAndLastName();
	 
	 @Query(value= "SELECT new com.bethsaida.org.models.CustomerDTO(c.id, c.firstName, c.lastName) " +
		        "FROM Customer c WHERE c = :customer")
	 List<CustomerDTO> displayCustomerFirstAndLastName(Customer customer);
	 
	 @Query(value="SELECT new com.bethsaida.org.models.CustomerDTO(c.id, c.firstName, c.lastName) " +
		        "FROM Customer c join c.marketer marketer where marketer = :marketer")
	 List<CustomerDTO> findMarketerCustomersDropdown(User marketer);
     
     @Query("SELECT customer from Customer customer join customer.marketer marketer where marketer = :marketer")
     List<Customer> findByMarketer(User marketer);
     
     @Query("SELECT COUNT(C) AS NumberOfCustomers from Customer C")
     Long getTotalCustomer();
     
     @Query("SELECT COUNT(customer) AS NumberOfCustomers from "
     		+ "Customer customer join customer.marketer marketer where marketer = :marketer")
     Long getTotalCustomerByMarketer(User marketer);
     
	
	  @Query("SELECT customer from Customer customer join "
	  		+ "customer.marketer marketer "
	      + "where marketer.branch = :branch") 
	  List<Customer> findByUserBranch(Branch branch);
	  
	  @Query("SELECT COUNT(customer) from Customer customer join "
		  		+ "customer.marketer marketer where marketer.branch = :branch") 
	  Long getTotalBranchCustomer(Branch branch);
	 
	 
	
}



