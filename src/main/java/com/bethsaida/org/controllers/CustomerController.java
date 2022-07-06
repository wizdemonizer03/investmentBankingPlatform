package com.bethsaida.org.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bethsaida.org.models.*;
import com.bethsaida.org.repository.CustomerAccountRepo;
import com.bethsaida.org.security.CustomerJwtTokenUtil;
import com.bethsaida.org.security.JwtCustomerDetailsService;
import com.bethsaida.org.security.JwtResponse;
import com.bethsaida.org.service.*;



//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin()
@RestController
public class CustomerController {
	
	 private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
 
	 @Autowired
	 JwtCustomerDetailsService jwtCustomerDetailsService;
	 
	 @Autowired
	 CustomerAccountRepo customerAccountRepository;
	
	 @Autowired
	 CustomerAccountService customerRepo;
	    
	 @Autowired
	 UserAccountService  userRepo;
	 
	 @Autowired
	 CustomerJwtTokenUtil customerJwtTokenUtil;
	 
	 @Autowired
	 private AuthenticationManager authenticationManager;
	
	
	 @GetMapping(value="list/Customers")
	 public List<Customer> getCustomers(){
	 return customerRepo.getCustomers();
	 }
	 
	 @GetMapping (value="pagedList/Customer") 
	 public Page<Customer> getPagedCustomerCount(Pageable pageable){
	 return customerRepo.getPagedCustomerCount(pageable);
	 }
	 
	 @GetMapping(value = "list/customer/{id}")
	 public Optional<Customer> getCustomer (@PathVariable Long id){
	 return customerRepo.getCustomer(id);
	 }
	 
	 @GetMapping (value = "customer/{id}")
	 public Customer getCustomerById(@PathVariable Long id){
	 return customerRepo.getCustomerById(id);
	 }
	     
	 @PostMapping(value="/registerCustomer", consumes = {MediaType.APPLICATION_JSON_VALUE})
	 public Customer registerCustomer(@RequestBody Customer customer, 
	    		                                @DateTimeFormat(pattern = "yyyy-MM-dd")Date registrationDate, 
	    		                                @DateTimeFormat(pattern = "yyyy-MM-dd")Date dateOfBirth) throws Exception{
	if (customerRepo.exist(customer.getEmail())){
	   throw new Exception("Customer already exist for this email");
		    }
	 customerRepo.registerCustomer(customer);
	 return customer;
	 }
	 
	 
	 @PostMapping(value="/{id}/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	 public Customer uploadDocs(@ModelAttribute("customer") Customer customer, 
			                    @RequestParam(name="file", required = false) MultipartFile multipartFormfile,
			                    @RequestParam(name="file1", required = false) MultipartFile multipartDocfile,
			                    @RequestParam(name="file2", required = false) MultipartFile multipartAdditionalfile,
	                            @PathVariable Long id) throws java.io.IOException, IOException {
	String formFile = StringUtils.cleanPath(multipartFormfile.getOriginalFilename());
	String docFile = StringUtils.cleanPath(multipartDocfile.getOriginalFilename());
	String additionalImage = StringUtils.cleanPath(multipartAdditionalfile.getOriginalFilename());
	customer.setFormFile(formFile);
	customer.setDocFile(docFile);
	customer.setAdditionalImage(additionalImage);
	customerAccountRepository.save(customer);
	String uploadDir = "customer-photos/";	
    FileUploadUtil.saveFile(uploadDir, formFile, multipartFormfile);
	FileUploadUtil.saveFile(uploadDir, docFile, multipartDocfile);
	FileUploadUtil.saveFile(uploadDir, additionalImage, multipartAdditionalfile);
	return customer;
	}
	 
