package com.Calendar;

import com.DAO.EventDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/1/16.
 */
public class EventService {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
    private static EventDao eventDao = (EventDao) context.getBean("eventDao");

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
}