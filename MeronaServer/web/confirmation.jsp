<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript"
	src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
</head>
<body>
<script>
console.log("hello world");
var IMP = window.IMP; // 생략해도 괜찮습니다.
IMP.init("imp06768101"); // "imp00000000" 대신 발급받은 "가맹점 식별코드"를 사용합니다.
//IMP.certification(param, callback) 호출
IMP.certification({ // param
  merchant_uid: 'merchant_' + new Date().getTime() 
}, function(rsp) {
    if ( rsp.success ) {
        // 인증성공
       console.log(rsp.imp_uid);
       console.log(rsp.merchant_uid);
       
       $.ajax({
               type : 'POST',
               url : '/certifications/confirm',
               dataType : 'json',
               data : {
                   imp_uid : rsp.imp_uid
               }
        }).done(function(){
          takeResponseAndHandle(rsp)
        });
           
   } else {
        // 인증취소 또는 인증실패
       var msg = '인증에 실패하였습니다.';
       msg += '에러내용 : ' + rsp.error_msg;

       alert(msg);
   }
});

function takeResponseAndHandle(rsp) {
   if ( rsp.success ) {
       // 인증성공
       console.log(rsp.imp_uid);
       console.log(rsp.merchant_uid);
   } else {
        // 인증취소 또는 인증실패
       var msg = '인증에 실패하였습니다.';
       msg += '에러내용 : ' + rsp.error_msg;

       alert(msg);
   }
}
console.log("hello world!");
</script>
</body>
</html>