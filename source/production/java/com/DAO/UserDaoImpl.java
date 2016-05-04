package com.DAO;

import com.Calendar.User;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

public class UserDaoImpl implements UserDao{
    // class variables //
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    boolean debug = true;


    // methods //
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /***************************************************************
     * Title: createUserTable
     * Description: Create the User table
     ***************************************************************/
    @Override
    public void createUserTable() {
        String query = "CREATE TABLE User(userID int, username VARCHAR(255), e_mail VARCHAR(255), password VARCHAR(255), " +
                "first_name VARCHAR(255), last_name VARCHAR(255));";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /*****************************************************************************************
     * Title: dropUserTable
     * Description: Drops table on a new instance of the web application
     ****************************************************************************************/
    @Override
    public void dropUserTable() {
        String query = "DROP TABLE User;";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(query);
    }

    /*****************************************************************************************
     * Title: insertUser
     * Description: Add a user to the database
     * @param user User object
     ****************************************************************************************/
    @Override
    public void insertUser(User user) {
        String query = "insert into User (userID, username, e_mail, password, first_name, last_name) values (?,?,?,?,?,?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {user.getUserID(), user.getUsername(),user.getE_mail(), user.getPassword(), user.getFirst_name(), user.getLast_name()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
        if(debug) System.out.printf("User: %s added with password: %s", user.getUsername(), user.getPassword());
    }

    // Boolean checking //
    /*****************************************************************************************
     * userExists
     * @param username
     * @return true if the user exists in the database
     ****************************************************************************************/
    @Override
    public boolean userExists(String username) {
        try {
            String query = "SELECT username FROM User WHERE username=?";
            Object[] input = new Object[]{username};
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String uname = (String) jdbcTemplate.queryForObject(query, input, String.class);

            if(debug) {
                System.out.println("result of query: " + uname);
                System.out.println("User exists");
            }
            return true;
        }
        catch(Exception e){
            if (debug) System.out.println("User does not exist");
            return false;
        }
    }
	
	 /*****************************************************************************************
     * userExistsMod
     * @param userID
     * @return true if the user exists in the database. Uses the userID as the unique identifier.
     ****************************************************************************************/
    @Override
    public boolean userExistsMod(String userID) {
        try {
            String query = "SELECT username FROM User WHERE UserID=?";
            Object[] input = new Object[]{userID};
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String uname = (String) jdbcTemplate.queryForObject(query, input, String.class);

            return true;
        }
        catch(Exception e){
            if (debug) System.out.println("User does not exist or Error in Checking userExists(userID)");
            return false;
        }
    }
	
    // count update//
    /*****************************************************************************************
     * countUser
     * @return count of users in user table
     ****************************************************************************************/
    @Override
    public int countUsers() {
        try {
            String query = "SELECT COUNT(*) FROM User";
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            int res = (int) jdbcTemplate.queryForObject(query, int.class);

            return res;
        }
        catch(Exception e){
            if (debug) System.out.println("error querying for count");
            return 0;
        }
    }

    /*****************************************************************************************
     * isAuthCorrect
     * Authenticates using two strings passed in parameters
     * @param username
     * @param password
     * @return true if authenticated correctly
     *****************************************************************************************/
    @Override
    public boolean isAuthCorrect(String username, String password) {
        try {
            String query = "SELECT username FROM User WHERE username=?" + " AND password=?";
            Object[] input = new Object[]{username,password};
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String q_result = (String) jdbcTemplate.queryForObject(query, input, String.class);

            if(debug)System.out.println("Authentication for " + username + " correct!(result="+q_result +")");
            return true;
        }
        catch(Exception e){
            if (debug) System.out.println("Authentication for " + username + " incorrect");
            return false;
        }
    }

    // Select statements //

    /*****************************************************************************************
     * selectUser
     * Selects and returns all attributes of the user given by the username parameter
     * @param username
     * @return User
     ****************************************************************************************/
    @Override
    public User selectUser(String username){
        String query = "SELECT * FROM User WHERE username = ?";
        Object[] input =  new Object[]{username};
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        User user = (User) jdbcTemplate.queryForObject(query, input, new UserMapper());
        return user;

    }

    /*****************************************************************************************
     * selectAllUsers
     * Selects and returns all users and their attributes
     * @return List<User>
     ****************************************************************************************/
    @Override
    public List<User> selectAllUsers(){
        String query = "SELECT DISTINCT userID, username, e_mail, password, first_name, last_name FROM User ORDER BY userID ASC";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query(query, new UserMapper());
        return users;
    }
	
	 /****************************************************************************************************************************************
     * Title: editUser
     * Description: Edits a user from the database.
     ****************************************************************************************************************************************/
    @Override
    public void editUser(User user) {
        String query = "UPDATE User SET username=?, e_mail=?, password=?, first_name=?, last_name=? WHERE UserID=?;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {user.getUsername(), user.getE_mail(), user.getPassword(), user.getFirst_name(), user.getLast_name(), user.getUserID()};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
        if(debug) System.out.printf("Updated user successfully");
    }

    /****************************************************************************************************************************************
     * Title: deleteUser
     * Description: Deletes the user from the database
     ****************************************************************************************************************************************/
    @Override
    public void deleteUser(String userID) {
        String query = "DELETE FROM User WHERE UserID=?;";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] inputs = new Object[] {userID};
        jdbcTemplate.update(query,inputs); // 'update' allows for non-static queries whereas execute wouldn't (e.g. '?')
    }
}
