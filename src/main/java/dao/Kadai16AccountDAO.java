package dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.Kadai16Account;
import util.GenerateHashedPw;
import util.GenerateSalt;

public class Kadai16AccountDAO {

	private static Connection getConnection() throws URISyntaxException, SQLException{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
	    
	    
	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
	public static int kadairegisterAccount(Kadai16Account account) {
		String sql = "INSERT INTO account VALUES(default, ?, ?, ?, ?, current_timestamp)";
		int result = 0;
		
		String salt = GenerateSalt.getSalt(32);
		
		String hashedPw = GenerateHashedPw.getSafetyPassword(account.getPassword(), salt);
		
		System.out.println("生成されたソルト："+salt);
		System.out.println("生成されたハッシュ値："+hashedPw);
		
		try(
				Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);
				){
			pstmt.setString(1, account.getName());
			pstmt.setInt(2, account.getAge());
			pstmt.setString(3, account.getGender());
			pstmt.setString(4, account.getMail());
			pstmt.setString(5, salt);
			pstmt.setString(6, hashedPw);
			
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			System.out.println(result + "件更新しました。");
		}
		return result;
	}
	
	public static String getSalt(String mail) {
		String sql = "SELECT salt FROM account WHERE mail = ?";
		
		try(
				Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);
				){
			pstmt.setString(1, mail);
			
			try(ResultSet rs = pstmt.executeQuery()){
				
				if(rs.next()) {
					String salt = rs.getString("salt");
					return salt;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Kadai16Account login(String mail, String hashedPw) {
		String sql = "SELECT * FROM account WHERE mail = ? AND password = ?";
		
		try(
				Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);
				){
			pstmt.setString(1, mail);
			pstmt.setString(2, hashedPw);
			
			try (ResultSet rs = pstmt.executeQuery()){
				
				if(rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					int age = rs.getInt("age");
					String gender = rs.getString("gender");
					String tel = rs.getString("tel");
					String salt = rs.getString("salt");
					String createdAt = rs.getString("created_at");
					
					return new Kadai16Account(id, name, age, gender, mail,salt, tel, null, createdAt);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
