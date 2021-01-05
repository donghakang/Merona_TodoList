<%@page import="java.util.List"%>
<%@page import="entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
List<User> users = (List<User>)request.getAttribute("list");

JSONObject jsonResult = new JSONObject();

if (users != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	
	JSONArray data = new JSONArray();
	for (User u : users) {
		JSONObject user = new JSONObject();
		user.put("user_id", u.getUser_id());
		user.put("id", u.getId());
		data.put(user);
	}
	
	jsonResult.put("result", "ok");
	jsonResult.put("friend", data);
	
} else {
	/*
	* 실시
	* result: fail
	*/
	jsonResult.put("result", "fail");
}

System.out.println(jsonResult.toString());
out.println(jsonResult.toString());
System.out.println("-----------");
%>


