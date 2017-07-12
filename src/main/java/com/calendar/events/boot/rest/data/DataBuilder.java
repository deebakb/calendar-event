package com.calendar.events.boot.rest.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.calendar.events.boot.rest.model.CalendarEvent;
import com.calendar.events.boot.rest.model.CalendarUser;

@Component
public class DataBuilder {
	
	public List<CalendarEvent> createCalendarEvent() {
      CalendarEvent calendarEvent1 = null;
      CalendarEvent calendarEvent2 = null;
      CalendarEvent calendarEvent3 = null;
      
      try{
        calendarEvent1 = new CalendarEvent("Daily Standup", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 09:00:00"),15,"Meeting Room1","joe@gmail.com, chris@gmail.com, smith@gmail.com", new Date(System.currentTimeMillis()),
            "true",new CalendarUser("Joe", "jdoe"));
        calendarEvent2 = new CalendarEvent("Backlog grooming", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 11:00:00"),60,"Meeting Room2","joe@gmail.com, chris@gmail.com, smith@gmail.com", new Date(System.currentTimeMillis()),
            "true",new CalendarUser("Joe", "jdoe"));

        calendarEvent3 = new CalendarEvent("Sprint Preplanning", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 14:00:00"),60,"Meeting Room3","joe@gmail.com, chris@gmail.com, smith@gmail.com, shiela@gmail.com, john@gmail.com", new Date(System.currentTimeMillis()),
            "true",new CalendarUser("Joe", "jdoe"));
        return Arrays.asList(calendarEvent1, calendarEvent2, calendarEvent3);
      }
      catch(ParseException e){
        e.printStackTrace();
        return null;
      }

	
	}
}
