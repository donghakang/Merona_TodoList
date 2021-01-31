<%@page import="entity.User"%>
<%@page import="entity.Noti"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%

List<Noti> noti = (List<Noti>)request.getAttribute("noti");
List<User> users = (List<User>)request.getAttribute("users");
List<User> friends = (List<User>)request.getAttribute("friends");

JSONObject jsonResult = new JSONObject();

if (noti != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	JSONArray data = new JSONArray();
	
	for (int i = 0; i < noti.size(); i ++ ){ 
		JSONObject item = new JSONObject();
		Noti n = noti.get(i);
		User u = users.get(i);
		User f = friends.get(i);
		JSONObject user_ = new JSONObject();
		user_.put("user_id", u.getUser_id());
		user_.put("id", u.getId());
		user_.put("name", u.getName());
		user_.put("email", u.getEmail());
		user_.put("birth", u.getBirth());
		user_.put("token", u.getToken());
		
		JSONObject friend_ = new JSONObject();
		friend_.put("user_id", f.getUser_id());
		friend_.put("id", f.getId());
		friend_.put("name", f.getName());
		friend_.put("email", f.getEmail());
		friend_.put("birth", f.getBirth());
		friend_.put("token", f.getToken());
		
		item.put("noti_id", n.getNoti_id());
		item.put("user", user_);
		item.put("friend", friend_);
		item.put("type", n.getType());
		item.put("pushDate", n.getPushDate());

		data.put(item);
	}
	
	jsonResult.put("result", "ok");
	jsonResult.put("data", data);
	
} else {
	/*
	* 실시
	* result: fail
	*/
	jsonResult.put("result", "fail");
}

out.println(jsonResult.toString());

%>


