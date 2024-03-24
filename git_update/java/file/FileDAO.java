package file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FileDAO {

	private Connection conn;
	private ResultSet rs;
	
	public FileDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "test";
			String dbPassword = "12345";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int upload(String fileName, int bbsID, String realFileName) {
		
		String SQL = "INSERT INTO BBS_FILE VALUES(?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,fileName);
			pstmt.setInt(2,bbsID);
			pstmt.setString(3, realFileName);
			System.out.println("파일 저장 성공");
			return pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String selectFileName(int bbsID) {
		
		String SQL = "SELECT realFileName FROM BBS_FILE WHERE bbsID = ?";
	
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,bbsID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	
}
