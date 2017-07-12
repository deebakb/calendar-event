package com.calendar.events.boot.rest.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.calendar.events.boot.rest.repository.CalendarRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner{

	@Autowired
	private DataBuilder dataBuilder;
	
	@Autowired
	private CalendarRepository calendarRepository;

	
	@Override
	public void run(String... arg0) throws Exception {
		
		log.debug("Loading test data...");
		dataBuilder.createCalendarEvent().forEach(event -> calendarRepository.save(event));
		log.debug("Test data loaded...");	
	}
}