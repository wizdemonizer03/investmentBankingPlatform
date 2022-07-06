package com.bethsaida.org.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


public class InvestmentDTO {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String Address ;
	private String category;
	private User marketer;
	private double rate;
    private Integer accountNumber;
    private BigDecimal principal;
    private BigDecimal maturityInterest;
    private BigDecimal currentInterest;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private InvestmentStatus status;
	private String tenure;
	
	
	public InvestmentDTO() {
	
	}

  public InvestmentDTO(String firstName, String lastName, String address, String category, User marketer, Long id,
		   Integer accountNumber,double rate, BigDecimal principal, BigDecimal maturityInterest, BigDecimal currentInterest, LocalDate startDate,
			LocalDate maturityDate, InvestmentStatus status, String tenure) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		Address = address;
		this.category = category;
		this.marketer = marketer;
		this.rate = rate;
		this.accountNumber = accountNumber;
		this.principal = principal;
		this.maturityInterest = maturityInterest;
		this.currentInterest = currentInterest;
		this.startDate = startDate;
		this.maturityDate = maturityDate;
		this.status = status;
		this.tenure = tenure;
	}


   public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
	
    public double getRate() {
		return rate;
	}

    public void setRate(double rate) {
		this.rate = rate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	public User getMarketer() { 
		return marketer; 
		}
	  
    public void setMarketer(User marketer) { 
		  this.marketer = marketer; 
	}
	 
	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getMaturityInterest() {
		return maturityInterest;
	}

	public void setMaturityInterest(BigDecimal maturityInterest) {
		this.maturityInterest = maturityInterest;
	}
	
	public BigDecimal getCurrentInterest() {
		return currentInterest;
	}

	public void setCurrentInterest(BigDecimal currentInterest) {
		this.currentInterest = currentInterest;
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

	public InvestmentStatus getStatus() {
		return status;
	}

	public void setStatus(InvestmentStatus status) {
		this.status = status;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	@Override
	public String toString() {
		return "InvestmentDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", Address="
				+ Address + ", category=" + category + ", marketer=" + marketer + ", rate=" + rate + ", accountNumber="
				+ accountNumber + ", principal=" + principal + ", maturityInterest=" + maturityInterest
				+ ", currentInterest=" + currentInterest + ", startDate=" + startDate + ", maturityDate=" + maturityDate
				+ ", status=" + status + ", tenure=" + tenure + "]";
	}

    
	
}
