<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs_comment.Bbs_commentDAO" %>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs_comment" class="bbs_comment.Bbs_comment" scope="page"/>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty name="bbs_comment" property="commentContent"/>
<jsp:setProperty name="bbs_comment" property="bbsID"/>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/custom.css">

<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		
		//로그인 안되어 있을 시 로그인 유도
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인 하세요')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}else{
			//아무 내용도 입력되지 않았을 때
			//System.out.println("WW bbsID : "+ bbs_comment.getBbsID());
			if(bbs_comment.getCommentContent() == null){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력인 안 된 항목이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}else{
				Bbs_commentDAO bbs_commentDAO = new Bbs_commentDAO();
				System.out.println(userID);
				//
				int result = bbs_commentDAO.write_comment(bbs_comment.getBbsID(), userID, bbs_comment.getCommentContent());
				//
				if(result == -1){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}
				else{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('댓글이 등록되었습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}
			}
		}
		
		
	%>
</body>
</html>