package mvc.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//FrontController은 클라이언트의 요청을 받아 처리하고 응답을 줄 수 있는 서블릿 이다.
//요청을 구분하고 그 요청에 맞는 기능을 동작하도록 연결을 해줌

@WebServlet(name = "frontController", urlPatterns = {"/login", "/join","/logout","/main"}, initParams = {@WebInitParam(name="handlerProperties", value = "/WEB-INF/handler.properties")})
public class FrontController extends HttpServlet{
	
	//HashMap생성
	private Map<String, CommandHandler> commandHandlerMap = new HashMap();
	
	
	//서블릿 생성 시 최초에 호출되는 init() 함수를 활용하여 
	//요청에 대한 기능을 연결해 주기위한 HashMap을 생ㅇ성
	@Override
	public void init() throws ServletException {
		
		System.out.println("----------------------------------");
		System.out.println("|            서블릿 생성          |");
		System.out.println("----------------------------------");
		
		//web.xml에서 선언한 handlerProperties값을 파라미터를 통해 받아옴
				String contextConfigFile = this.getInitParameter("handlerProperties");
				System.out.println("contextContigFile:"+contextConfigFile);
				//Properties객체가 자바에 원래 존재함.  객체생성
				Properties properties = new Properties();
				//FileInputStream생성
				FileInputStream fis = null;
				
				try {
					String contextConfigFilePath = this.getServletContext().getRealPath(contextConfigFile);
					fis = new FileInputStream(contextConfigFilePath);
					properties.load(fis);
					System.out.println("fis: "+ contextConfigFilePath);

					System.out.println("properties: "+properties);
				}catch(IOException e) {
					e.printStackTrace();
				}finally {
					if(fis != null) {
						try {
							fis.close();
						}catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				Iterator<Object> propIt = properties.keySet().iterator();
				while(propIt.hasNext()) {
					String command = (String)propIt.next();
					String handlerClassName = properties.getProperty(command);
					try {
						Class<?> handlerClass = Class.forName(handlerClassName);
						commandHandlerMap.put(command, (CommandHandler)handlerClass.newInstance());
					}catch(ClassNotFoundException e) {
						e.printStackTrace();
					}catch(IllegalAccessException e) {
						e.printStackTrace();
					}catch(InstantiationException e) {
						e.printStackTrace();
					}
				}

	}

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//GET,POST확인
		System.out.println("GET,POST요청 중 어떤 방식인지 : " + req.getMethod());
		
		if(req.getMethod().equals("GET")) {
			doGet(req, resp);
		}
		
		
		if(req.getMethod().equals("POST")) {
			doPost(req,resp);
		}
		
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("----------------------------------");
		System.out.println("|            GET 메소드           |");
		System.out.println("----------------------------------");
		//들어온 요청에서 매핑시킬 수 있는 정보만 자름
		String requestURI = req.getRequestURI().toString();
		String command = requestURI.substring(req.getContextPath().length());
		System.out.println("command : "+command);
		
		if(command.equals("/login")) {
			req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
		}
		else if(command.equals("/join")) {
			req.getRequestDispatcher("/WEB-INF/view/join.jsp").forward(req, resp);
		}else if(command.equals("/logout")) {
			HttpSession session = req.getSession();
			session.invalidate();
			System.out.println("세션제거 완료. 로그아웃!");
			resp.sendRedirect(req.getContextPath()+"/main");
		}else if(command.equals("/main")) {
			req.getRequestDispatcher("/WEB-INF/view/main.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//어떤 요청이 왔는지 확인해보기
				System.out.println("Controller가 요청 분석");
				String requestURI = req.getRequestURI().toString();
				System.out.println("요청URI : "+requestURI);
				
				//들어온 요청에서 매핑시킬 수 있는 정보만 자름
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
					if(viewPage != null) {
						// /bbs)project를 viewPage 앞에 추가
						viewPage = req.getContextPath()+viewPage;
						System.out.println(viewPage);
					}
					
				}
				
				if(viewPage != null) {
					//viewPage로 포워딩
					System.out.println("controller가 결과 데이터를 보여줄 뷰로 포워딩");
					resp.sendRedirect(viewPage);
				}
				
				
				//req.getRequestDispatcher(viewPage).forward(req, resp);
	}
}
