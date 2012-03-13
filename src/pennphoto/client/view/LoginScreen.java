package pennphoto.client.view;

import java.util.ArrayList;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Photo;
import pennphoto.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class LoginScreen extends Screen {
	private TextBox textBoxUsername;
	private TextBox textBoxPassword;
	private HomeScreen homeScreen;
	private int userId;

	public LoginScreen(PennPhoto pennphoto, DataServiceAsync dataService) {

		super(pennphoto, dataService);
		navigator.clear();
		right.clear();
		dpContainer.setBorderWidth(0);
		HTML headline = new HTML("<h1>PENN PHOTO LOGIN</h1>");
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(6);
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();
		cellFormatter.setColSpan(0, 0, 1);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		Label lblNewLabel_1 = new Label("User ID");
		flexTable.setWidget(0, 0, lblNewLabel_1);
		textBoxUsername = new TextBox();
		textBoxUsername.setWidth("150px");
		flexTable.setWidget(0, 1, textBoxUsername);
		Label lblPassword = new Label("Password");
		flexTable.setWidget(1, 0, lblPassword);
		textBoxPassword = new PasswordTextBox();
		textBoxPassword.setWidth("150px");
		flexTable.setWidget(1, 1, textBoxPassword);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
		Button btnSignIn = new Button("Sign In");
		btnSignIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onSigninClickPerformed(event);
			}
		});
		Button btnRegister = new Button("Register");
		btnRegister.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onRegisterClickPerformed(event);
			}
		});
		hp.add(btnSignIn);
		hp.add(btnRegister);
		//flexTable.setWidget(2, 0, btnSignIn);
		flexTable.setWidget(2, 1, hp);
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(flexTable);
		center.add(headline);
		center.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		center.add(decPanel);
		
		textBoxUsername.setText("20001");
		textBoxPassword.setText("123456");
	}

	protected void onRegisterClickPerformed(ClickEvent event) {
		RegisterScreen screen = pennphoto.getRegisterScreen();
		screen.refresh();
		pennphoto.setCurrentScreen(screen);
	}

	private void onSigninClickPerformed(ClickEvent event) {
		String textUserId = textBoxUsername.getText();
		String password = textBoxPassword.getText();
		if (textUserId.length() == 0 || password.length() == 0) {
			pennphoto.showAlert("Username or password should not be empty.");
		} else {
			try {
				userId = Integer.parseInt(textUserId);
			} catch (NumberFormatException e) {
				pennphoto.showAlert("Incorrect Format of UserId");
				return;
			}
			checkPassword(password);
		}
	}
	
	protected void checkPassword(String password) {
		dataService.authenticateUser(userId, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(String result) {
				if (!result.equals(textBoxPassword.getText())) {
					pennphoto.showAlert("Incorrect Password!!!");
					return;
				} 
				homeScreen = pennphoto.getHomeScreen();
				retrieveData();
				dataService.getTop10Photos(userId, new AsyncCallback<ArrayList<Photo>>() {
					@Override
					public void onFailure(Throwable caught) {
					}
					@Override
					public void onSuccess(ArrayList<Photo> photos) {
						pennphoto.setTop10Photos(photos);
					}
				});
				dataService.getRecommendedFriend(userId, new AsyncCallback<ArrayList<User>>() {
					@Override
					public void onFailure(Throwable caught) {
					}
					@Override
					public void onSuccess(ArrayList<User> result) {
						homeScreen.setRecommendedFriends(result);
					}
				});
			}
		});
	}
	
	public void retrieveData(){
		dataService.getNews(userId, new AsyncCallback<ArrayList<Photo>>(){
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(ArrayList<Photo> result) {
				homeScreen.setNews(result);
			}
		});
		dataService.getCurrentUserInfo(userId, new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(User result) {
				pennphoto.setCurrentUser(result);				
				homeScreen.setData(result);
				pennphoto.setCurrentScreen(homeScreen);
			}
		});
	}
}
					