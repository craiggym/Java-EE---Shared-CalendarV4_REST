package com.DAO;

import com.Calendar.User;

import java.util.List;

public interface UserDao {
    void createUserTable();
    void dropUserTable();
    void insertUser(User user);
    int countUsers();
    User selectUser(String username);
    List<User> selectAllUsers();
    boolean userExists(String username);
	boolean userExistsMod(String userID);
    boolean isAuthCorrect(String username, String password);
	void deleteUser(String userID);
    void editUser(User user);
}
