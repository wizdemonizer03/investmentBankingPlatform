package com.bethsaida.org.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.MyCustomerDetails;
import com.bethsaida.org.repository.CustomerAccountRepo;

@Transactional
@Service
public class JwtCustomerDetailsService implements UserDetailsService {
	  
	    @Autowired	
		private CustomerAccountRepo custRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Customer customer = custRepo.findByUserName(username);
		    if(customer == null) {
		    throw new UsernameNotFoundException("User not found");
			}
    return MyCustomerDetails.build(customer);
	}  

}
