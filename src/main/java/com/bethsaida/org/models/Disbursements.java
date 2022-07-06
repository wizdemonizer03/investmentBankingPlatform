package com.bethsaida.org.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Disbursements {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private int accountNumber;
	private String investmentPackage;
	private BigDecimal principal;
    private BigDecimal amountDisbursed;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate disbursementDate;
	
	@Enumerated(EnumType.STRING)
	private InvestmentStatus status;
	
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = Investment.class, 
	 fetch = FetchType.LAZY )
	@JoinColumn(name="investment_fk_id") 
	private Investment investment;
	
	public Disbursements() {}
	
	
    public Long getId() {
	return id;
	}
	
    public void setId(Long id) {
	this.id = id;
	}
    
    public int getAccountNumber() {
	return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
	this.accountNumber = accountNumber;
	}

	public String getInvestmentPackage() {
	return investmentPackage;
	}

	public void setInvestmentPackage(String investmentPackage) {
	this.investmentPackage = investmentPackage;
	}

	public BigDecimal getPrincipal() {
	return principal;
	}

	public void setPrincipal(BigDecimal principal) {
	this.principal = principal;
	}

	public BigDecimal getAmountDisbursed() {
	return amountDisbursed;
	}

	public void setAmountDisbursed(BigDecimal amountDisbursed) {
	this.amountDisbursed = amountDisbursed;
	}

	public LocalDate getDisbursementDate() {
	return disbursementDate;
	}

	public void setDisbursementDate(LocalDate disbursementDate) {
	this.disbursementDate = disbursementDate;
	}

	public InvestmentStatus getStatus() { 
	return status; 
	}
	  
	public void setInvestmentStatus(InvestmentStatus status) { 
	this.status = status; 
	}
	
    public Investment getInvestment() {
	return investment;
	}

	public void setInvestment(Investment investment) {
	this.investment = investment;
	}

}
