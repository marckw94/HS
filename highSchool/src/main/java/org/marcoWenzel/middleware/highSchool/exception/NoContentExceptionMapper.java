package org.marcoWenzel.middleware.highSchool.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class NoContentExceptionMapper implements ExceptionMapper<NoContentException> {

	@Override
	public Response toResponse(NoContentException arg0) {
		ErrorMessage errorMessage = new ErrorMessage("request is accepted but give an empty response",Status.NO_CONTENT.getStatusCode(),"https://github.com/marckw94/HS");
		System.out.println(errorMessage.getErrorMessage());
		// TODO Auto-generated method stub
		return Response.status(Status.NO_CONTENT).entity(errorMessage).build();
	}

}
