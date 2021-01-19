package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.JSONController;
import entity.Relationship;
import entity.Todos;
import entity.User;


@Repository("friendDao")
public class FriendDao {
	@Autowired
	private SqlSessionFactory factory;
	

	// 친구 신청 -------------------------------------------------------------------
	public boolean requestFriend(Relationship relationship) {
		int n = factory.openSession().insert("mybatis.FriendMapper.requestFriend", relationship);
		return (n > 0) ? true : false;
	}

	// 친구 신청 취소 -------------------------------------------------------------------
	public boolean deleteFriendRequest(Relationship relationship) {
		int n = factory.openSession().delete("mybatis.FriendMapper.deleteFriendRequest", relationship);
		return (n > 0) ? true : false;
	}

	
	// 친구 신청 확인 -------------------------------------------------------------------
	public boolean insertFriend(Relationship relationship) {
		int n = factory.openSession().insert("mybatis.FriendMapper.insertFriend", relationship);
		return (n > 0) ? true : false;
	}
	
	
	// 친구 관계 해제 -------------------------------------------------------------------
	public boolean deleteFriend(Relationship relationship) {
		int n = factory.openSession().delete("mybatis.FriendMapper.deleteFriend", relationship);
		
		return n > 0 ? true : false;
		
	}

	// 친구 신청 여부 확인
	public boolean selectFriendRequest(Relationship relationship) {
		Relationship isRequested = factory.openSession().selectOne("mybatis.FriendMapper.selectFriendRequest", relationship);
		return (isRequested != null) ? true : false;
	}
	
	// 친구 여부 확인
	public boolean selectFriend(Relationship relationship) {
		Relationship isFriend = factory.openSession().selectOne("mybatis.FriendMapper.selectFriend", relationship);
		return (isFriend != null) ? true : false;
	}
	
	// user의 친구 리스트 가져오기 
	public List<User> getFriendList(User user) {
		return factory.openSession().selectList("mybatis.FriendMapper.getFriendList", user);
	}

}

