package com.bethsaida.org.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class MarketerTransactionsHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal Target;
	
    private BigDecimal inFlow;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    
  //  private String name;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = User.class, 
	 fetch = FetchType.LAZY )
	@JoinColumn(name="marketer_id") 
    private User marketer;
    
    public MarketerTransactionsHistory () {
    
    }

	public MarketerTransactionsHistory(Long id, BigDecimal target, BigDecimal inFlow, LocalDate transactionDate, 
			User marketer) {
	this.id = id;
	Target = target;
	this.inFlow = inFlow;
	this.transactionDate = transactionDate;
	this.marketer = marketer;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTarget() {
		return Target;
	}

	public void setTarget(BigDecimal target) {
		Target = target;
	}

	public BigDecimal getInFlow() {
		return inFlow;
	}

	public void setInFlow(BigDecimal inFlow) {
		this.inFlow = inFlow;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	
    public User getMarketer() {
		return marketer;
	}

	public void setMarketer(User marketer) {
		this.marketer = marketer;
	}
	
	
}
