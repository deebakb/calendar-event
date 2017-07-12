package com.calendar.events.boot.rest.controller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.calendar.events.Application;
import com.calendar.events.boot.rest.data.DataBuilder;
import com.calendar.events.boot.rest.model.CalendarEvent;
import com.calendar.events.boot.rest.model.CalendarUser;
import com.calendar.events.boot.rest.repository.CalendarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class CalendarControllerTest {

	@Value("${local.server.port}")
	private int port;
	private URL base;
	private RestTemplate template;

	@Autowired
	private DataBuilder dataBuilder;
	
	@Autowired
	private CalendarRepository calendarRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8"; 
	
	
	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/rest/calendarEvent");
		template = new TestRestTemplate();		
		
		/* remove and reload test data */
		calendarRepository.deleteAll();		
		dataBuilder.createCalendarEvent().forEach(event -> calendarRepository.save(event));		
	}

	@Test
	public void getAllEvents() throws Exception {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		
		List<CalendarEvent> events = convertJsonToEvents(response.getBody());		
		assertThat(events.size(), equalTo(3));		
	}
	
	@Test
	public void getCalendarEventById() throws Exception {
		CalendarUser ca = new CalendarUser("Joe","jdoe");
		Long calendarId = getEventByTitle("Daily Standup");
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), calendarId), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		CalendarEvent event = convertJsonToEvent(response.getBody());
		
		assertThat(event.getCalendarUser().getUser(), equalTo(ca.getUser()));
		assertThat(event.getDuration(), equalTo(15));
		assertThat(event.getEventTime(), equalTo(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 09:00:00")));
		assertThat(event.getTitle(), equalTo("Daily Standup"));
		assertThat(event.getLocation(), equalTo("Meeting Room1"));
		
	}
	
	@Test
	public void createCalendarEvent() throws Exception {

	    CalendarUser ca = new CalendarUser("Joe", "jday");
		CalendarEvent event = new CalendarEvent("Backlog grooming", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 11:00:00"),60,"Meeting Room2","Joe,Chris,Smith", new Date(System.currentTimeMillis()),
            "true",ca);

		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/rest/calendarEvent", event, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		CalendarEvent ce = convertJsonToEvent(response.getBody());		
		assertThat(event.getCalendarUser().getUser(), equalTo(ca.getUser()));
		assertThat(event.getDuration(), equalTo(60));
		assertThat(event.getEventTime(), equalTo(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-07-21 11:00:00")));
		assertThat(event.getTitle(), equalTo("Backlog grooming"));
		assertThat(event.getLocation(), equalTo("Meeting Room2"));
		
	}

	@Test
	public void updateCustomer() throws Exception {
	    CalendarUser ca = new CalendarUser("Joe","jdoe");
		Long eventId = getEventByTitle("Daily Standup");
		ResponseEntity<String> eventResponse = template.getForEntity(String.format("%s/%s", base.toString(), eventId), String.class);
		assertThat(eventResponse.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(eventResponse.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		CalendarEvent ce = convertJsonToEvent(eventResponse.getBody());
		assertThat(ce.getCalendarUser().getUser(), equalTo(ca.getUser()));
		assertThat(ce.getDuration(), equalTo(15));
	
		assertThat(ce.getTitle(), equalTo("Daily Standup"));
		assertThat(ce.getLocation(), equalTo("Meeting Room1"));
		
		
		/* convert JSON response to Java and update name */
		CalendarEvent eventToUpdate = objectMapper.readValue(eventResponse.getBody(), CalendarEvent.class);
		eventToUpdate.setLocation("Meeting Room3");
		eventToUpdate.setDuration(30);;

		/* PUT updated event */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON); 
		HttpEntity<CalendarEvent> entity = new HttpEntity<CalendarEvent>(eventToUpdate, headers); 
		ResponseEntity<String> response = template.exchange(String.format("%s/%s", base.toString(), eventId), HttpMethod.PUT, entity, String.class, eventId);
		
		assertThat(response.getBody(), nullValue());
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));

		/* GET updated event and ensure data is updated as expected */
		ResponseEntity<String> getUpdatedEventResponse = template.getForEntity(String.format("%s/%s", base.toString(), eventId), String.class);
		assertThat(getUpdatedEventResponse.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(getUpdatedEventResponse.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		CalendarEvent updatedEvent = convertJsonToEvent(getUpdatedEventResponse.getBody());
		assertThat(updatedEvent.getCalendarUser().getUser(), equalTo(ca.getUser()));
		assertThat(updatedEvent.getDuration(), equalTo(30));
		assertThat(updatedEvent.getTitle(), equalTo("Daily Standup"));
		assertThat(updatedEvent.getLocation(), equalTo("Meeting Room3"));
		
	}

	@Test
	public void deleteCustomer() throws Exception {
	    CalendarUser ca = new CalendarUser("Joe","jdoe");
		Long eventId = getEventByTitle("Daily Standup");		
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), eventId), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		CalendarEvent event = convertJsonToEvent(response.getBody());
		assertThat(event.getCalendarUser().getUser(), equalTo(ca.getUser()));
		assertThat(event.getDuration(), equalTo(15));
		assertThat(event.getTitle(), equalTo("Daily Standup"));
		assertThat(event.getLocation(), equalTo("Meeting Room1"));
		
		/* delete event */
		template.delete(String.format("%s/%s", base.toString(), eventId), String.class);
		
		/* attempt to get event and ensure qwe get a 404 */
		ResponseEntity<String> secondCallResponse = template.getForEntity(String.format("%s/%s", base.toString(), eventId), String.class);
		assertThat(secondCallResponse.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void getNonExistantCalendarEventReturnsError404() throws Exception {
		
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), 999999), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));		
	}
	
	/**
	 * Convenience method for testing that gives us the event 
	 * id based on test user name. Need this as IDs will increment
	 * as tests are rerun
	 * 
	 * @param calendarUser
	 * @return calendar user id
	 */
	private Long getEventByTitle(String title){
		return calendarRepository.findByTitle(title).stream().findAny().get().getId();
	}
	
	private CalendarEvent convertJsonToEvent(String json) throws Exception {		
		//ObjectMapper mapper = new ObjectMapper();
		return objectMapper.readValue(json, CalendarEvent.class);
	}
	
	private List<CalendarEvent> convertJsonToEvents(String json) throws Exception {		
		//ObjectMapper mapper = new ObjectMapper();
		return objectMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, CalendarEvent.class));
	}
}