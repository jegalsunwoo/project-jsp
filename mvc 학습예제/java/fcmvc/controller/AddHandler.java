package fcmvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fcmvc.model.AddService;

//CommandHandler을 구현
public class AddHandler implements CommandHandler{
	
	//AddHandler는 AddService를 사용해야 한다.
	private AddService addService = new AddService();
	
	@Override
	public String handlerAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//요청이 들어왔을 때 파라미터를 통해 n1,n2값을 저장
		int n1 = Integer.parseInt(request.getParameter("n1"));
		int n2 = Integer.parseInt(request.getParameter("n2"));
		
		//AddService의 더하기기능 수행
		int result = addService.add(n1, n2);
		
		//더하기를 수행 한 후 결과물을 request에 저장
		request.setAttribute("result", result);
		
		//add.jsp라는 뷰 경로를 반환
		return "/WEB-INF/view/add.jsp";
	}
}
