package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			//DB 접근할 수 있도록 URL을 지정해준다
			String dbURL ="jdbc:mysql://localhost:3306/AJAX";
			
			//dbID,dbPassword
			String dbID="root";
			String dbPassword="mymysql";
			
			//드라이버를 검색
			Class.forName("com.mysql.jdbc.Driver");
			
			//실제로 연결
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword);
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	//데이터 베이스안의 USER테이블을 ArrayList로 가져오는 함수
	public ArrayList<User> search(String userName){
		//user정보를 가져오기 위한 select문 작성         //LIKE -> ? 값을 포함하는
		String SQL = "SELECT * FROM USER WHERE userName LIKE ?";
		
		//userList 생성
		ArrayList<User> userList = new ArrayList<User>();
		
		try {
			//pstmt인스턴스 안에 연결된 데이터베이스에 SQL문 삽입
			pstmt = conn.prepareStatement(SQL);
			
			//첫번째 ? 에 userName 입력 
			pstmt.setString(1, "%" + userName + "%");
			
			//rs에 쿼리문을 수행한 결과물을 담아줌
			rs = pstmt.executeQuery();
			
			//나온 결과를 하나씩 다 읽어 userList안에 저장
			while(rs.next()) {
				//user 인스턴스 생성
				User user = new User();
				
				//user 인스턴스의 set함수를 사용하여 user인스턴스 안에 정보를 넣어준다.
				user.setUserName(rs.getString(1));
				user.setUserAge(rs.getInt(2));
				user.setUserGender(rs.getString(3));
				user.setUserEmail(rs.getString(4));
				
				//담아준 정보를 userList에 추가
				userList.add(user);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//user 정보가 담긴 userList반환
		return userList;
	}

}
