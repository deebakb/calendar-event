package com.calendar.events.boot.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="")
public class CalendarUser{

	public CalendarUser(){}
	
	public CalendarUser(String name, String user) {
		this.name = name;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	/**
   * @return the id
   */
  public long getId(){
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id){
    this.id = id;
  }

  private String name;
	
	/**
   * @return the name
   */
  public String getName(){
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name){
    this.name = name;
  }

  private String user;

  /**
   * @return the user
   */
  public String getUser(){
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(String user){
    this.user = user;
  }
}