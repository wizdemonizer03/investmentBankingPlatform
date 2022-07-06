package com.bethsaida.org.config;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bethsaida.org.models.Activity;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.UserAccountRepository;
import com.bethsaida.org.service.ActivityService;


//@Component
public class ActivityInterceptor implements HandlerInterceptor {

	    @Autowired
	    UserAccountRepository userRepo;
	
	    @Autowired
	    private ActivityService activityService;

	/*
	 * @Autowired public ActivityInterceptor(ActivityService activityService) {
	 * this.activityService = activityService; }
	 */
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
	        	
	    	String userAgent = request.getHeader("User-Agent");
	        if (userAgent == null) userAgent = request.getHeader("user-agent");
	        String expires = response.getHeader("Expires");
	        Activity activity = new Activity();
	        activity.setIp(this.getClientIpAddress(request));
	        activity.setExpires(expires);
	        activity.setRequestMethod(request.getMethod());
	        activity.setUrl(request.getRequestURI());
	        
	        String pattern = "[\\s\\S]+";
	        
	        Matcher m = Pattern.compile(pattern).matcher(userAgent);
	       if (m.find()) {
	            activity.setUserAgent(m.group());
	       }
	        
	        if (SecurityContextHolder.getContext().getAuthentication() != null &&
	                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
	                //when Anonymous Authentication is enabled
	                !(SecurityContextHolder.getContext().getAuthentication()
	                        instanceof AnonymousAuthenticationToken)) {
	        	
			
			  Authentication authentication =
			  SecurityContextHolder.getContext().getAuthentication(); User loggedInUser =
			  userRepo.findByUserName(authentication.getName());
			 
	        	
	       //     User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            activity.setUser(loggedInUser);
	            if (!activity.getUrl().contains("image") && !activity.getUrl().equals("/"))
	                activity = activityService.save(activity);
	            return preHandle(request, response, handler);
	        } else if (activity.getUrl().equals("/")) {
	            Activity existingActivity = this.activityService.findFirst();
	            if (existingActivity != null) {
	                activity.setId(existingActivity.getId());
	                activity.setCreatedDate(existingActivity.getCreatedDate());
	                long totalVisitors = existingActivity.getTotalVisitors();
	                activity.setTotalVisitors(++totalVisitors);
	            } else
	                activity.setTotalVisitors(1L);
	            activity = this.activityService.save(activity);
	        }
	        return preHandle(request, response, handler);
	    }

	    private String getClientIpAddress(HttpServletRequest request) {
	        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	        if (xForwardedForHeader == null) {
	            return request.getRemoteAddr();
	        } else {
	            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
	            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
	            // we only want the client
	            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	        }
	    }
   }
	    