	 @GetMapping(value="/files/{fileName:.+}")
	 public ResponseEntity<Resource> getFileFromResource(@PathVariable String fileName,
			                            HttpServletRequest request) throws Exception {
	 
	 Resource resource = customerRepo.loadFileAsResource(fileName);
	 
	 String contentType = null;
     try {
         contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
     } catch (IOException ex) {
         logger.info("Could not determine file type.");
     }
	 if(contentType == null) {
         contentType = "application/octet-stream";
     }
	 
	 return ResponseEntity.ok()
             .contentType(MediaType.parseMediaType(contentType))
    //         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
             .body(resource);
 
	 }
	 
	 @GetMapping(value="/custom/{username}")
     public Customer findByUsername(@PathVariable String username){
     return customerRepo.findByUserName(username);
     }
	 
	 @GetMapping(value="/loggedInCustomerName")
	 public List<CustomerDTO> loggedInCustomer(Principal principal){
	 Customer loggedInUser = customerRepo.findByUserName(principal.getName());
	 return customerRepo.loggedInCustomerName(loggedInUser);
	     }
	 
	 @PostMapping(value = "/authenticate")
	 public ResponseEntity <?> createAuthenticationToken( @RequestBody MyCustomerDetails
			    authenticationRequest) throws Exception {  
			    authenticate(authenticationRequest.getUsername(),  
			    authenticationRequest.getPassword()); 
			
			    final MyCustomerDetails customerUserDetails =  (MyCustomerDetails) jwtCustomerDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			   
			    final String token = customerJwtTokenUtil.generateToken(customerUserDetails);
			  
			    return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK) ;
			    }
			    private void authenticate(String username, String password) throws Exception {
			    try {
			    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			    } catch (DisabledException e) {
			    throw new Exception("USER_DISABLED", e);
			    } catch (BadCredentialsException e) {
			    throw new Exception("INVALID_CREDENTIALS", e);
			    }
					}
	     
	 @DeleteMapping(value="/deleteCustomer/{id}")
	 public void deleteCustomer(@PathVariable Long id){
	 customerRepo.deleteCustomer(id);
	 }
	  
	  @PostMapping(value="update/customer/{id}",consumes= {"application/json"})
	  public void updateCustomer (@RequestBody Customer customer, @PathVariable Long id){
	  customerRepo.updateCustomer(id,customer);
	  }
	
	  @GetMapping(value="loggedInMarketerCustomersDropdown")
	  public List<CustomerDTO> getFirstNameAndLastNameOnly(Principal principal) {
	  User loggedInUser = userRepo.findByUserName(principal.getName());
	  return customerRepo.loggedInMarketerDropdown(loggedInUser);
	  }
	  
      @GetMapping(value="customerFirstAndLastName")
	  public List<CustomerDTO> getFirstNameAndLastNameOnly() throws Exception {
	  return customerRepo.getFirstNameAndLastNameOnly();
	  }
          
      @GetMapping(value="marketers/customers")
      public List<Customer> getListByMarketerName(Pageable pageable) throws Exception{
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
      User loggedInUser = userRepo.findByUserName(authentication.getName());
      return customerRepo.findByMarketer(loggedInUser,pageable );
      }
         
      @GetMapping(value="/centralCustomerTotal")
      public Long getTotalCustomerCount(){
      return customerRepo.getTotalCustomer();
      }
         
      @GetMapping(value="/marketerCustomerTotal")
      public Long getTotalCustomerByMarketerCount(){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
      User loggedInUser = userRepo.findByUserName(authentication.getName());
      return customerRepo.getTotalCustomerByMarketer(loggedInUser);
      }
         
	  @GetMapping(value="branch/customers") 
	  public List<Customer> getListByBranch(Principal principal,Pageable pageable){ 
	  User loggedInUser = userRepo.findByUserName(principal.getName());
	  Branch branchId = loggedInUser.getBranch();
	  return customerRepo.findByBranch(branchId,pageable); 
	  }
	 
	  @GetMapping(value="/branchCustomersCount")  
	  public Long getBranchCustomersCount(Principal principal) {
	  User loggedInUser = userRepo.findByUserName(principal.getName());	
	  Branch branchId = loggedInUser.getBranch();
	  return customerRepo.totalBranchCustomer(branchId);
	  }
         
  }