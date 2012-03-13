package pennphoto.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Circle;
import pennphoto.shared.Friend;
import pennphoto.shared.Institution;
import pennphoto.shared.Interest;
import pennphoto.shared.Photo;
import pennphoto.shared.Professor;
import pennphoto.shared.Student;
import pennphoto.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import pennphoto.shared.*;

public class AccountManageScreen extends Screen {

	private User user;
	private PasswordTextBox textBoxNewPassword1;
	private PasswordTextBox textBoxNewPassword2;
	private TextBox textBoxNewEmail;
	private ListBox lbGender;
	private TextBox textBoxNewAddr;
	private TextBox textBoxNewSchool;
	private TextBox textBoxNewInterest;
	private TextBox textBoxNewOffice;
	private TextBox textBoxNewResearchArea;
	private TextBox textBoxNewMajor;
	private TextBox textBoxNewGPA;
	private TextBox textBoxNewPhoto;
	private TextBox textBoxNewCircle;
	private ArrayList<CheckBox> cbPhoto = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> cbCircle = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> cbFriend = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> cbCircleFriend = new ArrayList<CheckBox>();

	private FlexTable profileTable;
	private VerticalPanel photoPanel;
	private FlexTable photoTable;
	private VerticalPanel circlePanel;
	private FlexTable circleTable1;
	private FlexTable circleTable2;
	private VerticalPanel friendPanel;
	private FlexTable friendTable;
	private HashMap<String, Institution> institutionMap;
	private HashMap<String, Interest> interestMap;
	
	public AccountManageScreen(PennPhoto pennphoto, DataServiceAsync dataService) {
		super(pennphoto, dataService);
	}

	private void refreshLeft() {
		left.clear();
		left.add(new HTML("<b>Name<b><br>"));
		left.add(new HTML(user.getFirstname()+" "+user.getLastName()+"<br>"));
		left.add(new HTML("<b>Email Address<b><br>"));
		left.add(new HTML(user.getEmail()+"<br>"));
		left.add(new HTML("<b>Gender</b><br>"));
		left.add(new HTML(user.getGender()+"<br>"));
		left.add(new HTML("<b>Date of Birth</b><br>"));
		left.add(new HTML(user.getDob()+"<br>"));

		if(user.getInstitutions()!=null){
			left.add(new HTML("<b>Institution</b><br>"));
			ArrayList<Institution> institutions = user.getInstitutions();
			Iterator<Institution> institutionIterator = institutions.iterator();
			while(institutionIterator.hasNext()){
				Institution institution = institutionIterator.next();
				if(institution!=null) {
					left.add(new HTML(institution.getInstitutionName()+"<br>"));
				}
			}
		}
		
		if(user.getInterests()!=null){
			left.add(new HTML("<b>Interests</b><br>"));
			ArrayList<Interest> interests = user.getInterests();
			Iterator<Interest> interestIterator = interests.iterator();
			while(interestIterator.hasNext()){
				Interest interest = interestIterator.next();
				if(interest!=null) {
					left.add(new HTML(interest.getInterest()+"<br>"));
				}
			}
		}
		
		if(user instanceof Professor){
			Professor p = (Professor)user;
			left.add(new HTML("<b>Research Area</b><br>"));
		    left.add(new HTML(p.getResearchArea()+"<br>"));
			left.add(new HTML("<b>Office<b><br>"));
			left.add(new HTML(p.getOffice()+"<br>"));
		}
		else if(user instanceof Student){
			Student s = (Student)user;
			left.add(new HTML("<b>Major<b><br>"));
			left.add(new HTML(s.getMajor()+"<br>"));
			left.add(new HTML("<b>GPA<b><br>"));
			left.add(new HTML(s.getGpa()+"<br>"));
			left.add(new HTML("<b>Advisor<b><br>"));
			left.add(new HTML(s.getAdvisor().getFirstname()+" "+s.getAdvisor().getLastName()+"<br>"));
			left.add(new HTML("<b>Years Advised<b><br>"));
			left.add(new HTML(s.getYearsAdvised()+"<br>"));
		}
	}
	
