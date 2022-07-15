package com.bethsaida.org.service;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bethsaida.org.models.AccountNumberPointer;
import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.Disbursements;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.InvestmentDTO;
import com.bethsaida.org.models.InvestmentStatus;
import com.bethsaida.org.models.InvestmentTopUpStatus;
import com.bethsaida.org.models.RollOverStatus;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.DisbursementsRepository;
import com.bethsaida.org.repository.InvestmentRepository;


@Service
public class InvestmentService {
	
	
	private final float taxWht = (float) 10/100;
	
	private final float  preLiquidationCharges = (float) 25/100;
	
	private Path investmentFileStorageLocation ;
	
    String uploadDir = "customer-photos/" ;
    
    @Autowired	
    private AccountNumberPointerService accountNumberPointerService;
	
	@Autowired	
	private InvestmentRepository investmentRepository;
	
	@Autowired	
	private DisbursementsRepository disbursementRepository;
	
	@Autowired
	private EmailService emailService;
	
	public InvestmentService() throws Exception { 
		this.investmentFileStorageLocation = Paths.get(uploadDir) .toAbsolutePath().normalize();
		   try { 
			  Files.createDirectories(this.investmentFileStorageLocation); 
			  } catch (Exception ex) { 
				  throw new
		  Exception("Could not create the directory where the uploaded files will be stored."
		  , ex); 
				  } 
		  }
	
	
	public List<Investment> findByDateBetween(LocalDate startDate, LocalDate endDate) {
	return investmentRepository.findByMaturityDateBetween(startDate, endDate);
	}
    
    
    public List<Investment> findByAccountNumber(int accountNumber){
    return investmentRepository.findByAccountNumber(accountNumber);
    }

   // @Cacheable("investments")
    public List<Investment> getInvestment() {
	return investmentRepository.findAll();
	}
    
    public Page<Investment> getPagedInvestmentCount(Pageable pageable){
    return investmentRepository.findAllByOrderByIdDesc(pageable);
    }
    
	private int nextAccountNumber(){
	int newAccount = accountNumberPointerService.getAccountNumberValue();
	AccountNumberPointer acctFromDb = accountNumberPointerService.findById(accountNumberPointerService.getAccountIdValue());
	acctFromDb.setNextAccountNumber(newAccount +1);
	accountNumberPointerService.updateAccountNumberPointer(acctFromDb.getId(), acctFromDb);
	return newAccount;
	 }

	public Investment CreateInvestment(Investment investment) throws AddressException, MessagingException, IOException {
    investment.setAccountNumber(nextAccountNumber());
    investment.setStatus(InvestmentStatus.Inactive);
    emailService.sendFirstLineApprovalEmail();
    return investmentRepository.save(investment);
    }
	
    
    public Investment getInvestmentById(Long id) {
    return investmentRepository.getById(id);
    }   
      
    public Investment firstLevelInvestmentActivation(Long id, Investment investment) throws AddressException, MessagingException, IOException {
    investment.setStatus(InvestmentStatus.Pending);
    emailService.sendSecondLineApprovalEmail();
    return investmentRepository.save(investment);
    }
    
    
    public Investment activateInvestment(Long id, Investment investment)
      {
    	 if (investmentRepository.findById(id).isPresent()) 
    	{
    	 investment.setStartDate(LocalDate.now());
        	 if(investment.getTenure().equalsIgnoreCase("365")) 
    	     {
    		  investment.setMaturityDate(investment.getStartDate().plusDays(365)); 
    		  } 
        	 else if (investment.getTenure().equalsIgnoreCase("120"))
    		  {
    		  investment.setMaturityDate(investment.getStartDate().plusDays(120));
    		    } 
    		 else if (investment.getTenure().equalsIgnoreCase("180"))
    		   {
    		  investment.setMaturityDate(investment.getStartDate().plusDays(180));
    		     } 
    		  
    		 else if (investment.getTenure().equalsIgnoreCase("90"))
    		    {
    			  investment.setMaturityDate(investment.getStartDate().plusDays(90));
    			  } 
    		 else if   (investment.getTenure().equalsIgnoreCase("30"))
    		          {
    				  investment.setMaturityDate(investment.getStartDate().plusDays(30));
    				   } 
    		 else if (investment.getTenure().equalsIgnoreCase("60"))
        		      {
        			  investment.setMaturityDate(investment.getStartDate().plusDays(60));
        			    } 
    	 
    	 LocalDate startDate = investment.getStartDate(); 
         LocalDate currentDate = LocalDate.now(); 
 		 long noOfDaysBetween = ChronoUnit.DAYS.between(startDate, currentDate); 
 		 BigDecimal CurrentInterest =  investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
 				  .multiply( new BigDecimal (noOfDaysBetween))
 				  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP) ;
 		 
 		 investment.setCurrentInterest(CurrentInterest);
 		 
 	     
 	   	LocalDate startDateMature = investment.getStartDate(); 
 	  	  LocalDate maturityDate = investment.getMaturityDate();
 	  	  long noOfDaysBetweenMature = ChronoUnit.DAYS.between(startDateMature, maturityDate); 
 	  	  BigDecimal initialMaturityInterest = investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
 	  			  .multiply( new BigDecimal (noOfDaysBetweenMature))
 	  			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP) ;
 	  	  
