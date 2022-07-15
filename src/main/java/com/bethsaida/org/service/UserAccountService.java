package com.bethsaida.org.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import javax.inject.Qualifier;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.CustomerDTO;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.MarketerTransactionsHistory;
import com.bethsaida.org.models.MyUserDetails;
//import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.User;
import com.bethsaida.org.models.UserDTO;
import com.bethsaida.org.models.UserStatus;
import com.bethsaida.org.repository.MarketerTransactionsHistoryRepository;
//import com.bethsaida.org.repository.CustomerAccountRepo;
import com.bethsaida.org.repository.UserAccountRepository;


@Transactional
@Service
public class UserAccountService {
	
	    @Autowired
	    InvestmentService investmentService;
	
	    @Autowired
        MarketerTransactionsHistoryRepository mrktTrnscHistRepo;
	
	    @Autowired	
	    private EmailService emailService;
	    
	    @Autowired	
	    private UserAccountRepository userRepository;
	    
	    private PasswordEncoder bCryptPasswordEncoder;
	   
	    public  UserAccountService (PasswordEncoder bCryptPasswordEncoder) {
	    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	    }
	
	    public User findByUserName(String username) {
	    return userRepository.findByUserName(username);
	    }
	   
	    public boolean exist(String email){
	    return userRepository.existsByEmail(email);
	    }
	   
	    public Branch findUserBranchId(Long id) {
	    return userRepository.findUserBranchId(id);
	    }
		
	    public List<User> getUsers(){
        return userRepository.findAll();
	    }
	   
	    public Page<User> getPagedUsersCount(Pageable pageable){
	    return userRepository.findAll(pageable);
	    }
	    
        @Transactional
        public User RegisterUser(User user) throws AddressException, MessagingException, IOException{
        String encoded = bCryptPasswordEncoder.encode(user.getPassword());
        emailService.sendUserRegistrationMail(user);
        user.setPassword(encoded);
        user.setStatus(UserStatus.Inactive);
        user.setEnabled(false);
        return userRepository.save(user);
		}
        
        
        public void activateUser(Long id) {
        userRepository.activateUser(id);
        }
		
        public void restrictUser(Long id) {
        userRepository.restrictUser(id);
        }
        
        public User getOneUser(Long id) {
        return userRepository.getById(id);
        }
        
        public Optional<User> getUser(Long id){
		return userRepository.findById(id);
		}

		public void updateUser(Long id, User user){
		userRepository.save(user);
		}
		
		@Transactional
        public String setMarketerTarget(Long id, BigDecimal target){
		userRepository.setMarketerMonthlyTarget(target, id);
		MarketerTransactionsHistory mrkTrnHistory = new MarketerTransactionsHistory ();
		mrkTrnHistory.setTarget(target);
		User marketer = userRepository.getById(id);
		mrkTrnHistory.setInFlow(investmentService.getMarketerMonthlyInflow(marketer));
	    mrkTrnHistory.setTransactionDate(LocalDate.now());
		mrkTrnHistory.setMarketer(marketer);
		mrktTrnscHistRepo.save(mrkTrnHistory);
		
		return "Target Saved and Transactions History Updated";
		
		}
		
	    public void deleteUser(Long id) {
	    userRepository.deleteById(id);
		}

	    public List<UserDTO> loggedInMarketerName(User marketer){
	    List<UserDTO> loggedInMarketer = userRepository.findByLoggedInMarketerName(marketer);
	    return loggedInMarketer;
	    }
	    
	    public List<UserDTO> getMarketerNameOnly() throws Exception {
	    List<UserDTO> marketerName = userRepository.findByMarketerName();
	    return marketerName;
	    }
	    
	    public List<UserDTO> loggedInUserName(User user){
		List<UserDTO> loggedInUser = userRepository.findByLoggedInUserName(user);
		return loggedInUser;
		}
	    
	  //  @Cacheable("marketersList")
	    public List<User> marketerList(Branch branch, Pageable pageable){
	    List<User> marketersList = userRepository.findBranchUsersByMarketer(branch);
	    return marketersList;
	    }
	    
	   // @Cacheable("allMarketers")
	    public List<User> allBranchMarketers(Pageable pageable){
	    List<User> allMarketers = userRepository.findAllBranchMarketers();
	    return allMarketers;
	    }
	    
	    public List<User> managementList(){
	    return userRepository.findAllManagementList();
	    }
	    
	    public Long getTotalUsers(){
	    Long userCount = userRepository.getTotalUsers();
	    return userCount;
	    }
	    
	    public Long getTotalBranchUsers(Branch branch) {
	    Long branchUserCount = 	userRepository.getTotalBranchUsers(branch);
	    return branchUserCount;
	    }
	    
	    public Long getTotalAllBranchMarketers() {
	    Long allBranchMarketers = userRepository.getTotalAllBranchMarketers();
	    return allBranchMarketers;
	    }
	    
	    public BigDecimal getMarketerMonthlyTarget(User marketer) {
	    BigDecimal monthlyTarget = userRepository.marketerMonthlyTarget(marketer);
	    return monthlyTarget;
	    }
	    
//	    public List<String> findManagerEmail() {
//	    return userRepository.getManagingDirectorEmail();  
//	    }  
//	    
	    
	    
}
