package com.bethsaida.org.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.AccountNumberPointer;
import com.bethsaida.org.repository.AccountNumberPointerRepo;

@Service
public class AccountNumberPointerService {
	
	@Autowired
	AccountNumberPointerRepo accountNumberPointerRepo;
	
	public int getAccountNumberValue() {
	return accountNumberPointerRepo.getAccountNumberValue();
	}
	
	public Long getAccountIdValue() {
	return accountNumberPointerRepo.getAccountIdValue();
	}
		
	
	public AccountNumberPointer findById(Long id) {
	return accountNumberPointerRepo.findById(id).get();
	}
	
	@Transactional
	@CachePut(cacheNames="accountnumberpointer", key="#id")
	public void updateAccountNumberPointer(Long id, AccountNumberPointer accountNumberPointer ){
    accountNumberPointerRepo.save(accountNumberPointer);
	}

}
