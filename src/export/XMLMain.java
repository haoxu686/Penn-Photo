package export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class XMLMain {
	
	private static String FROM_CIRCLE = "SELECT * FROM circle WHERE uid = ? ORDER BY cid ASC";
	private static String FROM_FRIEND = "SELECT * FROM friend WHERE cid = ? ORDER BY uid ASC";
	private static String FROM_INSTITUTION = "SELECT * FROM institution WHERE uid = ?";
	private static String FROM_INTEREST = "SELECT * FROM interest WHERE uid = ?";
	private static String FROM_PHOTO = "SELECT * FROM photo WHERE uid = ? ORDER BY photoid ASC";
	private static String FROM_PROFESSOR = "SELECT * FROM professor ORDER BY uid ASC";
	private static String FROM_RATING = "SELECT * FROM rating WHERE photoid = ?";
	private static String FROM_STUDENT = "SELECT * FROM student ORDER BY uid ASC";
	private static String FROM_TAG = "SELECT * FROM tag WHERE photoid = ?";
	private static String FROM_USER = "SELECT * FROM user WHERE uid = ?";
	private static String FROM_VISIBILITY = "SELECT * FROM visibility WHERE photoid = ?";
	
	private static PreparedStatement pFromCircle;
	private static PreparedStatement pFromFriend;
	private static PreparedStatement pFromInstitution;
	private static PreparedStatement pFromInterest;
	private static PreparedStatement pFromPhoto;
	private static PreparedStatement pFromProfessor;
	private static PreparedStatement pFromRating;
	private static PreparedStatement pFromStudent;
	private static PreparedStatement pFromTag;
	private static PreparedStatement pFromUser;
	private static PreparedStatement pFromVisibility;

	private static ResultSet rsCircle;
	private static ResultSet rsFriend;
	private static ResultSet rsInstitution;
	private static ResultSet rsInterest;
	private static ResultSet rsPhoto;
	private static ResultSet rsProfessor;
	private static ResultSet rsRating;
	private static ResultSet rsStudent;
	private static ResultSet rsTag;
	private static ResultSet rsUser;
	private static ResultSet rsVisibility;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://fling.seas.upenn.edu:3306/haoxu";
		Connection connection = DriverManager.getConnection(url, "haoxu", "QwerAsdf");
		pFromCircle = connection.prepareStatement(FROM_CIRCLE);
		pFromFriend = connection.prepareStatement(FROM_FRIEND);
		pFromInstitution = connection.prepareStatement(FROM_INSTITUTION);
		pFromInterest = connection.prepareStatement(FROM_INTEREST);
		pFromPhoto = connection.prepareStatement(FROM_PHOTO);
		pFromProfessor = connection.prepareStatement(FROM_PROFESSOR);
		pFromRating = connection.prepareStatement(FROM_RATING);
		pFromStudent = connection.prepareStatement(FROM_STUDENT);
		pFromTag = connection.prepareCall(FROM_TAG);
		pFromUser = connection.prepareStatement(FROM_USER);
		pFromVisibility = connection.prepareStatement(FROM_VISIBILITY);
		
		File outFile = new File("data.xml");
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		PrintWriter out = new PrintWriter(outFile);
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<photodb>");
		rsProfessor = pFromProfessor.executeQuery();
		while (rsProfessor.next()) {
			out.println("\t<professor>");
			out.println("\t\t<id>"+rsProfessor.getInt("uid")+"</id>");
			pFromUser.setInt(1, rsProfessor.getInt("uid"));
			rsUser = pFromUser.executeQuery();
			rsUser.next();
			out.println("\t\t<firstName>"+rsUser.getString("firstname")+"</firstName>");
			out.println("\t\t<lastName>"+rsUser.getString("lastname")+"</lastName>");
			out.println("\t\t<email>"+rsUser.getString("email")+"</email>");
			out.println("\t\t<dob>"+rsUser.getString("dob")+"</dob>");
			out.println("\t\t<gender>"+rsUser.getString("gender")+"</gender>");
			out.println("\t\t<address>"+rsUser.getString("address")+"</address>");
			out.println("\t\t<type>"+rsUser.getString("type")+"</type>");
			out.println("\t\t<password>"+rsUser.getString("password")+"</password>");
			pFromInstitution.setInt(1, rsUser.getInt("uid"));
			rsInstitution = pFromInstitution.executeQuery();
			while (rsInstitution.next()) {
				out.println("\t\t<institution>"+rsInstitution.getString("insName")+"</institution>");
			}
			rsInstitution.close();
			pFromInterest.setInt(1, rsUser.getInt("uid"));
			rsInterest = pFromInterest.executeQuery();
			while (rsInterest.next()) {
				out.println("\t\t<interest>"+rsInterest.getString("interest")+"</interest>");
			}
			rsInterest.close();
			pFromPhoto.setInt(1, rsUser.getInt("uid"));
			rsPhoto = pFromPhoto.executeQuery();
			while (rsPhoto.next()) {
				out.println("\t\t<photo>");
				out.println("\t\t\t<photoid>"+rsPhoto.getInt("photoid")+"</photoid>");
				out.println("\t\t\t<url>"+rsPhoto.getString("url")+"</url>");
				pFromRating.setInt(1, rsPhoto.getInt("photoid"));
				rsRating = pFromRating.executeQuery();
				while (rsRating.next()) {
					out.println("\t\t\t<rating>");
					out.println("\t\t\t\t<rater>"+rsRating.getInt("uid")+"</rater>");
					out.println("\t\t\t\t<score>"+rsRating.getFloat("score")+"</score>");
					out.println("\t\t\t</rating>");
				}
				rsRating.close();
				pFromTag.setInt(1, rsPhoto.getInt("photoid"));
				rsTag = pFromTag.executeQuery();
				while (rsTag.next()) {
					out.println("\t\t\t<tag>"+rsTag.getString("tag")+"</tag>");
				}
				rsTag.close();
				pFromVisibility.setInt(1, rsPhoto.getInt("photoid"));
				rsVisibility = pFromVisibility.executeQuery();
				while (rsVisibility.next()) {
					out.println("\t\t\t<visibleTo>"+rsVisibility.getInt("uid")+"</visibleTo>");
				}
				rsVisibility.close();
				out.println("\t\t\t<uploadTime>"+sdf.format(rsPhoto.getTimestamp("uploadTime"))+"</uploadTime>");
				out.println("\t\t</photo>");
			}
			rsPhoto.close();
			pFromCircle.setInt(1, rsUser.getInt("uid"));
			rsCircle = pFromCircle.executeQuery();
			while (rsCircle.next()) {
				out.println("\t\t<circle>");
				out.println("\t\t\t<cid>"+rsCircle.getInt("cid")+"</cid>");
				out.println("\t\t\t<name>"+rsCircle.getString("name")+"</name>");
				pFromFriend.setInt(1, rsCircle.getInt("cid"));
				rsFriend = pFromFriend.executeQuery();
				while (rsFriend.next()) {
					out.println("\t\t\t<containsFriend>"+rsFriend.getInt("uid")+"</containsFriend>");
				}
				rsFriend.close();
				out.println("\t\t</circle>");
			}
			rsCircle.close();
			rsUser.close();
			out.println("\t\t<pid>"+rsProfessor.getString("pid")+"</pid>");
			out.println("\t\t<researchArea>"+rsProfessor.getString("researchArea")+"</researchArea>");
			out.println("\t\t<office>"+rsProfessor.getString("office")+"</office>");
			out.println("\t</professor>");
		}
		rsProfessor.close();
		
		rsStudent = pFromStudent.executeQuery();
		while (rsStudent.next()) {
			out.println("\t<student>");
			out.println("\t\t<id>"+rsStudent.getInt("uid")+"</id>");
			pFromUser.setInt(1, rsStudent.getInt("uid"));
			rsUser = pFromUser.executeQuery();
			rsUser.next();
			out.println("\t\t<firstName>"+rsUser.getString("firstname")+"</firstName>");
			out.println("\t\t<lastName>"+rsUser.getString("lastname")+"</lastName>");
			out.println("\t\t<email>"+rsUser.getString("email")+"</email>");
			out.println("\t\t<dob>"+rsUser.getString("dob")+"</dob>");
			out.println("\t\t<gender>"+rsUser.getString("gender")+"</gender>");
			out.println("\t\t<address>"+rsUser.getString("address")+"</address>");
			out.println("\t\t<type>"+rsUser.getString("type")+"</type>");
			out.println("\t\t<password>"+rsUser.getString("password")+"</password>");
			pFromInstitution.setInt(1, rsUser.getInt("uid"));
			rsInstitution = pFromInstitution.executeQuery();
			while (rsInstitution.next()) {
				out.println("\t\t<institution>"+rsInstitution.getString("insName")+"</institution>");
			}
			rsInstitution.close();
			pFromInterest.setInt(1, rsUser.getInt("uid"));
			rsInterest = pFromInterest.executeQuery();
			while (rsInterest.next()) {
				out.println("\t\t<interest>"+rsInterest.getString("interest")+"</interest>");
			}
			rsInterest.close();
			pFromPhoto.setInt(1, rsUser.getInt("uid"));
			rsPhoto = pFromPhoto.executeQuery();
			while (rsPhoto.next()) {
				out.println("\t\t<photo>");
				out.println("\t\t\t<photoid>"+rsPhoto.getInt("photoid")+"</photoid>");
				out.println("\t\t\t<url>"+rsPhoto.getString("url")+"</url>");
				pFromRating.setInt(1, rsPhoto.getInt("photoid"));
				rsRating = pFromRating.executeQuery();
				while (rsRating.next()) {
					out.println("\t\t\t<rating>");
					out.println("\t\t\t\t<rater>"+rsRating.getInt("uid")+"</rater>");
					out.println("\t\t\t\t<score>"+rsRating.getFloat("score")+"</score>");
					out.println("\t\t\t</rating>");
				}
				rsRating.close();
				pFromTag.setInt(1, rsPhoto.getInt("photoid"));
				rsTag = pFromTag.executeQuery();
				while (rsTag.next()) {
					out.println("\t\t\t<tag>"+rsTag.getString("tag")+"</tag>");
				}
				rsTag.close();
				pFromVisibility.setInt(1, rsPhoto.getInt("photoid"));
				rsVisibility = pFromVisibility.executeQuery();
				while (rsVisibility.next()) {
					out.println("\t\t\t<visibleTo>"+rsVisibility.getInt("uid")+"</visibleTo>");
				}
				rsVisibility.close();
				out.println("\t\t\t<uploadTime>"+sdf.format(rsPhoto.getTimestamp("uploadTime"))+"</uploadTime>");
				out.println("\t\t</photo>");
			}
			rsPhoto.close();
			pFromCircle.setInt(1, rsUser.getInt("uid"));
			rsCircle = pFromCircle.executeQuery();
			while (rsCircle.next()) {
				out.println("\t\t<circle>");
				out.println("\t\t\t<cid>"+rsCircle.getInt("cid")+"</cid>");
				out.println("\t\t\t<name>"+rsCircle.getString("name")+"</name>");
				pFromFriend.setInt(1, rsCircle.getInt("cid"));
				rsFriend = pFromFriend.executeQuery();
				while (rsFriend.next()) {
					out.println("\t\t\t<containsFriend>"+rsFriend.getInt("uid")+"</containsFriend>");
				}
				rsFriend.close();
				out.println("\t\t</circle>");
			}
			rsCircle.close();
			rsUser.close();
			out.println("\t\t<sid>"+rsStudent.getString("sid")+"</sid>");
			out.println("\t\t<major>"+rsStudent.getString("major")+"</major>");
			out.println("\t\t<gpa>"+rsStudent.getString("gpa")+"</gpa>");
			out.println("\t\t<advisor>"+rsStudent.getString("advisor")+"</advisor>");
			out.println("\t</student>");
		}
		out.println("</photodb>");
		out.flush();
		out.close();
		System.out.println("DONE");
	}

}
