package fcmvc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//FrontController만 클라이언트의 요청을 받아 처리하고 응답을 줄 수 있는 Servlet이다.
//요청을 구분하고 그 요청에 맞는 기능으로 연결을 해줘야 함
public class FrontController extends HttpServlet{
	//요처을 구분하기 위해 미리 준비
	//commandHandlerMap에 HaspMap객체를 생성
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
	
	//sevlet객체가 생성되었을 때 최초에 한번 호출되는 초기화 함수
	@Override
	public void init() throws ServletException {
		//commandHandlerMap에 "/add"문자열과 매핑되는 객체를 AddHandler라고 등록
		System.out.println("핸들러 객체 생성 완료");
		commandHandlerMap.put("/add", new AddHandler());
		commandHandlerMap.put("/min", new MinHandler());
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET,POST요청 중 어떤 방식인지 : " + req.getMethod());
		//어떤 요청이 왔는지 확인해보기
		System.out.println("Controller가 요청 분석");
		String requestURI = req.getRequestURI().toString();
		System.out.println("요청URI : "+requestURI);
		
		//들어온 요청에서 "/add","/min"이랑 매핑시킬 수 있는 정보만 자름
		String command = requestURI.substring(req.getContextPath().length());
		System.out.println("command : "+command);
		
		CommandHandler handler = null;
		String viewPage = null;
		
		//
		if(requestURI.indexOf(req.getContextPath()) == 0) {
			//command가 /add라면 AddHandler꺼냄, /min이면 MinHandler
			handler = commandHandlerMap.get(command);
			System.out.println("핸들러 선택 완료");
			System.out.println(handler);
			
			
			//해들러엑션 동작 후 리턴값을 viewPage에 저장
			viewPage = handler.handlerAction(req,resp);
			System.out.println("Model 비즈니스 로직 동작");
		}
		
		//viewPage로 포워딩
		System.out.println("controller가 결과 데이터를 보여줄 뷰로 포워딩");
		req.getRequestDispatcher(viewPage).forward(req, resp);
	}

}
