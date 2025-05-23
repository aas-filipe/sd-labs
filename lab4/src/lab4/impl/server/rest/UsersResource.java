package lab4.impl.server.rest;

import java.util.List;
import java.util.logging.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import lab4.api.User;
import lab4.api.java.Result;
import lab4.api.java.Users;
import lab4.api.rest.RestUsers;
import lab4.impl.server.java.JavaUsers;

public class UsersResource implements RestUsers {

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	final Users impl;
	
	public UsersResource() {
		impl = new JavaUsers();
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);
		
		Result<String> res = impl.createUser(user);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
	}

	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		Result<User> res = impl.getUser(userId, password);
		if(!res.isOK()) {
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		}
		return res.value();
	}

	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; userData = " + user);
		// TODO: Complete method
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}

	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		// TODO: Complete method
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}

	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		
		Result<List<User>> res = impl.searchUsers(pattern);
		
		if(!res.isOK())
			throw new WebApplicationException(errorCodeToStatus(res.error()));
		
		return res.value();
	}
	
	protected static Status errorCodeToStatus( Result.ErrorCode error ) {
    	Status status =  switch( error) {
    	case NOT_FOUND -> Status.NOT_FOUND; 
    	case CONFLICT -> Status.CONFLICT;
    	case FORBIDDEN -> Status.FORBIDDEN;
    	case NOT_IMPLEMENTED -> Status.NOT_IMPLEMENTED;
    	case BAD_REQUEST -> Status.BAD_REQUEST;
    	default -> Status.INTERNAL_SERVER_ERROR;
    	};
    	
    	return status;
    }

}
