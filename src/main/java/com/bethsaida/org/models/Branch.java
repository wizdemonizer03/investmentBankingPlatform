package com.bethsaida.org.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Branch {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private String state;
	private String city;
	
	@JsonBackReference
	@OneToMany(mappedBy="branch",cascade = CascadeType.ALL, targetEntity= User.class)
	private List <User> user;
	
	public Branch() {
	}
	
	public Branch(Long id, String name, String address, String state, String city,List <User> user) {
	this.id = id;	
    this.name = name;
	this.address = address;
	this.state = state;
	this.city = city;
	this.user = user;
	}
   
	
   public Long getId() {
   return id;   
   }
   public void setId(Long id) {
   this.id = id;
   }
	
   public String getName() {
   return name;
   }
   public void setName(String name) {
   this.name = name;
   }
   
   public String getAddress() {
   return address;
   }
   public void setAddress(String address) {
   this.address = address;
   }
   
   public String getState() {
   return state;
   }
   public void setState(String state) {
   this.state = state;
   }
   
   public String getCity() {
   return city;
   }
   public void setCity(String city) {
   this.city = city;
   }
   
   public List <User> getUser() {
   return user;
   }
   public void setUser(List<User> user) {
   this.user = user;
   }

	/*
	 * @Override public String toString() { return "Branch [id=" + id + ", name=" +
	 * name + ", address=" + address + ", state=" + state + ", city=" + city +
	 * ", user=" + user + "]"; }
	 */
	   
   
   
   
}
