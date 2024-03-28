package mvc.model;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.UserDAO;

public class LoginService {
	
	public int loginAction(HttpServletRequest request, HttpServletResponse response) {
	
		try {
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("----------------------------------");
		System.out.println("|            로그인 서비스        |");
		System.out.println("----------------------------------");
		
		HttpSession session = request.getSession();
		
		String userID = null;
		
		if(session.getAttribute("userID") != null) {
			userID = (String)session.getAttribute("userID");
		}
		if(userID != null){
			System.out.println("이미 로그인 됨.");
		}
		
		
		userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");
		
		System.out.println(userID);
		System.out.println(userPassword);
		
		UserDAO userDAO = new UserDAO();
		
		int result = userDAO.login(userID, userPassword);
		if(result == 1) {
			session.setAttribute("userID", userID);
		}
		//로그인 성공 시 세션 부여해야함
		
		return result;
	}
}
