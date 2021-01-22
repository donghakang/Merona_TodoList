<%@page import="entity.Noti"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%

List<Noti> noti = (List<Noti>)request.getAttribute("noti");

JSONObject jsonResult = new JSONObject();

if (noti != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	JSONArray data = new JSONArray();
	for (Noti n : noti) {
		JSONObject item = new JSONObject();
		item.put("noti_id", n.getNoti_id());
		item.put("user_id", n.getUser_id());
		item.put("friend_id", n.getFriend_id());
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


