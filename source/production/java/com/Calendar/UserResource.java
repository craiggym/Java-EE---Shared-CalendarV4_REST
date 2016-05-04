package com.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/users")
public class UserResource {
    UserService userService = new UserService();

    /******************************************************************************************************************
     *Method: getAllUsers
     * Description: Returns a list of all the users in the database
     *****************************************************************************************************************/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(@Context UriInfo uriInfo){
        List<User> user = new ArrayList<User>();
        user = userService.getAllUsers();
        for(User u: user){
            String uri = getURI(uriInfo, u.getUsername());
            u.addLink(uri, "self");
            String uriLiked = getURIForLike(uriInfo, u.getUsername());
            u.addLink(uriLiked, "likedEvents");
        }
        return user;
    }

    /******************************************************************************************************************
     *Method: addUser
     * Description: Adds a user
     *****************************************************************************************************************/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(User user){
        User addUser = userService.addUser(user);
        if (addUser.getUserID() == -1) throw new WebApplicationException(Response.Status.CONFLICT); // user duplication
        else if (addUser.getUserID() == -2) throw new WebApplicationException(Response.Status.BAD_REQUEST); // required params not there
        else return addUser;
    }

    /******************************************************************************************************************
     *Method: getUser
     * Description: Uses the username parameter to return a particular user.
     *****************************************************************************************************************/
    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username, @Context UriInfo uriInfo){
        User user = null;
        user = userService.getUser(username);

        String uri = getURI(uriInfo, username);
        user.addLink(uri, "self");
        String uriLiked = getURIForLike(uriInfo, username);
        user.addLink(uriLiked, "likedEvents");

        return user;
    }

	 /******************************************************************************************************************
     *Method: editUser
     * Description: Edits a user with PUT using the parameters passed from the JSON body.
     *****************************************************************************************************************/
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User editUser(User user){
        User editUser = userService.editUser(user);
        if (editUser.getUserID() == -2) throw new WebApplicationException(Response.Status.BAD_REQUEST); // required params not there
        return editUser;
    }

    /******************************************************************************************************************
     *Method: deleteUser
     * Description: Deletes and event with DELETE
     *****************************************************************************************************************/
    @Path("/{username}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteEvent(@PathParam("username") String username){
        userService.deleteUser(username);
    }

    /******************************************************************************************************************
     *Method: getURI
     * Description: Returns the URI for the Links attribute
     *****************************************************************************************************************/
    public String getURI(UriInfo uriInfo, String id){
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class) // http.../Shared_Calendar/rest
                .path(id) // @path in method, place id at end
                .build()
                .toString();
        return uri;
    }

    /******************************************************************************************************************
     *Method: getURIForLike
     * Description: Returns the URI for the Links attribute pertaining to the Liked links
     *****************************************************************************************************************/
    public String getURIForLike(UriInfo uriInfo, String id){
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class) // http.../Shared_Calendar/rest
                .path(id) // @path in method, place id at end
                .path("likedEvents")
                .build()
                .toString();
        return uri;
    }

}
