package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Todos;


@Service("todoService")
public class TodoService {
	private TodoDao todoDao;
	
	@Autowired
	public TodoService(TodoDao todoDao) {
		super();
		this.todoDao = todoDao;
	}

	public List<Todos> getTodoList() {
		return todoDao.getTodoList();
	}
}
