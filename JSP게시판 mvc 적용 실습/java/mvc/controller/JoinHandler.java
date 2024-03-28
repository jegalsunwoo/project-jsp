package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.model.JoinService;

public class JoinHandler implements CommandHandler{
	
	private JoinService joinService = new JoinService();
	
	@Override
	public String handlerAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------------------------");
		System.out.println("|          회원가입 핸들러        |");
		System.out.println("----------------------------------");
		
		int result = joinService.joinAction(request, response);
		System.out.println("result :"+result);
		
		if(result == -1) {
			System.out.println("이미 아이디가 존재");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 아이디가 존재합니다.')");
			script.println("location.href = 'join'");
			script.println("</script>");
		}else if(result == -2) {
			System.out.println("입력되지 않은 항목이 있음");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력되지 않은 항목이 있습니다.')");
			script.println("location.href = 'join'");
			script.println("</script>");
		}else {
			System.out.println("회원가입 성공!");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('회원가입 성공! 로그인 해주세요.')");
			script.println("location.href = 'login'");
			script.println("</script>");
		}
		return null;
	}
}
