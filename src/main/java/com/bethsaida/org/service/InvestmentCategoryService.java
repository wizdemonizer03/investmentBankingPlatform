package com.bethsaida.org.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.InvestmentCategory;
import com.bethsaida.org.repository.InvestmentCategoryRepo;


@Service
public class InvestmentCategoryService {
	
	@Autowired
	private InvestmentCategoryRepo categoryRepo;
	
	public InvestmentCategory createCategory(InvestmentCategory category) {
	return categoryRepo.save(category);
	}

	public List<InvestmentCategory> getCategoryList(){
	return categoryRepo.findAll();
	}
	
  	public InvestmentCategory updateCategory(Long id, InvestmentCategory category) {
  	return categoryRepo.save(category);
  	}

	public void deleteCategorty(Long id) {
	categoryRepo.deleteById(id);
	}

}
