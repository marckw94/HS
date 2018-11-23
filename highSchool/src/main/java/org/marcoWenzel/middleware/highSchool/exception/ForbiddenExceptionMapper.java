package org.marcoWenzel.middleware.highSchool.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException>{

	@Override
	public Response toResponse(ForbiddenException arg0) {
		// TODO Auto-generated method stub
		ErrorMessage errorMessage = new ErrorMessage("request trying to log into forbidden resources",Status.FORBIDDEN.getStatusCode(),"https://github.com/marckw94/HS");
		System.out.println(errorMessage.getErrCode());
		System.out.println(errorMessage.getErrorMessage());
		// TODO Auto-generated method stub
		return Response.status(Status.FORBIDDEN).entity(errorMessage).build();
	}

}
