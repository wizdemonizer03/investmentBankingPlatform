package com.bethsaida.org.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Investment

   {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "AccountNumber", updatable = false, nullable = false)
    private Integer accountNumber;
    private String category;
	private BigDecimal principal;
	private BigDecimal currentInterest;
	private BigDecimal maturityInterest;
	private BigDecimal taxWitholding;
	private String tenure;
	private String investmentPackage;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate maturityDate;
	private double rate;
	private String whtStatus;
	@Enumerated(EnumType.STRING)
	private InvestmentStatus status;
	
	@Enumerated(EnumType.STRING)
	private InvestmentTopUpStatus topUpStatus;
	
	@Enumerated(EnumType.STRING)
	private RollOverStatus rollOverStatus;
	
	@Column(nullable = true, length = 64)
	private String document;
	@Column(nullable = true, length = 64)
	private String otherImage;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = Customer.class, 
	 fetch = FetchType.LAZY )
	@JoinColumn(name="customer_fk_id") 
	private Customer customer_id ;
	
	@JsonBackReference
	@OneToMany(mappedBy="investment",cascade = CascadeType.ALL, targetEntity=Disbursements.class)
	private List <Disbursements> disbursements;
	
	private String upFrontMaturity;
	
	private String rollOverDisbursement;
	private BigDecimal initialPrincipal;
	private LocalDate dateOfTopUp;
	private BigDecimal topUpAmount;
	private BigDecimal newMaturityInterest;
	
	public Investment() {
		
	}

    public Investment(Long id, Integer accountNumber, String category, BigDecimal principal, BigDecimal currentInterest,
			BigDecimal maturityInterest, BigDecimal taxWitholding, String tenure, String investmentPackage,
			LocalDate startDate, LocalDate maturityDate, double rate, String whtStatus, InvestmentStatus status,
			InvestmentTopUpStatus topUpStatus, RollOverStatus rollOverStatus, String document, String otherImage,
			Customer customer_id, List<Disbursements> disbursements, String upFrontMaturity,
			String rollOverDisbursement, BigDecimal initialPrincipal, LocalDate dateOfTopUp, BigDecimal topUpAmount,
			BigDecimal newMaturityInterest) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.category = category;
		this.principal = principal;
		this.currentInterest = currentInterest;
		this.maturityInterest = maturityInterest;
		this.taxWitholding = taxWitholding;
		this.tenure = tenure;
		this.investmentPackage = investmentPackage;
		this.startDate = startDate;
		this.maturityDate = maturityDate;
		this.rate = rate;
		this.whtStatus = whtStatus;
		this.status = status;
		this.topUpStatus = topUpStatus;
		this.rollOverStatus = rollOverStatus;
		this.document = document;
		this.otherImage = otherImage;
		this.customer_id = customer_id;
		this.disbursements = disbursements;
		this.upFrontMaturity = upFrontMaturity;
		this.rollOverDisbursement = rollOverDisbursement;
		this.initialPrincipal = initialPrincipal;
		this.dateOfTopUp = dateOfTopUp;
		this.topUpAmount = topUpAmount;
		this.newMaturityInterest = newMaturityInterest;
	}


public Long getId() {
	return id;
}


public void setId(Long id) {
	this.id = id;
}


public Integer getAccountNumber() {
	return accountNumber;
}


public void setAccountNumber(Integer accountNumber) {
	this.accountNumber = accountNumber;
}


public String getCategory() {
	return category;
}


public void setCategory(String category) {
	this.category = category;
}


public BigDecimal getPrincipal() {
	return principal;
}


public void setPrincipal(BigDecimal principal) {
	this.principal = principal;
}


public BigDecimal getCurrentInterest() {
	return currentInterest;
}


public void setCurrentInterest(BigDecimal currentInterest) {
	this.currentInterest = currentInterest;
}


public BigDecimal getMaturityInterest() {
	return maturityInterest;
}


public void setMaturityInterest(BigDecimal maturityInterest) {
	this.maturityInterest = maturityInterest;
}


public BigDecimal getTaxWitholding() {
	return taxWitholding;
}


public void setTaxWitholding(BigDecimal taxWitholding) {
	this.taxWitholding = taxWitholding;
}


public String getTenure() {
	return tenure;
}


