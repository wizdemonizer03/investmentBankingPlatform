package com.bethsaida.org.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;


public class SpringSecurityAuditorAware implements AuditorAware<String> {
  
  public Optional<String> getCurrentAuditor() {
  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  if (null != authentication) { 
  return Optional.ofNullable(authentication.getName()); 
  } 
  return Optional.empty(); 
  }
	 
}
