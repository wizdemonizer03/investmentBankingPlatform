package com.bethsaida.org.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bethsaida.org.service.CustomerAccountService;
import com.bethsaida.org.service.UserAccountService;

import io.jsonwebtoken.ExpiredJwtException;

@Component("CustomerJwtRequestFilter")
public class CustomerJwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private CustomerJwtTokenUtil customerJwtTokenUtil;
	
	@Autowired
	private JwtCustomerDetailsService jwtCustomerAccountService;
	
//	@Autowired
//	public CustomerJwtRequestFilter( @Lazy final JwtCustomerDetailsService jwtCustomerAccountService) {
//		this.jwtCustomerAccountService = jwtCustomerAccountService;
//	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		
		if (requestTokenHeader != null) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = customerJwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} 
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		      
			UserDetails userDetails = this.jwtCustomerAccountService.loadUserByUsername(username);
    	if (customerJwtTokenUtil.validateToken(jwtToken, userDetails)) {
    		
    		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
			usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else {
                System.out.println("Not Valid Token");
            }

		}
		
		filterChain.doFilter(request, response);
		
	}
	
	
	
	

}
