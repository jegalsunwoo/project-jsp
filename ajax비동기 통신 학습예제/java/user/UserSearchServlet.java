package user;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/UserSearchServlet")
public class UserSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//request로 넘어온 값을 UTF-8으로 처리하겠다
		request.setCharacterEncoding("UTF-8");
		
		//response를 text/html타입으로 UTF-8로 처리?
		response.setContentType("text/html;charset=UTF-8");
		
		//userName 변수생성 후 요청에 담긴 파라미터값 userName을 가져오도록 한다.
		String userName = request.getParameter("userName");
		
		response.getWriter().write(getJSON(userName));
		
	}
	
	
	//JSON -> 파싱하기 쉬운 하나의 형태이다.
	//회원을 검색했을 때 검색한 정보다 JSON형태로 출력이 된다. 이걸 다시 파싱하여 분석 후 우리에게 보여준다.
	//서버 역할을 하는 서블릿 같은 경우에는 JSON을 만들어 내는 역할을 함
	
	//index페이지에서 request를 하면 JSON형태로 response
	
	
	public String getJSON(String userName) {
		
		if(userName == null) userName ="";
		
		//버퍼 생성
		StringBuffer result = new StringBuffer("");
		//버퍼에 () 괄호 안 문자열 추가
		result.append("{\"result\":["); //현재 버퍼 상태 {result:[
		System.out.println("Buffer1 : "+ result);
		
		//userDAO 인스턴스 생성
		UserDAO userDAO = new UserDAO();
		//userList 생성 후 search결과 담기
		ArrayList<User> userList = userDAO.search(userName);
		for(int i = 0; i < userList.size(); i++) {
			result.append("[{\"value\": \""+userList.get(i).getUserName() + "\"},");
			result.append("{\"value\": \""+userList.get(i).getUserAge() + "\"},");
			result.append("{\"value\": \""+userList.get(i).getUserGender() + "\"},");
			result.append("{\"value\": \""+userList.get(i).getUserEmail() + "\"}],");
		}
		result.append("]}");
		System.out.println("Buffer2 : "+ result);
		return result.toString();
		
	}

}
