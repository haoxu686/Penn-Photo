package pennphoto.shared;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class User implements IsSerializable {
	private int userId;
	private String password;
	private String firstname;
	private String lastName;
	private String email;
	private String dob;
	private String gender;
	private String address;
	private String type;
	private ArrayList<Circle> circles;
	private ArrayList<User> friends;
	private ArrayList<Photo> photos;
	private ArrayList<Institution> institutions;
	private ArrayList<Interest> interests;
	
	public ArrayList<Circle> getCircles() {
		return circles;
	}
	public void setCircles(ArrayList<Circle> circles) {
		this.circles = circles;
	}
	public ArrayList<User> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	public ArrayList<Institution> getInstitutions() {
		return institutions;
	}
	public void setInstitutions(ArrayList<Institution> institutions) {
		this.institutions = institutions;
	}
	public ArrayList<Interest> getInterests() {
		return interests;
	}
	public void setInterests(ArrayList<Interest> interests) {
		this.interests = interests;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof User)) {
			return false;
		}
		User u = (User) o;
		return u.getUserId() == this.userId;
	}
}
