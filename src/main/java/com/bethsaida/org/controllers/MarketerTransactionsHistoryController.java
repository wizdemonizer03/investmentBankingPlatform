package com.bethsaida.org.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.MarketerTransactionsHistory;
import com.bethsaida.org.models.User;
import com.bethsaida.org.service.MarketerTransactionsHistoryService;
import com.bethsaida.org.service.UserAccountService;

@RestController
public class MarketerTransactionsHistoryController {
	
	@Autowired
	MarketerTransactionsHistoryService marketerTransactionsService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@GetMapping("/AllUsersTransaction")
	public ResponseEntity<List<MarketerTransactionsHistory>> getMarketersTransactions () {
	return new ResponseEntity<List<MarketerTransactionsHistory>>
	(marketerTransactionsService.displayAllTransactions(), HttpStatus.OK);
	}
	
	
	@GetMapping("/LoggedInMarketerTransactionHistory")
	public List<MarketerTransactionsHistory> loggedInUserTransactionHistory(Principal principal){
	User loggedInMarketer = userAccountService.findByUserName(principal.getName());
	return marketerTransactionsService.displayUsersTransactions(loggedInMarketer);
	}
	

}
