package com.Calendar;

import com.DAO.UserDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/1/16.
 */
public class UserService {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("AppContext.xml");
    private static UserDao userDao = (UserDao) context.getBean("userDao");
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
}
