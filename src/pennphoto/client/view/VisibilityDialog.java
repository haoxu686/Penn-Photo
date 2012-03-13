package pennphoto.client.view;

import java.util.ArrayList;
import java.util.HashMap;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Circle;
import pennphoto.shared.Photo;
import pennphoto.shared.User;
import pennphoto.shared.Visibility;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class VisibilityDialog extends BaseDialog {

	private AccountManageScreen screen;
	private PennPhoto pennphoto;
	private DataServiceAsync dataService;
	private Photo photo;
	private VerticalPanel rootPanel;
	private ScrollPanel scroller;
	private FlexTable flexTable;
	private CheckBox cbAll;
	private ArrayList<CheckBox> cbCircles;
	private ArrayList<ArrayList<CheckBox>> cbGroups;
	
	public VisibilityDialog(PennPhoto pennphoto, DataServiceAsync dataService, AccountManageScreen screen) {
		this.screen = screen;
		this.pennphoto = pennphoto;
		this.dataService = dataService;
		rootPanel = new VerticalPanel();
		rootPanel.setSpacing(10);
		rootPanel.add(new HTML("<h1>Please specify visibility"));
		rootPanel.setSize("400px", "400px");
		this.setWidget(rootPanel);
		scroller = new ScrollPanel();
		scroller.setSize("400px", "300px");
		flexTable = new FlexTable();
		scroller.setWidget(flexTable);
		rootPanel.add(scroller);
		Button bOk = new Button("OK");
		bOk.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButtonOkClickPerformed(event);
			}
		});
		rootPanel.add(bOk);
		cbCircles = new ArrayList<CheckBox>();
		cbGroups = new ArrayList<ArrayList<CheckBox>>();
	}
	
	private void onButtonOkClickPerformed(ClickEvent event) {
		ArrayList<Visibility> v = new ArrayList<Visibility>();
		if (cbAll.getValue()) {
			Visibility visibility = new Visibility();
			visibility.setUserId(0);
			v.add(visibility);
			dataService.addPhoto(photo, new AddPhotoAsyncCallback(photo, v));
		} else {
			HashMap<Integer, User> map = new HashMap<Integer, User>();
			for (int i = 0; i < cbGroups.size(); i++) {
				ArrayList<User> friends = pennphoto.getCurrentUser().getCircles().get(i).getFriends();
				ArrayList<CheckBox> cbItems = cbGroups.get(i);
				for (int j = 0; j < cbItems.size(); j++) {
					if (!cbItems.get(j).getValue()) {
						continue;
					}
					User friend = friends.get(j);
					User old = map.get(friend.getUserId());
					if (old != null) {
						continue;
					}
					Visibility visibility = new Visibility();
					visibility.setUserId(friend.getUserId());
					map.put(friend.getUserId(), friend);
					v.add(visibility);
				}
			}
			dataService.addPhoto(photo, new AddPhotoAsyncCallback(photo, v));
		}
	}
	
	public void refresh() {
		flexTable.clear();
		int counter = 0;
		cbGroups.clear();
		cbCircles.clear();
		cbAll = new CheckBox();
		flexTable.setWidget(counter, 0, cbAll);
		cbAll.setValue(true);
		Label lbAll = new Label();
		lbAll.setText("Everyone");
		flexTable.setWidget(counter, 1, lbAll);
		counter++;
		User user = pennphoto.getCurrentUser();
		ArrayList<Circle> circles = user.getCircles();
		for (int i = 0; i < circles.size(); i++) {
			CheckBox cbCircle = new CheckBox();
			cbCircle.addClickHandler(new CheckBoxCircleClickHandler(i));
			flexTable.setWidget(counter, 0, cbCircle);
			cbCircles.add(cbCircle);
			ArrayList<CheckBox> cbItems = new ArrayList<CheckBox>();
			cbGroups.add(cbItems);
			Label lbCircle = new Label();
			lbCircle.setText(circles.get(i).getName());
			flexTable.setWidget(counter, 1, lbCircle);
			counter++;
			ArrayList<User> friends = circles.get(i).getFriends();
			for (int j = 0; j < friends.size(); j++) {
				CheckBox cbFriend = new CheckBox();
				HorizontalPanel hp = new HorizontalPanel();
				flexTable.setWidget(counter, 1, hp);
				counter++;
				hp.add(cbFriend);
				cbItems.add(cbFriend);
				Label lbFriend = new Label();
				User friend = friends.get(j);
				lbFriend.setText(friend.getUserId() + " " + friend.getFirstname() + "." + friend.getLastName());
				hp.add(lbFriend);
			}
		}
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
		this.refresh();
	}
	
	private class CheckBoxCircleClickHandler implements ClickHandler {

		private int index;
		
		public CheckBoxCircleClickHandler(int index) {
			this.index = index;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			CheckBox cbCircle = cbCircles.get(index);
			boolean value;
			if (!cbCircle.getValue()) {
				value = false;
			} else {
				value = true;
			}
			ArrayList<CheckBox> cbItems = cbGroups.get(index);
			for (int i = 0; i < cbItems.size(); i++) {
				cbItems.get(i).setValue(value);
			}
		}
		
	}
	
	private class AddPhotoAsyncCallback implements AsyncCallback<Integer> {

		private Photo photo;
		private ArrayList<Visibility> v;
		
		public AddPhotoAsyncCallback(Photo photo, ArrayList<Visibility> v) {
			this.photo = photo;
			this.v = v;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Integer result) {
			photo.setPhotoId(result);
			for (int i = 0; i < v.size(); i++) {
				v.get(i).setPhotoId(result);
			}
			pennphoto.getCurrentUser().getPhotos().add(photo);
			screen.refreshForPhoto();
			dataService.addVisibility(v, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Void result) {
					
				}
			});
			hide();
		}
		
	}
	
}
