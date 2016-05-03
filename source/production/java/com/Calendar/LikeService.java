package com.Calendar;

import com.DAO.EventDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}
