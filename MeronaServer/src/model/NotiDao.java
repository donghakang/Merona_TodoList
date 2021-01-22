package model;

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


}
