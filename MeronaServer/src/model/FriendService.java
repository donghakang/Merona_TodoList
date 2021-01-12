package model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Relationship;

@Service("friendService")
public class FriendService {
	private FriendDao friendDao;
	
	@Autowired
	public FriendService(FriendDao friendDao) {
		super();
		this.friendDao = friendDao;
	}


	public boolean requestFriend(Relationship relationship) {
		return friendDao.requestFriend(relationship);
	}


	public boolean deleteFriendRequest(Relationship relationship) {
		return friendDao.deleteFriendRequest(relationship);
	}


	public boolean insertFriend(Relationship relationship) {
		return friendDao.insertFriend(relationship);
	}


	public boolean deleteFriend(Relationship relationship) {
		return friendDao.deleteFriend(relationship);
	}
	
}