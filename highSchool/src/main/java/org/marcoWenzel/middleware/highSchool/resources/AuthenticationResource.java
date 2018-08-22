package org.marcoWenzel.middleware.highSchool.resources;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.marcoWenzel.middleware.highSchool.dao.LogInDAO;
import org.marcoWenzel.middleware.highSchool.model.LogIn;
import org.marcoWenzel.middleware.highSchool.util.Category;
import org.marcoWenzel.middleware.highSchool.util.Link;
import org.marcoWenzel.middleware.highSchool.util.Secured;
import org.marcoWenzel.middleware.highSchool.util.TokenManager;



@Path("authentication")
public class AuthenticationResource {

		TokenManager tokenManager = new TokenManager();
		LogInDAO loginDao = new LogInDAO();
		//LOGIN : prende in ingresso username e password 
	    @POST
	    @Produces(MediaType.APPLICATION_XML)
	    @Consumes(MediaType.APPLICATION_XML)
	    public Response authenticateUser(LogIn log,@Context UriInfo uriInfo) {
	    	String uri;
	    	String username =log.getUsername();
	    	String category = log.getCategory();
	    	System.out.println(log.getUsername());
	    	LogIn userLog = loginDao.get(log.getUsername());
	    	//System.out.println(userLog.getUsername());
	    	List<Link> uris = new ArrayList<Link>();
	    	if (log.getCategory().equals("Parent")) {
	    		uri=uriInfo.getBaseUriBuilder().path(ParentResource.class)
					.resolveTemplate("user_id", log.getUsername()).build().toString();
	    		addLinkToList(uris, uri, "general services", "GET");
	    	}else if (log.getCategory().equals("Teacher")) {
	    		uri=uriInfo.getBaseUriBuilder().path(TeacherResource.class)
						.resolveTemplate("user_id", log.getUsername()).build().toString();
		    	addLinkToList(uris, uri, "general services", "GET");
	    	}else if(log.getCategory().equals("Admin")) {
	    		uri=uriInfo.getBaseUriBuilder().path(AdministratorResource.class)
						.build().toString();
	    		addLinkToList(uris, uri, "general services", "GET");
	    	}else {
    			return Response.status(Response.Status.BAD_REQUEST).build();

	    	}
			GenericEntity<List<Link>> e = new GenericEntity<List<Link>>(uris) {};
	    		if(userLog != null){
	    			String token = tokenManager.issueToken(username, category);
	    			
	    			return Response.status(Response.Status.OK).cookie(new NewCookie("Token", token)).entity(e).build();
	    			
	    		}
	    		else {
	    			return Response.status(Response.Status.UNAUTHORIZED).build();
	    			
	    		}

	            
	    }
	 	public void addLinkToList(List<Link> list,String url,String rel,String type) {
			Link newL= new Link();
			newL.setLink(url);
			newL.setRel(rel);
			newL.setType(type);
			list.add(newL);
	 	}
	    	
}
