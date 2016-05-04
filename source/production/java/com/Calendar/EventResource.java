package com.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */

/******************************************************************************************************************
 *Class: EventResource
 * Description: Handles all methods relating to the event resource
 *****************************************************************************************************************/
@Path("/events")
public class EventResource {
    EventService eventService = new EventService();

    /******************************************************************************************************************
     *Method: getAllEvents
     * Description: Returns a JSON list of all events in the database.
     *****************************************************************************************************************/
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

    /******************************************************************************************************************
     *Method: addEvent
     * Description: Adds an event with POST using the parameters passed from the JSON body.
     *****************************************************************************************************************/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Event addEvent(Event event){
        User user = new UserService().getUser(event.getUsername());
        Event addEvent = eventService.addEvent(event, user);

        if (event.getId() == -1) throw new WebApplicationException(Response.Status.CONFLICT); // event duplication
        else if (event.getId() == -2) throw new WebApplicationException(Response.Status.BAD_REQUEST); // Parameters
        else return event;
    }

    /******************************************************************************************************************
     *Method: editEvent
     * Description: Edits an event with PUT using the parameters passed from the JSON body.
     *****************************************************************************************************************/
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Event editEvent(Event event){
        User user = new UserService().getUser(event.getUsername());
        Event editEvent = eventService.editEvent(event, user);

        if (event.getId() == -2) throw new WebApplicationException(Response.Status.BAD_REQUEST); // Parameters
        return event;
    }

    /******************************************************************************************************************
     *Method: deleteEvent
     * Description: Deletes and event with DELETE
     *****************************************************************************************************************/
    @Path("/{eventID}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteEvent(@PathParam("eventID") String eventID){
        eventService.deleteEvent(eventID);
    }

    /******************************************************************************************************************
     *Method: getEvent
     * Description: Uses the eventID to return a specific event.
     *****************************************************************************************************************/
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

    /******************************************************************************************************************
     *Method: getURI
     * Description: Cleaner way to handle the redundancy of generating the URI for each link
     *****************************************************************************************************************/
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