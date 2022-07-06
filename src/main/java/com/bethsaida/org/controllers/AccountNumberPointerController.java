package com.bethsaida.org.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.AccountNumberPointer;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.service.AccountNumberPointerService;

@RestController
public class AccountNumberPointerController {
	
	@Autowired
	AccountNumberPointerService accountNumberPointerService ;
	
	@GetMapping(value="/accountnumber")
	public int getAccountNumberValue() {
	return accountNumberPointerService.getAccountNumberValue();
	}
	
	@GetMapping(value="/accountid")
	public Long getAccountIdValue() {
	return accountNumberPointerService.getAccountIdValue();
	}
	
	@GetMapping(value="/accountnumber/{id}")
	public AccountNumberPointer findById(@PathVariable Long id) {
	return accountNumberPointerService.findById(id);
	}
	
	@PutMapping(value="update/accountnumber/{id}")
	public void updateAccountNumberPointer (@RequestBody AccountNumberPointer accountNumberPointer, @PathVariable Long id){
	accountNumberPointerService.updateAccountNumberPointer(id, accountNumberPointer);
	}
	

}
