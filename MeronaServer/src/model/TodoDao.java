package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import entity.Address;
import entity.Todos;
import entity.User;


@Repository("todoDao")
public class TodoDao {
	@Autowired
	private SqlSessionFactory factory;
	

	public List<Todos> getTodoList() {
		return factory.openSession().selectList("mybatis.TodoMapper.getTodoList");
	}


	// 아이템 등록 ---------------------------------------------------
	public boolean insertItem(Todos todo) {
		int n = factory.openSession().insert("mybatis.TodoMapper.insertItem", todo);
		return (n > 0) ? true : false;
	}
	
	public boolean insertAddress(Address addr) {
		int n = factory.openSession().insert("mybatis.TodoMapper.insertAddress", addr);
		return (n > 0) ? true : false;
	}


	// 체크박스 클릭시 아이템 업데이트 ------------------------------------
	public boolean updateByCheckbox(Todos todo) {
		int n = factory.openSession().update("mybatis.TodoMapper.updateByCheckbox", todo);
		return (n > 0) ? true : false;
	}

	// 수정하기 ------------------------------------------------
	public boolean editItem(Todos todo) {
		int n = factory.openSession().update("mybatis.TodoMapper.editItem", todo);
		return (n > 0) ? true : false;
	}

	// 삭제하기 ------------------------------------------------
	public boolean deleteItem(Todos todo) {
		int n = factory.openSession().update("mybatis.TodoMapper.deleteItem", todo);
		return (n > 0) ? true : false;
	}


	
}

