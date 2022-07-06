package com.bethsaida.org.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.MarketerTransactionsHistory;
import com.bethsaida.org.models.User;

@Repository
public interface MarketerTransactionsHistoryRepository extends JpaRepository<MarketerTransactionsHistory, Long> {

 List<MarketerTransactionsHistory> findByMarketer(User marketer);
	
}
