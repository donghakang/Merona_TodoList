<%@page import="entity.Todos"%>
<%@page import="java.util.List"%>
<%@page import="entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
User user = (User)request.getAttribute("user");
List<Todos> my_data = (List<Todos>)request.getAttribute("data");
List<Todos> shared_data = (List<Todos>)request.getAttribute("shared_data");

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
	user_info.put("token", user.getToken());
	
	JSONArray data = new JSONArray();
	for (Todos t : my_data) {
		JSONObject data_tmp = new JSONObject();
		data_tmp.put("todo_id", t.getTodo_id());
		data_tmp.put("content", t.getContent());
		data_tmp.put("memo", t.getMemo());
		data_tmp.put("duedate", t.getDuedate());
		data_tmp.put("duetime", t.getDuetime());
		data_tmp.put("share_with", t.getShare_with());
		data_tmp.put("importance", t.getImportance());
		
		data_tmp.put("writer_id", t.getWriter_id());
		data_tmp.put("addr_id", t.getAddr_id());
		
		data_tmp.put("done", t.isDone());
		
		data.put(data_tmp);
	}
	
	JSONArray shared = new JSONArray();
	for (Todos sh : shared_data) {
		JSONObject data_tmp = new JSONObject();
		data_tmp.put("todo_id", sh.getTodo_id());
		data_tmp.put("content", sh.getContent());
		data_tmp.put("memo", sh.getMemo());
		data_tmp.put("duedate", sh.getDuedate());
		data_tmp.put("duetime", sh.getDuetime());
		data_tmp.put("share_with", sh.getShare_with());
		data_tmp.put("importance", sh.getImportance());
		
		data_tmp.put("writer_id", sh.getWriter_id());
		data_tmp.put("addr_id", sh.getAddr_id());
		
		data_tmp.put("done", sh.isDone());
		
		shared.put(data_tmp);
	}
	
	jsonResult.put("result", "ok");
	jsonResult.put("user", user_info);
	jsonResult.put("data", data);
	jsonResult.put("shared_data", shared);
	System.out.println("shared --> " + shared.toString());
} else {
	/*
	* 실시
	* result: fail
	*/
	jsonResult.put("result", "fail");
}

out.println(jsonResult.toString());
%>


