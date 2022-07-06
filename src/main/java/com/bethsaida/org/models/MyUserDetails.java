package com.bethsaida.org.models;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import com.bethsaida.org.service.MyUserDetailsService;

public class MyUserDetails implements UserDetails {
	
   private static final long serialVersionUID = -2456373662763511974L;

    private Long id;
	private String username;
	private String password;
	private String email;
	private boolean isEnabled;
    private boolean isLocked;
	private Collection<? extends GrantedAuthority> authorities;
	
	public MyUserDetails () {
		
	}
	
    public MyUserDetails(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean isEnabled, boolean isLocked) 
	
	{
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.isEnabled = isEnabled;
		this.isLocked = isLocked;
	}
    
    
	
	public static MyUserDetails build(User user) {
		
		List<GrantedAuthority> authorities = user.getUserRole()
				.stream().map(role -> new SimpleGrantedAuthority
						(role.getName()))
				.collect(Collectors.toList());
		
		return new MyUserDetails(user.getId(),
				user.getUserName(),
				user.getEmail(),
				user.getPassword(),
				authorities,
				user.isEnabled(),
				user.isLocked());
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
    
    
    public Long getId() {
		return id;
  }
    
    public String getEmail() {
    	return email;
    }
    
    @Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		if(this.isLocked == true) {
			return false;
		} else {
		return true;
		}
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		if (this.isEnabled == false) {
			return false; 
			} else {
		return true;
		}
	}

	

}
