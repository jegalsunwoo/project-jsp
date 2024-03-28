package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.model.LoginService;

//CommandHandler을 구현
public class LoginHandler implements CommandHandler {
	
	//LoginHandler는 LoginService사용
	private LoginService loginService = new LoginService();
	
	@Override
	public String handlerAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		//요청이 들어왔을 때 파라미터를 통해 n1,n2값을 저장
//		String userID = request.getParameter("userID");
//		String userPassword = request.getParameter("userPassword");
		
		System.out.println("----------------------------------");
		System.out.println("|            로그인 핸들러        |");
		System.out.println("----------------------------------");
		
		int result = loginService.loginAction(request, response);
		System.out.println("result :"+result);
		
		
		if(result == 1) {
			System.out.println("로그인 성공!");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인 성공!')");
			script.println("</script>");
			return "/main";
		}else if(result == 0)
		{
			System.out.println("비밀번호 불일지");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호가 틀립니다.')");
			script.println("location.href = 'login'");
			script.println("</script>");
		}
		else if(result == -1) {
			System.out.println("아이디 없음");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('아이디가 존재하지 않습니다.')");
			script.println("location.href = 'login'");
			script.println("</script>");
		}
		else if(result == -2) {
			System.out.println("데이터베이스 오류");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('데이터베이스 오류.')");
			script.println("location.href = 'login'");
			script.println("</script>");

		}
		return null;
	}
}
