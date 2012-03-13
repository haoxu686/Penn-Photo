package pennphoto.client.view;

import java.util.ArrayList;
import java.util.Iterator;

import pennphoto.client.model.DataService;
import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Photo;
import pennphoto.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


public class PennPhoto implements EntryPoint {

	private static final int APP_WIDTH = 1024;
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	private User currentUser;
	private ArrayList<Photo> top10Photos;
	
	private ArrayList<Screen> screens;
	private Screen current;
	private RootPanel display;
	
	private LoginScreen loginScreen;
	private HomeScreen homeScreen;
	private ProfileScreen profileScreen;
	private RegisterScreen registerScreen;
	private SearchUserScreen searchUserScreen;
	private SearchPhotoScreen searchPhotoScreen;
	private AccountManageScreen accountManageScreen;

	private AlertDialog alertDialog;
	private ImageView imageView;
	private FriendBrowser friendBrowser;
	private VisibilityDialog visibilityDialog;
	
	@Override
	public void onModuleLoad() {
		Window.enableScrolling(true);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				display.clear();
				display.add(current, Window.getClientWidth()/2-APP_WIDTH/2, 10);
			}
		});
		
		display = RootPanel.get();
		screens = new ArrayList<Screen>();
		loginScreen = new LoginScreen(this, dataService);
		homeScreen = new HomeScreen(this, dataService);
		screens.add(homeScreen);
		registerScreen = new RegisterScreen(this, dataService);
		profileScreen = new ProfileScreen(this, dataService);
		screens.add(profileScreen);
		searchUserScreen = new SearchUserScreen(this, dataService);
		screens.add(searchUserScreen);
		searchPhotoScreen = new SearchPhotoScreen(this, dataService);
		screens.add(searchPhotoScreen);
		accountManageScreen = new AccountManageScreen(this, dataService);
		screens.add(accountManageScreen);
		alertDialog = new AlertDialog();
		imageView = new ImageView(dataService);
		friendBrowser = new FriendBrowser(this, dataService);
		visibilityDialog = new VisibilityDialog(this, dataService, accountManageScreen);
		
		current = loginScreen;
		this.setCurrentScreen(loginScreen);
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
		friendBrowser.setUser(user);
		imageView.setCurrentUser(user);
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setTop10Photos(ArrayList<Photo> photos) {
		this.top10Photos = photos;
		for (int i = 0; i < screens.size(); i++) {
			screens.get(i).setTop10Photos(top10Photos);
		}
	}
	
	public LoginScreen getLoginScreen() {
		return loginScreen;
	}

	public HomeScreen getHomeScreen() {
		return homeScreen;
	}

	public ProfileScreen getProfileScreen() {
		return profileScreen;
	}

	public void setCurrentScreen(Screen screen) {
		display.clear();
		display.add(screen, Window.getClientWidth()/2-APP_WIDTH/2, 10);
		current = screen;
	}
	
	public void showAlert(String message) {
		alertDialog.setMessage(message);
		alertDialog.show();
		alertDialog.center();
	}
	
	public void viewImage(Photo photo) {
		imageView.setPhoto(photo);
		imageView.show();
		imageView.center();
	}

	public void showFriendBrowser() {
		friendBrowser.show();
		friendBrowser.center();
	}
	
	public static int getAppWidth() {
		return APP_WIDTH;
	}

	public RegisterScreen getRegisterScreen() {
		return registerScreen;
	}
	
	public SearchUserScreen getSearchUserScreen() {
		return searchUserScreen;
	}

	public SearchPhotoScreen getSearchPhotoScreen() {
		return searchPhotoScreen;
	}
	
	public AccountManageScreen getAccountManageScreen() {
		return accountManageScreen;
	}
	
	public void showVisibilityDialog(Photo photo) {
		visibilityDialog.setPhoto(photo);
		visibilityDialog.refresh();
		visibilityDialog.show();
		visibilityDialog.center();
	}
	
	public void notifyUserProfileChanged() {
		friendBrowser.setChanged(true);
	}
}
