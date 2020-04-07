package service;

import entity.User;
import remote.IUserRemote;

public class UserRemoteImpl implements IUserRemote {

    public User getUserById(String id) {
        return new User();
    }
}
