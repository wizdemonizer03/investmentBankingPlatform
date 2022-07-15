package com.bethsaida.org.controllers;

import java.math.BigDecimal;
import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.CustomerDTO;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.MyUserDetails;
import com.bethsaida.org.models.User;
import com.bethsaida.org.models.UserDTO;
import com.bethsaida.org.security.JwtRequestFilter;
import com.bethsaida.org.security.JwtResponse;
import com.bethsaida.org.security.JwtTokenUtil;
import com.bethsaida.org.security.JwtUserDetailsService;
//import com.bethsaida.org.service.MyUserDetailsService;
import com.bethsaida.org.service.UserAccountService;

//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin()
@RestController
public class UserController {
	
	     @Autowired
	     JwtUserDetailsService jwtUserDetailsService;
	
	     @Autowired
	     private AuthenticationManager authenticationManager;

	     @Autowired
	     private JwtTokenUtil jwtTokenUtil;
	
	     @Autowired
         UserAccountService userAccountService;
	     
	   
	    @PostMapping(value="/validate")
	    public ResponseEntity <?> createAuthenticationToken( @RequestBody MyUserDetails
	    authenticationRequest) throws Exception {  
	    authenticate(authenticationRequest.getUsername(),  
	    authenticationRequest.getPassword()); 
	
	    final MyUserDetails userDetails =  (MyUserDetails)   jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	   
	    final String token = jwtTokenUtil.generateToken(userDetails);
	  
	    return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK) ;
	    }
	    private void authenticate(String username, String password) throws Exception {
	    try {
	    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	    } catch (DisabledException e) {
	    throw new Exception("USER_DISABLED", e);
	    } catch (BadCredentialsException e) {
	    throw new Exception("INVALID_CREDENTIALS", e);
	    } catch (LockedException e) {
	    	throw new Exception("ACCOUNT_LOCKED", e );
	    }
			}
	     
	     @GetMapping(value="/user/{username}")
	     public User findByUsername(@PathVariable String username){
	     return userAccountService.findByUserName(username);
         }
	    
	     @GetMapping(value="/list/Users")
		 public List<User> getUsers(){
		 return userAccountService.getUsers();
		 }
	     
	     @GetMapping (value="pagedList/Users") 
	 	 public Page<User> getPagedInvestmentCount(Pageable pageable){
	 	 return userAccountService.getPagedUsersCount(pageable);
	 	 }
	     
	     @GetMapping(value = "/User/{id}")
	     public User getUser(@PathVariable Long id){
	 	 return userAccountService.getOneUser(id);
	 	 }
	 
	     @PostMapping(value="/registerUser", consumes = {MediaType.APPLICATION_JSON_VALUE} )
	     public User registerUser (@RequestBody User user, @DateTimeFormat(pattern = "yyyy-MM-dd")Date createdDate) throws Exception  {
	     if (userAccountService.exist(user.getEmail())){
	     throw new Exception("User already exist for this email");
	      }
	     userAccountService.RegisterUser(user);
	     return user;
	 	 }
	     
	     @PutMapping(value="/activateUser/{id}")
	     public void activateUser(@PathVariable Long id) {
	     userAccountService.activateUser(id);
	     }
	     
	     @PutMapping (value="/restrictUser/{id}")
	     public void restrictUser(@PathVariable Long id) {
	     userAccountService.restrictUser(id);
	     }
	     
	     @DeleteMapping(value="/deleteUser/{id}")
	     public void deleteUser(@PathVariable Long id){
	     userAccountService.deleteUser(id);
	     }
	      
	     @PutMapping(value="/update/user/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	     public void updateUser (@RequestBody User user, @PathVariable Long id){
	     userAccountService.updateUser(id,user);
	     }
	     
	     
	     @GetMapping(value="/findUsersBranch/{id}")
	     public Branch findUserBranchId(@PathVariable Long id) {
	     return userAccountService.findUserBranchId(id);
	     }
	 
	     @PostMapping(value="/setMarketerMonthlyTarget/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	     public String marketerTarget(@RequestParam(value = "target", required = false) BigDecimal target, @PathVariable Long id ) {
	     userAccountService.setMarketerTarget(id, target);
	     return "Target Saved and Transactions History Updated";
	     }
	     
	     @GetMapping(value="/marketerMonthlyTarget")
	     public BigDecimal MarketerMonthlyTarget(){
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
	     User loggedInUser = userAccountService.findByUserName(authentication.getName());
	     return userAccountService.getMarketerMonthlyTarget(loggedInUser);
	     }
	     
	     @GetMapping(value="/marketerName")
	     public List<UserDTO> getMarketerNameOnly() throws Exception{
	     return userAccountService.getMarketerNameOnly();
	     }
	     
	     @GetMapping(value="/loggedInMarketerName")
	     public List<UserDTO> loggedInMarketer(Principal principal){
	     User loggedInUser = userAccountService.findByUserName(principal.getName());
	     return userAccountService.loggedInMarketerName(loggedInUser);
	     }
	     
	     @GetMapping(value="/loggedInUserName")
	     public List<UserDTO> loggedInUser(Principal principal){
	     User loggedInUser = userAccountService.findByUserName(principal.getName());
	     return userAccountService.loggedInUserName(loggedInUser);
	     }
	     
	     @GetMapping(value="/BranchMarketersList")
	     public List<User> marketersListForDirectors(Principal principal, Pageable pageable){
	     User loggedInUser = userAccountService.findByUserName(principal.getName());
	     Branch branchId = loggedInUser.getBranch();
	     return userAccountService.marketerList(branchId, pageable); 
	     }
	     
	     @GetMapping(value="/AllBranchMarketers")
	     public List<User> allBranchMarketers(Pageable pageable){
	     List<User> allMarketers = userAccountService.allBranchMarketers(pageable);
	     return allMarketers;
	     }
	     
	     @GetMapping(value="/managementList")
	     public List<User> managementList(){
	     return userAccountService.managementList();
	     }
	     
	     @GetMapping(value="/centralUserTotal")
         public Long getTotalUserCount() {
         return userAccountService.getTotalUsers();
         }
	     
	     @GetMapping(value="/branchUsersTotal")
	     public Long getTotalBranchUsers(Principal principal){
	     User loggedInUser = userAccountService.findByUserName(principal.getName());
	     Branch branchId = loggedInUser.getBranch();
	     return userAccountService.getTotalBranchUsers(branchId);
	    }
	     
	     @GetMapping(value="/branchMarketersTotal")
	     public Long getBranchMarketersTotal() {
	     Long marketersTotal = userAccountService.getTotalAllBranchMarketers();
	     return marketersTotal;
	     }
	     

	     
	
    }
