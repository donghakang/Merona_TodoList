package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import entity.User;
import model.UserService;

@Controller
public class UserController {
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	// 로그인하기-------------------------------------
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public String login(@ModelAttribute User user) {
		System.out.println(user);
		if (userService.loginUser(user)) {
			// TODO: 로그인 성공
			return "loginOK";
		} else {
			// TODO: 로그인 실패
			return "loginFail";
		}
	}
	
	//추가하기(화면)-------------------------------------
	@RequestMapping(value="/insert.do")
	public String insert() {
		return "insert";		// /WEB-INF/view/insert.jsp를 의미 (applicationContext.xml 참고)
	}
	
	//추가하기(데이터 추가)--------------------------------
	@RequestMapping(value="/insertProc.do", method=RequestMethod.POST)
	public String insertProc(@ModelAttribute User user) {
		
		if(userService.insertUser(user) == true) {
			System.out.println("ok");
			return "redirect:/list.do";			// RequestMapping("/list.do")로 이동한다. 
		}else {
			return "insert";
		}
	}
	
	//전체 출력하기 --------------------------------------
	@RequestMapping(value="/list.do")
	public String list(Model model) {
		List<User> list = userService.listUser();
		model.addAttribute("list", list);
		return "list";
								// viewName, modelName, modelObject
								// list.jsp, request객체이름, 전달할 객체 
	}
	
	// 사용자 이름 검색 --------------------------------------------------------------
	// 리스트 반환.
	@RequestMapping(value="/searchFriend.do", method=RequestMethod.POST)
	public String searchFriend(@RequestParam(value="username", defaultValue="", required=false) String username, Model model) {		
		List<User> list = userService.searchFriend(username);
		model.addAttribute("list", list);
		return "searchFriend";
								// viewName, modelName, modelObject
								// list.jsp, request객체이름, 전달할 객체 
	}
}
/* 
@ModelAttribute User user는 아래와 동일
User user = new User();
user.setName(request.getParameter("name"))
user.setAge(request.getParameter("age"))
user.setHobby(request.getParameter("hobby"))
*/
