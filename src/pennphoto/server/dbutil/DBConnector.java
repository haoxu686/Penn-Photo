package pennphoto.server.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import pennphoto.server.DoubleRating;
import pennphoto.server.Recommendation;
import pennphoto.shared.Circle;
import pennphoto.shared.Friend;
import pennphoto.shared.Institution;
import pennphoto.shared.Interest;
import pennphoto.shared.Photo;
import pennphoto.shared.Professor;
import pennphoto.shared.Rating;
import pennphoto.shared.Student;
import pennphoto.shared.Tag;
import pennphoto.shared.User;
import pennphoto.shared.Visibility;


public class DBConnector {
	
	/**Self defined queries go here.
	 * Note that a pair of query String and PrepareStatement
	 * should be created at the same time
	 */
	private final String ADD_CIRCLE = "INSERT INTO circle (cid, uid, name) VALUES (?, ?, ?)";
	private final String ADD_FRIEND = "INSERT INTO friend (cid, uid) VALUES (?, ?)";
	private final String ADD_INSTITUTION = "INSERT INTO institution (uid, insName) VALUES (?, ?)";
	private final String ADD_INTEREST = "INSERT INTO interest (uid, interest) VALUES (?, ?)";
	private final String ADD_PHOTO = "INSERT INTO photo (photoid, uid, url) VALUES (?, ?, ?)";
	private final String ADD_PROFESSOR = "INSERT INTO professor (pid, uid, researchArea, office) VALUES (?, ?, ?, ?)";
	private final String ADD_RATING = "INSERT INTO rating (photoid, uid, score) VALUES (?, ?, ?)";
	private final String UPDATE_RATING = "UPDATE rating SET score = ? WHERE photoid = ? AND uid = ?";
	private final String ADD_STUDENT = "INSERT INTO student (sid, uid, major, gpa, advisor, yearsAdvised) VALUES (?, ?, ?, ?, ?, ?)";
	private final String ADD_TAG = "INSERT INTO tag (photoid, tag) VALUES (?, ?)";
	private final String ADD_USER = "INSERT INTO user (uid, firstname, lastname, email, dob, gender, address, type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final String ADD_VISIBILITY = "INSERT INTO visibility (photoid, uid) VALUES (?, ?)";
	
