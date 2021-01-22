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

import entity.Noti;
import entity.User;
import model.NotiService;


@Controller
public class NotiController {
	private NotiService notiService;
	
	@Autowired
	public NotiController(NotiService notiService) {
		super();
		this.notiService = notiService;
	}
	
	// 리스트 불러오기 ----------------------------------------------------------------
	@RequestMapping(value="/getNotiList.do", method=RequestMethod.POST)
	public String getNotiList(@ModelAttribute User user, Model model) {
		List<Noti> noti = notiService.getNotiList(user);
		model.addAttribute("noti", noti);
		return "notiList";
	}

	//추가하기(데이터 추가)--------------------------------
	@RequestMapping(value="/insertNoti.do", method=RequestMethod.POST)
	@ResponseBody
	public String insertItem(@ModelAttribute Noti noti) {
		System.out.println("NOTI: " + noti.getFriend_id());
		return JSONController.jsonTemplate(notiService.insertNoti(noti));
	}
	
}
