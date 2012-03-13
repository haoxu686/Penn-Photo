package pennphoto.shared;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Photo implements IsSerializable {
	private int photoId;
	private User owner;
	private String url;
	private Date timestamp;
	private ArrayList<String> tags;
	private ArrayList<Rating> ratings;
	private ArrayList<Visibility> visibilities;
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public ArrayList<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}
	public ArrayList<Visibility> getVisibilities() {
		return visibilities;
	}
	public void setVisibilities(ArrayList<Visibility> visibilities) {
		this.visibilities = visibilities;
	}
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
