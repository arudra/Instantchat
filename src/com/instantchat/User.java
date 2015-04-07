// Copyright 2010 Google Inc. All Rights Reserved.

package com.instantchat;

import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
/**
 * 
 *
 */
@PersistenceCapable
public class User {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;	//Uniquely generated key for each chat (generated on create)
  
  @Persistent
  private String userId;
  
  @Persistent
  private String firstname;
  
  @Persistent
  private String lastname;
  
  @Persistent
  private String bio;
  
  @Persistent
  private String picurl;
  
  @Persistent
  private String city;
  
  @Persistent
  private String country;
  
  @Persistent
  private String email;
  
  @Persistent
  private String gender;
  
  @Persistent
  private String age;
  
  public User(String userId){
	  this.userId = userId;
	  this.firstname = "John";
	  this.lastname = "Doe";
	  this.picurl = "avatar.png";
	  this.bio = "Hi";
	  this.city = "Toronto";
	  this.country = "Canada";
	  this.email = "john.doe@example.com";
	  this.gender = "M";
	  this.age ="20";
  }
  
  public void setUserId(String userId) {
	  this.userId = userId;
  }
  public void setFirstName(String firstname) {
	  this.firstname = firstname;
  }
  public void setLastName(String lastname) {
	  this.lastname = lastname;
  }
  public void setBio(String bio) {
	  this.bio = bio;
  }
  public void setCity(String city) {
	  this.city = city;
  }
  public void setCountry(String country) {
	  this.country = country;
  }
  public void setEmail(String email) {
	  this.email = email;
  }
  public void setGender(String gender) {
	  this.gender = gender;
  }
  public void setAge(String age) {
	  this.age = age;
  }
  public void setPicurl(String picurl) {
	  this.picurl = picurl;
  }
  
  public Key getKey() {
	  return this.key;
  }
  
  public String getUserId() {
	  return this.userId;
  }
  public String getFirstName() {
	  return this.firstname;
  }
  public String getLastName() {
	  return this.lastname;
  }
  public String getBio() {
	  return this.bio;
  }
  public String getCity() {
	  return this.city;
  }
  public String getCountry() {
	  return this.country;
  }
  public String getEmail() {
	  return this.email;
  }
  public String getGender() {
	  return this.gender;
  }
  public String getAge() {
	  return this.age;
  }
  public String getPicurl() {
	  return this.picurl;
  }

  

}