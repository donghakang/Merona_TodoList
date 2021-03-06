package model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Address;
import entity.AddressTodo;
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

	public boolean editItem(Todos todo, Address addr) {
		boolean n = todoDao.editItem(todo); 
		boolean m = todoDao.editAddress(addr);
			
		return n && m;
	}

	public boolean deleteItem(Todos todo) {
		return todoDao.deleteItem(todo);
	}

	public List<AddressTodo> getMyMapData(User user) {
		return todoDao.getMyMapData(user);
	}

	public List<AddressTodo> getSharedMapData(User user) {
		return todoDao.getSharedMapData(user);
	}

	public boolean updateMapNotification(List<Integer> ids) {
		boolean isTrue = false;
		for (int i : ids) { 
			if (!todoDao.updateMapNotification(i)) isTrue = false;
		}
		
		return isTrue;
	}
}
