package com.bethsaida.org.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.AccountNumberPointer;

@Repository
public interface AccountNumberPointerRepo extends JpaRepository<AccountNumberPointer, Long> {
 
	@Query("select anp.nextAccountNumber from AccountNumberPointer anp")
	int getAccountNumberValue();
	
	@Query("select anp.id from AccountNumberPointer anp")
	Long getAccountIdValue();
	 
	Optional<AccountNumberPointer> findById(Long id);
	
	}
