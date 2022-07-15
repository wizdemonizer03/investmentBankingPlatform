package com.bethsaida.org.models;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String firstName ;
	private String lastName;
	
	@Column(name="user_name", unique=true)
	private String userName;
	
	private String password;
	private String Gender; 
    private String phoneNumber;
    
    private boolean isEnabled ;
    
   @Column(unique=true)
	private String email;
	
	private BigDecimal target;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = Branch.class, 
	 fetch = FetchType.LAZY )
	@JoinColumn(name="branch_id") 
    private Branch branch;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
    name = "users_roles", 
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> userRole = new HashSet<UserRole>() ;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @JsonBackReference
	@OneToMany(mappedBy="marketer",cascade = CascadeType.ALL, targetEntity=Customer.class)
    private List <Customer> customer;
    
    @JsonBackReference(value="marketerHistory")
	@OneToMany(mappedBy="marketer",cascade = CascadeType.ALL, targetEntity=MarketerTransactionsHistory.class)
    private List<MarketerTransactionsHistory> marketerTransactionsHistory;
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getTarget() {
		return target;
	}

	public void setTarget(BigDecimal target) {
		this.target = target;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public List<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(List<Customer> customer) {
		this.customer = customer;
	}

	public List<MarketerTransactionsHistory> getMarketerTransactionsHistory() {
		return marketerTransactionsHistory;
	}

	public void setMarketerTransactionsHistory(List<MarketerTransactionsHistory> marketerTransactionsHistory) {
		this.marketerTransactionsHistory = marketerTransactionsHistory;
	}

 }
