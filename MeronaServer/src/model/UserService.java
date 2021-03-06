package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Todos;
import entity.User;

@Service("userService")
public class UserService {
	private UserDao userDao;
	
	@Autowired
	public UserService(UserDao userDao) {
		super();
		this.userDao = userDao;
	}


	public boolean loginUser(User user) {
		
		return userDao.loginUser(user);
	}

	public List<User> searchFriend(String username) {
		return userDao.searchFriend(username);
	}

	public int checkId(String username) {
		return userDao.checkId(username);
	}

	public int checkEmail(String email) {
		return userDao.checkEmail(email);
	}

	public boolean register(User user) {
		return userDao.register(user);
	}


	public User myPageUser(User user) {
		return userDao.myPageUser(user);
	}


	


	public boolean updateToken(User user) {
		return userDao.updateToken(user);
	}

	
	public User getUserPage(int user_id) {
		return userDao.getUserPage(user_id);
	}

	public List<Todos> getUserData(User user) {
		return userDao.getUserData(user);
	}


	public List<Todos> getUserSharedData(User user) {
		return userDao.getUserSharedData(user);
	}
	
	
}
