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

	public boolean insertItem(Todos todo) {
		return todoDao.insertItem(todo);
	}

	public boolean updateByCheckbox(Todos todo) {
		return todoDao.updateByCheckbox(todo);
	}

	public boolean editItem(Todos todo) {
		return todoDao.editItem(todo);
	}

	public boolean deleteItem(Todos todo) {
		return todoDao.deleteItem(todo);
	}
}