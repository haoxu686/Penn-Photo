package pennphoto.client.view;

import java.util.ArrayList;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Circle;
import pennphoto.shared.Friend;
import pennphoto.shared.User;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class SearchUserScreen extends Screen {
	
	private VerticalPanel searchResultPanel = new VerticalPanel();
	private ArrayList<User> result;
	private ArrayList<Button> bAddFriends;
	private ArrayList<ListBox> lbCircles;
	
	public SearchUserScreen(PennPhoto pennphoto, DataServiceAsync dataService) {
		super(pennphoto, dataService);
		center.add(searchResultPanel);
		lbCircles = new ArrayList<ListBox>();
		bAddFriends = new ArrayList<Button>();
	}
	
	public void setData(ArrayList<User> result) {
		this.result = result;
		searchResultPanel.clear();
		searchResultPanel.setSpacing(10);
		searchResultPanel.add(new HTML("<b>Search User Result<b><br>"));
		HorizontalPanel resultRowName = new HorizontalPanel();
		Button returnButton = new Button("Return");
		HTMLPanel userIdName = new HTMLPanel("User ID");
		HTMLPanel firstNameName = new HTMLPanel("First Name");
		HTMLPanel lastNameName = new HTMLPanel("Last Name");
		HTMLPanel genderName = new HTMLPanel("Gender");
		resultRowName.add(userIdName);
		resultRowName.add(firstNameName);
		resultRowName.add(lastNameName);
		resultRowName.add(genderName);
		resultRowName.setCellWidth(returnButton, "80px");
		resultRowName.setCellWidth(userIdName, "100px");
		resultRowName.setCellWidth(firstNameName, "100px");	
		resultRowName.setCellWidth(lastNameName, "100px");
		resultRowName.setCellWidth(genderName, "100px");
		resultRowName.setHeight("30px");
		searchResultPanel.add(resultRowName);
		lbCircles.clear();
		ArrayList<Circle> circles = pennphoto.getCurrentUser().getCircles();
		for (int i = 0; i < result.size(); i++) {
			User u = result.get(i);
			HorizontalPanel resultRow = new HorizontalPanel();
			resultRow.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			Button addButton = new Button("Add As Friend");
			bAddFriends.add(addButton);
			addButton.addClickHandler(new AddFriendClickHandler(i));
			addButton.setHeight("25px");
			ListBox lbCircle = new ListBox();
			for (int j = 0; j < circles.size(); j++) {
				lbCircle.addItem(circles.get(j).getName());
			}
			lbCircles.add(lbCircle);
		
			HTMLPanel userIdResult = new HTMLPanel(u.getUserId()+"");
			HTMLPanel firstNameResult = new HTMLPanel(u.getFirstname());
			HTMLPanel lastNameResult = new HTMLPanel(u.getLastName());
			HTMLPanel genderResult = new HTMLPanel(u.getGender());	
			
			resultRow.add(userIdResult);
			resultRow.add(firstNameResult);
			resultRow.add(lastNameResult);
			resultRow.add(genderResult);
			resultRow.add(addButton);
			resultRow.add(lbCircle);
			resultRow.setCellWidth(userIdResult, "100px");
			resultRow.setCellWidth(firstNameResult, "100px");
			resultRow.setCellWidth(lastNameResult, "100px");
			resultRow.setCellWidth(genderResult, "100px");
			resultRow.setCellWidth(addButton, "150px");
			resultRow.setCellWidth(lbCircle, "100px");
			searchResultPanel.add(resultRow);
		}
	}
	
	private class AddFriendClickHandler implements ClickHandler {

		private int index;
		
		public AddFriendClickHandler(int index) {
			this.index = index;
		}
		@Override
		public void onClick(ClickEvent event) {
			User friend = result.get(index);
			ListBox listBox = lbCircles.get(index);
			int s = listBox.getSelectedIndex();
			if (s == -1) {
				return;
			}
			int cid = pennphoto.getCurrentUser().getCircles().get(s).getCircleId();
			Friend toAdd = new Friend();
			toAdd.setCircleId(cid);
			toAdd.setFriendId(friend.getUserId());
			toAdd.setUserId(pennphoto.getCurrentUser().getUserId());
			dataService.addFriendToCircle(toAdd, new AddFriendAsyncCallback(index));
			dataService.getUserInfo(friend.getUserId(), new GetUserInfoAsyncCallback(s));
			pennphoto.notifyUserProfileChanged();
			bAddFriends.get(index).setEnabled(false);
		}
	}
	
	private class AddFriendAsyncCallback implements AsyncCallback<Void> {

		private int index;
		
		public AddFriendAsyncCallback(int index) {
			this.index = index;
		}
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void v) {
		}
		
	}
	
	private class GetUserInfoAsyncCallback implements AsyncCallback<User> {
		
		private int circleIndex;
		
		public GetUserInfoAsyncCallback(int circleIndex) {
			this.circleIndex = circleIndex;
		}

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(User u) {
			pennphoto.getCurrentUser().getCircles().get(circleIndex).getFriends().add(u);
			pennphoto.getCurrentUser().getFriends().add(u);
		}
	}
} 