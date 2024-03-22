<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="bbs_comment.Bbs_comment" %>
<%@ page import="bbs_comment.Bbs_commentDAO" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device=width" , initial-scale="1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">

<title>JSP 게시판 웹 사이트</title>
<style type = "text/css">
	a,a:hover{
		color: #000000;
		text-decoration: none;'	
	
	}
</style>
</head>
<body>

	<% 
		//bbs.jsp화면에서 게시물을 클릭하여 들어갈 때만 조회수가 올라가도록
		//count변수 0 으로 선언
		//세션에 count이름으로 0 저장
		//view.jsp에서 count이름의 세션을 호출하여 받은 값을 view.jsp에서 선언한 count에저장 -> 0
		//count가 0 일 경우에 count값을 1증가시켜주고 -> 조회수 증가 함수 실행 및 세션에 count라는 이름으로 1 저장
		//세션은 동일 브라우저 내에서 유지됨
		//다시 게시판으로 돌아와서 session.setAttribute("count", count); count를 0으로 초기화 해주지 않으면 새로고침을 하여도
		//동일 브라우저 내에서는 조회수가 증가하지 않는다.
		int count = 0;
		System.out.println("count" + count);
		session.setAttribute("count", count);
		
		
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		int pageNumber =1;
		if(request.getParameter("pageNumber") != null){
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			//System.out.println("pageNumber : "+pageNumber);
		}
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expand="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">JSP 게시판 웹 사이트</a>
		</div>
		<div class="collaps navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">메인</a></li>
				<li class="active"><a href="bbs.jsp">게시판</a></li>
			</ul>
			<%
				if(userID == null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspipup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>
			</ul>
			<% 		
				}else{
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspipup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%	
				}
			%>
			
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align; center; broder: 1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">번호</th>
						<th style="background-color: #eeeeee; text-align: center;">제목</th>
						<th style="background-color: #eeeeee; text-align: center;">작성자</th>
						<th style="background-color: #eeeeee; text-align: center;">작성일</th>
						<th style="background-color: #eeeeee; text-align: center;">댓글 수</th>
						<th style="background-color: #eeeeee; text-align: center;">조회수</th>
					</tr>
				</thead>
				<tbody>
					<%
						BbsDAO bbsDAO = new BbsDAO();
						Bbs_commentDAO bbs_commentDAO = new Bbs_commentDAO();
						ArrayList<Bbs> list = bbsDAO.getList(pageNumber);
						//System.out.println(list.size());
						for(int i = 0; i < list.size(); i++){
					%>
					<tr>
						<td style="text-align: center;"><%= list.get(i).getBbsID()%></td>
						<td style="text-align: center;"><a href="view.jsp?bbsID=<%= list.get(i).getBbsID() %>"><%= list.get(i).getBbsTitle().replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></a></td>
						<td style="text-align: center;"><%= list.get(i).getUserID()%></td>
						<td style="text-align: center;"><%= list.get(i).getBbsDate().substring(0,11) + list.get(i).getBbsDate().substring(11,13) + "시" + list.get(i).getBbsDate().substring(14,16) + "분" %></td>
						<td style="text-align: center;"><%= bbs_commentDAO.count(list.get(i).getBbsID())%></td>
						<td style="text-align: center;"><%= list.get(i).getViewCount()%></td>
					</tr>
					<%		
						}
					%>
					
				</tbody>
			</table>
			<%
				if(pageNumber != 1){
			%>
				<div style="text-align: center;">
    				<a href="bbs.jsp?pageNumber=<%=pageNumber-1 %>" class="btn btn-success btn-arraw-left">이전 페이지</a>
				</div>	
			<%	
				} if(bbsDAO.nextPage(pageNumber+1)){
			%>
				<div style="text-align: center;">
					<a href="bbs.jsp?pageNumber=<%=pageNumber+1 %>" class="btn btn-success btn-arraw-left">다음 페이지</a>	
				</div>
			<%	
				}
			%>
			<a href="write.jsp" class="btn btn-primary pull-right">글쓰기</a>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>