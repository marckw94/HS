package org.marcoWenzel.middleware.highSchool.util;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.marcoWenzel.middleware.highSchool.exception.DataNotFoundException;
import org.marcoWenzel.middleware.highSchool.exception.ForbiddenException;
import org.marcoWenzel.middleware.highSchool.exception.UnauthorizedException;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter{
	
	@Context
	private ResourceInfo resourceInfo;
	@Context
	private UriInfo uriInfo;
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String userInPath=null;
		System.out.println(uriInfo.getPathParameters());
		if(uriInfo.getPathParameters().containsKey("user_id")) {
			userInPath=uriInfo.getPathParameters().get("user_id").get(0);
			System.out.println("path param: "+uriInfo.getPathParameters().get("user_id").get(0));

		}
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		String token = authorizationHeader.substring("Bearer".length()).trim();
		
		String requestedUser=TokenManager.validateUserToken(token);
		System.out.println("Request user: "+requestedUser);
		Category userRole = Category.valueOf(TokenManager.validateToken(token));
		System.out.println("validateToken:"+TokenManager.validateToken(token));
		List<Category> classRoles = extractRoles(resourceInfo.getResourceClass());
		List<Category> methodRoles = extractRoles(resourceInfo.getResourceMethod());
		
		if (methodRoles.size() > 0) {
			if (!methodRoles.contains(userRole)) {
				System.out.println("entro1");
				throw new ForbiddenException();
			}
		}
		if (classRoles.size() > 0) {
			if (!classRoles.contains(userRole)) {
				System.out.println("entro2");
				throw new ForbiddenException();
			}
		}
		if (userInPath!=null && !requestedUser.equals(userInPath)) {
			System.out.println("unhautorized resources");
			throw new UnauthorizedException();
		}
		
	}

	private List<Category> extractRoles(AnnotatedElement annotatedElement) {
		List<Category> roles = new ArrayList<>();
		if (annotatedElement == null) {
			return roles;
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return roles;
			} else {
				Category[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}



	
}
