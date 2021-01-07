package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	// 회원가입시, 사용자 이름 가능여부 ---------------------------------------------------------------
	@RequestMapping(value="/checkId.do", method=RequestMethod.POST)
	@ResponseBody
	public String checkId(@RequestParam(value="username", defaultValue="", required=false) String username) {		
		int count = userService.checkId(username);
		return JSONController.jsonTemplate(count <= 0);
	}
	
	
	// 회원가입시 이메일 가능여부 ---------------------------------------------------------------
	@RequestMapping(value="/checkEmail.do", method=RequestMethod.POST)
	@ResponseBody
	public String checkEmail(@RequestParam(value="email", defaultValue="", required=false) String email) {		
		int count = userService.checkEmail(email);
		return JSONController.jsonTemplate(count <= 0);
	}
	
	// 회원가입 ----------------------------------------------------------------------------------
	@RequestMapping(value="/register.do", method=RequestMethod.POST)
	@ResponseBody
	public String register(@ModelAttribute User user) {
		return JSONController.jsonTemplate(userService.register(user));
	}
	
	
	
	// 로그인하기-------------------------------------
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	@ResponseBody
	public String login(@ModelAttribute User user) {
		return JSONController.jsonTemplate(userService.loginUser(user));
	}
	
	// 사용자 이름 검색 --------------------------------------------------------------
	// 리스트 반환.
	@RequestMapping(value="/searchFriend.do", method=RequestMethod.POST)
	public String searchFriend(@RequestParam(value="username", defaultValue="", required=false) String username, Model model) {		
		List<User> list = userService.searchFriend(username);
		model.addAttribute("list", list);
		return "searchFriend";
	}
	
	
	// 마이페이지 출력 --------------------------------------------------------------
	@RequestMapping(value="/myPage.do", method=RequestMethod.POST)
	public String searchFriend(@ModelAttribute User user, Model model) {		
		User user_info = userService.myPageUser(user);
		System.out.println(user_info.toString());
		model.addAttribute("user", user_info);
		return "myPage";
	}

}
/* 
@ModelAttribute User user는 아래와 동일
User user = new User();
user.setName(request.getParameter("name"))
user.setAge(request.getParameter("age"))
user.setHobby(request.getParameter("hobby"))
*/
