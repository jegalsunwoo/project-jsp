package mvc.model;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.UserDAO;
import user.UserDTO;

public class JoinService {
	
	public int joinAction(HttpServletRequest request, HttpServletResponse response) {
		
		UserDAO userDAO = new UserDAO();
		UserDTO userDTO = new UserDTO();
		
		try {
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("----------------------------------");
		System.out.println("|           회원가입 서비스        |");
		System.out.println("----------------------------------");
		
		HttpSession session = request.getSession();
		
		String userID = null;
		
		if(session.getAttribute("userID") != null) {
			userID = (String)session.getAttribute("userID");
		}
		if(userID != null){
			System.out.println("이미 로그인 됨.");
		}
		
		userDTO.setUserID(request.getParameter("userID"));
		userDTO.setUserPassword(request.getParameter("userPassword"));
		userDTO.setUserName(request.getParameter("userName"));
		userDTO.setUserGender(request.getParameter("userGender"));
		userDTO.setUserEmail(request.getParameter("userEmail"));
		
		System.out.println("userID : "+ userDTO.getUserID().isEmpty());
		System.out.println("userPassword : "+userDTO.getUserPassword());
		System.out.println("userName : "+userDTO.getUserName());
		System.out.println("userGender : "+userDTO.getUserGender());
		System.out.println("userEmail : "+userDTO.getUserEmail());
		
		if(userDTO.getUserID().isEmpty() || userDTO.getUserPassword().isEmpty() || userDTO.getUserName().isEmpty() 
				|| userDTO.getUserGender().isEmpty() || userDTO.getUserEmail().isEmpty()){
			return -2; //입력되지 않은 항목 있음
		}
		
		int result = userDAO.join(userDTO);
		
		return result;
	}
	
}
