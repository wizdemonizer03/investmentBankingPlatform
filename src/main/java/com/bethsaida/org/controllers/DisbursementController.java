package com.bethsaida.org.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.Disbursements;
import com.bethsaida.org.service.DisbursementService;

//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin()
@RestController
public class DisbursementController {
	
	@Autowired
	DisbursementService disbursementService;
	
	@GetMapping("list/disbursements")
	public ResponseEntity<List<Disbursements>> getDisbursements () 
	  {
	return new ResponseEntity<List<Disbursements>>(disbursementService.getDisbursements(), HttpStatus.OK);
	  }
	
	@GetMapping(value = "/disbursement/{id}")
    public Disbursements getDisbursementById(@PathVariable Long id)
    {
    	return disbursementService.getDisbursementsById(id);
    }
	
	@PostMapping(value = "/confirmDisbursement/{id}")
	public void confirmDisbursement(@PathVariable Long id, @RequestBody Disbursements disbursement) {
	disbursementService.confirmDisbursement(id, disbursement);
	}

	@GetMapping(value="/centralTotalAmountDisbursed")
    public BigDecimal getTotalDisbursed()
    {
    	return disbursementService.getTotalAmountDisbursed();
    }
	
    @DeleteMapping(value="/deleteDisbursement/{id}")
    public void deleteDisbursement(@PathVariable Long id) 
    {
    	disbursementService.deleteDisbursement(id);
    }
	
    @GetMapping(value="/centralTotalMonthlyAmountDisbursed")
    public List<?> getTotalMonthlyDisbursed()
    {
    	return disbursementService.getMonthlyTotalDisbursed();
    }
	
    @GetMapping(value="/totalDisbursedInvestments")
    public BigDecimal totalDisbursedInvestment() {
    return disbursementService.totalDisbursedInvestments();
    }
    
    
}
