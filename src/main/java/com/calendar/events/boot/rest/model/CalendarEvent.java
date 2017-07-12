package com.calendar.events.boot.rest.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CalendarEvent{

	public CalendarEvent(){}
	
	public CalendarEvent(String title, Date eventTime, Integer duration, String location, String attendees, Date reminderTime, String reminderSent, CalendarUser calendarUser) {
		super();
		this.title = title;
		this.eventTime = eventTime;
		this.duration = duration;
		this.location = location;
		this.attendees = attendees;
		this.reminderTime = reminderTime;
		this.reminderSent = reminderSent;
		this.calendarUser = calendarUser;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
    private String title;
    
    private Date eventTime; 
    
    private Integer duration;
    
    private String location;
    
    private String attendees;
    
    @OneToOne(cascade = {CascadeType.ALL})
    private CalendarUser calendarUser;

    private Date reminderTime;

    private String reminderSent;
    
	
	/**
     * @return the title
     */
    public String getTitle(){
      return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title){
      this.title = title;
    }

    /**
     * @return the eventTime
     */
    public Date getEventTime(){
      return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(Date eventTime){
      this.eventTime = eventTime;
    }

   /**
   * @return the duration
   */
  public Integer getDuration(){
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration(Integer duration){
    this.duration = duration;
  }

  /**
   * @return the location
   */
  public String getLocation(){
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location){
    this.location = location;
  }

  /**
   * @return the attendees
   */
  public String getAttendees(){
    return attendees;
  }

  /**
   * @param attendees the attendees to set
   */
  public void setAttendees(String attendees){
    this.attendees = attendees;
  }

  /**
   * @return the reminderTime
   */
  public Date getReminderTime(){
    return reminderTime;
  }

  /**
   * @param reminderTime the reminderTime to set
   */
  public void setReminderTime(Date reminderTime){
    this.reminderTime = reminderTime;
  }

  /**
   * @return the reminderSent
   */
  public String getReminderSent(){
    return reminderSent;
  }

  /**
   * @param reminderSent the reminderSent to set
   */
  public void setReminderSent(String reminderSent){
    this.reminderSent = reminderSent;
  }

  /**
   * @return the calendarUser
   */
  public CalendarUser getCalendarUser(){
    return calendarUser;
  }

  /**
   * @param calendarUser the calendarUser to set
   */
  public void setCalendarUser(CalendarUser calendarUser){
    this.calendarUser = calendarUser;
  }

  public Long getId(){
    return id;
  }
  
  public void setId(long id){
    this.id = id;
  }
}