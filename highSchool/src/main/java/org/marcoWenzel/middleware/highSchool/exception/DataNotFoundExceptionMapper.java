package org.marcoWenzel.middleware.highSchool.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException e) {
		ErrorMessage errorMessage = new ErrorMessage("this data miss,check the request",Status.BAD_REQUEST.getStatusCode(),"https://github.com/marckw94/HS");
		//System.out.println(errorMessage.getDocumentation());
		// TODO Auto-generated method stub
		return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
	}
	

}
