package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import entity.User;

@Repository("userDao")
public class UserDao {
	@Autowired
	private SqlSessionFactory factory;
	
	// 회원가입시, 사용자 이름 가능여부 --------------------------------------------------------------
	public int checkId(String username) {
		List<User> list = factory.openSession().selectList("mybatis.LoginMapper.checkId", username);
		return list.size();
	}
	
	// 회원가입시, 사용자 이메일 가능여부 --------------------------------------------------------------
	public int checkEmail(String email) {
		List<User> list = factory.openSession().selectList("mybatis.LoginMapper.checkEmail", email);
		return list.size();
	}
 
	// 회원가입 ----------------------------------------------------------------------------------
	public boolean register(User user) {
		int n = factory.openSession().insert("mybatis.LoginMapper.registerUser",user);
		return (n > 0)?true:false;
	}
	
	
	
	// 로그인 !
	public boolean loginUser(User user) {
		User loginUser = factory.openSession().selectOne("mybatis.LoginMapper.getLoginUser",user);
		// TODO: 리턴 값을 User 로 교체한다.
		System.out.println(loginUser);
		if (loginUser != null) {
			return true;
		} else {
			return false;
		}
	}
	
	// 사용자 이름 검색 --------------------------------------------------------------------------- 
	// 리스트 반환.
	public List<User> searchFriend(String username) {
		return factory.openSession().selectList("mybatis.LoginMapper.searchFriend", username);
	}

	// 마이페이지 출력 --------------------------------------------------------------
	public User myPageUser(User user) {
		return factory.openSession().selectOne("mybatis.LoginMapper.getLoginUser", user);
	}


	
	
	
	
}

