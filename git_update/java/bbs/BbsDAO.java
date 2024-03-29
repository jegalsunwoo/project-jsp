package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {
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
		
		public String getDate() {
			String SQL = "SELECT NOW()";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString(1);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ""; //데이터베이스 오류
		}
		
		public int getNext() {
			String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt(1)+1;
				}
				return 1; // 첫 번째 게시물인 경우
			}catch(Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		public int write(String bbsTitle, String userID, String bbsContent) {
			//System.out.println("write 함수 내부");
			String SQL = "INSERT INTO BBS VALUES(?,?,?,?,?,?,?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1,getNext());
				pstmt.setString(2,bbsTitle);
				pstmt.setString(3,userID);
				pstmt.setString(4,getDate());
				pstmt.setString(5,bbsContent);
				pstmt.setInt(6,1);
				pstmt.setInt(7,0);
				System.out.println("write 완료. ");
				return pstmt.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
			
		}
		
		public ArrayList<Bbs> getList(int pageNumber){
			//System.out.println("getList() 함수 내부");
			String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			ArrayList<Bbs> list = new ArrayList<Bbs>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					bbs.setViewCount(rs.getInt(7));
					list.add(bbs);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		public boolean nextPage(int pageNumber) {
			//System.out.println("nextPage() 함수 내부");
			String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					//System.out.println("nextPage true!");
					return true;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public Bbs getBbs(int bbsID) {
			//System.out.println("getBbs() 함수 내부");
			String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, bbsID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					bbs.setViewCount(rs.getInt(7));
					return bbs;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public int update(int bbsID, String bbsTitle, String bbsContent) {
			String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ? ";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1,bbsTitle);
				pstmt.setString(2,bbsContent);
				pstmt.setInt(3, bbsID);
				return pstmt.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		public int delete(int bbsID) {
			String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
			String SQL1 = "UPDATE BBS_COMMENT SET commentAvailable = 0 WHERE bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1,bbsID);
				PreparedStatement pstmt1 = conn.prepareStatement(SQL1);
				pstmt1.setInt(1,bbsID);
				//System.out.println("pstmt1"+pstmt1.executeUpdate());
				//System.out.println("pstmt" + pstmt.executeUpdate());
				
				
				int pstmtInt = pstmt.executeUpdate();
				int pstmtInt1 = pstmt1.executeUpdate();
				if(pstmtInt >=0 && pstmtInt1 >= 0) {
					return pstmtInt >= pstmtInt1 ? pstmtInt : pstmtInt1;
				}
				else {
					return -1; //데이터베이스 오류
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류	
		}
		
		public int viewCountUpdate(int bbsID) {
			String SQL = "UPDATE BBS SET viewCount = viewCount + 1 WHERE bbsID = ? ";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, bbsID);
				return pstmt.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}

}
