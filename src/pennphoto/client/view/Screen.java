package pennphoto.client.view;

import java.util.ArrayList;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Photo;
import pennphoto.shared.PhotoCriteria;
import pennphoto.shared.User;
import pennphoto.shared.UserCriteria;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;


public class Screen extends Composite {

	protected ArrayList<Photo> top10Photos;
	protected PennPhoto pennphoto;
	protected DataServiceAsync dataService;
	protected DockPanel dpContainer;
	protected Grid header;
	protected VerticalPanel leftRoot;
	protected VerticalPanel navigator;
	protected VerticalPanel left;
	protected VerticalPanel center;
	protected VerticalPanel rightRoot;
	protected VerticalPanel friendships;
	protected VerticalPanel right;
	
	private SearchUserScreen searchUserScreen;
	private SearchPhotoScreen searchPhotoScreen;
	private VerticalPanel searchUserDisplayPanel = new VerticalPanel();
	private HorizontalPanel searchFirstNamePanel = new HorizontalPanel();
	private TextBox searchFirstNameTextBox = new TextBox();
	private HorizontalPanel searchLastNamePanel = new HorizontalPanel();
	private TextBox searchLastNameTextBox = new TextBox();
	private HorizontalPanel searchGenderPanel = new HorizontalPanel();
	private ListBox searchGenderListBox = new ListBox();
	private HorizontalPanel searchInsPanel = new HorizontalPanel();
	private TextBox searchInsTextBox = new TextBox();
	private HorizontalPanel searchInterestPanel = new HorizontalPanel();
	private TextBox searchInterestTextBox = new TextBox();
	private VerticalPanel searchPhotoDisplayPanel = new VerticalPanel();
	private HorizontalPanel searchPhotoTagPanel = new HorizontalPanel();
	private TextBox searchPhotoTagTextBox = new TextBox();
	