 	  	// get tax witholding from initial maturity interest
 	  	BigDecimal taxwitheld = initialMaturityInterest.multiply(new BigDecimal (taxWht));
 	  	
 	  	///get final maturity Interest by subtracting taxwitheld from initial maturity Interest
 	  	BigDecimal finalMaturityInterest = initialMaturityInterest.subtract(taxwitheld);
 	  	
 	    investment.setTaxWitholding(taxwitheld);
 	    
 	   	investment.setMaturityInterest(finalMaturityInterest);
 	   	
    	investment.setStatus(InvestmentStatus.Active);
  
    	//get today's date and compare with Maturity date to set Investment maturity status
    	
    	LocalDate today = LocalDate.now();
    	
    	if(today.compareTo(investment.getMaturityDate()) == 0)
    	 {
    		
    		investment.setStatus(InvestmentStatus.Matured);
    	}
    	
       return investmentRepository.save(investment)  ; 
    	
    	}
    	
    else {
    		return null;
    	}
  }
   
    
   public void approveDisbursement(Long id, Investment investment){
	 if(investment.getStatus().equals(InvestmentStatus.Matured))
	     {
		   if (investment.getWhtStatus().equalsIgnoreCase("Yes")){
           investment.setTaxWitholding(investment.getTaxWitholding());
           investment.setMaturityInterest(investment.getMaturityInterest());
			 } 
		   else if (investment.getWhtStatus().equalsIgnoreCase("No")){
				investment.setMaturityInterest(investment.getMaturityInterest()); 
			    }
			
	        }
	       investment.setRollOverDisbursement(investment.getRollOverDisbursement());
	       investment.setStatus(InvestmentStatus.Awaiting_Disbursement);
	       
	      investmentRepository.save(investment) ;
             
            //Add disbursement to Disbursements record
            Disbursements disbursements = new Disbursements();
 		    disbursements.setAccountNumber(investment.getAccountNumber());
 		    disbursements.setPrincipal(investment.getPrincipal());
 		    disbursements.setInvestment(investment);
 		    disbursements.setInvestmentPackage(investment.getInvestmentPackage());
 		 if(investment.getRollOverDisbursement().equalsIgnoreCase("Yes")) {
 		    disbursements.setAmountDisbursed(investment.getMaturityInterest());
 		 //   disbursements.setRollOverDisbursement(investment.isRollOverDisbursement());
 		    }
 		 else if(investment.getRollOverDisbursement().equalsIgnoreCase("No")) {
 		    	 disbursements.setAmountDisbursed(investment.getMaturityInterest()
 		    			 .add(investment.getPrincipal()));
 		  //  	disbursements.setRollOverDisbursement(investment.isRollOverDisbursement());
 		    }
 		    disbursements.setDisbursementDate(investment.getMaturityDate().plusDays(1));
 		   disbursements.setInvestmentStatus(InvestmentStatus.Awaiting_Disbursement);
 		    disbursementRepository.save(disbursements);
 		}
   
    @Transactional
    public void setInvestmentStatus(Long id) {
    investmentRepository.setInvestmentStatus(id);
    }
   
    public void liquidateInvestment(Long id, Investment investment) {
    investment.setStatus(InvestmentStatus.Awaiting_Liquidation);	
    investmentRepository.save(investment);
    }
    
    public void investmentExecutiveToAuditLiquidationReview(Long id, Investment investment) throws AddressException, MessagingException, IOException {
    investment.setStatus(InvestmentStatus.Awaiting_Review);
    emailService.sendLiquidationReviewNotification();
    investmentRepository.save(investment);
    }
    
    public void auditToManagementLiquidationApproval(Long id, Investment investment) {
    investment.setStatus(InvestmentStatus.Awaiting_Approval);
    investmentRepository.save(investment);
    }
   
    public void declineInvestment(Long id, Investment investment) {
    investment.setStatus(InvestmentStatus.Declined);	
    investmentRepository.save(investment);
    }
    
    @Transactional
    public Investment topUpInvestment(Long id, BigDecimal Amount, Investment investment) {
   // get the current number of days to calculate Interest as at today's date
    LocalDate startDate = investment.getStartDate(); 
    LocalDate currentDate = LocalDate.now();
    long noOfDaysForInitialInterest = ChronoUnit.DAYS.between(startDate, currentDate); 
    BigDecimal initialMaturityInterest = investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
 			  .multiply( new BigDecimal (noOfDaysForInitialInterest))
 			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP);
    //add amount to be topped to Initial principal
    investment.setInitialPrincipal(investment.getPrincipal());
    investment.setPrincipal(investment.getPrincipal().add(Amount));
    //get the number of days from today to maturity days to calculate final maturity Interest
    LocalDate currentDate2 = LocalDate.now();
    LocalDate maturityDate = investment.getMaturityDate();
    long noOfDaysForMaturity = ChronoUnit.DAYS.between(currentDate2, maturityDate);
    BigDecimal finalMaturityInterest = investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
			  .multiply( new BigDecimal (noOfDaysForMaturity))
			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP);
    //add initial maturity to final maturity
  //  investment.setMaturityInterest(initialMaturityInterest.add(finalMaturityInterest));
    investment.setNewMaturityInterest(initialMaturityInterest.add(finalMaturityInterest));
    investment.setTopUpAmount(Amount);
    investment.setDateOfTopUp(currentDate2);
    investment.setTopUpStatus(InvestmentTopUpStatus.YES);
    return investmentRepository.save(investment);
    }
    
    public Investment rollOverInvestment(Long id, Investment investment) {
	// roll over can only be done for matured investments
    if(investment.getStatus().equals(InvestmentStatus.Matured)) {
       //get and set roll over date as the investment start date
    	LocalDate currentDate = LocalDate.now();
        investment.setStartDate(currentDate);
          //check investment tenure to calculate maturity date
      if(investment.getTenure().equalsIgnoreCase("365")) {
		 investment.setMaturityDate(investment.getStartDate().plusDays(365)); 
		   } else if (investment.getTenure().equalsIgnoreCase("120")){
		     investment.setMaturityDate(investment.getStartDate().plusDays(120));
		       }else if (investment.getTenure().equalsIgnoreCase("180")){
		         investment.setMaturityDate(investment.getStartDate().plusDays(180));
		          } else if (investment.getTenure().equalsIgnoreCase("90")) {
			        investment.setMaturityDate(investment.getStartDate().plusDays(90));
			         }  else if   (investment.getTenure().equalsIgnoreCase("30")){
				       investment.setMaturityDate(investment.getStartDate().plusDays(30));
				        } else if (investment.getTenure().equalsIgnoreCase("60")) {
    		         	  investment.setMaturityDate(investment.getStartDate().plusDays(60));
    			            } 
    	investment.setAccountNumber(investment.getAccountNumber());
    	investment.setCategory(investment.getCategory());
    	investment.setCustomer_id(investment.getCustomer_id());
    	
    	//check if interest has been paid or not so as to compute the new principal
    	//if Interest has been paid , roll over only principal
    	//if Interest has not been paid , roll over principal + interest as new principal
    	
    	if (investment.getUpFrontMaturity().equalsIgnoreCase("Yes")) {
    	investment.setPrincipal(investment.getPrincipal());
    	
    	    long noOfDaysBetweenMature = ChronoUnit.DAYS.between(investment.getStartDate(), investment.getMaturityDate()); 
	  	  BigDecimal RollOverMaturityInterest = investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
	  			  .multiply( new BigDecimal (noOfDaysBetweenMature))
	  			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP) ;
    	
    	investment.setMaturityInterest(RollOverMaturityInterest);
    	} else if (investment.getUpFrontMaturity().equalsIgnoreCase("No")) {
    		
    		investment.setPrincipal(investment.getPrincipal().add(investment.getMaturityInterest()));
    		
    		long noOfDaysBetweenMature = ChronoUnit.DAYS.between(investment.getStartDate(), investment.getMaturityDate()); 
  	  	  BigDecimal newRollOverMaturityInterest = investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
  	  			  .multiply( new BigDecimal (noOfDaysBetweenMature))
  	  			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP) ;
    	investment.setMaturityInterest(newRollOverMaturityInterest);	
    	}
    	investment.setTaxWitholding(investment.getMaturityInterest().multiply(new BigDecimal (taxWht)));
    	investment.setRate(investment.getRate());
    	investment.setStatus(InvestmentStatus.Active);
    	investment.setRollOverStatus(RollOverStatus.YES);
    	
        }    
    
    	return investmentRepository.save(investment);
    }
    
    
	/*
	 * public void firstLevelApproveLiquidation(Long id, Investment investment) {
	 * investment.setStatus(InvestmentStatus.Pending_Liquidation);
	 * investmentRepository.save(investment); }
	 * 
	 */
    public void approveLiquidation(Long id, Investment investment) 
    
    {  
       if(investment.getStatus().equals(InvestmentStatus.Awaiting_Liquidation)){
       investment.setTaxWitholding(investment.getMaturityInterest()                     
	   			   .multiply(new BigDecimal (preLiquidationCharges))) ;
	          
	   investment.setMaturityInterest(investment.getMaturityInterest()
			.subtract (investment.getTaxWitholding()));
		      }
       investment.setStatus(InvestmentStatus.Awaiting_Disbursement);
       investmentRepository.save(investment);
    
       Disbursements disbursements = new Disbursements();
	   disbursements.setAccountNumber(investment.getAccountNumber());
	   disbursements.setPrincipal(investment.getPrincipal());
	   disbursements.setInvestment(investment);
	   disbursements.setInvestmentPackage(investment.getInvestmentPackage());
	   
	   if(investment.getUpFrontMaturity().equalsIgnoreCase("Yes")) {
		   disbursements.setAmountDisbursed(investment.getPrincipal().subtract
	   (investment.getMaturityInterest().multiply(new BigDecimal (preLiquidationCharges))));
	   }
	   
	   disbursements.setAmountDisbursed(investment.getMaturityInterest()
	    			 .add(investment.getPrincipal()));
	   disbursements.setDisbursementDate(investment.getMaturityDate().plusDays(1));
	   disbursements.setInvestmentStatus(InvestmentStatus.Awaiting_Disbursement);
	   disbursementRepository.save(disbursements);
     }
    
    
    public void calculateDailyMaturityInterests(Investment investment) {
    LocalDate startDate = investment.getStartDate(); 
    LocalDate currentDate = LocalDate.now(); 
    
    long noOfDaysBetween = ChronoUnit.DAYS.between(startDate, currentDate); 
	 BigDecimal CurrentInterest =  investment.getPrincipal().multiply( new BigDecimal(investment.getRate()))
			  .multiply( new BigDecimal (noOfDaysBetween))
			  .divide(new BigDecimal (36500),2,RoundingMode.HALF_UP) ;
	 
	investment.setCurrentInterest(CurrentInterest);
    investmentRepository.save(investment);
    }
    
    
   
    public Optional<Investment> getInvestment(Long id){
	return investmentRepository.findById(id);
	}
	
    public void updateInvestment(Long id, Investment investment){
    investmentRepository.save(investment);
	}

    public void deleteInvestment(Long id) {
    investmentRepository.deleteById(id);
	}

    public List<InvestmentDTO> getCustomerAndInvestmentInfo(Customer customer){
    List<InvestmentDTO> custInvestmentInfo = investmentRepository.getJointInvestmentInformation(customer);
    return custInvestmentInfo;
    }

	public BigDecimal getTotalPrincipal(){
	BigDecimal totalPrincipal = investmentRepository.getTotalPrincipal();
	return totalPrincipal;
	}
	
	public BigDecimal getMarketerMonthlyInflow(User marketer){
	BigDecimal monthlyInflow = investmentRepository.getMarketerMonthlyInflow(marketer);
	return monthlyInflow;
	}
	
	public BigDecimal getCustomerTotalPrincipal(Customer customer) {
	return investmentRepository.getTotalCustomerPrincipal(customer);	
	}
	
    public BigDecimal customerTotalMaturity(Customer customer) {
	return investmentRepository.totalCustomerMaturity(customer);
    }
    
    public BigDecimal customerDailyInterest(Customer customer) {
    return investmentRepository.getDailyCustomerInterest(customer);
    }
     
	public BigDecimal getTotalMaturityInterest(){
	BigDecimal totalMaturity = investmentRepository.getTotalMaturityInterest();
	return totalMaturity;
	}
	
	public BigDecimal totalActiveInvestments() {
	return investmentRepository.totalActiveInvestments();
	}
	
	public List<?> getTotalMonthlyMaturityInterest(){
	List<?> monthlyMaturity = investmentRepository.getTotalMonthlyMaturityInterest();
	return monthlyMaturity;
	}
	
	public List<?> getTotalMonthlyPrincipal(){
	List<?> monthlyPrincipal = investmentRepository.getTotalMonthlyPrincipal();
	return monthlyPrincipal;
	}
	
	public List<?> getTotalMonthlyBranchData(Branch branch){
	List<?> monthlyBranchData = investmentRepository.getBranchReporting(branch);
	return monthlyBranchData;
	}
	
	public Page<Investment> findByMarketer(User marketer, Pageable pageable){
	return  investmentRepository.findByMarketer(marketer, pageable);
	
	}
	
	public  BigDecimal getTotalPrincipalByMarketer(User marketer){
	BigDecimal marketerTotalPrincipal =  investmentRepository.getTotalPrincipalByMarketer(marketer);
	return marketerTotalPrincipal;
	}
	
   public List<Investment> findByUserBranch(Branch branch, Pageable pageable){
   List<Investment> branchInvestmentList = investmentRepository.findByUserBranch(branch);
   return branchInvestmentList;
   }
   
   public  BigDecimal getTotalBranchPrincipal(Branch branch) {
   BigDecimal branchPrincipal = investmentRepository.getTotalBranchPrincipal(branch);
   return branchPrincipal;
   }
	
   public  BigDecimal getTotalBranchInterest(Branch branch) {
   BigDecimal branchInterest = investmentRepository.getTotalBranchPrincipal(branch);
   return branchInterest;
   }
   
   public BigDecimal totalWeeklyInvestmentMaturing() {
   return investmentRepository.totalWeeklyMaturingInvestment();
   }
   
   public Long totalweeklyMarketerMaturingInvestment(User marketer) {
   return investmentRepository.totalWeeklyMarketerMaturingInvestment(marketer);
   }
   
   public List<Investment> weeklyMaturingInvestment(){
   return investmentRepository.weeklyMaturingInvestment();
   }
   
   
   public List<Investment> weeklyMarketerMaturingInvestmentt(User marketer){
   return investmentRepository.weeklyMaturingMarketerInvestment(marketer);
   }

   @SuppressWarnings("serial")
   public List<Investment> findInvestmentByParameter(LocalDate startDate, LocalDate endDate, Integer accountNumber,
   InvestmentStatus status, Pageable pageable){
   List<Investment> investment = investmentRepository.findAll(new Specification<Investment>() {
		  
   @SuppressWarnings("deprecation")
   @Override
   public Predicate toPredicate(Root<Investment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
   Predicate pr = cb.conjunction();
		
   if (Objects.nonNull(startDate) && Objects.nonNull(endDate) && startDate.isBefore(endDate)){
   pr = cb.and(pr, cb.between(root.get("maturityDate"), startDate, endDate));
   }
   if (accountNumber != null){ 
   pr = cb.and(pr, cb.equal(root.get("accountNumber"), accountNumber)); 
   }
   if (Objects.nonNull(status)){ 
   pr = cb.and(pr, cb.equal(root.get("status"), status)); 
   }
   cq.orderBy(cb.asc(root.get("id")));
   return pr;
   }}, pageable).getContent();
   return investment;
   }

   // All Reporting Investment Services starts from here 
   
   public List<Investment> getAllActiveInvestments(){
   return investmentRepository.getAllActiveInvestments();
   }
   
   public List<Investment> getAllDisbursedInvestments(){
   return investmentRepository.getAllDisbursedInvestments();
   }
   
   public List<Investment> getAllMaturedInvestments(){
   return investmentRepository.getAllMaturedInvestments();
   }
   
   public Long getTotalMaturedInvestments() {
   return investmentRepository.getTotalMaturedInvestments();
   }
   
    public List<Investment> getAllRolledOverInvestments(){
   return investmentRepository.getAllRolledOverInvestments();
   }
   
   public Long getTotalRolledOverInvestments() {
   return investmentRepository.getTotalRolledOverInvestments();
   }
   
   public List<Investment> getAllToppedUpInvestments(){
   return investmentRepository.getAllToppedUpInvestments();
   }
   
   public Long getTotalToppedUpInvestments() {
   return investmentRepository.getTotalToppedUpInvestments();
   }
	
   public List<Investment> getAllInterestPaidInvestments(){
   return investmentRepository.getAllInterestPaidInvestments();
   }
   
   public Long getTotalUpFrontMaturityPaidInvestments() {
   return investmentRepository.getTotalUpFrontMaturityPaidInvestments();
   }
   
   
  
  }
	
  
