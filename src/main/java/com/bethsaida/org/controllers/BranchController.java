package com.bethsaida.org.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.service.BranchService;

//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin()
@RestController
public class BranchController {

  @Autowired
  BranchService branchService;
	
  @GetMapping (value="list/branches")
  public List<Branch> getBranches(){
  return branchService.getBranches();
  }
  
  @PostMapping (value="/createBranch", consumes = {MediaType.APPLICATION_JSON_VALUE})
  public @ResponseBody Branch createBranch(@RequestBody Branch branch) {
  return branchService.createBranch(branch);
  }
	
  @DeleteMapping(value="/deleteBranch/{id}")
  public void deleteBranch(@PathVariable Long id) {
  branchService.deleteBranch(id);
  }
	
  @PutMapping(value="/update/branch/{id}")
  public String updateBranch(@RequestBody Branch branch, @PathVariable Long id) {
  branchService.updateBranch(id, branch);
  return "Branch updated successfully";
  }

}
