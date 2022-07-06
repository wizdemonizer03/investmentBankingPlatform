package com.bethsaida.org;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.bethsaida.org.config.ActivityInterceptor;
import com.bethsaida.org.models.MyUserDetails;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.UserAccountRepository;
import com.bethsaida.org.security.SpringSecurityAuditorAware;


@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
@EnableWebMvc
@EnableCaching
@ComponentScan
@EnableJpaRepositories
@SpringBootApplication
public class BethsaidaApplication {
	
	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder application) { return
	 * application.sources(SpringApplicationBuilder.class); }
	 */
	
    public static void main(String[] args){
    SpringApplication.run(BethsaidaApplication.class, args);
       }
    
    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
	}
    
    @Bean
    public AuditorAware<String> auditorAware() {
    return new SpringSecurityAuditorAware();
    }
    
	
	@Bean 
	public ActivityInterceptor handlerInterceptor() { 
    return new ActivityInterceptor(); 
    }
	 
   
	
	/*
	 * @Bean public MappedInterceptor activityInterceptor() { return new
	 * MappedInterceptor(null, new ActivityInterceptor()); }
	 */
    
   }
