package controller;

import org.json.JSONObject;

public class JSONController {
	// -- result가 ok 혹은 fail 만 있는 데이터 전송은 Template 을 이용한다.
	public static String jsonTemplate(boolean data) {
		JSONObject json = new JSONObject();
		
		if(data) json.put("result", "ok");
		else json.put("result", "fail");
		return json.toString();
	}
}
