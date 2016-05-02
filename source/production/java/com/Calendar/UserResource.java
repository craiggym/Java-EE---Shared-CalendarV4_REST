package com.Calendar;

import javax.ws.rs.*;
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

    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username){
        User user = null;
        user = userService.getUser(username);
        return user;
    }


}
