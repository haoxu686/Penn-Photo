package pennphoto.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PhotoCriteria implements IsSerializable {
	
	private ArrayList<String> tag;
	private int userId;
	
	public PhotoCriteria() {
		tag = new ArrayList<String>();
	}

	public int getUserId() {
		return userId;
	}
	
	public ArrayList<String> getTag() {
		return tag;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void addTag(String tag) {
		this.tag.add(tag);
	}

}
