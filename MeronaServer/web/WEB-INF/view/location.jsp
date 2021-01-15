<%@page import="java.util.List"%>
<%@page import="entity.Todos"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray" %>
<%
String apiKey = (String)request.getAttribute("apiKey");
String location = (String)request.getAttribute("location");

String apiURL = "//dapi.kakao.com/v2/maps/sdk.js?appkey=" + apiKey + "&libraries=services";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>키워드로 장소검색하고 목록으로 표출하기</title>
 
</head>
<body id="body">
<%-- <div class="map_wrap">
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div>

    <div id="menu_wrap" class="bg_white">
        <div class="option">
            <div>
                <form onsubmit="searchPlaces(); return false;">
                    키워드 : <input type="text" value="<%=location %>" id="keyword" size="15"> 
                    <button type="submit">검색하기</button> 
                </form>
            </div>
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
</div> --%>
<script type="text/javascript" src="<%=apiURL%>"></script>
<script>

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();                      // -- 

// 키워드로 장소를 검색합니다
searchPlaces();

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch( '<%=location%>', placesSearchCB); 
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        document.write('{"result":"ok", "description":"1", ');
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        // displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        document.write('{"result":"ok", "description":"0"}');           // 검색 결과가 없습니다.
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {
        document.write('{"result":"fail", "description":"0"');         // 통신 오류

        return;

    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {
    document.write('locations: [')
    for ( var i=0; i<places.length; i++ ) {
        document.write('{')
        document.write('"lat": "' + places[i].y + '", ');
        document.write('"lng": "' + places[i].x + '", ');
        document.write('"place_name": "' + places[i].place_name + '", ');
        document.write('"road_address_name": "' + places[i].road_address_name + '", ');
        document.write('"address_name": "' + places[i].address_name + '"');
        if (i == places.length - 1) {
            document.write('}')
        } else {
            document.write('}, ')
        }
        // document.write("---------------------------------------");
        // document.write(places[i].y, places[i].x);
        // document.write(places[i].place_name);
        // document.write(places[i].road_address_name);
        // document.write(places[i].address_name);
        // document.write(places[i].phone);
    }
    document.write(']')
}

</script>


</body>
</html>