package com.bethsaida.org.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.repository.BranchRepository;

@Service
public class BranchService {

	@Autowired
	BranchRepository branchRepository;
	
	public List<Branch> getBranches(){
	return branchRepository.findAll();
	}
	
	public Branch createBranch(Branch branch) {
	return branchRepository.save(branch);
	}
	
	public Branch findBranchById(Long id) {
	return branchRepository.getById(id);
	}
	
	public Branch updateBranch(Long id, Branch branch) {
	return branchRepository.save(branch);
	}
	
	public String deleteBranch(Long id) {
	branchRepository.deleteById(id);
	return "Branch "+branchRepository.findById(id)+"Deleted Successfuully";
	
	}
	
}
