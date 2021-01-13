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
		relationship.swap();
		if (friendDao.selectFriendRequest(relationship)) {
			System.out.println("1. 확인 완료 ");
			if (friendDao.deleteFriendRequest(relationship)) {
				System.out.println("2. 확인 완료 ");
				return friendDao.insertFriend(relationship);
			} else {
				System.out.println("2. 확인 실패 ");
				return false;
			}
		} else {
			System.out.println("1. 확인 실패 ");
			return false;
		}
	}


	public boolean deleteFriend(Relationship relationship) {
		boolean n = friendDao.deleteFriend(relationship);
		relationship.swap();
		boolean m = friendDao.deleteFriend(relationship);
		return (n || m) ? true : false;
		
	}


	public int checkStatus(Relationship relationship) {
		/* 
		 * result type : 
		 * -1 - error
		 * 0 - 아무 관계가 아니다.
		 * 1 - 내가 친구 신청을 보낸 상태
		 * 2 - 친구가 신청을 보낸 상태
		 * 3 - 친구 상태
		 */
		
		boolean isFriend1 = friendDao.selectFriend(relationship);
		boolean isFriendRequesting = friendDao.selectFriendRequest(relationship);
		relationship.swap();
		boolean isFriend2 = friendDao.selectFriend(relationship);
		boolean isFriendRequested  = friendDao.selectFriendRequest(relationship);
		
		System.out.println("TESTING STATUS------------------------");
		System.out.println("친구 관계: ---- " + isFriend1 + "  " + isFriend2);
		System.out.println("친구 신청 받음 - " + isFriendRequested);
		System.out.println("친구 신청 을함 - " + isFriendRequesting);
		
		if (isFriendRequesting && isFriendRequested) {
			return -1;
		}
		
		
		if (isFriend1 || isFriend2) {
			return 3;
		} else if (isFriendRequesting) {
			return 1;
		} else if (isFriendRequested) {
			return 2;
		} else {
			return 0;
		}
	}
	
}