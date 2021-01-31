package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entity.Noti;
import entity.Todos;
import entity.User;


@Repository("notiDao")
public class NotiDao {

	@Autowired
	private SqlSessionFactory factory;
	
	
	// 알림 등록 ---------------------------------------------------
	public boolean insertNoti(Noti noti) {
		int n = factory.openSession().insert("mybatis.NotiMapper.insertNoti", noti);
		return (n > 0) ? true : false;
	}


	// 리스트 불러오기 ---------------------------------------------------
	public List<Noti> getNotiList(User user) {
		return factory.openSession().selectList("mybatis.NotiMapper.getNotiList", user);
	}


	public List<User> getUserList(List<Noti> noti) {
		List<User> users = new ArrayList<>();
		for (Noti n : noti) {
			User u = factory.openSession().selectOne("mybatis.NotiMapper.getUserList", n);
			users.add(u);
		}
		return users;
	}


	public List<User> getFriendList(List<Noti> noti) {
		List<User> users = new ArrayList<>();
		for (Noti n : noti) {
			User u = factory.openSession().selectOne("mybatis.NotiMapper.getFriendList", n);
			users.add(u);
		}
		return users;
	}


}
