package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tag implements IsSerializable {
	private int photoId;
	private String tag;
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
