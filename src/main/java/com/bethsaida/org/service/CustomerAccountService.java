package com.bethsaida.org.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bethsaida.org.controllers.CustomerController;
import com.bethsaida.org.models.*;
import com.bethsaida.org.repository.*;

@Transactional
@Service
public class CustomerAccountService {
	
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
    private PasswordEncoder bCryptPasswordEncoder;
	
    @Autowired	
	private CustomerAccountRepo custRepo;
    
    @Autowired
    private UserAccountRepository userAccountRepo;
    

	private Path fileStorageLocation ;
	
    String uploadDir = "customer-photos/" ;
    
    @Autowired	
    private EmailService emailService;
    
    
	public CustomerAccountService() throws Exception { 
	this.fileStorageLocation = Paths.get(uploadDir) .toAbsolutePath().normalize();
	  
	  try { 
		  Files.createDirectories(this.fileStorageLocation); 
		  } catch (Exception ex) { 
			  throw new
	  Exception("Could not create the directory where the uploaded files will be stored."
	  , ex); 
			  } 
	  }
	 
	
   // @Cacheable("customers")
    public List<Customer> getCustomers(){
	return custRepo.findAll();
	}
    
    public List<Customer> getPagedCustomer(Integer pageNo, Integer pageSize){
    Pageable paging = PageRequest.of(pageNo, pageSize);	
    Page<Customer> pagedResult = custRepo.findAll(paging);
    if (pagedResult.hasContent()) {
    return pagedResult.getContent();
    }else{
    return new ArrayList <Customer>();
     }
    }
    
    public boolean exist(String email){
    return custRepo.existsByEmail(email);
	}
    
    public Page<Customer> getPagedCustomerCount(Pageable pageable){
    return custRepo.findAll(pageable);
    }
    
    @Transactional
    public Customer registerCustomer(Customer customer) throws AddressException, MessagingException, IOException {
    String encoded = bCryptPasswordEncoder.encode(customer.getPassword());
    emailService.sendCustomerRegistrationMail(customer);
    customer.setPassword(encoded);
	custRepo.save(customer);
	
	User user = new User();
	user.setUserName(customer.getUserName());
	user.setPassword(customer.getPassword());
	user.setFirstName(customer.getFirstName());
	user.setLastName(customer.getLastName());
	user.setGender(customer.getGender());
	user.setPhoneNumber(customer.getPhoneNumber());
	Branch custBranch = userAccountRepo.findUserBranchId(customer.getMarketer().getId());
	user.setBranch(custBranch);
	user.setEmail(customer.getEmail());
	user.setCreatedDate(customer.getRegistrationDate());
//	user.setUserRole(customer.getUserRole());
	
	userAccountRepo.save(user);
	
	return customer;
	
	}
    
	
	public Resource loadFileAsResource(String fileName) throws Exception {
    Path filePath = fileStorageLocation.resolve(fileName).normalize();
    Resource resource = new UrlResource(filePath.toUri());
    if(resource.exists()) {
    return resource;
      }else {
    throw new Exception("File not found " + fileName);
      }
    }
 
	
	public List<CustomerDTO> loggedInCustomerName(Customer customer){
	List<CustomerDTO> loggedInCustomer = custRepo.displayCustomerFirstAndLastName(customer);
	return loggedInCustomer;
	}
	
	
	 public Customer findByUserName(String username) {
	 return custRepo.findByUserName(username);
     }
	
	public Optional<Customer> getCustomer(Long id){
    return custRepo.findById(id);
	}
	
	public Customer getCustomerById(Long id) {
    return custRepo.getById(id);
    }   

	public void updateCustomer(Long id, Customer customer){
    custRepo.save(customer);
	}

    public void deleteCustomer(Long id) {
	custRepo.deleteById(id);
	}

    public List<CustomerDTO> getFirstNameAndLastNameOnly() throws Exception {
    List<CustomerDTO> customerName = custRepo.findByFirstNameAndLastName();
    return customerName;
    }
    
    public List<CustomerDTO> loggedInMarketerDropdown(User marketer) {
    List<CustomerDTO> loggedInMarketerCustomersDropdown = custRepo.findMarketerCustomersDropdown(marketer);
    return loggedInMarketerCustomersDropdown;
    }
    
    public List<Customer> findByMarketer(User marketer, Pageable pageable){
    List<Customer> marketerList = custRepo.findByMarketer(marketer);
    return marketerList;
    }
    
    public Long getTotalCustomer(){
    Long customerCount = custRepo.getTotalCustomer();
    return customerCount;
    }
	
    public Long getTotalCustomerByMarketer(User marketer){
    Long marketerCustomerCount = custRepo.getTotalCustomerByMarketer(marketer);
    return marketerCustomerCount;
    }
    
	
	public List<Customer> findByBranch(Branch branch, Pageable pageable) { 
	List<Customer> branchList = custRepo.findByUserBranch(branch); 
	return branchList; 
	}
	 
    public Long totalBranchCustomer(Branch branch) {
	Long branchCustomers = custRepo.getTotalBranchCustomer(branch);
	return branchCustomers;
	}

}
