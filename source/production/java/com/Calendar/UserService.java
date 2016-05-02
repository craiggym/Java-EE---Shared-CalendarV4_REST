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

    public List<User> getAllUsers(){
        try {
            List<User> users = new ArrayList<User>();
            UserDao userDao = (UserDao) context.getBean("userDao");
            users = userDao.selectAllUsers();

            return users;
        }
        catch(NullPointerException nullpointer){
            nullpointer.printStackTrace();
            return null;
        }
    };
}
