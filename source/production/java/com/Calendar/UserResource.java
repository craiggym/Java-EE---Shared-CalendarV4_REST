package com.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/users")
public class UserResource {
    UserService userService = new UserService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(@Context UriInfo uriInfo){
        List<User> user = new ArrayList<User>();
        user = userService.getAllUsers();
        for(User u: user){
            String uri = uriInfo.getBaseUriBuilder()
                    .path(UserResource.class)
                    .path(u.getUsername())
                    .build()
                    .toString();
            u.addLink(uri, "self");
        }
        return user;
    }

    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username, @Context UriInfo uriInfo){
        User user = null;
        user = userService.getUser(username);

        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(username)
                .build()
                .toString();
        user.addLink(uri, "self");

        return user;
    }


}
