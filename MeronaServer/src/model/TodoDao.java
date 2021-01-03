package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import entity.Todos;


@Repository("todoDao")
public class TodoDao {
	@Autowired
	private SqlSessionFactory factory;
	

	public List<Todos> getTodoList() {
		return factory.openSession().selectList("mybatis.TodoMapper.getTodoList");
	}

}

