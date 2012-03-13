package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Interest implements IsSerializable {
	private int userId;
	private String interest;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	
}
