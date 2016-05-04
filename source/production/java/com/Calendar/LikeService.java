package com.Calendar;

import com.DAO.EventDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/2/16.
 */
public class LikeService {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
    EventDao eventDao = (EventDao) context.getBean("eventDao");

    public List<Event> getUserLikedEvents(String username){
        try {
            List<Event> events = new ArrayList<Event>();
            events = eventDao.selectLikedEvents(username);

            return events;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /************************************************************************************************************************************
     *Method: deleteEvent
     * Description: Deletes the event specified from the path
     ************************************************************************************************************************************/
    public void deleteLikedEvent(String username,String eventID){
        try{
            Event event = eventDao.getEventById(Integer.parseInt(eventID));
            if (event.getEventAuthor() == username) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            eventDao.deleteLikedEvent(eventID);
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
        }
    }


}