	private final String GET_USER = "SELECT * FROM user WHERE uid = ?";
	private final String GET_CIRCLES = "SELECT * FROM circle WHERE uid = ?";
	private final String GET_FRIENDS_IN_CIRCLE = "SELECT U.uid AS uid, firstName, lastName FROM friend F, user U WHERE cid = ? AND F.uid = U.uid";
	private final String GET_TOP10_PHOTOS = 
		"SELECT photoid, url FROM " +
		"(SELECT AVG(score) AS score, P.photoid AS photoid, url FROM " +
		"((SELECT photoid, url FROM photo WHERE uid = ?) UNION (SELECT Ph.photoid, url FROM photo Ph, visibility V WHERE Ph.photoid  = V.photoid AND (V.uid = ? OR V.uid = 0))) P, rating R " +
		"WHERE P.photoid = R.photoid GROUP BY P.photoid) T " +
		"ORDER BY score DESC LIMIT 10";
	private final String GET_RATINGS = "SELECT * FROM rating WHERE photoid = ?";
	private final String GET_RATING = "SELECT * FROM rating WHERE photoid = ? AND uid = ?";
	private final String GET_TAGS = "SELECT * FROM tag WHERE photoid = ?";
	private final String GET_TAG = "SELECT * FROM tag WHERE photoid = ? AND tag = ?";
	private final String GET_FRIENDS = "SELECT DISTINCT U.uid AS uid, firstName, lastName, type FROM circle C, friend F, user U WHERE C.cid = F.cid AND F.uid = U.uid AND C.uid = ?";
	private final String GET_PASSWORD = "SELECT password FROM user WHERE uid = ?";
	private final String GET_PROFESSOR = "SELECT * FROM professor WHERE uid = ?";
	private final String GET_STUDENT = "SELECT * FROM student WHERE uid = ?";
	private final String GET_ADVISEES = "SELECT U.uid AS uid, sid, firstName, lastName FROM user U, student S WHERE U.uid = S.uid AND S.advisor = ?";
	private final String GET_ADVISOR = "SELECT U.uid AS uid, pid, firstName, lastName FROM user U, professor P WHERE U.uid = P.uid AND P.pid = ?";
	private final String GET_NEWS = "SELECT P.photoid, P.uid, P.url, P.uploadTime " +
			"from user U, circle C, friend F, photo P, visibility V " +
			"where U.uid = C.uid and C.cid = F.cid and F.uid = P.uid and P.photoid = V.photoid and (V.uid = U.uid OR V.uid = 0) and U.uid = ? " +
			"order by P.uploadTime DESC limit 10";
	private final String GET_PHOTOS_OF_USER = "SELECT P.photoid AS photoid, P.uid AS uid, P.url AS url FROM photo P, visibility V WHERE P.photoid = V.photoid AND P.uid = ? AND (V.uid = ? OR V.uid = 0)";
	private final String GET_PROFESSORS = "SELECT *, p.pid pid from user u, professor p where u.type='P' and u.uid=p.uid";
	private final String GET_PHOTOS = "SELECT * FROM photo WHERE uid = ?";
	private final String GET_INSTITUTIONS = "SELECT * FROM institution WHERE uid = ?";
	private final String GET_INTERESTS = "SELECT * FROM interest WHERE uid = ?";
	private final String UPDATE_PASSWORD = "UPDATE user SET password=? WHERE uid = ?";
	private final String UPDATE_USER = "UPDATE user SET email=?, gender=?, address=?, password = ? WHERE uid = ?";
	private final String UPDATE_PROFESSOR = "UPDATE professor SET researchArea=?, office=? WHERE uid = ?";
	private final String UPDATE_STUDENT = "UPDATE student SET major=?, gpa=? WHERE uid = ?";
	
	private final String DELETE_FRIEND = "DELETE FROM friend WHERE uid = ? AND cid IN (SELECT cid FROM circle WHERE uid = ?)";
	private final String DELETE_PHOTO = "DELETE FROM photo WHERE photoid = ?";
	private final String DELETE_TAG = "DELETE FROM tag WHERE photoid = ?";
	private final String DELETE_RATING = "DELETE FROM rating WHERE photoid = ?";
	private final String DELETE_VISIBILITY = "DELETE FROM visibility WHERE photoid = ?";
	private final String DELETE_CIRCLE = "DELETE FROM circle WHERE cid = ?";
	private final String DELETE_FRIEND_FROM_CIRCLE = "DELETE FROM friend WHERE cid = ?";
	
	private PreparedStatement psAddCircle;
	private PreparedStatement psAddFriend;
	private PreparedStatement psAddInstitution;
	private PreparedStatement psAddInterest;
	private PreparedStatement psAddPhoto;
	private PreparedStatement psAddProfessor;
	private PreparedStatement psAddRating;
	private PreparedStatement psUpdateRating;
	private PreparedStatement psAddStudent;
	private PreparedStatement psAddTag;
	private PreparedStatement psAddUser;
	private PreparedStatement psAddVisibility;
	
	private PreparedStatement psGetUser;
	private PreparedStatement psGetCircles;
	private PreparedStatement psGetFriendsInCircle;
	private PreparedStatement psGetTop10Photos;
	private PreparedStatement psGetRatings;
	private PreparedStatement psGetRating;
	private PreparedStatement psGetTags;
	private PreparedStatement psGetTag;
	private PreparedStatement psGetFriends;
	private PreparedStatement psGetPassword;
	private PreparedStatement psGetProfessor;
	private PreparedStatement psGetStudent;
	private PreparedStatement psGetAdvisees;
	private PreparedStatement psGetAdvisor;
	private PreparedStatement psGetNews;
	private PreparedStatement psGetPhotosOfUser;
	private PreparedStatement psGetProfessors;
	private PreparedStatement psUpdatePassword;
	private PreparedStatement psGetPhotos;
	private PreparedStatement psGetInstitutions;
	private PreparedStatement psGetInterests;
	private PreparedStatement psUpdateUser;
	private PreparedStatement psUpdateProfessor;
	private PreparedStatement psUpdateStudent;
	
