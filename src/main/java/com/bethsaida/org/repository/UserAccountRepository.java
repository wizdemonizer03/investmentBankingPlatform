package com.bethsaida.org.repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.Branch;
import com.bethsaida.org.models.User;
import com.bethsaida.org.models.UserDTO;


@Repository
public interface UserAccountRepository extends JpaRepository <User, Long>  {
	
	 User getById(Long id);
	
	 Optional<User> findById(Long id);

	 User findByUserName(String username);
	 
	 boolean existsByEmail(String email);
	 
	 @Query(value="select u.email from User u join u.userRole usr where usr.name = 'AUDIT'")
	 String findAuditorEmail();
	 
	 @Query(value="select u.email from User u join u.userRole usr where usr.name = 'INVESTMENT EXECUTIVE'")
	 String findInvestmentExecutiveEmail();
	 
	 @Query(value="select u.email from User u join u.userRole usr where usr.name = 'MANAGING DIRECTOR'")
	 List<String> getManagingDirectorEmail();
	 
	 @Query(value="select branch from User u where u.id = :id")
	 Branch findUserBranchId(Long id);
	 
	 @Query(value="SELECT new com.bethsaida.org.models.UserDTO(u.id, u.firstName, u.lastName) " +
		        "FROM User u JOIN u.userRole usr WHERE usr.name = 'MARKETER'")
	 List<UserDTO> findByMarketerName();
	 
	 @Query(value="SELECT new com.bethsaida.org.models.UserDTO(user.id, user.firstName, user.lastName) " +
		        "FROM User user JOIN user.userRole usr ON user = :marketer WHERE usr.name = 'MARKETER'")
	 List<UserDTO> findByLoggedInMarketerName(User marketer);
	 
	 @Query(value="SELECT new com.bethsaida.org.models.UserDTO(user.id, user.firstName, user.lastName) " +
		        "FROM User user WHERE user = :user")
	 List<UserDTO> findByLoggedInUserName(User user);
	 
	 @Query("SELECT COUNT(U) AS NumberOfUsers from User U")
     Long getTotalUsers();
	 
	 @Query("SELECT COUNT(U) AS NumberOfUsers from User U WHERE U.branch = :branch")
     Long getTotalBranchUsers(Branch branch);
	 
	 @Query("select count(user) from User user join user.userRole usr where usr.name = 'MARKETER'")
	 Long getTotalAllBranchMarketers();
	 
	 @Modifying
	 @Query("update User user set user.target = :target where user.id = :id")
	 void setMarketerMonthlyTarget(@Param("target") BigDecimal target, @Param("id") Long id);
	 
	
	 @Query("select user from User user join user.branch branch on branch = :branch join user.userRole usr where usr.name = 'MARKETER'") 
	 List<User> findBranchUsersByMarketer(Branch branch);
	 
	 @Query("select user from User user join user.userRole usr where usr.name = 'MARKETER'")
	 List<User> findAllBranchMarketers();
	 
	 @Query("select user from User user join user.userRole usr WHERE NOT usr.name = 'MARKETER'")
	 List<User> findAllManagementList();
	
	 @Query("select user.target FROM User user WHERE user = :marketer")
	 BigDecimal marketerMonthlyTarget(User marketer);
	 
	 @Modifying
	 @Transactional
	 @Query("update User user SET user.isEnabled  = 1, status = 'Active' WHERE user.id = ?1")
	 public void activateUser(Long id);
	 
	 
	 @Modifying
	 @Transactional
	 @Query("update User user SET user.isEnabled = 0 WHERE user.id = ?1")
	 public void restrictUser(Long id);
	 
	 @Modifying
	 @Transactional
	 @Query("update User user SET user.isLocked = 1 WHERE user.id = ?1")
	 public void lockUser(Long id);
}
