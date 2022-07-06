package com.bethsaida.org.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.InvestmentCategory;
import com.bethsaida.org.service.InvestmentCategoryService;

@RestController
public class InvestmentCategoryController {
	
	@Autowired 
	private InvestmentCategoryService categoryService;
	
	@GetMapping(value = "/categoryList")	
	public List<InvestmentCategory> categoryList(){
	return categoryService.getCategoryList();
	}
	
	@PostMapping(value = "/createCategory")
	public InvestmentCategory createCategory(InvestmentCategory category) {
	return categoryService.createCategory(category);
	}
	
	@PutMapping(value="/updateCategory")
	public InvestmentCategory updateCategory(Long id, InvestmentCategory category) {
	return categoryService.updateCategory(id, category);
	}

	
	@DeleteMapping(value="/deleteCategory")
	public void deleteCategory(Long id) {
	categoryService.deleteCategorty(id);
	}
	
}
