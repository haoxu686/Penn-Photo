package pennphoto.client.view;

import java.util.ArrayList;
import java.util.HashMap;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Photo;
import pennphoto.shared.Rating;
import pennphoto.shared.Tag;
import pennphoto.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;


public class ImageView extends BaseDialog {
	
	private DataServiceAsync dataService;
	private User user;
	private Photo photo;
	private Grid photoView;
	private VerticalPanel photoRoot;
	private HTML tagView;
	private String tagString;
	private TextBox textNewTag;
	private org.cobogw.gwt.user.client.ui.Rating starRating;
	private HashMap<String, String> tagTable;
	private HashMap<Integer, Rating> ratingTable;
	
	public ImageView(DataServiceAsync dataService) {
		this.dataService = dataService;
		
		VerticalPanel rootPanel = new VerticalPanel();
		rootPanel.setSize("600px", "800px");
		//rootPanel.setSpacing(10);
		rootPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);

		photoRoot = new VerticalPanel();
		photoRoot.setSize("600px", "600px");
		photoRoot.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		photoRoot.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		photoView = new Grid(1, 1);
		//photoView.setSize("600px", "600px");
		rootPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		photoRoot.add(photoView);
		rootPanel.add(photoRoot);
		
		tagView = new HTML();
		rootPanel.add(tagView);
		
		HorizontalPanel hpActions = new HorizontalPanel();
		hpActions.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hpActions.setSpacing(10);
		textNewTag = new TextBox();
		textNewTag.setWidth("150px");
		hpActions.add(textNewTag);
		Button buttonAddTag = new Button("Add Tag");
		hpActions.add(buttonAddTag);
		hpActions.setCellHorizontalAlignment(buttonAddTag, HorizontalPanel.ALIGN_RIGHT);
		buttonAddTag.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButtonAddTagActionPerformed(event);
			}
		});
		
		starRating = new org.cobogw.gwt.user.client.ui.Rating(0, 5);
		hpActions.add(starRating);
		Button buttonRate = new Button("Rate");
		hpActions.add(buttonRate);
		buttonRate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButtonRateActionPerformed(event);
			}
		});
		rootPanel.add(hpActions);
		rootPanel.setCellHorizontalAlignment(hpActions, HorizontalPanel.ALIGN_CENTER);
		this.setWidget(rootPanel);
		tagTable = new HashMap<String, String>();
		ratingTable = new HashMap<Integer, Rating>();
	}
	
	private void onButtonAddTagActionPerformed(ClickEvent event) {
		String input = textNewTag.getText().trim();
		if (input.equals("")) {
			return;
		}
		String [] newTags = input.split(",[ ]*");
		for (int i = 0; i < newTags.length; i++) {
			String oldTag = tagTable.get(newTags[i].toLowerCase());
			if (oldTag != null) {
				continue;
			}
			Tag tag = new Tag();
			tag.setPhotoId(photo.getPhotoId());
			tag.setTag(newTags[i]);
			dataService.addTag(tag, new CustomTaggingAsyncCallback(newTags[i]));
		}
	}
	
	private void onButtonRateActionPerformed(ClickEvent event) {
		float score = starRating.getValue();
		Rating rating = new Rating();
		rating.setPhotoId(photo.getPhotoId());
		rating.setScore(score);
		rating.setRaterId(user.getUserId());
		Rating oldRating = ratingTable.get(user.getUserId());
		if (oldRating == null) {
			dataService.addRating(rating, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Void result) {
				}	
			});
			photo.getRatings().add(rating);
			ratingTable.put(user.getUserId(), rating);
		} else {
			dataService.updateRating(rating, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(Void result) {
				}	
			});
			oldRating.setScore(rating.getScore());
		}
		
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
		tagTable.clear();
		ratingTable.clear();
		photoView.clearCell(0, 0);
		FitImage image = new FitImage();
		image.setWidth("600px");
		image.setHeight("600px");
		image.setUrl(photo.getUrl());
		photoView.setWidget(0, 0, image);
		ArrayList<String> tags = photo.getTags();
		if (tags.size() == 0) {
			tagString = "";
		} else {
			tagString = tags.get(0);
			tagTable.put(tags.get(0), tags.get(0));
		}
		for (int i = 1; i < tags.size(); i++) {
			tagString += ", ";
			tagString += tags.get(i);
			tagTable.put(tags.get(i), tags.get(i));
		}
		tagView.setText(tagString);
		textNewTag.setText("");
		ArrayList<Rating> ratings = photo.getRatings();
		for (int i = 0; i < ratings.size(); i++) {
			Rating rating = ratings.get(i);
			ratingTable.put(rating.getRaterId(), rating);
		}
		Rating rating = ratingTable.get(user.getUserId());
		if (rating != null) {
			starRating.setValue((int) rating.getScore());
		} else {
			starRating.setValue(0);
		}
	}
	
	private synchronized void updateTag(String tag) {
		photo.getTags().add(tag);
		tagString += ",";
		tagString += tag;
		tagView.setText(tagString);
		tagTable.put(tag, tag);
	}

	public void setCurrentUser(User user) {
		this.user = user;
	}

	private class CustomTaggingAsyncCallback implements AsyncCallback<Void> {

		private String newTag;
		
		public CustomTaggingAsyncCallback(String newTag) {
			this.newTag = newTag;
		}
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			updateTag(newTag);
		}
	}
}
