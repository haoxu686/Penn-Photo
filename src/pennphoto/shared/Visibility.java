package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Visibility implements IsSerializable {
	private int photoId;
	private int userId;
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
