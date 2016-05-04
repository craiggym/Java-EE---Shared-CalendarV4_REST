package com.Calendar;

import com.DAO.EventDao;
import com.DAO.UserDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/1/16.
 */
public class UserService {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
    private static UserDao userDao = (UserDao) context.getBean("userDao");
    private static EventDao eventDao = (EventDao) context.getBean("eventDao");


    /******************************************************************************************************************
     *Method: getAllUsers
     * Description: Returns a list of all the users in the database
     *****************************************************************************************************************/
    public List<User> getAllUsers(){
        try {
            List<User> users = new ArrayList<User>();
            users = userDao.selectAllUsers();

            return users;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /******************************************************************************************************************
     *Method: getUser
     * Description: Returns a user given by the parameter 'user'
     *****************************************************************************************************************/
    public User getUser(String username){
        try{
            User user = userDao.selectUser(username);
            return user;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /******************************************************************************************************************
     *Method: getURI
     * Description: Returns the URI for the Links attribute
     *****************************************************************************************************************/
    public User addUser(User user){
        try{ // Before inserting, check if required parameters are there //
            if(user.getUsername() == null || user.getFirst_name() == null || user.getLast_name() == null ||
                    user.getPassword() == null) {
                user.setUserID(-2); // bad request
                return user;
            }

            else if (!userDao.userExists(user.getUsername())) { //User doesn't exist. Proceed with user add
                user.setUserID(userDao.countUsers() + 1); // From POST, server handles the ID

                userDao.insertUser(user);
                return user;
            }

            else {
                user.setUserID(-1); // Return user with error id
                return user;
            }
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }
	
	 /************************************************************************************************************************************
     *Method: editUser
     * Description: Edits the user from the User table. 
     ************************************************************************************************************************************/
    public User editUser(User user){
        String idToString = Long.toString(user.getUserID());
        try{
            if (userDao.userExistsMod(idToString)){ // Proceed if user exists, Uses the ID as reference.
                userDao.editUser(user);
                return user;
            }
            else { // Act as POST if not exist
               return addUser(user);
            }
        }catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    }

    /************************************************************************************************************************************
     *Method: deleteUser
     * Description: Deletes the user specified from the path
     ************************************************************************************************************************************/
    public void deleteUser(String username){
        User user = userDao.selectUser(username);
        String idToString = Long.toString(user.getUserID());
        if(!userDao.userExistsMod(idToString)) throw new WebApplicationException(Response.Status.NOT_FOUND);
        else { // Proceed with delete if user exists in the database
            try {
                userDao.deleteUser(Long.toString(user.getUserID()));
            } catch (NullPointerException nullpointer) {
                nullpointer.printStackTrace();
            }
        }
    }
}
