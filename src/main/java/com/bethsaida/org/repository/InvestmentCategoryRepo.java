package com.bethsaida.org.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.InvestmentCategory;

@Repository
public interface InvestmentCategoryRepo extends JpaRepository<InvestmentCategory, Long> {
	

	

}
