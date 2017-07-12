package com.calendar.events.boot.rest.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.calendar.events.boot.rest.exception.CalendarEventNotFoundException;
import com.calendar.events.boot.rest.exception.InvalidCalendarEventRequestException;
import com.calendar.events.boot.rest.model.CalendarEvent;
import com.calendar.events.boot.rest.repository.CalendarRepository;

/**
 * Calendar Controller exposes a series of RESTful endpoints
 */
@RestController
public class CalendarController {

	@Autowired
	private CalendarRepository calendarRepository;

	/**
    * The logging instance.
    */
    private final Logger logger = LoggerFactory.getLogger(CalendarController.class);
	/**
	 * Get event using id. Returns HTTP 404 if customer not found
	 * 
	 * @param eventId
	 * @return retrieved CalendarEvent
	 */
	@RequestMapping(value = "/rest/calendarEvent/{eventId}", method = RequestMethod.GET)
	public CalendarEvent getCalendarEvent(@PathVariable("eventId") Long eventId) {
		
		if (null==eventId) {
			throw new InvalidCalendarEventRequestException();
		}
		
		CalendarEvent event = calendarRepository.findOne(eventId);
		
		if(null==event){
			throw new CalendarEventNotFoundException();
		}
		
		return event;
	}

	
	/**
	 * Gets all events.
	 *
	 * @return the events
	 */
	@RequestMapping(value = "/rest/calendarEvent", method = RequestMethod.GET)
	public List<CalendarEvent> getCustomers() {
		
		return (List<CalendarEvent>) calendarRepository.findAll();
	}

	/**
	 * Get events between date range
	 */
	@RequestMapping(value = "/rest/calendarEvent/{startDate}/{endDate}", method = RequestMethod.GET)
    public List<CalendarEvent> getCalendarEventBetweenDates(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        
        if (null == startDate || null == endDate) {
            throw new InvalidCalendarEventRequestException();
        }
        try{
          Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
          Date end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
          List<CalendarEvent> events = calendarRepository.findBetweenDateRange(start, end);
          
          if(null==events){
              throw new CalendarEventNotFoundException();
          }
          return events;
        }
        catch(ParseException e){
          e.printStackTrace();
          return null;
        }
        
        
        
    }
	
	
	/**
	 * Create a new event and return in response with HTTP 201
	 *
	 * @param the event
	 * @return created event
	 */
	@RequestMapping(value = { "/rest/calendarEvent" }, method = { RequestMethod.POST })
	public CalendarEvent createEvent(@RequestBody CalendarEvent event, HttpServletResponse httpResponse, WebRequest request) {

	  CalendarEvent createdEvent = null;
	  CalendarEvent ce = sendReminderEvent(event);
      
	  createdEvent = calendarRepository.save(ce);
	  httpResponse.setStatus(HttpStatus.CREATED.value());
		
	  return createdEvent;
	}

	
	/**
	 * Update event with given event id.
	 *
	 * @param calendarEvent
	 */
	@RequestMapping(value = { "/rest/calendarEvent/{eventId}" }, method = { RequestMethod.PUT })
	public void updateCustomer(@RequestBody CalendarEvent calendarEvent, @PathVariable("eventId") Long eventId,
								   		  HttpServletResponse httpResponse) {

		if(!calendarRepository.exists(eventId)){
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
		}
		else{
		    CalendarEvent ce = sendReminderEvent(calendarEvent);
			calendarRepository.save(ce);
			httpResponse.setStatus(HttpStatus.NO_CONTENT.value());	
		}
	}

	
	/**
	 * Deletes the event with given event id if it exists and returns HTTP204.
	 *
	 * @param eventId the event id
	 */
	@RequestMapping(value = "/rest/calendarEvent/{eventId}", method = RequestMethod.DELETE)
	public void removeCustomer(@PathVariable("eventId") Long eventId, HttpServletResponse httpResponse) {

		if(calendarRepository.exists(eventId)){
			calendarRepository.delete(eventId);	
		}
		
		httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
	}
	
	/**
	 * Method to send event reminder
	 * @param event
	 * @return
	 */
	private CalendarEvent sendReminderEvent(CalendarEvent event){
	  String reminderMessage = "Subject :"+ event.getTitle() +"\r\n"+ "Attendees: "+event.getAttendees()+"\r\n"+"Meeting time: "+event.getEventTime()+"\r\n"+"Location: "+event.getLocation();
      DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");

      // Set the formatter to use a different timezone  
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));  

	  Date d1 = event.getReminderTime();
	  Date d2 = new Date(System.currentTimeMillis());
	  
	  long timeInSeconds = 0;
	  if(d1.after(d2)){
	    timeInSeconds = (d1.getTime() - d2.getTime())/1000;
	  }
	  Timer timer = new Timer();
      timer.schedule(new Reminder(timer, reminderMessage), timeInSeconds);
      event.setReminderSent("true");
      event.setReminderTime(d1);
      return event;
	}

	/**
	 * 
	 * @author deeba
	 *
	 */
	class Reminder extends java.util.TimerTask {
	  Timer timer;
	  String msg = null;
	  Reminder(Timer t, String reminderMessage){
	    timer = t;
	    msg = reminderMessage;
	  }
      @Override
      public void run() {
          System.out.format(msg);
          logger.info(msg);
          timer.cancel(); // Terminate the timer thread
      }
  }
}