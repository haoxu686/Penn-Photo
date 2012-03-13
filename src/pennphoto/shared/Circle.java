package pennphoto.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Circle implements IsSerializable {
	private int circleId;
	private int userId;
	private String name;
	private ArrayList<User> friends;
	
	public ArrayList<User> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}
	public int getCircleId() {
		return circleId;
	}
	public void setCircleId(int circleId) {
		this.circleId = circleId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int uerId) {
		this.userId = uerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
