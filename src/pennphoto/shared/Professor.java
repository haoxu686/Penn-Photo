package pennphoto.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Professor extends User implements IsSerializable {
	private String staffId;
	private String researchArea;
	private String office;
	private ArrayList<Student> advisees;
	public ArrayList<Student> getAdvisees() {
		return advisees;
	}
	public void setAdvisees(ArrayList<Student> advisees) {
		this.advisees = advisees;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getResearchArea() {
		return researchArea;
	}
	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
}
