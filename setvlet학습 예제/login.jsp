<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%!
	public void init() throws ServletException{
	System.out.println("login.jsp 초기화");
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- action속성을 따로 지정하지 않으면 현재 url그대로 활용함 -->
<form method="post" action="login">
	<input type="text" name="userID">아이디<br>
	<input type="text" name="userPW">비밀번호<br>
	<input type="submit" name="로그인">

</form>
</body>
</html>