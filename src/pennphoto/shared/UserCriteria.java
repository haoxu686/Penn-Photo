package pennphoto.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserCriteria implements IsSerializable {

	private int userId;
	private String firstName;
	private String lastName;
	private String gender;
	private ArrayList<String> institution;
	private ArrayList<String> interest;
	
	public UserCriteria() {
		firstName = null;
		lastName = null;
		gender = null;
		institution = new ArrayList<String>();
		interest = new ArrayList<String>();
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ArrayList<String> getInstitution() {
		return institution;
	}

	public void addInstitution(String institution) {
		this.institution.add(institution);
	}

	public ArrayList<String> getInterest() {
		return interest;
	}

	public void addInterest(String interest) {
		this.interest.add(interest);
	}
}
