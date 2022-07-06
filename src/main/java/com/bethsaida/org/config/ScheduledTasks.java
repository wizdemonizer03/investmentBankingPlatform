package com.bethsaida.org.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bethsaida.org.models.Activity;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.service.ActivityService;
import com.bethsaida.org.service.EmailService;
import com.bethsaida.org.service.InvestmentService;


@Component
public class ScheduledTasks {

	 private final ActivityService activityService;
     
	
	 private final InvestmentService investmentRepository;
	 
	 private final EmailService emailService;
	 
	 
	    @Autowired
	    public ScheduledTasks(ActivityService activityService, InvestmentService investmentRepository,
	    		EmailService emailService) {
	        this.activityService = activityService;
	        this.investmentRepository = investmentRepository;
	        this.emailService = emailService;
	    }

	    @Scheduled(cron = "0 1 15 * * ?")
	    void deleteActivitiesOlderThanAMonth() {
	        Activity firstActivity = this.activityService.findFirst();
	        List<Activity> activityList = this.activityService.findAll();
	        for (Activity activity : activityList) {
	            if (!activity.getId().equals(firstActivity.getId())) { // exclude first activity
	                if (this.getDateDiff(activity.getCreatedDate(), new Date(), TimeUnit.DAYS) >= 31) {
	                    this.activityService.delete(activity.getId());
	                    System.out.println("Deleted activity! " + activity.getId());
	                }
	            }
	        }
	        System.out.println("Deleted old user activities!");
	    }
	
	
	    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	        long diffInDays = date2.getTime() - date1.getTime();
	        return timeUnit.convert(diffInDays,TimeUnit.DAYS);
	    }
	    

	    @Scheduled(cron="0 0 0 * * *", zone = "Africa/Lagos")
	    void changeInvestmentStatus() throws AddressException, MessagingException, IOException {
	    LocalDate today = LocalDate.now();
	    List<Investment> investmentList = this.investmentRepository.getInvestment();
	     for(Investment investment : investmentList) {
	    	if (investment.getMaturityDate().compareTo(today) == 0) {
	           investmentRepository.setInvestmentStatus(investment.getId());
         		emailService.sendMaturityNotification();
	    	}
	    }
	     System.out.println("Investment Status Updated");
	    }
	    
	    
	    @Scheduled(cron="0 0 0 * * *", zone = "Africa/Lagos")
	    void pushInvestmentToRollOver() {
	    LocalDate today = LocalDate.now();
	    List<Investment> investmentList = this.investmentRepository.getInvestment();
		   for(Investment investment : investmentList)
		      if(ChronoUnit.DAYS.between(investment.getMaturityDate(), today) >= 1) {
	          investmentRepository.rollOverInvestment(investment.getId(), investment);
	    	}
		   System.out.println("Investment has been rolled over");
	    }
	    
	    
	    @Scheduled(cron="0 0 0 * * *", zone = "Africa/Lagos")
	    void calculateDailyMaturityInterest() {
	    List<Investment> investmentList = this.investmentRepository.getInvestment();	
	    for(Investment investment : investmentList) {
	    investmentRepository.calculateDailyMaturityInterests(investment);
	     }
	    System.out.println("Current Interest Calculated");
	    
	    }
	    
	    
}
