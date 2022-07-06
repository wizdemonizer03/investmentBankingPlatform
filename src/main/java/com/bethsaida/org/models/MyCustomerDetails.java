package com.bethsaida.org.models;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyCustomerDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5087929420394311276L;

	private Long id;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public MyCustomerDetails() {
		
	}
	
	public MyCustomerDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities ) {
	
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

     public static MyCustomerDetails build(Customer customer) {
	   
	
    	 List<GrantedAuthority> authorities = customer.getCustomerRole()
 				.stream().map(role -> new SimpleGrantedAuthority
 						(role.getName()))
 				.collect(Collectors.toList());
		 
	   
		return new MyCustomerDetails(customer.getId(), 
				customer.getUserName(), customer.getPassword(), authorities);
	}


   @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
   
    @Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
