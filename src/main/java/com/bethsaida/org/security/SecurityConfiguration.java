



package com.bethsaida.org.security;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.bethsaida.org.config.ActivityInterceptor;
import com.bethsaida.org.service.ActivityService;
import com.bethsaida.org.service.UserAccountService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration  {
	
	 @Autowired
	 ActivityInterceptor activityInterceptor;
	 
     public void addInterceptors(InterceptorRegistry registry) {
	 addInterceptors(registry); 
	 registry.addInterceptor(activityInterceptor); }
	 
	 
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
     exposeDirectory("customer-photos", registry);
     }
	
	private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
         registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
	
	    @Bean 
	    CorsConfigurationSource corsConfigurationSource() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	    }
	 
	 @Bean 
	 public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,  UserDetailsService userDetailsService) {
		 DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		 daoAuthenticationProvider.setUserDetailsService(userDetailsService); 
		 return daoAuthenticationProvider; 
	  }
		 
	 

	@Configuration
	@Order(1)
	public static class UserConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		
//		@Autowired
	//	private UserDetailsService myUserDetailsService;
		
		@Autowired 
		private JwtRequestFilter jwtRequestFilter;
		
		
		@Autowired
		private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
		
	 
	   @Bean
	   public BCryptPasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
	   }
	
		
		  //@Primary
		  @Bean
		  @Override 
		  public UserDetailsService userDetailsService() {
		  return new JwtUserDetailsService(); 
		  }
		 
		 
	 
		/*
		 * @Bean public DaoAuthenticationProvider
		 * daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService
		 * userDetailsService) { DaoAuthenticationProvider daoAuthenticationProvider =
		 * new DaoAuthenticationProvider();
		 * daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		 * daoAuthenticationProvider.setUserDetailsService(userDetailsService); return
		 * daoAuthenticationProvider; }
		 */
		 
	 
	 

	 @Override
	 @Bean
	 public AuthenticationManager authenticationManagerBean() throws Exception {
	     return super.authenticationManagerBean();
	 }
	 
	  @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
		 }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.cors().and().csrf().disable()
	  .authorizeRequests()
      .antMatchers( "/validate",  
    		   "approveDisbursement/**", "/registerUser", "activateInvestment/**"
    		,"/accountnumber/**", "/authenticate"
    		   )
      .permitAll()
	//  .antMatchers("activateInvestment/**").hasRole("MANAGING DIRECTOR")
	  
	  .anyRequest().authenticated()
	  .and()
	
	 .exceptionHandling()
	 .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	  
	 .and()
	 .formLogin().permitAll()
	  
	  .and()
	  .sessionManagement()
	  .maximumSessions(1)
	  .and()
	  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	  .and()
	  .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")
	  .deleteCookies("JSESSIONID");
	  http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
		  
	}
	
	@Configuration
	@Order(2)
	public static class CustomerConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired 
		private CustomerJwtRequestFilter customerJwtRequestFilter;
		
	//	@Autowired
	//	private UserDetailsService myUserDetailsService;
		
		@Autowired
		private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
		
	 
	   @Bean
	   public BCryptPasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder();
	   }
	
		
		  @Bean 
		  @Override
		  public UserDetailsService userDetailsService() { 
		  return new JwtCustomerDetailsService(); }
		 
		
//		 @Bean 
//		 public DaoAuthenticationProvider  daoAuthenticationProvider(PasswordEncoder passwordEncoder,  UserDetailsService userDetailsService) { 
//		  DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		  daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//		  daoAuthenticationProvider.setUserDetailsService(userDetailsService); 
//		  return daoAuthenticationProvider; }
//		 
	    
	    @Bean
	    public CustomerJwtTokenUtil customerJwtTokenUtil() {
	    return new CustomerJwtTokenUtil();
	    }
	 
	 
	 @Override
	 @Bean
	 public AuthenticationManager authenticationManagerBean() throws Exception {
	     return super.authenticationManagerBean();
	 }
	 
	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
         .passwordEncoder(passwordEncoder());
    
		 }
		
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
		  http.cors().and().csrf().disable()
		  .authorizeRequests()
	      .antMatchers( "/authenticate", "/registerCustomer")
	      .permitAll()
		//  .antMatchers("activateInvestment/**").hasRole("MANAGING DIRECTOR")
		  
		  .anyRequest().authenticated()
		  .and()
		
		 .exceptionHandling()
		 .authenticationEntryPoint(jwtAuthenticationEntryPoint)
		  
		 .and()
		 .formLogin().permitAll()
		  
		  .and()
		  .sessionManagement()
		  .maximumSessions(1)
		  .and()
		  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		  .and()
		  .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")
		  .deleteCookies("JSESSIONID");
		  http.addFilterBefore(customerJwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		
	}
    
    }
	
     