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

import entity.Relationship;
import entity.User;
import model.FriendService;
import model.UserService;

@Controller
public class FriendController {
	private FriendService friendService;
	
	@Autowired
	public FriendController(FriendService friendService) {
		super();
		this.friendService = friendService;
	}
	
//	// 자기의 친구 리스트를 확인한다. ---------------------------------------------------
//	@RequestMapping(value="/getFriendList.do", method=RequestMethod.POST) 
//	@ResponseBody 
//	public String getFriendList(@ModelAttribute User user) {
//		JSONObject json = new JSONObject();
//		
//		List<User> friends = friendService.getFriendList(user);
//	
//		if (friends == null) {
//			json.put("result", "fail");
//		} else {
//			json.put("result", "ok");
//		}
//		
//		
//		return json.toString();
//	}
//	
	
	
	// 친구 신청 -------------------------------------------------------------------
	@RequestMapping(value="/requestFriend.do", method=RequestMethod.POST) 
	@ResponseBody
	public String requestFriend(@ModelAttribute Relationship relationship){
		
		return JSONController.jsonTemplate(friendService.requestFriend(relationship));
	}
	// 친구 신청 취소 -------------------------------------------------------------------
	@RequestMapping(value="/deleteFriendRequest.do", method=RequestMethod.POST) 
	@ResponseBody
	public String deleteFriendRequest(@ModelAttribute Relationship relationship){
		
		return JSONController.jsonTemplate(friendService.deleteFriendRequest(relationship));
	}
	
	// 친구 신청 확인 -------------------------------------------------------------------
	@RequestMapping(value="/insertFriend.do", method=RequestMethod.POST) 
	@ResponseBody
	public String insertFriend(@ModelAttribute Relationship relationship){
		
		return JSONController.jsonTemplate(friendService.insertFriend(relationship));
	}
	
	// 친구 관계 해제 -------------------------------------------------------------------
	@RequestMapping(value="/deleteFriend.do", method=RequestMethod.POST) 
	@ResponseBody
	public String deleteFriend(@ModelAttribute Relationship relationship){
		
		return JSONController.jsonTemplate(friendService.deleteFriend(relationship));
	}

	

}





//// 로그인하기-------------------------------------
//@RequestMapping(value="/login.do", method=RequestMethod.POST)
//@ResponseBody
//public String login(@ModelAttribute User user) {
//	return JSONController.jsonTemplate(userService.loginUser(user));
//}
//
//// 사용자 이름 검색 --------------------------------------------------------------
//// 리스트 반환.
//@RequestMapping(value="/searchFriend.do", method=RequestMethod.POST)
//public String searchFriend(@RequestParam(value="username", defaultValue="", required=false) String username, Model model) {		
//	List<User> list = userService.searchFriend(username);
//	model.addAttribute("list", list);
//	return "searchFriend";
//}