	public Screen(PennPhoto pennphoto, DataServiceAsync dataService) {
		this.pennphoto = pennphoto;
		this.dataService = dataService;
		dpContainer = new DockPanel();
		dpContainer.setBorderWidth(1);
		dpContainer.setWidth(PennPhoto.getAppWidth()+"px");
		dpContainer.setHeight(Window.getClientHeight()-20+"px");
		header = new Grid(1, 10);
		dpContainer.add(header, DockPanel.NORTH);
		leftRoot = new VerticalPanel();
		leftRoot.setWidth(PennPhoto.getAppWidth()*3/20+"px");
		dpContainer.add(leftRoot, DockPanel.WEST);
		navigator = new VerticalPanel();
		navigator.setSpacing(10);
		Anchor aHomePage = new Anchor("HomePage");
		Anchor aAccountManage = new Anchor("My Account");
		Anchor aBrowseFriends = new Anchor("Browse Friends");
		aHomePage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onHomePageClickPerformed(event);
			}
		});
		aAccountManage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onAccountManageClickPerformed(event);
			}
		});
		aBrowseFriends.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onBrowseFriendsClickPerformed(event);
			}
		});
		navigator.add(aHomePage);
		navigator.add(aAccountManage);
		navigator.add(aBrowseFriends);
		leftRoot.add(navigator);
		left = new VerticalPanel();
		left.setSpacing(5);
		leftRoot.add(left);
		center = new VerticalPanel();
		center.setWidth(PennPhoto.getAppWidth()*14/20+"px");
		dpContainer.add(center, DockPanel.CENTER);
		rightRoot = new VerticalPanel();
		friendships = new VerticalPanel();
		rightRoot.add(friendships);
		right = new VerticalPanel();
		right.setWidth(PennPhoto.getAppWidth()*3/20+"px");
		rightRoot.add(right);
		dpContainer.add(rightRoot, DockPanel.EAST);
		dpContainer.setCellHeight(header, "110px");
		
		setSearchUserDisplay();
		right.add(searchUserDisplayPanel);
		right.add(new HTML("<br>"));
		right.add(new HTML("_______________________"));
		right.add(new HTML("<br>"));
		setSearchPhotoDisplay();
		right.add(searchPhotoDisplayPanel);
		this.initWidget(dpContainer);
	}
	
	private void onHomePageClickPerformed(ClickEvent event) {
		HomeScreen homeScreen = pennphoto.getHomeScreen();
		homeScreen.refreshLeft();
		pennphoto.setCurrentScreen(homeScreen);
	}
	
	private void onAccountManageClickPerformed(ClickEvent event) {
		AccountManageScreen screen = pennphoto.getAccountManageScreen();
		screen.setData(pennphoto.getCurrentUser());
		pennphoto.setCurrentScreen(screen);
	}
	
	private void onBrowseFriendsClickPerformed(ClickEvent event) {
		pennphoto.showFriendBrowser();
	}
	
	private void onImageClickPerformed(ClickEvent event, int index) {
		pennphoto.viewImage(top10Photos.get(index));
	}
	
	public void setTop10Photos(ArrayList<Photo> photos) {
		this.top10Photos = photos;
		for (int i = 0; i < 10; i++) {
			header.clearCell(0, i);
		}
		for (int i = 0; i < photos.size(); i++) {
			FitImage image = new FitImage();
			image.setUrl(photos.get(i).getUrl());
			image.setFixedSize(100, 100);
			image.addClickHandler(new CustomClickHandler(i));
			DOM.setStyleAttribute(image.getElement(), "cursor", "pointer");
			header.setWidget(0, i, image);
		}
	}
	
	private void setSearchUserDisplay() {
		searchUserDisplayPanel.setSpacing(5);
		searchFirstNamePanel.clear();
		searchFirstNamePanel.setSpacing(5);
		HTMLPanel searchFirstNameHtmlPanel = new HTMLPanel("First Name:"); 
		searchFirstNameHtmlPanel.setSize("60px", "20px");
		searchFirstNamePanel.add(searchFirstNameHtmlPanel);
		searchFirstNameTextBox.setWidth("60px");
		searchFirstNamePanel.add(searchFirstNameTextBox);
		
		searchLastNamePanel.clear();
		searchLastNamePanel.setSpacing(5);
		HTMLPanel searchLastNameHtmlPanel = new HTMLPanel("Last Name:");
		searchLastNameHtmlPanel.setSize("60px", "20px");
		searchLastNamePanel.add(searchLastNameHtmlPanel);
		searchLastNameTextBox.setWidth("60px");
		searchLastNamePanel.add(searchLastNameTextBox);
		
		searchGenderPanel.clear();
		searchGenderPanel.setSpacing(5);
		HTMLPanel searchGenderHtmlPanel = new HTMLPanel("Gender:");
		searchGenderHtmlPanel.setSize("60px", "20px");
		searchGenderPanel.add(searchGenderHtmlPanel);
		searchGenderListBox.clear();
		searchGenderListBox.addItem("--");
		searchGenderListBox.addItem("M");
		searchGenderListBox.addItem("F");
		searchGenderListBox.setWidth("40px");
		searchGenderPanel.add(searchGenderListBox);
		
		searchInsPanel.clear();
		searchInsPanel.setSpacing(5);
		HTMLPanel searchInsHtmlPanel = new HTMLPanel("Institution:");
		searchInsHtmlPanel.setSize("60px","20px");
		searchInsPanel.add(searchInsHtmlPanel);
		searchInsTextBox.setWidth("60px");
		searchInsPanel.add(searchInsTextBox);
		
		searchInterestPanel.clear();
		searchInterestPanel.setSpacing(5);
		HTMLPanel searchInterestHtmlPanel = new HTMLPanel("Interest:");
		searchInterestHtmlPanel.setSize("60px", "20px");
		searchInterestPanel.add(searchInterestHtmlPanel);
		searchInterestTextBox.setWidth("60px");
		searchInterestPanel.add(searchInterestTextBox);

		searchUserDisplayPanel.clear();
		searchUserDisplayPanel.add(searchFirstNamePanel);
		searchUserDisplayPanel.add(searchLastNamePanel);
		searchUserDisplayPanel.add(searchGenderPanel);
		searchUserDisplayPanel.add(searchInsPanel);
		searchUserDisplayPanel.add(searchInterestPanel);
		Button searchButton = new Button("Search");
		searchUserDisplayPanel.add(searchButton);
			
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getSearchUserResult(event);
			}
		});
	}
	
	private void getSearchUserResult(ClickEvent event) {
		boolean allEmpty = true;
		UserCriteria criteria = new UserCriteria();
		criteria.setUserId(pennphoto.getCurrentUser().getUserId());
		String firstName = searchFirstNameTextBox.getText().trim();
		if (!firstName.matches("[a-zA-Z]*")) {
			pennphoto.showAlert("Incorrect Name Format");
			return;
		}
		if (firstName.equals("")) {
			firstName = null;
		} else {
			allEmpty = false;
		}
		criteria.setFirstName(firstName);
		String lastName = searchLastNameTextBox.getText().trim();
		if (!lastName.matches("[a-zA-Z]*")) {
			pennphoto.showAlert("Incorrect Name Format");
			return;
		}
		if (lastName.equals("")) {
			lastName = null;
		} else {
			allEmpty = false;
		}
		criteria.setLastName(lastName);
		String gender = searchGenderListBox.getItemText(searchGenderListBox.getSelectedIndex());
		if (gender.equals("--")) {
			gender = null;
		} else {
			allEmpty = false;
		}
		criteria.setGender(gender);
		String text = searchInsTextBox.getText().trim();
		String [] institutions;
		if (text.equals("")) {
			institutions = new String [0];
		} else {
			institutions = text.split(",[ ]*");
			allEmpty = false;
		}
		for (int i = 0; i < institutions.length; i++) {
			if (!institutions[i].matches("[a-zA-Z0-9 ]*")) {
				pennphoto.showAlert("Malformed Insitution Name");
				return;
			}
			criteria.addInstitution(institutions[i]);
		}
		text = searchInterestTextBox.getText().trim();
		String [] interests;
		if (text.equals("")) {
			interests = new String [0];
		} else {
			interests = text.split(",[ ]*");
			allEmpty = false;
		}
		for (int i = 0; i < interests.length; i++) {
			if (!interests[i].matches("[a-zA-Z0-9 ]*")) {
				pennphoto.showAlert("Malformed Interest Name");
				return;
			}
			criteria.addInterest(interests[i]);
		}
		if (allEmpty) {
			pennphoto.showAlert("At least one criteria must be specified");
			return;
		}
		dataService.getSearchUser(criteria, new AsyncCallback<ArrayList<User>>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(ArrayList<User> result) {
				searchUserScreen = pennphoto.getSearchUserScreen();
				searchUserScreen.setData(result);
				pennphoto.setCurrentScreen(searchUserScreen);
			}
		});
	}
	
	private void setSearchPhotoDisplay() {	
		searchPhotoDisplayPanel.setSpacing(5);
		searchPhotoTagPanel.clear();
		searchPhotoTagPanel.setSpacing(5);
		HTMLPanel searchPhotoTagHtmlPanel = new HTMLPanel ("Photo Tag:");
		searchPhotoTagHtmlPanel.setSize("60px", "20px");
		searchPhotoTagTextBox.setWidth("60px");
		searchPhotoTagPanel.add(searchPhotoTagHtmlPanel);
		searchPhotoTagPanel.add(searchPhotoTagTextBox);
		
		searchPhotoDisplayPanel.clear();
		searchPhotoDisplayPanel.add(searchPhotoTagPanel);
		Button searchButton = new Button("Search");
		searchPhotoDisplayPanel.add(searchButton);
		
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getSearchPhotoResult(event);
			}
		});
	}

	private void getSearchPhotoResult(ClickEvent event) {
		PhotoCriteria criteria = new PhotoCriteria();
		String text = searchPhotoTagTextBox.getText().trim();
		String [] tags;
		if (text.equals("")) {
			tags = new String [0];
			pennphoto.showAlert("At least one tag must be specified");
			return;
		} else {
			tags = text.split(",[ ]*");
		}
		for (int i = 0; i < tags.length; i++) {
			if (!tags[i].matches("[a-zA-Z0-9 ]*")) {
				pennphoto.showAlert("Malformed Tag");
				return;
			}
			criteria.addTag(tags[i]);
		}
		criteria.setUserId(pennphoto.getCurrentUser().getUserId());
		dataService.getSearchPhoto(criteria, new AsyncCallback<ArrayList<Photo>>() {
			@Override
			public void onFailure(Throwable caught) {
				
			}
			@Override
			public void onSuccess(ArrayList<Photo> result) {
				searchPhotoScreen = pennphoto.getSearchPhotoScreen();
				searchPhotoScreen.setData(result);
				pennphoto.setCurrentScreen(searchPhotoScreen);
			}
		});
	}
	
	private class CustomClickHandler implements ClickHandler {
		
		private int index;
		public CustomClickHandler(int index) {
			this.index = index;
		}

		@Override
		public void onClick(ClickEvent event) {
			onImageClickPerformed(event, index);
		}
	}
}
