<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="file.FileDAO" %>
<%@ page import="java.io.File" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>

<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty name="bbs" property="bbsTitle"/>
<jsp:setProperty name="bbs" property="bbsContent"/>


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
		
		//////////////////////////////////////////////
		BbsDAO bbsDAO = new BbsDAO();

		int bbsID = bbs.getBbsID();
		System.out.println("bbsID : " + bbsID);
		String directory = application.getRealPath("/images/");
		System.out.println("directory : " + directory);
		
		File targetDir = new File(directory);
		if(!targetDir.exists()){
			targetDir.mkdirs();
		}
		
		int maxSize = 1024 * 1024 * 500;
		String encoding = "UTF-8";
		
		MultipartRequest multipartRequest = new MultipartRequest(request, directory, maxSize, encoding, new DefaultFileRenamePolicy());
		
		String fileName = multipartRequest.getOriginalFileName("file");
		String realFileName = multipartRequest.getFilesystemName("file");
		
		
		// 받아온 변수 처리
		String bbsTitle = multipartRequest.getParameter("bbsTitle");
		String bbsContent = multipartRequest.getParameter("bbsContent");
		System.out.println("bbsT : " + bbsTitle);

		System.out.println("bbsContent : " + bbsContent);
		bbs.setBbsTitle(bbsTitle);
		bbs.setBbsContent(bbsContent);
		
		
		//로그인 안되어 있을 시 로그인 유도
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인 하세요')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}else{
			
			//테스트
			System.out.println("bbsTitle : "+ bbsTitle);
			System.out.println("bbsContent : "+ bbsContent);
			
			
			//null값이 아니라 ""이기 때문에 isEmpty() true일 경우 추가
			if(bbsTitle == null || bbsTitle.isEmpty() ||  bbsContent == null || bbsContent.isEmpty()){
				
				System.out.println("뭐지?");
				
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력인 안 된 항목이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			
			else //로그인 되어있을 때
			{
				System.out.println(userID);
				int result = bbsDAO.write(bbsTitle, userID, bbsContent);
				
				
				
				//파일  db 저장
				bbs.setBbsID(bbsDAO.getNext()-1);
				new FileDAO().upload(fileName,bbs.getBbsID(),realFileName);
				System.out.println("허허 : "+bbs.getBbsID());
				out.write("filename : " + fileName + "<br>");
				
				if(result == -1){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}
				
				else
				{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기 성공!')");
					script.println("location.href = 'bbs.jsp'");
					script.println("</script>");
				}
			}
		}
		
		
	%>
</body>
</html>