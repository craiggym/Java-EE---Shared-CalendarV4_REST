package com.DAO;

import com.Calendar.Event;
import java.util.List;

public interface EventDao {
    void insertEvent(Event event);
    void createEventTable();
    void dropEventTable();
    boolean eventsExists(String username);
    int countEvents();
    boolean eventsExists();
    boolean hasEvent(String eventname, String username, String creator);
    boolean hasEventMod(String id, String username, String creator);
    List<Event> selectRecentEvent(String username);
    List<Event> selectAllEvent(String username);
    List<Event> selectAllEvents();
    List<Event> selectAllEventsUnsorted();
    List<Event> selectLikedEvents(String username);
    Event getEventById(int id);
    void editEvent(Event event);
    void deleteEvent(String eventID);
    void deleteLikedEvent(String eventID);
}
