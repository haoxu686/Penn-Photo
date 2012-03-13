package pennphoto.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import pennphoto.client.model.DataService;
import pennphoto.server.dbutil.ConnectionManager;
import pennphoto.server.dbutil.DBConnector;
import pennphoto.shared.Circle;
import pennphoto.shared.Friend;
import pennphoto.shared.Institution;
import pennphoto.shared.Interest;
import pennphoto.shared.Photo;
import pennphoto.shared.PhotoCriteria;
import pennphoto.shared.Professor;
import pennphoto.shared.Rating;
import pennphoto.shared.Student;
import pennphoto.shared.Tag;
import pennphoto.shared.User;
import pennphoto.shared.UserCriteria;
import pennphoto.shared.Visibility;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	private static final long serialVersionUID = 1L;

	@Override
	public int addCircle(Circle circle) {
		DBConnector connector = ConnectionManager.getDBConnector();
		int result = connector.addCircle(circle);
		connector.release();
		return result;
	}

	@Override
	public void updateCircle(Circle circle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCircle(Circle circle) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.deleteCircle(circle);
		connector.release();
	}

	@Override
	public void addFriendToCircle(Friend friend) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.addFriendToCircle(friend);
		Friend reverse = new Friend();
		reverse.setFriendId(friend.getUserId());
		ArrayList<Circle> reverseCircle = connector.getCircles(friend.getFriendId());
		if (reverseCircle.size() == 0) {
			Circle circle = new Circle();
			circle.setName("TMP");
			circle.setUserId(friend.getFriendId());
			connector.addCircle(circle);
			reverse.setCircleId(circle.getCircleId());
			connector.addFriendToCircle(reverse);
		} else {
			Circle circle = reverseCircle.get(0);
			reverse.setCircleId(circle.getCircleId());
			connector.addFriendToCircle(reverse);
		}
		connector.release();
	}

	@Override
	public void deleteFriendFromCircle(Friend friend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addInstitution(Institution institution) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.addInstitution(institution);
		connector.release();
	}

	@Override
	public void deleteInstitution(Institution institution) {
		
	}

	@Override
	public void addInterest(Interest interest) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.addInterest(interest);
		connector.release();
	}

	@Override
	public void deleteInterest(Interest interest) {
		// TODO Auto-generated method stub

	}

	@Override
	public int addPhoto(Photo photo) {
		DBConnector connector = ConnectionManager.getDBConnector();
		int result = connector.addPhoto(photo);
		connector.release();
		return result;
	}

	@Override
	public void deletePhoto(Photo photo) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.deletePhoto(photo);
		connector.release();
	}

	@Override
	public int addProfessor(Professor professor) {
		DBConnector connector = ConnectionManager.getDBConnector();
		int result = connector.addProfessor(professor);
		connector.release();
		return result;
	}

	@Override
	public void updateProfessor(Professor professor) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.updateUser(professor);
		connector.updateProfessor(professor);
		connector.release();
	}

	@Override
	public void deleteProfessor(Professor professor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRating(Rating rating) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.addRating(rating);
		connector.release();
	}

	@Override
	public void updateRating(Rating rating) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.updateRating(rating);
		connector.release();
	}

	@Override
	public int addStudent(Student student) {
		DBConnector connector = ConnectionManager.getDBConnector();
		int result = connector.addStudent(student);
		connector.release();
		return result;
	}

	@Override
	public void updateStudent(Student student) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.updateUser(student);
		connector.updateStudent(student);
		connector.release();
	}

	@Override
	public void deleteStudent(Student student) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTag(Tag tag) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.addTag(tag);
		connector.release();
	}

	@Override
	public void deleteTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addVisibility(ArrayList<Visibility> v) {
		DBConnector connector = ConnectionManager.getDBConnector();
		for (int i = 0; i < v.size(); i++) {
			connector.addVisibility(v.get(i));
		}
		connector.release();
	}

	@Override
	public void deleteVisibility(Visibility visibility) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getCurrentUserInfo(int userId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		User user = connector.getUser(userId);
		ArrayList<Circle> circles = connector.getCircles(userId);
		user.setCircles(circles);
		for (int i = 0; i < circles.size(); i++) {
			Circle circle = circles.get(i);
			ArrayList<User> friends = connector.getFriendsInCircle(circle.getCircleId());
			circle.setFriends(friends);
		}
		ArrayList<User> ids = connector.getFriends(userId);
		ArrayList<User> friends = new ArrayList<User>();
		for (int i = 0; i < ids.size(); i++) {
			User friend = connector.getUser(ids.get(i).getUserId());
			ArrayList<User> someones = connector.getFriends(friend.getUserId());
			friend.setFriends(someones);
			friends.add(friend);
		}
		user.setFriends(friends);
		ArrayList<Institution> institutions = connector.getInstitutions(userId);
		user.setInstitutions(institutions);
		ArrayList<Interest> interests = connector.getInterests(userId);
		user.setInterests(interests);
		if (user instanceof Professor) {
			Professor professor = (Professor) user;
			ArrayList<Student> adviseeIds = professor.getAdvisees();
			ArrayList<Student> advisees = new ArrayList<Student>();
			for (int i = 0; i < adviseeIds.size(); i++) {
				Student student = (Student) connector.getUser(adviseeIds.get(i).getUserId());
				ArrayList<User> someones = connector.getFriends(student.getUserId());
				student.setFriends(someones);
				advisees.add(student);
			}
			professor.setAdvisees(advisees);
		} else if (user instanceof Student) {
			Student student = (Student) user;
			Professor advisor = (Professor) connector.getUser(student.getAdvisor().getUserId());
			ArrayList<User> someones = connector.getFriends(advisor.getUserId());
			advisor.setFriends(someones);
			student.setAdvisor(advisor);
		}
		connector.release();
		return user;
	}

	@Override
	public ArrayList<Photo> getTop10Photos(int userId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Photo> result = connector.getTop10Photos(userId);
		for (int i = 0; i < result.size(); i++) {
			Photo photo = result.get(i);
			ArrayList<Rating> ratings = connector.getRatings(photo.getPhotoId());
			photo.setRatings(ratings);
			ArrayList<String> tags = connector.getTags(photo.getPhotoId());
			photo.setTags(tags);
		}
		connector.release();
		return result;
	}

	@Override
	public User getUserInfo(int userId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		User user = connector.getUser(userId);
		ArrayList<User> friends = connector.getFriends(userId);
		user.setFriends(friends);
		if (user instanceof Professor) {
			Professor professor = (Professor) user;
			ArrayList<Student> adviseeIds = professor.getAdvisees();
			ArrayList<Student> advisees = new ArrayList<Student>();
			for (int i = 0; i < adviseeIds.size(); i++) {
				Student student = (Student) connector.getUser(adviseeIds.get(i).getUserId());
				ArrayList<User> someones = connector.getFriends(student.getUserId());
				student.setFriends(someones);
				advisees.add(student);
			}
			professor.setAdvisees(advisees);
		} else if (user instanceof Student) {
			Student student = (Student) user;
			Professor advisor = (Professor) connector.getUser(student.getAdvisor().getUserId());
			ArrayList<User> someones = connector.getFriends(advisor.getUserId());
			advisor.setFriends(someones);
		}
		connector.release();
		return user;
	}
	
	@Override
	public String authenticateUser(int userID){
		DBConnector connector = ConnectionManager.getDBConnector();
		String result = connector.getPassword(userID);
		connector.release();
		return result;
	}

	@Override
	public ArrayList<User> getSearchUser(UserCriteria criteria) {
		HashSet<Integer> userIds1 = null;
		HashSet<Integer> userIds2 = null;
		HashSet<Integer> userIds3 = null;
		HashSet<Integer> userIds = null;
		DBConnector connector = ConnectionManager.getDBConnector();
		String firstName = criteria.getFirstName();
		String lastName = criteria.getLastName();
		String gender = criteria.getGender();
		ArrayList<String> institution = criteria.getInstitution();
		ArrayList<String> interest = criteria.getInterest();
		String sql = "SELECT uid FROM user WHERE ";
		if (firstName != null) {
			sql = sql+ "firstName = \'"+ firstName + "\' AND ";
		}
		if (lastName != null) {
			sql = sql + "lastName = \'" + lastName + "\' AND ";
		}
		if (gender != null) {
			sql = sql + "gender = \'" + gender + "\' AND ";
		}
		if (gender != null || firstName != null || lastName != null) {
			sql = sql.substring(0, sql.length()-4);
			ArrayList<Integer> result = connector.searchUserByCriteria(sql);
			userIds1 = new HashSet<Integer>();
			for (int i = 0; i < result.size(); i++) {
				userIds1.add(result.get(i));
			}
		}
		if (institution.size() != 0) {
			sql = "SELECT uid FROM institution WHERE ";
			for (int i = 0; i < institution.size(); i++) {
				sql = sql + "insName LIKE \'%" + institution.get(i) + "%\' OR ";
			}
			sql = sql.substring(0, sql.length()-3);
			ArrayList<Integer> result = connector.searchUserByCriteria(sql);
			userIds2 = new HashSet<Integer>();
			for (int i = 0; i < result.size(); i++) {
				userIds2.add(result.get(i));
			}
		}
		ArrayList<User> friends = connector.getFriends(criteria.getUserId());
		ArrayList<Integer> friendIds = new ArrayList<Integer>();
		for (int i = 0; i < friends.size(); i++) {
			friendIds.add(friends.get(i).getUserId());
		}
		if (interest.size() != 0) {
			sql = "SELECT uid FROM interest WHERE ";
			for (int i = 0; i < interest.size(); i++) {
				sql = sql + "interest LIKE \'%" + interest.get(i) + "%\' OR ";
			}
			sql = sql.substring(0, sql.length()-3);
			ArrayList<Integer> result = connector.searchUserByCriteria(sql);
			userIds3 = new HashSet<Integer>();
			for (int i = 0; i < result.size(); i++) {
				userIds3.add(result.get(i));
			}
		}
		if (userIds1 != null) {
			userIds = userIds1;
		}
		if (userIds2 != null) {
			userIds = userIds2;
		}
		if (userIds3 != null) {
			userIds = userIds3;
		}
		if (userIds1 != null) {
			userIds.retainAll(userIds1);
		}
		if (userIds2 != null) {
			userIds.retainAll(userIds2);
		}
		if (userIds3 != null) {
			userIds.retainAll(userIds3);
		}
		userIds.removeAll(friendIds);
		userIds.remove(new Integer(criteria.getUserId()));
		ArrayList<Integer> ids = new ArrayList<Integer>(userIds);
		ArrayList<User> result = new ArrayList<User>();
		for (int i = 0; i < ids.size(); i++) {
			User user = connector.getUser(ids.get(i));
			result.add(user);
		}
		connector.release();
		return result;
	}
	
	@Override
	public ArrayList<Photo> getSearchPhoto(PhotoCriteria criteria) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<String> tag = criteria.getTag();
		String sql = "SELECT P.photoid AS photoid, P.uid AS uid, url FROM photo P, visibility V, tag T " +
				"WHERE P.photoid = V.photoid AND P.photoid = T.photoid AND (V.uid = 0 OR V.uid = " + criteria.getUserId() + ") AND (";
		for (int i = 0; i < tag.size(); i++) {
			sql = sql + "tag LIKE \'%" + tag.get(i) + "%\' OR ";
		}
		sql = sql.substring(0, sql.length()-3);
		sql += ")";
		ArrayList<Photo> result = connector.searchPhotoByCriteria(sql);
		for (int i = 0; i < result.size(); i++) {
			Photo photo = result.get(i);
			ArrayList<String> tags = connector.getTags(photo.getPhotoId());
			ArrayList<Rating> ratings = connector.getRatings(photo.getPhotoId());
			photo.setTags(tags);
			photo.setRatings(ratings);
		}
		connector.release();
		return result;
	}
	@Override
	public ArrayList<Photo> getNews(int userID2) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Photo> result = connector.getNewPhotos(userID2);
		for (int i = 0; i < result.size(); i++) {
			Photo photo = result.get(i);
			ArrayList<String> tags = connector.getTags(photo.getPhotoId());
			ArrayList<Rating> ratings = connector.getRatings(photo.getPhotoId());
			photo.setTags(tags);
			photo.setRatings(ratings);
		}
		connector.release();
		return result;
	}

	@Override
	public ArrayList<Photo> getPhotoOfUser(int ownerId, int viewerId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Photo> result = connector.getPhotosOfUser(ownerId, viewerId);
		for (int i = 0; i < result.size(); i++) {
			Photo photo = result.get(i);
			ArrayList<String> tags = connector.getTags(photo.getPhotoId());
			ArrayList<Rating> ratings = connector.getRatings(photo.getPhotoId());
			photo.setTags(tags);
			photo.setRatings(ratings);
		}
		connector.release();
		return result;
	}

	@Override
	public ArrayList<Professor> getProfessors() {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Professor> profs = connector.getProfessors();
		connector.release();
		return profs;
	}

	@Override
	public void deleteFriend(int userId, int friendId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		connector.deleteFriend(userId, friendId);
		connector.release();
	}
	
	@Override
	public ArrayList<Photo> getPhotos(int userId){
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Photo> result = connector.getPhotos(userId);
		for (int i = 0; i < result.size(); i++) {
			Photo photo = result.get(i);
			ArrayList<Rating> ratings = connector.getRatings(photo.getPhotoId());
			photo.setRatings(ratings);
			ArrayList<String> tags = connector.getTags(photo.getPhotoId());
			photo.setTags(tags);
		}
		connector.release();
		return result;
	}

	@Override
	public ArrayList<Circle> getCircles(int userId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Circle> result = connector.getCircles(userId);
		connector.release();
		return result;
	}

	@Override
	public ArrayList<User> getRecommendedFriend(int userId) {
		DBConnector connector = ConnectionManager.getDBConnector();
		ArrayList<Recommendation> strangers = connector.getStrangers(userId);
		for (int i = 0; i < strangers.size(); i++) {
			Recommendation r = strangers.get(i);
			int result = connector.getMutualFriendNumber(userId, r.getUserId());
			r.setScore(r.getScore()+2*result);
			result = connector.getMutualInstitutionNumber(userId, r.getUserId());
			r.setScore(r.getScore()+result*result);
			ArrayList<DoubleRating> ratings = connector.getMutalRatings(userId, r.getUserId());
			double numerator = 0;
			double denominator = 1;
			double vX = 0;
			double vY = 0;
			for (int j = 0; j < ratings.size(); j++) {
				DoubleRating rating = ratings.get(j);
				numerator += rating.getScoreX()*rating.getScoreY();
				vX += rating.getScoreX()*rating.getScoreX();
				vY += rating.getScoreY()*rating.getScoreY();
			}
			vX = Math.sqrt(vX);
			vY = Math.sqrt(vY);
			denominator = vX*vY;
			if (denominator == 0) {
				numerator = 0;
			} else {
				numerator /= denominator;
			}
			numerator *= ratings.size();
			result = (int) Math.round(numerator);
			r.setScore(r.getScore()+result);
		}
		Collections.sort(strangers, new RecommendationComparator());
		int count;
		if (strangers.size() > 3) {
			count = 3;
		} else {
			count = strangers.size();
		}
		ArrayList<User> result = new ArrayList<User>();
		for (int i = 0; i < count; i++) {
			User user = connector.getUser(strangers.get(i).getUserId());
			result.add(user);
		}
		return result;
	}
}
