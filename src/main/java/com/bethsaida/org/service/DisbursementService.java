package com.bethsaida.org.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Disbursements;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.InvestmentStatus;
import com.bethsaida.org.repository.DisbursementsRepository;
import com.bethsaida.org.repository.InvestmentRepository;

@Service
public class DisbursementService {
	
	@Autowired
	DisbursementsRepository disbursementRepository;
	
	@Autowired
    InvestmentRepository investmentRepository;
	
	@Autowired
	InvestmentService investmentService;
	
	public List<Disbursements> getDisbursements(){
	return disbursementRepository.findAll();
    }
	
	public void deleteDisbursement(Long id){
	disbursementRepository.deleteById(id);
	}
	
	public Disbursements getDisbursementsById(Long id) {
    return disbursementRepository.getById(id);
    }   
     
	
	public Optional<Disbursements> getDisbursementById(Long id){
	return disbursementRepository.findById(id);
	}
	
	public BigDecimal getTotalAmountDisbursed(){
	BigDecimal totalDisbursed = disbursementRepository.getTotalAmountDisbursed();
	return totalDisbursed;
	}
	
	public List<?> getMonthlyTotalDisbursed() {
	List<?> totalMonthlyDisbursed = disbursementRepository.getTotalMonthlyMaturityInterest();
	return totalMonthlyDisbursed;
	}
	 
	public void confirmDisbursement(Long id, Disbursements disbursement) {
	disbursement.setInvestmentStatus(InvestmentStatus.Disbursed);
	disbursementRepository.save(disbursement);
	Investment existing = investmentRepository.getById(disbursement.getInvestment().getId());
    existing.setStatus(InvestmentStatus.Disbursed);
    investmentRepository.save(existing);
    }
	 
	public BigDecimal totalDisbursedInvestments() {
	return disbursementRepository.totalDisbursementsCount();
	}
	 
	 
	 
}
