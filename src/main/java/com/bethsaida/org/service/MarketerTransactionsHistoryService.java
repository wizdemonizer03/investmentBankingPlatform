package com.bethsaida.org.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.MarketerTransactionsHistory;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.MarketerTransactionsHistoryRepository;

@Service
public class MarketerTransactionsHistoryService {
	
	@Autowired
	MarketerTransactionsHistoryRepository marketerTransactionsRepo;
	
	
	public List<MarketerTransactionsHistory> displayAllTransactions() {
	return marketerTransactionsRepo.findAll();
	}

	public List<MarketerTransactionsHistory> displayUsersTransactions(User marketer){
	return marketerTransactionsRepo.findByMarketer(marketer);
	}
}
