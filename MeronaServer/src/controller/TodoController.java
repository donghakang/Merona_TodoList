package controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import entity.Address;
import entity.Todos;
import entity.User;
import model.TodoService;
import template.ApiKey;

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
	public String getTodoList(@ModelAttribute User user, Model model) {
		List<Todos> list = todoService.getTodoList(user);
		model.addAttribute("todoList", list);
		return "list";
	}
	
	
	//추가하기(데이터 추가)--------------------------------
	@RequestMapping(value="/insertItem.do", method=RequestMethod.POST)
	@ResponseBody
	public String insertItem(@ModelAttribute Todos todo, 
			@RequestParam(value="address_name", defaultValue="", required=false) String address_name,
			@RequestParam(value="road_address_name", defaultValue="", required=false) String road_address_name,
			@RequestParam(value="category_name", defaultValue="", required=false) String category_name,
			@RequestParam(value="place_name", defaultValue="", required=false) String place_name,
			@RequestParam(value="x", defaultValue="", required=false) String x,
			@RequestParam(value="y", defaultValue="", required=false) String y
			) {
		Address addr = new Address();
	
		addr.setAddress_name(address_name);
		addr.setRoad_address_name(road_address_name);
		addr.setCategory_name(category_name);
		addr.setPlace_name(place_name);
		addr.setLat(Double.parseDouble(y));
		addr.setLng(Double.parseDouble(x));

		System.out.println(todo.isDone());
		return JSONController.jsonTemplate(todoService.insertItem(todo, addr));
	}
	
	// 업데이트하기 (체크박스만) ----------------------------------
	@RequestMapping(value="/updateCheckbox.do", method=RequestMethod.POST)
	@ResponseBody
	public String updateCheckbox(@ModelAttribute Todos todo) {
		return JSONController.jsonTemplate(todoService.updateByCheckbox(todo));
	}
	
	// 수정하기 ------------------------------------------------
	@RequestMapping(value="/editItem.do", method=RequestMethod.POST)
	@ResponseBody
	public String editItem(@ModelAttribute Todos todo) {
		return JSONController.jsonTemplate(todoService.editItem(todo));
	}
	
	// 삭제하기 ------------------------------------------------
	@RequestMapping(value="/deleteItem.do", method=RequestMethod.POST)
	@ResponseBody
	public String deleteItem(@ModelAttribute Todos todo, Model model) {
		return JSONController.jsonTemplate(todoService.deleteItem(todo));
	}
	
	
	// 지도 검색하기 ------------------------------------------------
	@RequestMapping(value="/searchLocation.do")
	public String searchLocation(@RequestParam(value="location", defaultValue="", required=false) String location, Model model) {
		model.addAttribute("apiKey", ApiKey.getApiJSKey());
		model.addAttribute("location", location);
		return "location";
	}
	
}
/* 
@ModelAttribute User user는 아래와 동일
User user = new User();
user.setName(request.getParameter("name"))
user.setAge(request.getParameter("age"))
user.setHobby(request.getParameter("hobby"))
*/
