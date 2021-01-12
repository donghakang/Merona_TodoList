package controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import entity.User;
import model.UserService;

@Controller
public class NotiController {
//	private UserService userService;

//	@Autowired
//	public UserController(UserService userService) {
//		super();
//		this.userService = userService;
//	}
//	
	// 회원가입시, 사용자 이름 가능여부
	// ---------------------------------------------------------------
	@RequestMapping(value = "/push.do")
	public String push() {
		ArrayList<String> token = new ArrayList<>();
		token.add(
				"c96CkM_eSO2ef_EfE9F50U:APA91bEMP6weEFraMyn4qOTIwXEJvkxTVGnfucD33bQibea5syP08wrM566o5l8Zt5g5rNEcbH_UaVevy1O-IWO6hed6fDjrjkTERy_syXADQ5VzVjcmp00MLq1QksmzBT2RqHW9fE_z");
		String server_key = "AAAA0G64bRA:APA91bEc2y3dYzif-D0K2aQAEcnOv7x08jq8ex0XpfVk8pac2YIL2-HPkincS1ZBI0Z0Gi24qTeIXUdp-QHCtFOj0lfDO53ZL8lDNSOUbA908rzTQTGz6wZWRojEQZejYKpASRr5UeF4";

		try {
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + server_key);
			conn.setDoOutput(true);

			JSONArray registration_ids = new JSONArray();
			for (String id : token) {
				registration_ids.put(id);	
			}
			
			String title = "아하하하";
			String body = "내요요요요요요요용";
			JSONObject info = new JSONObject();
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", body);
			
			info.put("notification", notification);
			info.put("registration_ids", token);

			OutputStream os = conn.getOutputStream();

			// 서버에서 날려서 한글 깨지는 사람은 아래처럼 UTF-8로 인코딩해서 날려주자
			os.write(info.toString().getBytes("UTF-8"));
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				System.out.println(responseCode + " : Successful");
			} else if (responseCode == 400) {
				System.out.println(responseCode + " : Problem in JSON Parsing");
			} else if (responseCode == 401) {
				System.out.println(responseCode + " : token ID");
			} else {
				System.out.println(responseCode + " : Connection ERR");
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			System.out.println(response.toString());

		} catch (Exception e) {
			System.out.println("not working");
			e.printStackTrace();
		}

		return "tmp";
	}

}
/*
 * @ModelAttribute User user는 아래와 동일 User user = new User();
 * user.setName(request.getParameter("name"))
 * user.setAge(request.getParameter("age"))
 * user.setHobby(request.getParameter("hobby"))
 */
