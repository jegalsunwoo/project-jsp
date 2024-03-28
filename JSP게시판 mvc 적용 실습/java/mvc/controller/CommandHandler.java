package mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//handlerAction 동작
public interface CommandHandler {
	//request, response 를 받아서 동작, 동작 결과는 String 을 반환
	public String handlerAction(
			HttpServletRequest request, 
			HttpServletResponse response)throws ServletException, IOException;

}
