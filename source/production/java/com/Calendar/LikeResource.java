package com.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 5/2/16.
 */
@Path("/users/{username}/likedEvents")
public class LikeResource {
    UserService userService = new UserService();
    LikeService likeService = new LikeService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getLikedEvents(@PathParam("username") String username, @Context UriInfo uriInfo){
        List<Event> likedEvents = new ArrayList<Event>();
        likedEvents = likeService.getUserLikedEvents(username);
        User user = userService.getUser(username);
        for(Event e: likedEvents){
            String uri = getURIForLike(uriInfo, username);
            user.addLink(uri, "likedEvents");
        }
        return likedEvents;
    }

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
