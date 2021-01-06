<%@page import="java.util.List"%>
<%@page import="entity.Todos"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%

boolean result = (boolean)request.getAttribute("result");

JSONObject jsonResult = new JSONObject();

if (result) {
	/*
	* 성공시
	* result: ok 		(검색후, 사용가능)
	*/
	jsonResult.put("result", "ok");
	
} else {
	/*
	* 실시
	* result: fail      (검색, 사용불가능 )
	*/
	jsonResult.put("result", "fail");
}

System.out.println(jsonResult.toString());
out.println(jsonResult.toString());
%>


