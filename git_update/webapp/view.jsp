<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="bbs.BbsDAO" %>
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
</head>



<!-- 네비 게이션 부분 건들일 필요 없음 -->
<body>
	<% 
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		int bbsID = 0;
		if(request.getParameter("bbsID") != null){
			//System.out.printf("%s%n",request.getParameter("bbsID"));
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
			//System.out.println("bbsID : "+ bbsID);
		}
		if(bbsID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		
		Bbs bbs = new BbsDAO().getBbs(bbsID);
		BbsDAO bbsDAO = new BbsDAO();
		Bbs_comment bbs_comment = new Bbs_comment();
		Bbs_commentDAO bbs_commentDAO = new Bbs_commentDAO();
		
		//System.out.println("댓글 수" + bbs_commentDAO.count(bbsID));
		ArrayList<Bbs_comment> clist = bbs_commentDAO.getCommentList(bbsID);
		
		
		// 세션에서 변수 count를 가져옵니다.
		int count = (Integer) session.getAttribute("count");
		System.out.println("1view Count: " + count);
		if(count == 0){
			count++;
			System.out.println("2view Count: " + count);
			session.setAttribute("count", count);
			bbsDAO.viewCountUpdate(bbsID);
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
	
	
<!-- 게시글 출력 부분-->

	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3" style="background-color: #eeeeee; text-align: center;">게시판 글 보기</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%;">글 제목</td>
						<td colspan="2"><%= bbs.getBbsTitle().replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></td>
					</tr>
					<tr>
						<td>작성자</td>
						<td colspan="2"><%= bbs.getUserID() %></td>
					</tr>
					<tr>
						<td>작성일자</td>
						<td colspan="2"><%= bbs.getBbsDate().substring(0,11) + bbs.getBbsDate().substring(11,13) + "시" + bbs.getBbsDate().substring(14,16) + "분" %></td>
					</tr>
					<tr>
						<td>내용</td>
						<td colspan="2" style="min-height: 300px; text-align: left;"><%= bbs.getBbsContent().replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></td>
					</tr>
				</tbody>
			</table>
			<a href="bbs.jsp" class="btn btn-primary">목록</a>
			<%
				if(userID != null && userID.equals(bbs.getUserID())){
			%>
					<a href="update.jsp?bbsID=<%=bbsID%>" class="btn btn-primary">수정</a>
					<a onclick="return confirm('정말로 삭제하시겠습니까?')" href="deleteAction.jsp?bbsID=<%=bbsID%>" class="btn btn-primary">삭제</a>
			<% 
				}
			%>
		</div>
	</div>
	<!-- 댓글 출력 부분 border: 1px solid 이거 테두리 -->	
	<div class="container"><br><br><br><br>
	    <div class="row">
	        <table class="table table-striped" style="width: 100%; text-align: center; ">
	            <thead>
	                <tr>
	                    <th colspan="3" style="text-align: left;">전체 댓글 개수 : <%=clist.size()%>개</th>
	                </tr>
	            </thead>
	            <tbody>

                	<%
                		for(int i = 0; i < clist.size(); i++){   		
                	%>		
						<tr>
							<td colspan="1" style="width: 10%; text-align: left;" ><%= clist.get(i).getUserID() %></td>
							<td colspan="1" style="width: 75%; text-align: left;"><%= clist.get(i).getCommentContent().replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n","<br>") %></td>
							<td colspan="2" style="width: 15%; text-align: right;"><%= clist.get(i).getCommentDate() %></td>
						</tr>
                	<%
                		}
                	%>
         
	            </tbody>
	        </table>
	    </div>
	</div>
	
	<!-- 댓글 작성 부분 -->	
	<div class="container">
		<div class="row">
			<form method="post" action="commentWriteAction.jsp">
				<input type="hidden" name="bbsID" value="<%= bbsID %>">
				<table class="table table-striped" style="text-align; center; broder: 1px solid #dddddd">
					<thead>
						<tr>
							<th colspan="2" style="background-color: #eeeeee; text-align: center;">댓글</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><textarea class="form-control" placeholder="댓글 내용" name="commentContent" maxlength="100" style="height: 50px"></textarea></td>
						</tr>
					</tbody>
				</table>
				<input type="submit" class="btn btn-primary pull-right" value="댓글 작성">
			</form>		
		</div>
	</div>
	 
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>