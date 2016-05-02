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
            String uri = getURI(uriInfo, Long.toString(e.getId()));
            String uriUser = getURIUser(uriInfo, e.getUsername());
            e.addLink(uri, "self");
            e.addLink(uriUser, "user");
        }
        return events;
    }

    @Path("/{eventID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEvent(@PathParam("eventID") String eventID, @Context UriInfo uriInfo) {
        Event event = null;
        event = eventService.getEvent(eventID);

        String uri = getURI(uriInfo, eventID);
        String uriUser = getURIUser(uriInfo, event.getUsername());
        event.addLink(uri, "self");
        event.addLink(uriUser, "user");

        return event;
    }

    public String getURI(UriInfo uriInfo, String eventID){
        String uri = uriInfo.getBaseUriBuilder()
                .path(EventResource.class)
                .path(eventID)
                .build()
                .toString();
        return uri;
    }
    public String getURIUser(UriInfo uriInfo, String author){
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(author)
                .build()
                .toString();
        return uri;
    }
}