	private PreparedStatement psDeleteFriend;
	private PreparedStatement psDeletePhoto;
	private PreparedStatement psDeleteTag;
	private PreparedStatement psDeleteRating;
	private PreparedStatement psDeleteVisibility;
	private PreparedStatement psDeleteCircle;
	private PreparedStatement psDeleteFriendFromCircle;
	
	private static int photoId;
	private static int userId;
	private static int circleId;
	private int index;
	private Connection connection;
	protected DBConnector(int index) {
		this.index = index;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/haoxu";
			connection = DriverManager.getConnection(url, "root", "cis550root");
			
			/**PrepareStatement initialization go here*/
			psAddCircle = connection.prepareStatement(ADD_CIRCLE);
			psAddFriend = connection.prepareStatement(ADD_FRIEND);
			psAddInstitution = connection.prepareStatement(ADD_INSTITUTION);
			psAddInterest = connection.prepareStatement(ADD_INTEREST);
			psAddPhoto = connection.prepareStatement(ADD_PHOTO);
			psAddProfessor = connection.prepareStatement(ADD_PROFESSOR);
			psAddRating = connection.prepareStatement(ADD_RATING);
			psUpdateRating = connection.prepareStatement(UPDATE_RATING);
			psAddStudent = connection.prepareStatement(ADD_STUDENT);
			psAddTag = connection.prepareStatement(ADD_TAG);
			psAddUser = connection.prepareStatement(ADD_USER);
			psAddVisibility = connection.prepareStatement(ADD_VISIBILITY);
			
			psGetUser = connection.prepareStatement(GET_USER);
			psGetCircles = connection.prepareCall(GET_CIRCLES);
			psGetFriendsInCircle = connection.prepareStatement(GET_FRIENDS_IN_CIRCLE);
			psGetTop10Photos = connection.prepareStatement(GET_TOP10_PHOTOS);
			psGetRatings = connection.prepareStatement(GET_RATINGS);
			psGetRating = connection.prepareStatement(GET_RATING);
			psGetTags = connection.prepareStatement(GET_TAGS);
			psGetTag = connection.prepareStatement(GET_TAG);
			psGetFriends = connection.prepareStatement(GET_FRIENDS);
			psGetPassword = connection.prepareStatement(GET_PASSWORD);
			psGetProfessor = connection.prepareStatement(GET_PROFESSOR);
			psGetStudent = connection.prepareStatement(GET_STUDENT);
			psGetAdvisees = connection.prepareStatement(GET_ADVISEES);
			psGetAdvisor = connection.prepareStatement(GET_ADVISOR);
			psGetNews = connection.prepareStatement(GET_NEWS);
			psGetPhotosOfUser = connection.prepareStatement(GET_PHOTOS_OF_USER);
			psGetProfessors = connection.prepareStatement(GET_PROFESSORS);
			psGetInstitutions = connection.prepareCall(GET_INSTITUTIONS);
			psGetInterests = connection.prepareCall(GET_INTERESTS);
			psUpdatePassword = connection.prepareStatement(UPDATE_PASSWORD);
			psGetPhotos = connection.prepareStatement(GET_PHOTOS);
			psUpdateUser = connection.prepareStatement(UPDATE_USER);
			psUpdateProfessor = connection.prepareStatement(UPDATE_PROFESSOR);
			psUpdateStudent = connection.prepareStatement(UPDATE_STUDENT);
			
			psDeleteFriend = connection.prepareStatement(DELETE_FRIEND);
			psDeletePhoto = connection.prepareStatement(DELETE_PHOTO);
			psDeleteTag = connection.prepareStatement(DELETE_TAG);
			psDeleteRating = connection.prepareStatement(DELETE_RATING);
			psDeleteVisibility = connection.prepareStatement(DELETE_VISIBILITY);
			psDeleteCircle = connection.prepareStatement(DELETE_CIRCLE);
			psDeleteFriendFromCircle = connection.prepareStatement(DELETE_FRIEND_FROM_CIRCLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**Do not change*/
	protected static void setMaxPhotoId(int id) {
		photoId = id;
	}
	
	/**Do not change*/
	protected static void setMaxUserId(int id) {
		userId = id;
	}
	
	protected static void setMaxCircleId(int id) {
		circleId = id;
	}
	
	/**This function allocates id for newly added user
	 * Do not change
	 */
	private static synchronized int allocateUserId() {
		userId++;
		return userId;
	}
	
	/**This function allocates id for newly added photo
	 * Do not change
	 */
	private static synchronized int allocatePhotoId() {
		photoId++;
		return photoId;
	}
	
	private static synchronized int allocateCircleId() {
		circleId++;
		return circleId;
	}
	
	/**Self defined methods go here*/
	public int addCircle(Circle circle) {
		circle.setCircleId(allocateCircleId());
		try {
			psAddCircle.setInt(1, circle.getCircleId());
			psAddCircle.setInt(2, circle.getUserId());
			psAddCircle.setString(3, circle.getName());
			psAddCircle.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return circle.getCircleId();
	}
	
	public void addFriendToCircle(Friend friend) {
		try {
			psAddFriend.setInt(1, friend.getCircleId());
			psAddFriend.setInt(2, friend.getFriendId());
			psAddFriend.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addInstitution(Institution institution) {
		try {
			psAddInstitution.setInt(1, institution.getUserId());
			psAddInstitution.setString(2, institution.getInstitutionName());
			psAddInstitution.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addInterest(Interest interest) {
		try {
			psAddInterest.setInt(1, interest.getUserId());
			psAddInterest.setString(2, interest.getInterest());
			psAddInterest.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addPhoto(Photo photo) {
		try {
			photo.setPhotoId(allocatePhotoId());
			psAddPhoto.setInt(1, photo.getPhotoId());
			psAddPhoto.setInt(2, photo.getOwner().getUserId());
			psAddPhoto.setString(3, photo.getUrl());
			psAddPhoto.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return photo.getPhotoId();
	}
	
	public int addProfessor(Professor professor) {
		professor.setUserId(allocateUserId());
		professor.setStaffId("staff"+professor.getUserId());
		try {
			psAddUser.setInt(1, professor.getUserId());
			psAddUser.setString(2, professor.getFirstname());
			psAddUser.setString(3, professor.getLastName());
			psAddUser.setString(4, professor.getEmail());
			psAddUser.setString(5, professor.getDob());
			psAddUser.setString(6, professor.getGender());
			psAddUser.setString(7, professor.getAddress());
			psAddUser.setString(8, professor.getType());
			psAddUser.setString(9, professor.getPassword());
			psAddUser.executeUpdate();
			
			psAddProfessor.setString(1, professor.getStaffId());
			psAddProfessor.setInt(2, professor.getUserId());
			psAddProfessor.setString(3, professor.getResearchArea());
			psAddProfessor.setString(4, professor.getOffice());
			psAddProfessor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return professor.getUserId();
	}
	
	public void addRating(Rating rating) {
		try {
			psGetRating.setInt(1, rating.getRaterId());
			psGetRating.setInt(2, rating.getRaterId());
			ResultSet rs = psGetRating.executeQuery();
			if (rs.next()) {
				this.updateRating(rating);
				
			} else {
				psAddRating.setInt(1, rating.getPhotoId());
				psAddRating.setInt(2, rating.getRaterId());
				psAddRating.setFloat(3, rating.getScore());
				psAddRating.executeUpdate();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateRating(Rating rating) {
		try {
			psUpdateRating.setFloat(1, rating.getScore());
			psUpdateRating.setInt(2, rating.getPhotoId());
			psUpdateRating.setInt(3, rating.getRaterId());
			psUpdateRating.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addStudent(Student student) {
		student.setUserId(allocateUserId());
		student.setStuId("stu"+student.getUserId());
		try {
			psAddUser.setInt(1, student.getUserId());
			psAddUser.setString(2, student.getFirstname());
			psAddUser.setString(3, student.getLastName());
			psAddUser.setString(4, student.getEmail());
			psAddUser.setString(5, student.getDob());
			psAddUser.setString(6, student.getGender());
			psAddUser.setString(7, student.getAddress());
			psAddUser.setString(8, student.getType());
			psAddUser.setString(9, student.getPassword());
			psAddUser.executeUpdate();
			
			psAddStudent.setString(1, student.getStuId());
			psAddStudent.setInt(2, student.getUserId());
			psAddStudent.setString(3, student.getMajor());
			psAddStudent.setFloat(4, student.getGpa());
			psAddStudent.setString(5, student.getAdvisor().getStaffId());
			psAddStudent.setInt(6, student.getYearsAdvised());
			psAddStudent.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student.getUserId();
	}
	
	public void addTag(Tag tag) {
		try {
			psGetTag.setInt(1, tag.getPhotoId());
			psGetTag.setString(2, tag.getTag());
			ResultSet rs = psGetTag.executeQuery();
			if (!rs.next()) {
				psAddTag.setInt(1, tag.getPhotoId());
				psAddTag.setString(2, tag.getTag());
				psAddTag.executeUpdate();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addVisibility(Visibility visibility) {	
		try {
			psAddVisibility.setInt(1, visibility.getPhotoId());
			psAddVisibility.setInt(2, visibility.getUserId());
			psAddVisibility.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public User getUser(int userId) {
		User user = null;
		try {
			psGetUser.setInt(1, userId);
			ResultSet rs = psGetUser.executeQuery();
			rs.next();
			String type = rs.getString("type");
			if (type.equals("P")) {
				Professor professor = this.getProfessor(userId);
				professor.setUserId(rs.getInt("uid"));
				professor.setFirstname(rs.getString("firstName"));
				professor.setLastName(rs.getString("lastName"));
				professor.setEmail(rs.getString("email"));
				professor.setDob(rs.getString("dob"));
				professor.setGender(rs.getString("gender"));
				professor.setAddress(rs.getString("address"));
				professor.setType(rs.getString("type"));
				professor.setPassword(rs.getString("password"));
				user = professor;
			} else if (type.equals("S")) {
				Student student = this.getStudent(userId);
				student.setUserId(rs.getInt("uid"));
				student.setFirstname(rs.getString("firstName"));
				student.setLastName(rs.getString("lastName"));
				student.setEmail(rs.getString("email"));
				student.setDob(rs.getString("dob"));
				student.setGender(rs.getString("gender"));
				student.setAddress(rs.getString("address"));
				student.setType(rs.getString("type"));
				student.setPassword(rs.getString("password"));
				user = student;
			} else {
				user = new User();
				user.setUserId(rs.getInt("uid"));
				user.setFirstname(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setEmail(rs.getString("email"));
				user.setDob(rs.getString("dob"));
				user.setGender(rs.getString("gender"));
				user.setAddress(rs.getString("address"));
				user.setType(rs.getString("type"));
				user.setPassword(rs.getString("password"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public ArrayList<Circle> getCircles(int userId) {
		ArrayList<Circle> result = new ArrayList<Circle>(5);
		try {
			psGetCircles.setInt(1, userId);
			ResultSet rs = psGetCircles.executeQuery();
			while (rs.next()) {
				Circle circle = new Circle();
				circle.setCircleId(rs.getInt("cid"));
				circle.setUserId(userId);
				circle.setName(rs.getString("name"));
				result.add(circle);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<User> getFriendsInCircle(int circleId) {
		ArrayList<User> result = new ArrayList<User>(10);
		try {
			psGetFriendsInCircle.setInt(1, circleId);
			ResultSet rs = psGetFriendsInCircle.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("uid"));
				user.setFirstname(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				result.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Photo> getTop10Photos(int userId) {
		ArrayList<Photo> result = new ArrayList<Photo>(10);
		try {
			psGetTop10Photos.setInt(1, userId);
			psGetTop10Photos.setInt(2, userId);
			ResultSet rs = psGetTop10Photos.executeQuery();
			while (rs.next()) {
				Photo photo = new Photo();
				photo.setPhotoId(rs.getInt("photoid"));
				photo.setUrl(rs.getString("url"));
				result.add(photo);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Rating> getRatings(int photoid) {
		ArrayList<Rating> result = new ArrayList<Rating>();
		try {
			psGetRatings.setInt(1, photoid);
			ResultSet rs = psGetRatings.executeQuery();
			while (rs.next()) {
				Rating rating = new Rating();
				rating.setRaterId(rs.getInt("uid"));
				rating.setScore(rs.getFloat("score"));
				result.add(rating);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<String> getTags(int photoid) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			psGetTags.setInt(1, photoid);
			ResultSet rs = psGetTags.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("tag").toLowerCase());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<User> getFriends(int userId) {
		ArrayList<User> result = new ArrayList<User>();
		try {
			psGetFriends.setInt(1, userId);
			ResultSet rs = psGetFriends.executeQuery();
			while (rs.next()) {
				String type = rs.getString("type");
				User user = null;;
				if (type.equals("P")) {
					user = new Professor();
					user.setType("P");
				} else if (type.equals("S")) {
					user = new Student();
					user.setType("S");
				} else {
					user = new User();
				}
				user.setUserId(rs.getInt("uid"));
				user.setFirstname(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				result.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getPassword(int userID2) {
		String result="";
		try{
			psGetPassword.setInt(1, userID2);
			ResultSet rs = psGetPassword.executeQuery();
			while(rs.next()){
				result = rs.getString("password");
			}
			rs.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	private Professor getProfessor(int userId) {
		Professor professor = new Professor();
		try {
			psGetProfessor.setInt(1, userId);
			ResultSet rs = psGetProfessor.executeQuery();
			rs.next();
			professor.setStaffId(rs.getString("pid"));
			professor.setResearchArea(rs.getString("researchArea"));
			professor.setOffice(rs.getString("office"));
			rs.close();
			ArrayList<Student> advisees = this.getAdvisees(professor.getStaffId());
			professor.setAdvisees(advisees);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return professor;
	}
	
	private Student getStudent(int userId) {
		Student student = new Student();
		try {
			psGetStudent.setInt(1, userId);
			ResultSet rs = psGetStudent.executeQuery();
			rs.next();
			student.setStuId(rs.getString("sid"));
			student.setMajor(rs.getString("major"));
			student.setGpa(rs.getFloat("gpa"));
			Professor advisor = this.getAdvisor(rs.getString("advisor"));
			student.setAdvisor(advisor);
			student.setYearsAdvised(rs.getInt("yearsAdvised"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}
	
	private ArrayList<Student> getAdvisees(String staffId) {
		ArrayList<Student> result = new ArrayList<Student>();
		try {
			psGetAdvisees.setString(1, staffId);
			ResultSet rs = psGetAdvisees.executeQuery();
			while (rs.next()) {
				Student student = new Student();
				student.setUserId(rs.getInt("uid"));
				student.setStuId(rs.getString("sid"));
				student.setFirstname(rs.getString("firstName"));
				student.setLastName(rs.getString("lastName"));
				result.add(student);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private Professor getAdvisor(String staffId) {
		Professor professor = new Professor();
		try {
			psGetAdvisor.setString(1, staffId);
			ResultSet rs = psGetAdvisor.executeQuery();
			rs.next();
			professor.setUserId(rs.getInt("uid"));
			professor.setStaffId(rs.getString("pid"));
			professor.setFirstname(rs.getString("firstName"));
			professor.setLastName(rs.getString("lastName"));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return professor;
	}
	
	public ArrayList<Integer> searchUserByCriteria(String sql) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				result.add(rs.getInt("uid"));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Photo> searchPhotoByCriteria(String sql) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Photo photo = new Photo();
				photo.setPhotoId(rs.getInt("photoid"));
				User owner = this.getUser(rs.getInt("uid"));
				photo.setOwner(owner);
				photo.setUrl(rs.getString("url"));
				result.add(photo);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void release() {
		ConnectionManager.release(index);
	}
	
	public ArrayList<Photo> getNewPhotos(int userID2) {
		ArrayList<Photo> photos=new ArrayList<Photo>();
		try {
			psGetNews.setInt(1, userID2);
			ResultSet rs = psGetNews.executeQuery();
			while(rs.next()){
				Photo photo = new Photo();
				photo.setPhotoId(rs.getInt("photoid"));
				User owner = this.getUser(rs.getInt("uid"));
				photo.setOwner(owner);
				photo.setUrl(rs.getString("url"));
				photo.setTimestamp(rs.getTimestamp("uploadTime"));
				photos.add(photo);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return photos;
	}

	public ArrayList<Photo> getPhotosOfUser(int ownerId, int viewerId) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		try {
			psGetPhotosOfUser.setInt(1, ownerId);
			psGetPhotosOfUser.setInt(2, viewerId);
			ResultSet rs = psGetPhotosOfUser.executeQuery();
			while (rs.next()) {
				Photo photo = new Photo();
				photo.setPhotoId(rs.getInt("photoid"));
				User owner = this.getUser(rs.getInt("uid"));
				photo.setOwner(owner);
				photo.setUrl(rs.getString("url"));
				result.add(photo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Professor> getProfessors() {
		ArrayList<Professor> profs = new ArrayList<Professor>();
		try {
			ResultSet rs = psGetProfessors.executeQuery();
			while(rs.next()){
				Professor p = new Professor();
				p.setFirstname(rs.getString("firstname"));
				p.setLastName(rs.getString("lastname"));
				p.setUserId(rs.getInt("uid"));
				p.setStaffId(rs.getString("pid"));
				profs.add(p);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return profs;
	}
	
	public ArrayList<Photo> getPhotos(int userId) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		try {
			psGetPhotos.setInt(1, userId);
			ResultSet rs = psGetPhotos.executeQuery();
			while (rs.next()) {
				Photo photo = new Photo();
				photo.setPhotoId(rs.getInt("photoid"));
				photo.setUrl(rs.getString("url"));
				User user = this.getUser(rs.getInt("uid"));
				photo.setOwner(user);
				result.add(photo);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateUser(User user){
		try {
			psUpdateUser.setString(1, user.getEmail());
			psUpdateUser.setString(2, user.getGender());
			psUpdateUser.setString(3, user.getAddress());
			psUpdateUser.setString(4, user.getPassword());
			psUpdateUser.setInt(5, user.getUserId());			
			psUpdateUser.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProfessor(Professor professor) {
		try {
			psUpdateProfessor.setString(1, professor.getResearchArea());
			psUpdateProfessor.setString(2, professor.getOffice());
			psUpdateProfessor.setInt(3, professor.getUserId());
			psUpdateProfessor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePassword(int userId, String Password){
		try {
			psUpdatePassword.setInt(1, userId);
			psUpdatePassword.setString(2, Password);
			psUpdatePassword.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFriend(int userId, int friendId){
		try {
			psDeleteFriend.setInt(1, friendId);
			psDeleteFriend.setInt(2, userId);
			psDeleteFriend.executeUpdate();
			psDeleteFriend.setInt(1, userId);
			psDeleteFriend.setInt(2, friendId);
			psDeleteFriend.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePhoto(Photo photo){
		try {
			psDeleteTag.setInt(1, photo.getPhotoId());
			psDeleteTag.executeUpdate();
			psDeleteRating.setInt(1, photo.getPhotoId());
			psDeleteRating.executeUpdate();
			psDeleteVisibility.setInt(1, photo.getPhotoId());
			psDeleteVisibility.executeUpdate();
			psDeletePhoto.setInt(1, photo.getPhotoId());
			psDeletePhoto.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void deleteCircle(Circle circle){
		try {
			psDeleteFriendFromCircle.setInt(1, circle.getCircleId());
			psDeleteFriendFromCircle.executeUpdate();
			psDeleteCircle.setInt(1, circle.getCircleId());
			psDeleteCircle.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateStudent(Student student) {
		try {
			psUpdateStudent.setString(1, student.getMajor());
			psUpdateStudent.setFloat(2, student.getGpa());
			psUpdateStudent.setInt(3, student.getUserId());
			psUpdateStudent.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Recommendation> getStrangers(int userId) {
		ArrayList<Recommendation> result = new ArrayList<Recommendation>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT uid FROM user WHERE uid <> " + userId + " AND uid NOT IN (" + 
							"SELECT DISTINCT F.uid FROM circle C, friend F " +
							"WHERE C.cid = F.cid AND C.uid = " + userId + ")";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Recommendation r = new Recommendation();
				r.setUserId(rs.getInt("uid"));
				result.add(r);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getMutualFriendNumber(int userId1, int userId2) {
		int result = 0;
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT count(DISTINCT F1.uid) AS number FROM circle C1, friend F1, circle C2, friend F2 " +
					"WHERE C1.cid = F1.cid AND F1.uid = C2.uid AND C2.cid = F2.cid AND " +
					"C1.uid = " + userId1 + " AND F2.uid = " + userId2;
			ResultSet rs = statement.executeQuery(sql);
			rs.next();
			result = rs.getInt("number");
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getMutualInstitutionNumber(int userId1, int userId2) {
		int result = 0;
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT count(*) AS number FROM institution I1 " +
					"WHERE I1.uid = " + userId1 + " AND I1.insName IN (" + 
					"SELECT insName FROM institution I2 WHERE I2.uid = " + userId2 + ")";
			ResultSet rs = statement.executeQuery(sql);
			rs.next();
			result = rs.getInt("number");
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<DoubleRating> getMutalRatings(int userId1, int userId2) {
		ArrayList<DoubleRating> result = new ArrayList<DoubleRating>();
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT R1.score AS scoreX, R2.score AS scoreY FROM rating R1, rating R2 " + 
						"WHERE R1.photoid = R2.photoid AND R1.uid = " + userId1 + 
						" AND R2.uid = " + userId2;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				DoubleRating rating = new DoubleRating();
				rating.setScoreX(rs.getFloat("scoreX"));
				rating.setScoreY(rs.getFloat("scoreY"));
				result.add(rating);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Institution> getInstitutions(int userId) {
		ArrayList<Institution> result = new ArrayList<Institution>();
		try {
			psGetInstitutions.setInt(1, userId);
			ResultSet rs = psGetInstitutions.executeQuery();
			while (rs.next()) {
				Institution institution = new Institution();
				institution.setUserId(userId);
				institution.setInstitutionName(rs.getString("insName"));
				result.add(institution);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Interest> getInterests(int userId) {
		ArrayList<Interest> result = new ArrayList<Interest>();
		try {
			psGetInterests.setInt(1, userId);
			ResultSet rs = psGetInterests.executeQuery();
			while (rs.next()) {
				Interest interest = new Interest();
				interest.setUserId(userId);
				interest.setInterest(rs.getString("interest"));
				result.add(interest);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
