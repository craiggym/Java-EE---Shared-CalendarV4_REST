package com.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/events")
public class EventResource {
    EventService eventService = new EventService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEvents(@Context UriInfo uriInfo) {
        List<Event> events =eventService.getAllEvents();
        for(Event e:events){
            String uri = uriInfo.getBaseUriBuilder()
                    .path(EventResource.class)
                    .path(Long.toString(e.getId()))
                    .build()
                    .toString();
            e.addLink(uri, "self");
        }
        return events;
    }

    @Path("/{eventID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEvent(@PathParam("eventID") String eventID, @Context UriInfo uriInfo) {
        Event event = null;
        event = eventService.getEvent(eventID);

        String uri = uriInfo.getBaseUriBuilder()
                .path(EventResource.class)
                .path(eventID)
                .build()
                .toString();
        event.addLink(uri, "self");

        return event;
    }
}