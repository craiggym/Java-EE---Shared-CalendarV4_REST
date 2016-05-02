package com.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/events")
public class EventResource {
    EventService eventService = new EventService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Path("/{eventID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Event getUser(@PathParam("eventID") String eventID) {
        Event event = null;
        event = eventService.getEvent(eventID);
        return event;
    }
}