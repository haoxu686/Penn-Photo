package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Institution implements IsSerializable {
	private int userId;
	private String institutionName;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
}
