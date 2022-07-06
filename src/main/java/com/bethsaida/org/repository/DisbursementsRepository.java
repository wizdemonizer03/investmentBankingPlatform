package com.bethsaida.org.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.Disbursements;

@Repository
public interface DisbursementsRepository extends JpaRepository <Disbursements, Long  >  {

    Disbursements getById(Long id);
	
	Optional<Disbursements> findById(Long id);
	
	List<Disbursements> findByAccountNumber(int accountNumber);
	
	@Query("SELECT SUM (amountDisbursed) as total_amount_disbursed from Disbursements d")
	BigDecimal getTotalAmountDisbursed();
	
	@Query("SELECT DATE_FORMAT(disbursementDate,'%M'), SUM (amountDisbursed) as monthly_disbursed from Disbursements "
			+ "d GROUP BY DATE_FORMAT(disbursementDate,'%M') ORDER BY DATE_FORMAT(disbursementDate,'%M') DESC ")
	List<?> getTotalMonthlyMaturityInterest();
	
	@Query("select COUNT(disbursement) from Disbursements disbursement where status = 'Disbursed'")
	BigDecimal totalDisbursementsCount();
	
}
