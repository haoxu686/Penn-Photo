package pennphoto.client.view;

import java.util.ArrayList;
import java.util.HashMap;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Photo;
import pennphoto.shared.Professor;
import pennphoto.shared.Student;
import pennphoto.shared.User;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class FriendBrowser extends BaseDialog {

	private PennPhoto pennphoto;
	private DataServiceAsync dataService;
	private VerticalPanel root;
	private AbsolutePanel view;
	private Label info;
	private Button ok;
	private User user;
	private User selectedUser;
	private JavaScriptObject htGraph;
	private HashMap<Integer, User> friendTable;
	private boolean changed;
	
	public FriendBrowser(PennPhoto pennphoto, DataServiceAsync dataService) {
		this.pennphoto = pennphoto;
		this.dataService = dataService;
		htGraph = null;
		changed = true;
		root = new VerticalPanel();
		root.setSpacing(10);
		root.setSize("400px", "500px");
		view = new AbsolutePanel();
		view.getElement().setId("friendbrowser");
		view.setSize("400px", "400px");
		root.add(view);
		HorizontalPanel footer = new HorizontalPanel();
		footer.setSpacing(10);
		footer.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		info = new Label();
		info.setWidth("150px");
		footer.add(info);
		ok = new Button("  GO  ");
		ok.setEnabled(false);
		selectedUser = null;
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButtonOkActionPerformed(event);
			}
		});
		footer.add(ok);
		Button dismiss = new Button("Close");
		dismiss.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		footer.add(dismiss);
		root.add(footer);
		this.setWidget(root);
		friendTable = new HashMap<Integer, User>();
	}
	
	private void onButtonOkActionPerformed(ClickEvent event) {
		if (selectedUser.getPhotos() == null) {
			dataService.getPhotoOfUser(selectedUser.getUserId(), pennphoto.getCurrentUser().getUserId(), new AsyncCallback<ArrayList<Photo>>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(ArrayList<Photo> result) {
					selectedUser.setPhotos(result);
					changeToProfileScreen();
				}
			});
		} else {
			changeToProfileScreen();
		}
	}
	
	private void changeToProfileScreen() {
		ProfileScreen profile = pennphoto.getProfileScreen();
		profile.setUser(selectedUser);
		profile.setPhotos(selectedUser.getPhotos());
		this.hide();
		pennphoto.setCurrentScreen(profile);
	}
	
	public void selectUser (int userId, String name) {
		User friend = friendTable.get(userId);
		if (friend == null) {
			info.setText(name);
			if (user.getUserId() == userId) {
				selectedUser = user;
				ok.setEnabled(true);
			} else  {
				ok.setEnabled(false);
			}
		} else {
			this.selectedUser = friend;
			info.setText(name);
			ok.setEnabled(true);
		}
	}
	
	public void setUser(User user) {
		this.user = user;
		changed = true;
	}
	
	public void setChanged(boolean b) {
		changed = b;
	}

	@Override
	public void show() {
		super.show();
		info.setText("");
		ok.setEnabled(false);
		JSONObject data;
		if (htGraph == null) {
			JSONObject thisUser = new JSONObject();
			thisUser.put("id", new JSONNumber(user.getUserId()));
			thisUser.put("name", new JSONString(user.getFirstname()+"."+user.getLastName()));
			data = new JSONObject();
			data.put("$color", new JSONString("#f00"));
			thisUser.put("data", data);
			htGraph = FriendVisualization.createGraph(thisUser.toString(), this);
		}
		if (!changed) {
			return;
		}
		friendTable.clear();
		FriendVisualization.clear(htGraph, String.valueOf(user.getUserId()));
		ArrayList<User> friendList = new ArrayList<User>(user.getFriends());
		if (user instanceof Professor) {
			Professor professor = (Professor) user;
			friendList.addAll(professor.getAdvisees());
		} else if (user instanceof Student) {
			Student student = (Student) user;
			friendList.add(student.getAdvisor());
		}
		JSONObject thisUser = new JSONObject();
		JSONArray friends = new JSONArray();
		for (int i = 0; i < friendList.size(); i++) {
			User friend = friendList.get(i);
			friendTable.put(friend.getUserId(), friend);
			JSONObject thisFriend = new JSONObject();
			thisFriend.put("id", new JSONNumber(friend.getUserId()));
			thisFriend.put("name", new JSONString(friend.getFirstname()+"."+friend.getLastName()));
			data = new JSONObject();
			data.put("$color", new JSONString("#0f0"));
			thisFriend.put("data", data);
			friends.set(friends.size(), thisFriend);
		}
		thisUser.put("id", new JSONNumber(user.getUserId()));
		thisUser.put("name", new JSONString(user.getFirstname()+"."+user.getLastName()));
		thisUser.put("children", friends);
		FriendVisualization.addToGraph(htGraph, thisUser.toString());
		for (int i = 0; i < friendList.size(); i++) {
			User friend = friendList.get(i);
			ArrayList<User> someoneList = new ArrayList<User>(friend.getFriends());
			if (friend instanceof Professor) {
				Professor professor = (Professor) friend;
				someoneList.addAll(professor.getAdvisees());
			} else if (friend instanceof Student) {
				Student student = (Student) friend;
				someoneList.add(student.getAdvisor());
			}
			JSONObject thisFriend = new JSONObject();
			JSONArray someones = new JSONArray();
			for (int j = 0; j < someoneList.size(); j++) {
				User someone = someoneList.get(j);
				JSONObject thisSomeone = new JSONObject();
				thisSomeone.put("id", new JSONNumber(someone.getUserId()));
				thisSomeone.put("name", new JSONString(someone.getFirstname()+"."+someone.getLastName()));
				data = new JSONObject();
				if (someone.getUserId() == user.getUserId()) {
					continue;
				} else if (friendTable.get(someone.getUserId()) != null) {
					data.put("$color", new JSONString("#0f0"));
				} else {
					data.put("$color", new JSONString("#00f"));
				}
				thisSomeone.put("data", data);
				someones.set(someones.size(), thisSomeone);
			}
			thisFriend.put("id", new JSONNumber(friend.getUserId()));
			thisFriend.put("name", new JSONString(friend.getFirstname()+"."+friend.getLastName()));
			thisFriend.put("children", someones);
			FriendVisualization.addToGraph(htGraph, thisFriend.toString());
		}
		changed = false;
	}
	
	private class GetPhotosOfUserCallback implements AsyncCallback<ArrayList<Photo>> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(ArrayList<Photo> result) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
