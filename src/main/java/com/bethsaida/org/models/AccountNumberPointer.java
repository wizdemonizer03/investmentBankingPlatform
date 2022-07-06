package com.bethsaida.org.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountNumberPointer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;
	private int nextAccountNumber;
	
	public AccountNumberPointer() {
		
	}
	
	public AccountNumberPointer(Long id, int nextAccountNumber) {
		super();
		this.id = id;
		this.nextAccountNumber = nextAccountNumber;
	}

	public Long getId() {
	return id;
	}
	
	public Long setId(Long id) {
	return this.id = id;
	}

	public int getNextAccountNumber() {
		return nextAccountNumber;
	}

	public int setNextAccountNumber(int nextAccountNumber) {
		return this.nextAccountNumber = nextAccountNumber;
	}
	
	
	
	
}
