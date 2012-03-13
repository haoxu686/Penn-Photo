package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Student extends User implements IsSerializable {
	private String stuId;
	private String major;
	private float gpa;
	private Professor advisor;
	private int yearsAdvised;
	
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public float getGpa() {
		return gpa;
	}
	public void setGpa(float gpa) {
		this.gpa = gpa;
	}
	public Professor getAdvisor() {
		return advisor;
	}
	public void setAdvisor(Professor advisor) {
		this.advisor = advisor;
	}
	public int getYearsAdvised() {
		return yearsAdvised;
	}
	public void setYearsAdvised(int yearsAdvised) {
		this.yearsAdvised = yearsAdvised;
	}
}
