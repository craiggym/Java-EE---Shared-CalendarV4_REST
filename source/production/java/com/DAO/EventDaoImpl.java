package com.DAO;

import com.Calendar.Event;
import com.Calendar.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EventDaoImpl implements EventDao{
    // class variables //
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private static boolean debug = true;


    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /***************************************************************
     * Title: insertEvent
     * Description: Add an event to the database
     * @param event An Event object
     ***************************************************************/
    @Override
    public void insertEvent(Event event) {
        String query = "INSERT INTO Event (EventID, EventName, EventDate, EventDesc, EventUser, EventCreator) VALUES (?,?,?,?,?,?);";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {event.getId(), event.getEventName(), event.getEventDate(), event.getEventDescription(), event.getUsername(), event.getEventAuthor()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
        if(debug) System.out.printf("Added event with name: %s and with user: %s and author: %s", event.getEventName(), event.getUsername(), event.getEventAuthor());
    }

    /******************************************************************************************************************
     *Method: dropEventtable
     * Description: Drops the event table
     *****************************************************************************************************************/
    @Override
    public void dropEventTable() {
        String query = "DROP TABLE Event;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /******************************************************************************************************************
     *Method: createEventTable
     * Description: Creates the event table in the database. Typically used after the dropEventTable
     *****************************************************************************************************************/
    @Override
    public void createEventTable() {
        String query = "CREATE TABLE Event(EventID int, EventName VARCHAR(255), EventDate Date, EventDesc VARCHAR(255), EventUser VARCHAR(255), EventCreator VARCHAR(255));";
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /******************************************************************************************************************
     *Method: eventsExists
     * Description: Takes the parameter username and returns whether or not user has events existing
     *****************************************************************************************************************/
    @Override
    public boolean eventsExists(String username) {
        try {
            String query = "SELECT Count(*) FROM Event WHERE EventUser=?";
            Object[] input = new Object[]{username};
            jdbcTemplate = new JdbcTemplate(dataSource);
            int result = (int) jdbcTemplate.queryForObject(query, input, int.class);

            if (debug) System.out.println("result of query(count from Event where username=user): " + result);
            if (result > 0) return true;
        } catch (Exception e) {
            if (debug) System.out.println("Exception caught in sql query for event count");
            return false;
        }
        return false;
    }

    /******************************************************************************************************************
     *Method: eventsExists
     * Description: Returns whether or not there are events existing in the database
     *****************************************************************************************************************/
    @Override
    public boolean eventsExists() {
        try {
            String query = "SELECT Count(*) FROM Event";
            jdbcTemplate = new JdbcTemplate(dataSource);
            int result = (int) jdbcTemplate.queryForObject(query, int.class);

            if (debug) System.out.println("result of query(total events)): " + result);
            if (result > 0) return true;
        } catch (Exception e) {
            if (debug) System.out.println("Exception caught in sql query for all_user event count");
            return false;
        }
        return false;
    }

    /***********************************************************************************************************************************
     *Method: hasEvent
     * Description: Returns whether or not the user has the specific event. Basically compares the username to the creator of the event
     ***********************************************************************************************************************************/
    @Override
    public boolean hasEvent(String eventname, String username, String creator) {
        try {
            String query = "SELECT EventName FROM Event WHERE EventUser='"+username+"' AND EventName='"+eventname+"' AND EventCreator='"+creator+"'";
            jdbcTemplate = new JdbcTemplate(dataSource);
            String result = (String) jdbcTemplate.queryForObject(query, String.class);

            if (debug) System.out.println("result of hasEvent: " + result);
            return true;
        } catch (Exception e) {
            if (debug) System.out.println("Exception caught in sql query for hasEvent!!");
            return false;
        }
    }

    @Override
    public boolean hasEventMod(String id, String username, String creator){
        try {
            String query = "SELECT EventUser FROM Event WHERE EventUser='"+username+"' AND EventID='"+id+"' AND EventCreator='"+creator+"'";
            jdbcTemplate = new JdbcTemplate(dataSource);
            String result = (String) jdbcTemplate.queryForObject(query, String.class);

            if (debug) System.out.println("result of hasEvent: " + result);
            return true;
        } catch (Exception e) {
            if (debug) System.out.println("Exception caught in sql query for hasEvent!!");
            return false;
        }
    }

    /******************************************************************************************************************
     *Method: countEvents
     * Description: Returns the number of events in the database. Used for incrementing the event count.
     *****************************************************************************************************************/
    @Override
    public int countEvents() {
        try {
            String query = "SELECT MAX(EventID) FROM Event";
            jdbcTemplate = new JdbcTemplate(dataSource);
            int res = (int) jdbcTemplate.queryForObject(query, int.class);

            return res;
        }
        catch(Exception e){
            if (debug) System.out.println("error querying for count");
            return 0;
        }
    }

    /******************************************************************************************************************
     *Method: selectRecentEvent
     * Description: Returns a list of only the recent events
     *****************************************************************************************************************/
    @Override
    public List<Event> selectRecentEvent(String username) {
        Date todays_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,3);
        Date beyond_date = cal.getTime();

        String query = "SELECT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser='"+username +"' AND EventDate >= " + todays_date + "  ORDER BY EventDate ASC";
        Object[] input = new Object[]{username};
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        return events;
    }

    /******************************************************************************************************************
     *Method: selectAllEvents
     * Description: Returns a list of all the events in the database pertaining to the user
     *****************************************************************************************************************/
    @Override
    public List<Event> selectAllEvent(String username) {
        String query = "SELECT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser='"+username +"' ORDER BY EventDate ASC";
        Object[] input = new Object[]{username};
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        return events;
    }

    /******************************************************************************************************************
     *Method: selectAllEvents
     * Description: Returns a list of all the events in the database
     *****************************************************************************************************************/
    @Override
    public List<Event> selectAllEvents() {
        String query = "SELECT DISTINCT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser=EventCreator ORDER BY EventDate ASC";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        return events;
    }

    /**********************************************************************************************************************************
     *Method: SelectAllEventsUnsorted
     * Description: Used for the JSON response. Returns the list of all events sorted by the order which they were created, NOT date
     **********************************************************************************************************************************/
    @Override
    public List<Event> selectAllEventsUnsorted() {
        String query = "SELECT DISTINCT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser=EventCreator ORDER BY EventID ASC";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        return events;
    }

    /******************************************************************************************************************
     *Method: getEventById
     * Description: Returns an event given by the parameter ID
     *****************************************************************************************************************/
    @Override
    public Event getEventById(int id) {
        String query = "select EventID, EventName, EventDate, EventDesc, EventUser, EventCreator from Event where EventID=" + id + " limit 1";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        if (events != null && events.size() > 0) {
            return events.get(0);
        }
        return null;
    }

    /******************************************************************************************************************
     *Method: selectLikedEvents
     * Description: Uses the parameter username to return a list of all the events they liked
     * NOTE: Users liked their created events by default.
     *****************************************************************************************************************/
    @Override
    public List<Event> selectLikedEvents(String username) {
        String query = "SELECT DISTINCT EventID, EventName, EventDate, EventDesc, EventUser, EventCreator FROM Event WHERE EventUser = \'"+username+"\' ORDER BY EventDate ASC";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Event> events = jdbcTemplate.query(query, new EventMapper());
        return events;
    }

    /****************************************************************************************************************************************
     * Title: editEvent
     * Description: Edits an event in the database. Only the title, description, and the date can be edited. All else should remain the same
     * @param event An Event object
     ****************************************************************************************************************************************/
    @Override
    public void editEvent(Event event) {
        String query = "UPDATE Event SET EventName=?, EventDate=?, EventDesc=?, EventUser=?, EventCreator=? WHERE EventID=?;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {event.getEventName(), event.getEventDate(), event.getEventDescription(), event.getUsername(), event.getEventAuthor(), event.getId()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
        if(debug) System.out.printf("Updated event with name: %s and with user: %s and author: %s", event.getEventName(), event.getUsername(), event.getEventAuthor());
    }

    /****************************************************************************************************************************************
     * Title: deleteEvent
     * Description: Deletes the event from the database
     ****************************************************************************************************************************************/
    @Override
    public void deleteEvent(String eventID) {
        String query = "DELETE FROM Event WHERE EventID=?;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {eventID};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
    }

    /****************************************************************************************************************************************
     * Title: deleteLikedEvent
     * Description: Deletes the event from the database
     ****************************************************************************************************************************************/
    @Override
    public void deleteLikedEvent(String eventID) {
        String query = "DELETE FROM Event WHERE EventID=? AND EventUser != EventCreator;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {eventID};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
    }
}