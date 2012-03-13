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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import pennphoto.shared.*;

@RemoteServiceRelativePath("DataService")
public interface DataService extends RemoteService {

	/** Primitive operations to each table.
	 * Complicated queries go into the next section
	 */
	public int addCircle(Circle circle);
	public void updateCircle(Circle circle);
	public void deleteCircle(Circle cirlce);
	
	public void addFriendToCircle(Friend friend);
	public void deleteFriendFromCircle(Friend friend);
	
	public void addInstitution(Institution institution);
	public void deleteInstitution(Institution institution);
	
	public void addInterest(Interest interest);
	public void deleteInterest(Interest interest);
	
	public int addPhoto(Photo photo);
	public void deletePhoto(Photo photo);
	
	public int addProfessor(Professor professor);
	public void updateProfessor(Professor professor);
	public void deleteProfessor(Professor professor);
	
	public void addRating(Rating rating);
	public void updateRating(Rating rating);
	
	public int addStudent(Student student);
	public void updateStudent(Student student);
	public void deleteStudent(Student student);
	
	public void addTag(Tag tag);
	public void deleteTag(Tag tag);
	
	public void addVisibility(ArrayList<Visibility> v);
	public void deleteVisibility(Visibility visibility);
	
	public void deleteFriend(int userId, int friendId);
	
	/**Complicated queries go here*/
	public User getCurrentUserInfo(int userId);
	public User getUserInfo(int userId);
	public ArrayList<Photo> getTop10Photos(int userId);
	public String authenticateUser(int userId);
	public ArrayList<User> getSearchUser(UserCriteria criteria);
	public ArrayList<Photo> getSearchPhoto(PhotoCriteria criteria);
	public ArrayList<Photo> getNews(int userID2);
	public ArrayList<Photo> getPhotoOfUser(int ownerId, int viewerId);
	public ArrayList<Professor> getProfessors();
	public ArrayList<Photo> getPhotos(int userId);
	public ArrayList<Circle> getCircles(int userId);
	public ArrayList<User> getRecommendedFriend(int userId);
}
