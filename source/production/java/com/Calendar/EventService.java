package com.Calendar;

import com.DAO.EventDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/1/16.
 */

/******************************************************************************************************************
 *Class: EventService
 * Description: The services which are called from eventResource
 *****************************************************************************************************************/
public class EventService {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
    private static EventDao eventDao = (EventDao) context.getBean("eventDao");

    /******************************************************************************************************************
     *Method: getAllEvents
     * Description: Returns a list of all the users
     *****************************************************************************************************************/
    public List<Event> getAllEvents(){
        try {
            List<Event> events = new ArrayList<Event>();
            events = eventDao.selectAllEventsUnsorted();

            return events;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /******************************************************************************************************************
     *Method: getEvent
     * Description: Uses the string input as eventID and queries the event
     *****************************************************************************************************************/
    public Event getEvent(String eventID){
        try{
            int intID = Integer.parseInt(eventID);
            Event event = eventDao.getEventById(intID);
            return event;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /******************************************************************************************************************
     *Method: addEvent
     * Description: Inserts the event into the event table. Checks for duplication first.
     *****************************************************************************************************************/
    public Event addEvent(Event event, User user){
        try{
            if (!eventDao.hasEvent(event.getEventName(), user.getUsername(), event.getEventAuthor())){ //User doesn't exist. Proceed with user add
                event.setId(eventDao.countEvents() + 1);
                eventDao.insertEvent(event);
                return event;
            }
            else {
                event.setId(-1); // Return user with error id
                return event;
            }
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /************************************************************************************************************************************
     *Method: editEvent
     * Description: Edits the event from the event table. Can only modify events which were
     * created by the user (i.e. cannot modify a liked event). Only the event title, description, and date can be modified. References
     * the eventID.
     ************************************************************************************************************************************/
    public Event editEvent(Event event, User user){
        String username = user.getUsername();
        try{
            String idToString = Long.toString(event.getId());
            if(username != event.getEventAuthor()){ // Modified event must have same username and author //
                event.setId(-1);
                return event;
            }

            if (eventDao.hasEventMod(idToString, username, event.getEventAuthor())){ // Proceed if event exists first
                eventDao.editEvent(event);
                return event;
            }
            else {
                event.setId(-1); // Return user with error id if user does not own the event or if it doesn't exist
                return event;
            }
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /************************************************************************************************************************************
     *Method: deleteEvent
     * Description: Deletes the event specified from the path
     ************************************************************************************************************************************/
    public void deleteEvent(String eventID){
        try{
            eventDao.deleteEvent(eventID);
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
        }
    }
}
