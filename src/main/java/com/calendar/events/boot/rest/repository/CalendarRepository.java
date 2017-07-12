package com.calendar.events.boot.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.calendar.events.boot.rest.model.CalendarEvent;

public interface CalendarRepository extends CrudRepository<CalendarEvent, Long> {

    public List<CalendarEvent> findByTitle(String title); 
    
    @Query("select c from CalendarEvent c where eventTime between (:beginDate) and (:endDate)")
    public List<CalendarEvent> findBetweenDateRange(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
}