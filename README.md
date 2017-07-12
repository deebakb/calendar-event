# Calendar - Event

Calendar Event is a spring boot application to create calendar events

## Technology stack

* Java 8
* Spring Boot
* HSQL
* Hibernate
* JPARepository
* JUnit
* POSTMAN


## Configuration of Local Environment

1. Install:
* JDK
* Maven




* Eclipse with plugins:
- STS plugin
- [EclEmma](http://www.eclemma.org/installation.html) (code coverage plugin)
- [Lombok] (https://projectlombok.org/download.html)
* Chrome and Postman plugin


## Getting started
```
$ mvn Spring-Boot:run
```

Junit Test - CalendarControllerTest.java

Log file Location  - \calendar-application\temp\application.log  ---> All th event reminders are written to this log

Model Info - CalendarUser.java, CalendarEvent.java

REST API information
```````````````````````
Note :  These APIs can be tested either using tools such as POSTMAN or CURL or through the JUNIT test scenarios in CalendarControllerTest.java
@POST
URL  : http://localhost:8080/rest/calendarEvent
Header : Content-type = application/json
Request Body : {
  "title": "Team Retrospective",
  "eventTime": "2017-07-21T13:00:00.000+0000",
  "duration": 15,
  "location": "Meeting Room1",
  "attendees": "Danny@gmail.com,Chris@gmail.com,Sally@gmail.com",
  "calendarUser": {
    "name": "chris",
    "user": "cguard"
  },
  "reminderTime": "2017-07-12T24:40:00Z",
  "reminderSent": "false"
}
On Success Response = 201 Created

@GET  [To retrieve all events]
URL : http://localhost:8080/rest/calendarEvent

@GET [To retrieve event based on event id]
URL : http://localhost:8080/rest/calendarEvent/{id}

@GET [To retrieve events within a date range for example in a month, week year etc]
URL : http://localhost:8080/rest/calendarEvent/{start-date}/{end-date} 
e.g. http://localhost:8080/rest/calendarEvent/2017-08-01 13:00:00/2017-08-30 13:00:00

@PUT [To edit an event based on the event id]
URL: http://localhost:8080/rest/calendarEvent/{id}
Header : Content-type = application/json
Request Body : {
  "title": "Team Retrospective",
  "eventTime": "2017-07-21T13:00:00.000+0000",
  "duration": 15,
  "location": "Meeting Room1",
  "attendees": "Danny@gmail.com,Chris@gmail.com,Sally@gmail.com",
  "calendarUser": {
    "name": "chris",
    "user": "cguard"
  },
  "reminderTime": "2017-07-12T24:40:00Z",
  "reminderSent": "false"
}

@DELETE [To delete a event]
URL: http://localhost:8080/rest/calendarEvent/{id}
On Success: 204 No Content