package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import entity.Todos;
import entity.User;
import model.TodoService;

@Controller
public class TodoController {
	private TodoService todoService;
	
	@Autowired
	public TodoController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}
	
	// Todo List 출력하기 -------------------------------------
	@RequestMapping(value="/todoList.do")
	public String getTodoList(Model model) {
		List<Todos> list = todoService.getTodoList();
		model.addAttribute("todoList", list);
		return "list";
	}
	
	
	//추가하기(데이터 추가)--------------------------------
	@RequestMapping(value="/insertItem.do", method=RequestMethod.POST)
	public String insertItem(@ModelAttribute Todos todo, Model model) {
		
		if(todoService.insertItem(todo)) {
			model.addAttribute("result", true);
						// RequestMapping("/list.do")로 이동한다. 
		}else {
			model.addAttribute("result", false);
		}
		return "insertItem";
	}
	
	// 업데이트하기 (체크박스만) ----------------------------------
	@RequestMapping(value="/updateCheckbox.do", method=RequestMethod.POST)
	public String updateCheckbox(@ModelAttribute Todos todo, Model model) {
		if (todoService.updateByCheckbox(todo)) model.addAttribute("result", true);
		else model.addAttribute("result", false);
		
		return "updateCheckbox";
	}
	
	// 수정하기 ------------------------------------------------
	@RequestMapping(value="/editItem.do", method=RequestMethod.POST)
	public String editItem(@ModelAttribute Todos todo, Model model) {
		if (todoService.editItem(todo)) model.addAttribute("result", true);
		else model.addAttribute("result", false);
		
		return "editItem";
	}
	
	// 삭제하기 ------------------------------------------------
	@RequestMapping(value="/deleteItem.do", method=RequestMethod.POST)
	public String deleteItem(@ModelAttribute Todos todo, Model model) {
		if (todoService.deleteItem(todo)) model.addAttribute("result", true);
		else model.addAttribute("result", false);
		
		return "deleteItem";
	}
}
/* 
@ModelAttribute User user는 아래와 동일
User user = new User();
user.setName(request.getParameter("name"))
user.setAge(request.getParameter("age"))
user.setHobby(request.getParameter("hobby"))
*/
