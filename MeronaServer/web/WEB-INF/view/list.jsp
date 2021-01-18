<%@page import="java.util.List"%>
<%@page import="entity.Todos"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
/* JSONObject json = new JSONObject();
json.put("name", "aaa");
json.put("age", "256");
json.put("hobby", "bike");
 */
/* JSONObject json = new JSONObject();
json.put("result", "ok");

System.out.println(json.toString());
out.println(json.toString());
System.out.println("-----------"); */

List<Todos> todos = (List<Todos>)request.getAttribute("todoList");

JSONObject jsonResult = new JSONObject();

if (todos != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	JSONArray data = new JSONArray();
	for (Todos t : todos) {
		JSONObject item = new JSONObject();
		item.put("todo_id", t.getTodo_id());
		item.put("content", t.getContent());
		item.put("memo", t.getMemo());
		item.put("duedate", t.getDuedate());
		item.put("duetime", t.getDuetime());
		item.put("share_with", t.getShare_with());
		item.put("importance", t.getImportance());
		item.put("writer_id", t.getWriter_id());
		item.put("addr_id", t.getAddr_id());
		
		item.put("done", t.isDone());
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

System.out.println(jsonResult.toString());
out.println(jsonResult.toString());
System.out.println("-----------");
%>


