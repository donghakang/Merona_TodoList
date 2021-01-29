<%@page import="entity.Address"%>
<%@page import="entity.AddressTodo"%>
<%@page import="java.util.List"%>
<%@page import="entity.Todos"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
List<AddressTodo> todos = (List<AddressTodo>)request.getAttribute("myData");
List<AddressTodo> shared = (List<AddressTodo>)request.getAttribute("sharedData");
JSONObject jsonResult = new JSONObject();

if (todos != null) {
	/*
	* 성공시
	* result: ok
	* data : json array of results
	*/
	JSONArray data = new JSONArray();
	for (AddressTodo addressTodo : todos) {
		JSONObject item = new JSONObject();
		
		Todos t = addressTodo.getTodos();
		Address a = addressTodo.getAddress();
		
		item.put("todo_id", t.getTodo_id());
		item.put("content", t.getContent());
		item.put("memo", t.getMemo());
		item.put("duedate", t.getDuedate());
		item.put("duetime", t.getDuetime());
		item.put("share_with", t.getShare_with());
		item.put("importance", t.getImportance());
		item.put("writer_id", t.getWriter_id());
		item.put("done", t.isDone());
		
		JSONObject address = new JSONObject();
		address.put("addr_id", a.getAddr_id());
		address.put("address_name", a.getAddress_name());
		address.put("place_name", a.getPlace_name());
		address.put("road_address_name", a.getRoad_address_name());
		address.put("category_name", a.getCategory_name());
		address.put("lat", a.getLat());
		address.put("lng", a.getLng());
		System.out.println("A.GETNOTIFIY" + a.isNotify());
		address.put("notify", a.isNotify());
		
		item.put("address", address);
		
		data.put(item);
	}
	
	JSONArray shared_data = new JSONArray();
	
	for (AddressTodo addressTodo : shared) {
		JSONObject item = new JSONObject();
		
		Todos t = addressTodo.getTodos();
		Address a = addressTodo.getAddress();
		
		item.put("todo_id", t.getTodo_id());
		item.put("content", t.getContent());
		item.put("memo", t.getMemo());
		item.put("duedate", t.getDuedate());
		item.put("duetime", t.getDuetime());
		item.put("share_with", t.getShare_with());
		item.put("importance", t.getImportance());
		item.put("writer_id", t.getWriter_id());
		item.put("done", t.isDone());
		
		JSONObject address = new JSONObject();
		address.put("addr_id", a.getAddr_id());
		address.put("address_name", a.getAddress_name());
		address.put("place_name", a.getPlace_name());
		address.put("road_address_name", a.getRoad_address_name());
		address.put("category_name", a.getCategory_name());
		address.put("lat", a.getLat());
		address.put("lng", a.getLng());
		System.out.println("A.GETNOTIFIY" + a.isNotify());
		address.put("notify", a.isNotify());
		
		item.put("address", address);
		
		shared_data.put(item);
	}
	
	
	jsonResult.put("result", "ok");
	jsonResult.put("data", data);
	jsonResult.put("shared", shared_data);
	
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


