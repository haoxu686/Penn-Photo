package pennphoto.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Rating implements IsSerializable {
	private int photoId;
	private int rater;
	private float score;
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public int getRaterId() {
		return rater;
	}
	public void setRaterId(int userId) {
		this.rater = userId;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
}
