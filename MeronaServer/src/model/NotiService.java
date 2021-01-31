package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Noti;
import entity.User;

@Service("notiService")
public class NotiService {
	private NotiDao notiDao;
	
	@Autowired
	public NotiService(NotiDao notiDao) {
		super();
		this.notiDao = notiDao;
	}

	
	public boolean insertNoti(Noti noti) {
		return notiDao.insertNoti(noti);
	}


	public List<Noti> getNotiList(User user) {
		return notiDao.getNotiList(user);
	}


	public List<User> getUserList(List<Noti> noti) {
		return notiDao.getUserList(noti);
	}


	public List<User> getFriendList(List<Noti> noti) {
		// TODO Auto-generated method stub
		return notiDao.getFriendList(noti);
	}
	
	
}
