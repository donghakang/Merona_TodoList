package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Address;
import entity.Todos;
import entity.User;


@Service("todoService")
public class TodoService {
	private TodoDao todoDao;
	
	@Autowired
	public TodoService(TodoDao todoDao) {
		super();
		this.todoDao = todoDao;
	}

	public List<Todos> getTodoList(User user) {
		return todoDao.getTodoList(user);
	}

	public boolean insertItem(Todos todo, Address addr) {
		boolean address = todoDao.insertAddress(addr);
		boolean item = todoDao.insertItem(todo);
		
		return (address && item);

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