	private void refreshForRight() {
		right.clear();
		Button btnMngProfile = new Button("Manage Profile");
		btnMngProfile.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onMngProfileClickPerformed(event);
			}
		});
		right.add(btnMngProfile);

		Button btnMngFriend = new Button("Manage Friends");
		btnMngFriend.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onMngFriendClickPerformed(event);
			}
		});
		right.add(btnMngFriend);

		Button btnMngCircle = new Button("Manage Circles");
		btnMngCircle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onMngCircleClickPerformed(event);
			}
		});
		right.add(btnMngCircle);

		Button btnMngPhoto = new Button("Manage Photos");
		btnMngPhoto.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onMngPhotoClickPerformed(event);
			}
		});
		right.add(btnMngPhoto);
	}
	
	private void refreshForProfile() {
		center.clear();
		center.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		center.add(profileTable);
	}
	
	public synchronized void refreshForPhoto() {
		photoTable.clear();
		cbPhoto.clear();
		ArrayList<Photo> userPhotos = user.getPhotos();
		for (int i = 0; i < userPhotos.size(); i++) {
			CheckBox cb = new CheckBox();
			cb.setText(String.valueOf(userPhotos.get(i).getPhotoId()));
			cbPhoto.add(cb);
			photoTable.setWidget(i + 2, 0, cb);
			Label lblPhoto = new Label(userPhotos.get(i).getUrl());
			photoTable.setWidget(i + 2, 1, lblPhoto);
		}
		center.clear();
		center.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		center.add(photoPanel);
	}
	
	private void refreshForCircle() {
		circleTable1.clear();
		circleTable2.clear();
		cbCircleFriend.clear();
		cbCircle.clear();
		ArrayList<User> userCircleFriends = user.getFriends();
		for (int i = 0; i < userCircleFriends.size(); i++) {
			CheckBox cb = new CheckBox();
			cb.setText(String.valueOf(userCircleFriends.get(i).getUserId()));
			cbCircleFriend.add(cb);
			circleTable1.setWidget(i + 2, 0, cb);
			Label lblFriend1 = new Label(userCircleFriends.get(i).getFirstname());
			circleTable1.setWidget(i + 2, 1, lblFriend1);
			Label lblFriend2 = new Label(userCircleFriends.get(i).getLastName());
			circleTable1.setWidget(i + 2, 2, lblFriend2);
		}
		ArrayList<Circle> userCircles = user.getCircles();
		for (int i = 0; i < userCircles.size(); i++) {
			CheckBox cb = new CheckBox();
			cb.setText(String.valueOf(userCircles.get(i).getCircleId()));
			cbCircle.add(cb);
			circleTable2.setWidget(i + 4, 0, cb);
			Label lblCircle = new Label(userCircles.get(i).getName());
			circleTable2.setWidget(i + 4, 1, lblCircle);
		}
		center.clear();
		center.add(circlePanel);
	}
	
	private void refreshForFriend() {
		friendTable.clear();
		cbFriend.clear();
		ArrayList<User> userFriends = user.getFriends();
		for (int i = 0; i < userFriends.size(); i++) {
			CheckBox cb = new CheckBox();
			cb.setText(String.valueOf(userFriends.get(i).getUserId()));
			cbFriend.add(cb);
			friendTable.setWidget(i, 0, cb);
			Label lblFriend1 = new Label(userFriends.get(i).getFirstname());
			friendTable.setWidget(i, 1, lblFriend1);
			Label lblFriend2 = new Label(userFriends.get(i).getLastName());
			friendTable.setWidget(i, 2, lblFriend2);
		}
		center.clear();
		center.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		center.add(friendPanel);
	}
	
	private void createProfilePanel() {
		HTML headline = new HTML("<h1>PROFILE MANAGEMENT</h1>");
		center.add(headline);
		Label lblHead = new Label("Update Profile");
		center.add(lblHead);
		Label lblNotice1 = new Label("For profile modification, please enter your changes below.");
		center.add(lblNotice1);
		profileTable = new FlexTable();
		profileTable.setCellSpacing(6);
		Label lblNewPassword1 = new Label("New Password:");
		profileTable.setWidget(1, 0, lblNewPassword1);
		textBoxNewPassword1 = new PasswordTextBox();
		textBoxNewPassword1.setWidth("300px");
		profileTable.setWidget(1, 1, textBoxNewPassword1);
		Label lblNewPassword2 = new Label("Re-enter New Password:");
		profileTable.setWidget(2, 0, lblNewPassword2);
		textBoxNewPassword2 = new PasswordTextBox();
		textBoxNewPassword2.setWidth("300px");
		profileTable.setWidget(2, 1, textBoxNewPassword2);
		Label lblNewEmail = new Label("Email:");
		profileTable.setWidget(3, 0, lblNewEmail);
		textBoxNewEmail = new TextBox();
		textBoxNewEmail.setWidth("300px");
		profileTable.setWidget(3, 1, textBoxNewEmail);
		Label lblNewGender = new Label("Gender:");
		profileTable.setWidget(5, 0, lblNewGender);
		lbGender = new ListBox();
		lbGender.addItem(null);
		lbGender.addItem("M");
		lbGender.addItem("F");
		lbGender.setVisibleItemCount(1);
		profileTable.setWidget(5, 1, lbGender);
		Label lblNewAddr = new Label("Address:");
		profileTable.setWidget(6, 0, lblNewAddr);
		textBoxNewAddr = new TextBox();
		textBoxNewAddr.setWidth("500px");
		profileTable.setWidget(6, 1, textBoxNewAddr);
		Label lblNewSchool = new Label("Educational Institution:");
		profileTable.setWidget(7, 0, lblNewSchool);
		textBoxNewSchool = new TextBox();
		textBoxNewSchool.setWidth("500px");
		profileTable.setWidget(7, 1, textBoxNewSchool);
		Label lblNewInterest = new Label("Interest:");
		profileTable.setWidget(8, 0, lblNewInterest);
		textBoxNewInterest = new TextBox();
		textBoxNewInterest.setWidth("500px");
		profileTable.setWidget(8, 1, textBoxNewInterest);

		if (user instanceof Professor) {
			Label lblNewOffice = new Label("Office:");
			profileTable.setWidget(9, 0, lblNewOffice);
			textBoxNewOffice = new TextBox();
			textBoxNewOffice.setWidth("500px");
			profileTable.setWidget(9, 1, textBoxNewOffice);
			Label lblNewResearchArea = new Label("Research Area:");
			profileTable.setWidget(10, 0, lblNewResearchArea);
			textBoxNewResearchArea = new TextBox();
			textBoxNewResearchArea.setWidth("500px");
			profileTable.setWidget(10, 1, textBoxNewResearchArea);
		}

		if (user instanceof Student) {
			Label lblNewMajor = new Label("Major:");
			profileTable.setWidget(9, 0, lblNewMajor);
			textBoxNewMajor = new TextBox();
			textBoxNewMajor.setWidth("500px");
			profileTable.setWidget(9, 1, textBoxNewMajor);
			Label lblNewGPA = new Label("GPA:");
			profileTable.setWidget(10, 0, lblNewGPA);
			textBoxNewGPA = new TextBox();
			textBoxNewGPA.setWidth("500px");
			profileTable.setWidget(10, 1, textBoxNewGPA);
		}

		Button btnSave = new Button("Save");
		btnSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					onSaveClickPerformed(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		profileTable.setWidget(11, 1, btnSave);
	}
	
	private void createPhotoPanel() {
		photoPanel = new VerticalPanel();
		HTML headline = new HTML("<h1>PHOTO MANAGEMENT</h1>");
		photoPanel.add(headline);
		Label lblHead = new Label("Manage Photo");
		photoPanel.add(lblHead);
		Label lblNotice = new Label("To upload photo, enter the photo's URL below.");
		photoPanel.add(lblNotice);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(6);
		Label lblNewPhoto = new Label("Photo URL:");
		flexTable.setWidget(0, 0, lblNewPhoto);
		textBoxNewPhoto = new TextBox();
		textBoxNewPhoto.setWidth("600px");
		flexTable.setWidget(0, 1, textBoxNewPhoto);

		Button btnAddPhoto = new Button("Add New Photo");
		btnAddPhoto.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onAddPhotoClickPerformed(event);
			}
		});
		flexTable.setWidget(1, 1, btnAddPhoto);
		photoTable = new FlexTable();
		flexTable.setWidget(2, 1, photoTable);
		Button btnDeletePhoto = new Button("Delete Selected Photo");
		btnDeletePhoto.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDelPhotoClickPerformed(event);
			}
		});
		flexTable.setWidget(3, 1, btnDeletePhoto);
		photoPanel.add(flexTable);
	}
	
	private void createCirclePanel() {
		circlePanel = new VerticalPanel();
		HTML headline = new HTML("<h1>CIRCLE MANAGEMENT</h1>");
		circlePanel.add(headline);
		Label lblHead = new Label("Manage Circle List");
		circlePanel.add(lblHead);
		Label lblNotice = new Label("To add new circle, enter the name of new circle and select friends below.");
		circlePanel.add(lblNotice);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(6);
		Label lblNewCircle = new Label("New Circle Name:");
		flexTable.setWidget(0, 0, lblNewCircle);
		textBoxNewCircle = new TextBox();
		textBoxNewCircle.setWidth("600px");
		flexTable.setWidget(0, 1, textBoxNewCircle);
		circlePanel.add(flexTable);
		FlexTable flexTable1 = new FlexTable();
		flexTable1.setCellSpacing(6);
		Label lblNewCircleFriend = new Label("Friend List:");
		flexTable1.setWidget(0, 0, lblNewCircleFriend);

		Button btnAddCircle = new Button("Add New Circle");
		btnAddCircle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onAddCircleClickPerformed(event);
			}
		});
		circleTable1 = new FlexTable();
		flexTable1.setWidget(2, 1, circleTable1);
		flexTable1.setWidget(3, 1, btnAddCircle);

		Button btnDeleteCircle = new Button("Delete Selected Circle");
		btnDeleteCircle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDelCircleClickPerformed(event);
			}
		});
		circleTable2 = new FlexTable();
		flexTable1.setWidget(4, 1, circleTable2);
		flexTable1.setWidget(5, 1, btnDeleteCircle);
		circlePanel.add(flexTable1);
	}
	
	private void createFriendPanel() {
		friendPanel = new VerticalPanel();
		HTML headline = new HTML("<h1>FRIEND MANAGEMENT</h1>");
		friendPanel.add(headline);
		Label lblHead = new Label("Manage Friend List");
		friendPanel.add(lblHead);
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(6);
		Button btnDeleteFriend = new Button("Delete Selected Friend");
		btnDeleteFriend.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDelFriendClickPerformed(event);
			}
		});
		friendTable = new FlexTable();
		flexTable.setWidget(1, 1, friendTable);
		flexTable.setWidget(2, 1, btnDeleteFriend);
		friendPanel.add(flexTable);
	}
	
	public void setData(User user) {
		this.user = user;
		institutionMap = new HashMap<String, Institution>();
		ArrayList<Institution> institutions = user.getInstitutions();
		for (int i = 0; i < institutions.size(); i++) {
			Institution ins = institutions.get(i);
			institutionMap.put(ins.getInstitutionName().toLowerCase(), ins);
		}
		interestMap = new HashMap<String, Interest>();
		ArrayList<Interest> interests = user.getInterests();
		for (int i = 0; i < interests.size(); i++) {
			Interest interest = interests.get(i);
			interestMap.put(interest.getInterest().toLowerCase(), interest);
		}
		this.refreshLeft();
		this.refreshForRight();
		this.createProfilePanel();
		this.createPhotoPanel();
		this.createCirclePanel();
		this.createFriendPanel();
		this.refreshForProfile();
	}
	
	private void onMngProfileClickPerformed(ClickEvent event) {
		this.refreshForProfile();
	}

	private void onMngPhotoClickPerformed(ClickEvent event) {
		if (user.getPhotos() == null) {
			dataService.getPhotos(user.getUserId(), new AsyncCallback<ArrayList<Photo>>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(ArrayList<Photo> result) {
					user.setPhotos(result);
					refreshForPhoto();
				}
			});
		} else {
			refreshForPhoto();
		}
	}
	
	private void onMngCircleClickPerformed(ClickEvent event) {
		this.refreshForCircle();
	}
	
	private void onMngFriendClickPerformed(ClickEvent event) {
		this.refreshForFriend();
	}
	
	private void onSaveClickPerformed(ClickEvent event) {
		if (!textBoxNewPassword1.getText().isEmpty() && !textBoxNewPassword2.getText().isEmpty()) {
			if (textBoxNewPassword1.getText().equals(textBoxNewPassword2.getText())) {
				user.setPassword(textBoxNewPassword1.getText());
			} else {
				pennphoto.showAlert("New Password Re-enter Didn't Match!");
			}
		} else if (!textBoxNewPassword1.getText().isEmpty() || !textBoxNewPassword2.getText().isEmpty()) {
			pennphoto.showAlert("Please Fill Both New Password and Re-enter New Password Section!");
		}
		
		if (!textBoxNewEmail.getText().isEmpty()) {
			user.setEmail(textBoxNewEmail.getText());
		}
		if (lbGender.getSelectedIndex() != 0) {
			user.setGender(lbGender.getItemText(lbGender.getSelectedIndex()));
		}
		if (!textBoxNewAddr.getText().isEmpty()) {
			user.setAddress(textBoxNewAddr.getText());
		}
		
		if (!textBoxNewSchool.getText().isEmpty()) {
			Institution institution = new Institution();
			institution.setUserId(user.getUserId());
			institution.setInstitutionName(textBoxNewSchool.getText());
			Institution old = institutionMap.get(institution.getInstitutionName().toLowerCase());
			if (old == null) {
				dataService.addInstitution(institution, new AddInstitutionAsyncCallback(institution));
			} else {
				pennphoto.showAlert("Duplicated Institution Name!");
			}
		}
		
		if (!textBoxNewInterest.getText().isEmpty()) {
			Interest interest = new Interest();
			interest.setUserId(user.getUserId());
			interest.setInterest(textBoxNewInterest.getText());
			Interest old = interestMap.get(interest.getInterest().toLowerCase());
			if (old == null) {
				dataService.addInterest(interest, new AddInterestAsyncCallback(interest));
			} else {
				pennphoto.showAlert("Duplicated Interest!");
			}
		}
		if (user instanceof Professor) {
			Professor professor = (Professor) user;
			if (!textBoxNewResearchArea.getText().isEmpty()) {
				professor.setResearchArea(textBoxNewResearchArea.getText());
			} 
			if (!textBoxNewOffice.getText().isEmpty()) {
				professor.setOffice(textBoxNewOffice.getText());
			} 
			dataService.updateProfessor(professor, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Void user) {
					refreshLeft();
				}
			});
		} else if (user instanceof Student) {
			Student student = (Student) user;
			if (!textBoxNewMajor.getText().isEmpty()) {
				student.setMajor(textBoxNewMajor.getText());
			} 
			if (!textBoxNewGPA.getText().isEmpty()) {
				student.setGpa(Float.parseFloat(textBoxNewGPA.getText()));
			} 
			dataService.updateStudent(student, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Void user) {
					refreshLeft();
				}
			});
		}
		refreshForProfile();
	}

	private void onAddPhotoClickPerformed(ClickEvent event) {
		Photo newPhoto = new Photo();
		User owner = new User();
		owner.setUserId(user.getUserId());
		newPhoto.setOwner(owner);
		newPhoto.setUrl(textBoxNewPhoto.getText());
		pennphoto.showVisibilityDialog(newPhoto);
		//dataService.addPhoto(newPhoto, new AddPhotoAsyncCallback(newPhoto));
	}

	private void onDelPhotoClickPerformed(ClickEvent event) {
		for (int i = 0; i < cbPhoto.size(); i++) {
			if (!cbPhoto.get(i).getValue()) {
				continue;
			}
			Photo photo = user.getPhotos().get(i);
			Photo toDelete = new Photo();
			toDelete.setPhotoId(photo.getPhotoId());
			dataService.deletePhoto(toDelete, new DeletePhotoAsyncCallback(photo));
		}
	}

	private void onAddCircleClickPerformed(ClickEvent event) {
		ArrayList<User> userNewCircleFriend = new ArrayList<User>();
		for (int i = 0; i < cbCircleFriend.size(); i++) {
			if (!cbCircleFriend.get(i).getValue()) {
				continue;
			}
			int id = Integer.parseInt(cbCircleFriend.get(i).getText());
			ArrayList<User> userFriends = user.getFriends();
			for (int j = 0; j < userFriends.size(); j++) {
				if (userFriends.get(j).getUserId() == id) {
					userNewCircleFriend.add(userFriends.get(j));
				}
			}
		}
		String name = textBoxNewCircle.getText();
		Circle newCircle = new Circle();
		newCircle.setUserId(user.getUserId());
		newCircle.setName(name);
		newCircle.setFriends(userNewCircleFriend);
		dataService.addCircle(newCircle, new AddCircleAsyncCallback(newCircle, userNewCircleFriend));
	}

	private void onDelCircleClickPerformed(ClickEvent event) {
		for (int i = 0; i < cbCircle.size(); i++) {
			if (!cbCircle.get(i).getValue()) {
				continue;
			}
			Circle circle = user.getCircles().get(i);
			Circle toDelete = new Circle();
			toDelete.setCircleId(circle.getCircleId());
			dataService.deleteCircle(toDelete, new DeleteCircleAsyncCallback(circle));
		}
	}

	private void onDelFriendClickPerformed(ClickEvent event) {
		for (int i = 0; i < cbFriend.size(); i++) {
			if (!cbFriend.get(i).getValue()) {
				continue;
			}
			User friend = user.getFriends().get(i);
			dataService.deleteFriend(user.getUserId(), friend.getUserId(), new DeleteFriendAsyncCallback(friend));
			pennphoto.notifyUserProfileChanged();
		}
	}
	
	private class AddInstitutionAsyncCallback implements AsyncCallback<Void> {
		
		private Institution ins;
		
		public AddInstitutionAsyncCallback(Institution ins) {
			this.ins = ins;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			user.getInstitutions().add(ins);
			institutionMap.put(ins.getInstitutionName().toLowerCase(), ins);
		}
		
	}
	
	private class AddInterestAsyncCallback implements AsyncCallback<Void> {
		
		private Interest interest;
		
		public AddInterestAsyncCallback(Interest interest) {
			this.interest = interest;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			user.getInterests().add(interest);
			interestMap.put(interest.getInterest().toLowerCase(), interest);
		}
		
	}
	
	private class DeletePhotoAsyncCallback implements AsyncCallback<Void> {

		private Photo photo;
		
		public DeletePhotoAsyncCallback(Photo photo) {
			this.photo = photo;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			user.getPhotos().remove(photo);
			refreshForPhoto();
		}
		
	}
	
	private class AddCircleAsyncCallback implements AsyncCallback<Integer> {

		private Circle circle;
		private ArrayList<User> friends;
		
		public AddCircleAsyncCallback(Circle circle, ArrayList<User> friends) {
			this.circle = circle;
			this.friends = friends;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Integer result) {
			circle.setCircleId(result);
			user.getCircles().add(circle);
			refreshForCircle();
			for (int i = 0; i < friends.size(); i++) {
				Friend friend = new Friend();
				friend.setCircleId(result);
				friend.setFriendId(friends.get(i).getUserId());
				dataService.addFriendToCircle(friend, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
					}
					@Override
					public void onSuccess(Void result) {
					}
				});
			}
		}
		
	}

	private class DeleteCircleAsyncCallback implements AsyncCallback<Void> {

		private Circle circle;
		
		public DeleteCircleAsyncCallback(Circle circle) {
			this.circle = circle;
		}
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			user.getCircles().remove(circle);
			int j;
			ArrayList<Circle> circles = user.getCircles();
			ArrayList<User> friendToDelete = circle.getFriends();
			for (int i = 0; i < friendToDelete.size(); i++) {
				for (j = 0; j < circles.size(); j++) {
					ArrayList<User> existFriend = circles.get(j).getFriends();
					if (existFriend.indexOf(friendToDelete.get(i)) != -1) {
						break;
					}
				}
				if (j == circles.size()) {
					user.getFriends().remove(friendToDelete.get(i));
				}
			}
			refreshForCircle();
		}
		
	}

	private class DeleteFriendAsyncCallback implements AsyncCallback<Void> {

		private User friend;
		
		public DeleteFriendAsyncCallback(User friend) {
			this.friend = friend;
		}
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			user.getFriends().remove(friend);
			ArrayList<Circle> circles = user.getCircles();
			for (int i = 0; i < circles.size(); i++) {
				ArrayList<User> friendInCircle = circles.get(i).getFriends();
				friendInCircle.remove(friend);
			}
			refreshForFriend();
		}
		
	}
}