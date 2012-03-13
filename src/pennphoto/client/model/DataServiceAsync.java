package pennphoto.client.model;

import java.util.ArrayList;

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

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface DataServiceAsync {

	/**Please be careful to keep the format of this file 
	 * the same as DataService.java for easier lookup
	 */
	void addCircle(Circle circle, AsyncCallback<Integer> callback);
	void updateCircle(Circle circle, AsyncCallback<Void> callback);
	void deleteCircle(Circle circle, AsyncCallback<Void> callback);

	void addFriendToCircle(Friend friend, AsyncCallback<Void> callback);
	void deleteFriendFromCircle(Friend friend, AsyncCallback<Void> callback);

	void addInstitution(Institution institution, AsyncCallback<Void> callback);
	void deleteInstitution(Institution institution, AsyncCallback<Void> callback);

	void addInterest(Interest interest, AsyncCallback<Void> callback);
	void deleteInterest(Interest interest, AsyncCallback<Void> callback);

	void addPhoto(Photo photo, AsyncCallback<Integer> callback);
	void deletePhoto(Photo photo, AsyncCallback<Void> callback);

	void addProfessor(Professor professor, AsyncCallback<Integer> callback);
	void updateProfessor(Professor professor, AsyncCallback<Void> callback);
	void deleteProfessor(Professor professor, AsyncCallback<Void> callback);

	void addRating(Rating rating, AsyncCallback<Void> callback);
	void updateRating(Rating rating, AsyncCallback<Void> callback);

	void addStudent(Student student, AsyncCallback<Integer> callback);
	void updateStudent(Student student, AsyncCallback<Void> callback);
	void deleteStudent(Student student, AsyncCallback<Void> callback);

	void addTag(Tag tag, AsyncCallback<Void> callback);
	void deleteTag(Tag tag, AsyncCallback<Void> callback);

	void addVisibility(ArrayList<Visibility> v, AsyncCallback<Void> callback);
	void deleteVisibility(Visibility visibility, AsyncCallback<Void> callback);
	
	void getCurrentUserInfo(int userId, AsyncCallback<User> callback);
	void getUserInfo(int userId, AsyncCallback<User> callback);
	void getTop10Photos(int userId, AsyncCallback<ArrayList<Photo>> callback);
	void authenticateUser(int userId, AsyncCallback<String> callback);
	void getSearchUser(UserCriteria criteria, AsyncCallback<ArrayList<User>> callback);
	void getSearchPhoto(PhotoCriteria criteria, AsyncCallback<ArrayList<Photo>> callback);
	void getNews(int userID2, AsyncCallback<ArrayList<Photo>> asyncCallback);
	void getPhotoOfUser(int ownerId, int viewerId, AsyncCallback<ArrayList<Photo>> callback);
	void getProfessors(AsyncCallback<ArrayList<Professor>> callback);
	void deleteFriend(int userId, int friendId, AsyncCallback<Void> callback);
	void getPhotos(int userId, AsyncCallback<ArrayList<Photo>> callback);
	void getCircles(int userId, AsyncCallback<ArrayList<Circle>> callback);
	void getRecommendedFriend(int userId, AsyncCallback<ArrayList<User>> callback);
}
