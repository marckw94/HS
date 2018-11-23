package org.marcoWenzel.middleware.highSchool.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException>{

	@Override
	public Response toResponse(UnauthorizedException arg0) {
		// TODO Auto-generated method stub
		ErrorMessage errorMessage = new ErrorMessage("You have no authorization for these resources",Response.Status.UNAUTHORIZED.getStatusCode(),"https://github.com/marckw94/HS");
		//System.out.println(errorMessage.getDocumentation());
		// TODO Auto-generated method stub
		return Response.status(Status.UNAUTHORIZED).entity(errorMessage).build();

	}
}