package com.bethsaida.org.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.MyUserDetails;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.UserAccountRepository;

@Transactional
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired	
    private UserAccountRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(username);
    if(user == null) {
    throw new UsernameNotFoundException("User not found");
	}
        
    return MyUserDetails.build(user);
    }

}
