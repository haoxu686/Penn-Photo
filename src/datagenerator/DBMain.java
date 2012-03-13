package datagenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DBMain {
	private static String ADD_CIRCLE = "INSERT INTO circle (cid, uid, name) VALUES (?, ?, ?)";
	private static String ADD_FRIEND = "INSERT INTO friend (cid, uid) VALUES (?, ?)";
	private static String ADD_INSTITUTION = "INSERT INTO institution (uid, insName) VALUES (?, ?)";
	private static String ADD_INTEREST = "INSERT INTO interest (uid, interest) VALUES (?, ?)";
	private static String ADD_PHOTO = "INSERT INTO photo (photoid, uid, url) VALUES (?, ?, ?)";
	private static String ADD_PROFESSOR = "INSERT INTO professor (pid, uid, researchArea, office) VALUES (?, ?, ?, ?)";
	private static String ADD_RATING = "INSERT INTO rating (photoid, uid, score) VALUES (?, ?, ?)";
	private static String ADD_STUDENT = "INSERT INTO student (sid, uid, major, gpa, advisor, yearsAdvised) VALUES (?, ?, ?, ?, ?, ?)";
	private static String ADD_TAG = "INSERT INTO tag (photoid, tag) VALUES (?, ?)";
	private static String ADD_USER = "INSERT INTO user (uid, firstname, lastname, email, dob, gender, address, type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static String ADD_VISIBILITY = "INSERT INTO visibility (photoid, uid) VALUES (?, ?)";
	
	private static PreparedStatement pAddCircle;
	private static PreparedStatement pAddFriend;
	private static PreparedStatement pAddInstitution;
	private static PreparedStatement pAddInterest;
	private static PreparedStatement pAddPhoto;
	private static PreparedStatement pAddProfessor;
	private static PreparedStatement pAddRating;
	private static PreparedStatement pAddStudent;
	private static PreparedStatement pAddTag;
	private static PreparedStatement pAddUser;
	private static PreparedStatement pAddVisibility;
	
	private static Statement init;
	
	private static String [] userFirstNames = {"", "Aaron", "Bob", "Chris", "Dave", "Edward", 
													"Frank", "George", "Helen", "Ian", "Jack", 
													"Katherine", "Luke", "Micheal", "Nimo", "Oliver", 
													"Peter", "Q", "Ross", "Steve", "Ted"};
	private static String [] userLastNames = {"", "Smith", "Johnson", "Williams", "Jones", "Brown", 
		 											"Davis", "Miller", "Wilson", "Moore", "Taylor", 
		 											"Anderson", "Thomas", "Jackson", "White", "Harris", 
		 											"Martin", "Thompson", "Garcia", "Martines", "Robinson"};
	private static String [] userDobs = {"", "1969-1-1", "1969-2-2", "1969-3-3", "1969-4-4", "1969-5-5", 
												"1969-6-6", "1969-7-7", "1969-8-8", "1969-9-9", "1969-10-10", 
												"1989-1-1", "1989-2-2", "1989-3-3", "1989-4-4", "1989-5-5", 
												"1989-6-6", "1989-7-7", "1989-8-8", "1989-9-9", "1989-10-10"};
	private static String [] researchAreas = {"", "Algorithm", "Machine Learing", "AI", "Database", "Architecture",
													"Network", "Data Mining", "Graphics", "OS", "Robotics"};
	private static String [] offices = {"", "LEVINE 501","LEVINE 502", "LEVINE 503","LEVINE 504","LEVINE 505",
											"LEVINE 506","LEVINE 507","LEVINE 508","LEVINE 509","LEVINE 510"};
	private static String [] addresses = {"", "3801 Walnut", "3802 Walnut", "3803 Walnut", "3804 Walnut", "3805 Walnut", 
												"3806 Walnut", "3807 Walnut", "3808 Walnut", "3809 Walnut", "3810 Walnut", 
												"3811 Walnut", "3812 Walnut", "3813 Walnut", "3814 Walnut", "3815 Walnut", 
												"3816 Walnut", "3817 Walnut", "3818 Walnut", "3819 Walnut", "3820 Walnut"};
	private static String [] institutions = {"Harvard", "Penn", "MIT", "CIT", "Princeton", 
												"Columbia", "Brown", "Cornell", "Stanford", "John Hopkins", 
												"NYU", "Berkely", "UCLA", "USC", "Dartmouth", 
												"Yale", "Purdue", "UIUC", "Texus Austin", "Drexel"};
	private static String [] interests = {"football", "basketball", "movie", "music", "travel", 
											"reading", "swimming", "badminton", "cyber games", "baseball", 
											"hockey", "clubbing", "chatting", "cartons", "other"};
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		File urlFile = new File("url.txt");
		BufferedReader reader = new BufferedReader(new FileReader(urlFile));
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://fling.seas.upenn.edu:3306/haoxu";
		Connection connection = DriverManager.getConnection(url, "haoxu", "QwerAsdf");
		pAddCircle = connection.prepareStatement(ADD_CIRCLE);
		pAddFriend = connection.prepareStatement(ADD_FRIEND);
		pAddInstitution = connection.prepareStatement(ADD_INSTITUTION);
		pAddInterest = connection.prepareStatement(ADD_INTEREST);
		pAddPhoto = connection.prepareStatement(ADD_PHOTO);
		pAddProfessor = connection.prepareStatement(ADD_PROFESSOR);
		pAddRating = connection.prepareStatement(ADD_RATING);
		pAddStudent = connection.prepareStatement(ADD_STUDENT);
		pAddTag = connection.prepareStatement(ADD_TAG);
		pAddUser = connection.prepareStatement(ADD_USER);
		pAddVisibility = connection.prepareStatement(ADD_VISIBILITY);
		
		init = connection.createStatement();
		init.executeUpdate("DELETE FROM circle");
		init.executeUpdate("DELETE FROM friend");
		init.executeUpdate("DELETE FROM institution");
		init.executeUpdate("DELETE FROM interest");
		init.executeUpdate("DELETE FROM photo");
		init.executeUpdate("DELETE FROM professor");
		init.executeUpdate("DELETE FROM rating");
		init.executeUpdate("DELETE FROM student");
		init.executeUpdate("DELETE FROM tag");
		init.executeUpdate("DELETE FROM user");
		init.executeUpdate("DELETE FROM visibility");
		
		int cid, photoid, friendid;
		int x, y;
		double random;
		cid = 20001;
		photoid = 20001;
		for (int i = 1; i <= 20; i++) {
			pAddUser.setInt(1, 20000+i);
			pAddUser.setString(2, userFirstNames[i]);
			pAddUser.setString(3, userLastNames[i]);	
			pAddUser.setString(4, userFirstNames[i]+"@seas.upenn.edu");
			pAddUser.setString(5, userDobs[i]);
			pAddUser.setString(6, "M");
			pAddUser.setString(7, addresses[i]);
			if (i <= 10) {
				pAddUser.setString(8, "P");
			} else {
				pAddUser.setString(8, "S");
			}
			pAddUser.setString(9, "123456");
			pAddUser.executeUpdate();
			
			random = Math.random();
			x = (int) (random*100);
			x %= 20;
			random = Math.random();
			y = (int) (random*100);
			y %= 20;
			while (y == x) {
				random = Math.random();
				y = (int) (random*100);
				y %= 20;
			}
			pAddInstitution.setInt(1, 20000+i);
			pAddInstitution.setString(2, institutions[x]);
			pAddInstitution.executeUpdate();
			pAddInstitution.setInt(1, 20000+i);
			pAddInstitution.setString(2, institutions[y]);
			pAddInstitution.executeUpdate();
			
			random = Math.random();
			x = (int) (random*100);
			x %= 15;
			random = Math.random();
			y = (int) (random*100);
			y %= 15;
			while (y == x) {
				random = Math.random();
				y = (int) (random*100);
				y %= 15;
			}
			pAddInterest.setInt(1, 20000+i);
			pAddInterest.setString(2, interests[x]);
			pAddInterest.executeUpdate();
			pAddInterest.setInt(1, 20000+i);
			pAddInterest.setString(2, interests[y]);
			pAddInterest.executeUpdate();
			
			for (int j = 0; j < 5; j++) {
				pAddPhoto.setInt(1, photoid);
				pAddPhoto.setInt(2, 20000+i);
				pAddPhoto.setString(3, reader.readLine());
				pAddPhoto.executeUpdate();
				
				pAddTag.setInt(1, photoid);
				pAddTag.setString(2, "test");
				pAddTag.executeUpdate();
				
				random = Math.random();
				x = (int) (random*100);
				x = x%3+1;
				friendid = i-x;
				if (friendid <= 0) {
					friendid = 20+friendid;
				}
				random = Math.random();
				y = (int) (random*100);
				y = y%5+1;
				pAddRating.setInt(1, photoid);
				pAddRating.setInt(2, 20000+friendid);
				pAddRating.setFloat(3, y);
				pAddRating.executeUpdate();
				
				friendid = i+x;
				if (friendid > 20) {
					friendid -= 20;
				}
				random = Math.random();
				y = (int) (random*100);
				y = y%5+1;
				pAddRating.setInt(1, photoid);
				pAddRating.setInt(2, 20000+friendid);
				pAddRating.setFloat(3, y);
				pAddRating.executeUpdate();
				
				photoid++;
			}
			
			pAddCircle.setInt(1, cid);
			pAddCircle.setInt(2, 20000+i);
			pAddCircle.setString(3, userFirstNames[i]);
			pAddCircle.executeUpdate();
			for (int j = 1; j <= 3; j++) {
				friendid = i-j;
				if (friendid <= 0) {
					friendid = 20+friendid;
				}
				pAddFriend.setInt(1, cid);
				pAddFriend.setInt(2, 20000+friendid);
				pAddFriend.executeUpdate();
				
				for (int k = 1; k <= 5; k++) {
					pAddVisibility.setInt(1, photoid-k);
					pAddVisibility.setInt(2, 20000+friendid);
					pAddVisibility.executeUpdate();
				}
			}
			cid++;
			
			pAddCircle.setInt(1, cid);
			pAddCircle.setInt(2, 20000+i);
			pAddCircle.setString(3, userLastNames[i]);
			pAddCircle.executeUpdate();
			for (int j = 1; j <= 3; j++) {
				friendid = i+j;
				if (friendid > 20) {
					friendid -= 20;
				}
				pAddFriend.setInt(1, cid);
				pAddFriend.setInt(2, 20000+friendid);
				pAddFriend.executeUpdate();
				
				for (int k = 1; k <= 5; k++) {
					pAddVisibility.setInt(1, photoid-k);
					pAddVisibility.setInt(2, 20000+friendid);
					pAddVisibility.executeUpdate();
				}
			}
			cid++;
			for (int k = 1; k <= 5; k++) {
				pAddVisibility.setInt(1, photoid-k);
				pAddVisibility.setInt(2, 20000+i);
				pAddVisibility.executeUpdate();
			}
			
		}
		for (int i = 1; i <= 10; i++) {
			pAddProfessor.setString(1, "staff"+String.valueOf(20000+i));
			pAddProfessor.setInt(2, 20000+i);
			pAddProfessor.setString(3, researchAreas[i]);
			pAddProfessor.setString(4, offices[i]);
			pAddProfessor.executeUpdate();
		}
		for (int i = 11; i <= 20; i++) {
			pAddStudent.setString(1, "stu"+String.valueOf(20000+i));
			pAddStudent.setInt(2, 20000+i);
			pAddStudent.setString(3, "CIS");
			random = Math.random();
			x = (int) (random*100);
			x %= 10;
			random = x;
			random /= 10;
			random += 3;
			pAddStudent.setFloat(4, (float) random);
			random = Math.random();
			x = (int) (random*100);
			x = x%10+1;
			pAddStudent.setString(5, "staff"+String.valueOf(20000+x));
			pAddStudent.setInt(6, 1);
			pAddStudent.executeUpdate();
		}
		System.out.println("DONE");
		connection.close();
	}

}
