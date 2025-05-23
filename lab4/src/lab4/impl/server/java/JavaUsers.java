package lab4.impl.server.java;

import java.util.List;
import java.util.logging.Logger;

import lab4.api.User;
import lab4.api.java.Result;
import lab4.api.java.Result.ErrorCode;
import lab4.api.java.Users;
import lab4.impl.server.persistence.Hibernate;

public class JavaUsers implements Users {

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    private Hibernate hibernate;

    public JavaUsers() {
        hibernate = Hibernate.getInstance();
    }

    @Override
    public Result<String> createUser(User user) {
        Log.info("createUser : " + user);

        // Check if user data is valid
        if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null
                || user.getEmail() == null) {
            Log.info("User object invalid.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        try {
            hibernate.persist(user);
        } catch (Exception e) {
            e.printStackTrace(); //Most likely the exception is due to the user already existing...
            Log.info("User already exists.");
            return Result.error(ErrorCode.CONFLICT);
        }

        return Result.ok(user.getUserId());
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        Log.info("getUser : user = " + userId + "; pwd = " + password);

        // Check if user is valid
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        User user = null;
        try {
            user = hibernate.get(User.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }

        // Check if user exists
        if (user == null) {
            Log.info("User does not exist.");
            return Result.error(ErrorCode.NOT_FOUND);
        }

        // Check if the password is correct
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect");
            return Result.error(ErrorCode.FORBIDDEN);
        }

        return Result.ok(user);

    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        Log.info("updateUser : user = " + userId + "; pwd = " + password);
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }
        User oldUser = null;
        try {
            oldUser= hibernate.get(User.class, userId);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }
        if (oldUser == null) {
            return Result.error(ErrorCode.NOT_FOUND);
        }
        if (!oldUser.getPassword().equals(password)) {
            return Result.error(ErrorCode.FORBIDDEN);
        }

        hibernate.update(user);
        return Result.ok(user);
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        Log.info("deleteUser : user = " + userId + "; pwd = " + password);
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }
        User user = null;
        try {
            user = hibernate.get(User.class, userId);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }
        if (user == null) {
            return Result.error(ErrorCode.NOT_FOUND);
        }
        if(!user.getPassword().equals(password)) {
            return Result.error(ErrorCode.FORBIDDEN);
        }
        try {
            hibernate.delete(user);
        } catch (Exception e) {
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }


        return Result.ok(user);
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            Log.info("searchUsers : pattern is null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        return null;
    }

}
