package lab2.server.resources;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import lab2.api.User;
import lab2.api.service.RestUsers;

@Singleton
public class UsersResource implements RestUsers {

	private final Map<String, User> users;

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	private static final String AVATAR_DIRECTORY = "avatarFiles";
	private static final String DEFAULT_AVATAR_FILE = "default.png";

	public UsersResource() {
		this.users = new ConcurrentHashMap<String,User>();
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		// Check if user data is valid
		if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null
				|| user.getEmail() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		// Insert new user, checking if userId already exists
		if (users.putIfAbsent(user.getUserId(), user) != null) {
			Log.info("User already exists.");
			throw new WebApplicationException(Status.CONFLICT);
		}
		return user.getUserId();
	}

	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		// Check if user is valid
		if (userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		var user = users.get(userId);

		// Check if user exists
		if (user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		// Check if the password is correct
		if (!user.getPassword().equals(password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		return user;
	}

	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; userData = " + user);
		User oldUser = getUser(userId, password);
		users.put(oldUser.getUserId(), user);
		return oldUser;
	}

	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		User oldUser = getUser(userId, password);
		return users.remove(userId);
	}

	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		List<User> selectedUser = new ArrayList<User>();
		if (pattern == null || pattern.isEmpty()) {
			return new ArrayList<>(users.values());
		}
		for (User user : users.values()) {
			if (user.getFullName().contains(pattern)){
				selectedUser.add(user);
			}
		}
		return selectedUser;
	}

	@Override
	public void associateAvatar(String userId, String password, byte[] avatar) {
		Log.info("associate an avatar : user = " + userId + "; pwd = " + password + "; avatarSize = " + avatar.length);

		if (avatar.length == 0) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		User usr = this.getUser(userId, password);

		Path pathToFile = Paths.get(AVATAR_DIRECTORY + File.separator + usr.getUserId() + ".png");

		try {
			Files.deleteIfExists(pathToFile); 
			Files.write(pathToFile, avatar);
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void removeAvatar(String userId, String password) {
		Log.info("delete an avatar : user = " + userId + "; pwd = " + password);

		User usr = getUser(userId, password);
		Path pathToFile = Paths.get(AVATAR_DIRECTORY + File.separator + usr.getUserId() + ".png");

		try {
			if (!Files.deleteIfExists(pathToFile)) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public byte[] getAvatar(String userId) {
		if (users.get(userId) == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		Path pathToFile = Paths.get(AVATAR_DIRECTORY + File.separator + userId + ".png");

		try {
			if (Files.exists(pathToFile)) {
				return Files.readAllBytes(pathToFile);
			} else {
				pathToFile = Paths.get(AVATAR_DIRECTORY + File.separator + DEFAULT_AVATAR_FILE);
				return Files.readAllBytes(pathToFile);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

	}

}