public void setTenure(String tenure) {
	this.tenure = tenure;
}


public String getInvestmentPackage() {
	return investmentPackage;
}


public void setInvestmentPackage(String investmentPackage) {
	this.investmentPackage = investmentPackage;
}


public LocalDate getStartDate() {
	return startDate;
}


public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
}


public LocalDate getMaturityDate() {
	return maturityDate;
}


public void setMaturityDate(LocalDate maturityDate) {
	this.maturityDate = maturityDate;
}


public double getRate() {
	return rate;
}


public void setRate(double rate) {
	this.rate = rate;
}


public String getWhtStatus() {
	return whtStatus;
}


public void setWhtStatus(String whtStatus) {
	this.whtStatus = whtStatus;
}


public InvestmentStatus getStatus() {
	return status;
}


public void setStatus(InvestmentStatus status) {
	this.status = status;
}


public InvestmentTopUpStatus getTopUpStatus() {
	return topUpStatus;
}


public void setTopUpStatus(InvestmentTopUpStatus topUpStatus) {
	this.topUpStatus = topUpStatus;
}


public RollOverStatus getRollOverStatus() {
	return rollOverStatus;
}

public void setRollOverStatus(RollOverStatus rollOverStatus) {
	this.rollOverStatus = rollOverStatus;
}

public String getDocument() {
	return document;
}


public void setDocument(String document) {
	this.document = document;
}


public String getOtherImage() {
	return otherImage;
}


public void setOtherImage(String otherImage) {
	this.otherImage = otherImage;
}


public Customer getCustomer_id() {
	return customer_id;
}


public void setCustomer_id(Customer customer_id) {
	this.customer_id = customer_id;
}


public List<Disbursements> getDisbursements() {
	return disbursements;
}


public void setDisbursements(List<Disbursements> disbursements) {
	this.disbursements = disbursements;
}


public String getUpFrontMaturity() {
	return upFrontMaturity;
}


public void setUpFrontMaturity(String upFrontMaturity) {
	this.upFrontMaturity = upFrontMaturity;
}


public String getRollOverDisbursement() {
	return rollOverDisbursement;
}


public void setRollOverDisbursement(String rollOverDisbursement) {
	this.rollOverDisbursement = rollOverDisbursement;
}


public LocalDate getDateOfTopUp() {
	return dateOfTopUp;
}


public void setDateOfTopUp(LocalDate dateOfTopUp) {
	this.dateOfTopUp = dateOfTopUp;
}


public BigDecimal getInitialPrincipal() {
	return initialPrincipal;
}

public void setInitialPrincipal(BigDecimal initialPrincipal) {
	this.initialPrincipal = initialPrincipal;
}


public BigDecimal getTopUpAmount() {
	return topUpAmount;
}


public void setTopUpAmount(BigDecimal topUpAmount) {
	this.topUpAmount = topUpAmount;
}


public BigDecimal getNewMaturityInterest() {
	return newMaturityInterest;
}


public void setNewMaturityInterest(BigDecimal newMaturityInterest) {
	this.newMaturityInterest = newMaturityInterest;
}

@Override
public String toString() {
	return "Investment [id=" + id + ", accountNumber=" + accountNumber + ", category=" + category + ", principal="
			+ principal + ", currentInterest=" + currentInterest + ", maturityInterest=" + maturityInterest
			+ ", taxWitholding=" + taxWitholding + ", tenure=" + tenure + ", investmentPackage=" + investmentPackage
			+ ", startDate=" + startDate + ", maturityDate=" + maturityDate + ", rate=" + rate + ", whtStatus="
			+ whtStatus + ", status=" + status + ", topUpStatus=" + topUpStatus + ", rollOverStatus=" + rollOverStatus
			+ ", document=" + document + ", otherImage=" + otherImage + ", customer_id=" + customer_id
			+ ", disbursements=" + disbursements + ", upFrontMaturity=" + upFrontMaturity + ", rollOverDisbursement="
			+ rollOverDisbursement + ", initialPrincipal=" + initialPrincipal + ", dateOfTopUp=" + dateOfTopUp
			+ ", topUpAmount=" + topUpAmount + ", newMaturityInterest=" + newMaturityInterest + "]";
}


}
