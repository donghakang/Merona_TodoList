package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.User;

@Service("userService")
public class UserService {
	private UserDao userDao;
	
	@Autowired
	public UserService(UserDao userDao) {
		super();
		this.userDao = userDao;
	}

	public boolean insertUser(User user) {
		return userDao.insertUser(user);
	}

	public List<User> listUser() {
		return userDao.listUser();
	}

	public boolean loginUser(User user) {
		
		return userDao.loginUser(user);
	}
}
