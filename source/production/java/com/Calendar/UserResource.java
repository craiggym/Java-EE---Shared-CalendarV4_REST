package com.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/users")
public class UserResource {
    UserService userService = new UserService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsersPOST(){
        return "You posted";
    }


}
