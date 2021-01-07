<%@page import="entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
User user = (User)request.getAttribute("user");

JSONObject jsonResult = new JSONObject();

if (user != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	JSONObject user_info = new JSONObject();
	user_info.put("user_id", user.getUser_id());
	user_info.put("id", user.getId());
	user_info.put("name", user.getName());
	user_info.put("email", user.getEmail());
	user_info.put("birth", user.getBirth());
	
	
	jsonResult.put("result", "ok");
	jsonResult.put("data", user_info);
	
} else {
	/*
	* 실시
	* result: fail
	*/
	jsonResult.put("result", "fail");
}

out.println(jsonResult.toString());
%>


