package com.bethsaida.org.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Customer  {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 8348682056500740593L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String password;
	private String firstName ;
	private String lastName;
	private String gender;
	private String Address; 
	private String maritalStatus;
	private String category;
	private String motherMaidenName;
	private String idType;
	private String  idNumber;
	private String phoneNumber;
	private String email;
	private String formFile;
	private String docFile;
	private String additionalImage;
	private String bankName;
	private String bankAccountNumber ;	
	
	
	@OneToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="beneficiary_id", referencedColumnName = "id" )
	private Beneficiary beneficiary;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date registrationDate;
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(targetEntity = User.class, 
	 fetch = FetchType.LAZY )
	@JoinColumn(name="marketer_id") 
	private User marketer ;
	@JsonBackReference
	@OneToMany(mappedBy="customer_id",cascade = CascadeType.ALL, targetEntity=Investment.class)
	private List<Investment> investment;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
    name = "customers_roles", 
    joinColumns = @JoinColumn(name = "customer_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> customerRole = new HashSet<UserRole>() ;
	
	
	public Customer() {
	}

    public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    public Customer(Long id, String userName, String password, String firstName, String lastName, String gender,
			String address, String maritalStatus, String category, String motherMaidenName, String idType,
			String idNumber, String phoneNumber, String email, String formFile, String docFile, String additionalImage,
			String bankName, String bankAccountNumber, Beneficiary beneficiary, Date dateOfBirth, Date registrationDate,
			User marketer, List<Investment> investment, Set<UserRole> customerRole) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		Address = address;
		this.maritalStatus = maritalStatus;
		this.category = category;
		this.motherMaidenName = motherMaidenName;
		this.idType = idType;
		this.idNumber = idNumber;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.formFile = formFile;
		this.docFile = docFile;
		this.additionalImage = additionalImage;
		this.bankName = bankName;
		this.bankAccountNumber = bankAccountNumber;
		this.beneficiary = beneficiary;
		this.dateOfBirth = dateOfBirth;
		this.registrationDate = registrationDate;
		this.marketer = marketer;
		this.investment = investment;
		this.customerRole = customerRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFormFile() {
		return formFile;
	}

	public void setFormFile(String formFile) {
		this.formFile = formFile;
	}

	public String getDocFile() {
		return docFile;
	}

	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}

	public String getAdditionalImage() {
		return additionalImage;
	}

	public void setAdditionalImage(String additionalImage) {
		this.additionalImage = additionalImage;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public Beneficiary getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(Beneficiary beneficiary) {
		this.beneficiary = beneficiary;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public User getMarketer() {
		return marketer;
	}

	public void setMarketer(User marketer) {
		this.marketer = marketer;
	}

	public List<Investment> getInvestment() {
		return investment;
	}

	public void setInvestment(List<Investment> investment) {
		this.investment = investment;
	}

	public Set<UserRole> getCustomerRole() {
		return customerRole;
	}

	public void setCustomerRole(Set<UserRole> customerRole) {
		this.customerRole = customerRole;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", gender=" + gender + ", Address=" + Address + ", maritalStatus="
				+ maritalStatus + ", category=" + category + ", motherMaidenName=" + motherMaidenName + ", idType="
				+ idType + ", idNumber=" + idNumber + ", phoneNumber=" + phoneNumber + ", email=" + email
				+ ", formFile=" + formFile + ", docFile=" + docFile + ", additionalImage=" + additionalImage
				+ ", bankName=" + bankName + ", bankAccountNumber=" + bankAccountNumber + ", beneficiary=" + beneficiary
				+ ", dateOfBirth=" + dateOfBirth + ", registrationDate=" + registrationDate + ", marketer=" + marketer
				+ ", investment=" + investment + ", customerRole=" + customerRole + "]";
	}

	

	
	}
