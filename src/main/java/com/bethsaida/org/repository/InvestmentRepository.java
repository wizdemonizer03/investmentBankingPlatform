package com.bethsaida.org.repository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.InvestmentDTO;
import com.bethsaida.org.models.User;



@Repository
public interface InvestmentRepository extends JpaRepository <Investment, Long>, JpaSpecificationExecutor<Investment>

 {
	
	Investment getById(Long id);
	
	Optional<Investment> findById(Long id);
	
	List<Investment> findByMaturityDateBetween(LocalDate startDate, LocalDate endDate);
	
	List<Investment> findByAccountNumber(int accountNumber);
	
//	@Query("SELECT new com.bethsaida.org.models.InvestmentDTO (c.firstName, c.lastName, c.Address, c.category, c.marketer, "
//			+ "i.accountNumber,i.rate, i.principal, i.maturityInterest, i.startDate, i.maturityDate, i.status, i.tenure)"
//			+ " FROM Investment i JOIN i.customer_id c")
//	List<InvestmentDTO> getJoinInformation(Long id);

	@Query("SELECT SUM (principal) as total_principal from Investment i")
	BigDecimal getTotalPrincipal();
	
	@Query("SELECT SUM (maturityInterest) as total_maturity from Investment i")
	BigDecimal getTotalMaturityInterest();
	
	@Query("SELECT DATE_FORMAT(maturityDate,'%M'), SUM (maturityInterest) as monthly_maturity from Investment "
			+ "i GROUP BY DATE_FORMAT(maturityDate,'%M') ORDER BY DATE_FORMAT(maturityDate,'%M') DESC ")
	List<?> getTotalMonthlyMaturityInterest();
	
	@Query("SELECT DATE_FORMAT(startDate,'%M'), SUM (principal) as monthly_principal from Investment "
			+ "i GROUP BY DATE_FORMAT(startDate,'%M') ORDER BY DATE_FORMAT(startDate,'%M') DESC ")
	List<?> getTotalMonthlyPrincipal();
	
	@Query("select investment from Investment investment join "
			+ "investment.customer_id.marketer marketer where marketer = :marketer")
    Page<Investment> findByMarketer(User marketer, Pageable pageable);
	
	@Query("SELECT SUM(investment.principal) as "
			+ "total_principal from "
			+ "Investment investment join investment.customer_id.marketer "
			+ "marketer where marketer = :marketer")
	BigDecimal getTotalPrincipalByMarketer(User marketer);
	
	@Query("select SUM(investment.principal) from Investment investment join "
			+ "investment.customer_id customer where customer = :customer")
	BigDecimal getTotalCustomerPrincipal(Customer customer);
	
	@Query("select SUM(investment.maturityInterest) from Investment investment join "
			+ "investment.customer_id customer where customer = :customer")
	BigDecimal totalCustomerMaturity(Customer customer);
	
	@Query("select investment.currentInterest from Investment investment join"
			+ " investment.customer_id customer where customer = :customer")
	BigDecimal getDailyCustomerInterest(Customer customer);
	
	@Query("select sum(investment.principal) from Investment investment JOIN investment.customer_id.marketer "
	 		+ "marketer ON marketer_id = :marketer WHERE MONTH(investment.startDate) = MONTH(CURDATE()) "
	 		+ "AND YEAR(investment.startDate) = YEAR(CURDATE())")
	BigDecimal getMarketerMonthlyInflow(User marketer);
	
	 @Query("select investment from Investment investment join "
			+ "investment.customer_id.marketer marketer where marketer.branch = :branch")
	 List<Investment> findByUserBranch(Branch branch);
	
    @Query("SELECT SUM(investment.principal) from Investment investment "
    		+ "join investment.customer_id.marketer marketer where marketer.branch = :branch ")
    BigDecimal getTotalBranchPrincipal(Branch branch);
	
    @Query("SELECT SUM(investment.maturityInterest) from Investment investment "
    		+ "join investment.customer_id.marketer marketer where marketer.branch = :branch ")
    BigDecimal getTotalBranchInterest(Branch branch);
    
    
    @Query("select sum (investment.principal) as total_branch_principal, sum (investment.maturityInterest) as total_branch_interest ,"
    		+ " count (customer_fk_id) as total_branch_customers from Investment investment join "
    		+ "investment.customer_id.marketer marketer where marketer.branch = :branch ")
    List<?> getBranchReporting(Branch branch);
    
    @Query("SELECT new com.bethsaida.org.models.InvestmentDTO (c.firstName, c.lastName, c.Address, c.category, c.marketer, "
			+ "i.id,i.accountNumber,i.rate, i.principal, i.maturityInterest, i.currentInterest, i.startDate, i.maturityDate, i.status, i.tenure)"
			+ " FROM Investment i JOIN i.customer_id c where c = :customer")
	List<InvestmentDTO> getJointInvestmentInformation(Customer customer);

	
	@Query("select COUNT(investment) from Investment investment where status = 'Active'")
    BigDecimal totalActiveInvestments();
	
	
	@Query("select COUNT(investment) from Investment investment WHERE YEARWEEK(maturityDate) = YEARWEEK(NOW())")
	BigDecimal totalWeeklyMaturingInvestment();

	Page<Investment> findAllByOrderByIdDesc(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("update Investment investment set investment.status = 'Matured' where investment.id = :id")
	void setInvestmentStatus(Long id);
	
	
	// All Reporting Endpoints Query Starts from here 
	
	@Query("select investment from Investment investment where status = 'Active'")
	List<Investment> getAllActiveInvestments();
	
	@Query("select investment from Investment investment where status = 'Disbursed'")
	List<Investment> getAllDisbursedInvestments();
	
	@Query("select investment from Investment investment where rollOverStatus = 'YES'")
	List<Investment> getAllRolledOverInvestments();
	
	@Query("select COUNT(investment) from Investment investment where rollOverStatus = 'YES'")
	Long getTotalRolledOverInvestments();
	
	@Query("select investment from Investment investment where status = 'Matured'")
	List<Investment> getAllMaturedInvestments();
	
	@Query("select COUNT(investment) from Investment investment where status = 'Matured'")
	Long getTotalMaturedInvestments();
	
	
	@Query("select investment from Investment investment where topUpStatus = 'YES'")
	List<Investment> getAllToppedUpInvestments();
	
	@Query("select COUNT(investment) from Investment investment where topUpStatus = 'YES'")
	Long getTotalToppedUpInvestments();
	
	
	@Query("select investment from Investment investment where upFrontMaturity = 'Yes'")
	List<Investment> getAllInterestPaidInvestments();
	
	@Query("select COUNT(investment) from Investment investment where upFrontMaturity = 'Yes'")
	Long getTotalUpFrontMaturityPaidInvestments();
	
	
  }
