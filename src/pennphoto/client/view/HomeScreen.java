package pennphoto.client.view;

import java.util.ArrayList;
import java.util.Iterator;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Institution;
import pennphoto.shared.Interest;
import pennphoto.shared.Photo;
import pennphoto.shared.Professor;
import pennphoto.shared.Rating;
import pennphoto.shared.Student;
import pennphoto.shared.Tag;
import pennphoto.shared.User;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.reveregroup.gwt.imagepreloader.FitImage;


public class HomeScreen extends Screen {

	private User user;
	private ArrayList<Photo> photos;
	private ArrayList<User> recommendations;
	
    private ArrayList<TextArea> tas = new ArrayList<TextArea>();
    private ArrayList<FlowPanel> tagsView = new ArrayList<FlowPanel>();
    private ArrayList<org.cobogw.gwt.user.client.ui.Rating> ratingsView = new ArrayList<org.cobogw.gwt.user.client.ui.Rating>();
    private ArrayList<HorizontalPanel> avgScoreView = new ArrayList<HorizontalPanel>();
	
	public HomeScreen(PennPhoto pennphoto, DataServiceAsync dataService) {
		super(pennphoto, dataService);
	}
	
	public void refreshLeft() {
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
	
	public void setRecommendedFriends(ArrayList<User> recommendations) {
		this.recommendations = recommendations;
		CellList<User> cellList = new CellList<User>(new AbstractCell<User>() {
			@Override
			public void render(Context context, User value, SafeHtmlBuilder sb) {
				sb.appendEscapedLines(value.getUserId()+"\n"+value.getFirstname()+"."+value.getLastName()+"\n\n  ");
			}
		});
		final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>();
		cellList.setSelectionModel(selectionModel);
		cellList.setRowCount(recommendations.size());
		cellList.setRowData(0, recommendations);
		friendships.clear();
		friendships.add(new HTML("<i style='color:#C0C0C0'>People you may know</i></br>"));
		friendships.setSpacing(5);
		friendships.add(cellList);
		friendships.add(new HTML("_____________________"));
	}
	
	public void setNews(ArrayList<Photo> photos) {
		center.clear();
		tas.clear();
		tagsView.clear();
		ratingsView.clear();
		avgScoreView.clear();
		VerticalPanel newsPanel = new VerticalPanel();
		newsPanel.setSpacing(6);
		this.photos = photos;
		for (int i = 0; i < photos.size(); i++) {
			Photo photo = photos.get(i);
			FitImage image = new FitImage();
			
			image.setUrl(photo.getUrl());
			image.setFixedSize(200, 200);
			image.addClickHandler(new ImageClickHandler(photo));
			DOM.setStyleAttribute(image.getElement(), "cursor", "pointer");

			VerticalPanel vp = new VerticalPanel();	
			vp.addStyleName("gwt-picPanel");
			HorizontalPanel north = new HorizontalPanel();
			Hyperlink a = new Hyperlink();
			a.setText(photo.getOwner().getFirstname() + " " + photo.getOwner().getLastName());
			//a.onBrowserEvent();
			north.add(new HTML("<h2 style='font-family:verdana;color:blue'>"+a+"  </h2>"));
			north.add(new HTML("<h2 style='color:#FFFFFF'>0</h2>"));
			north.add(new HTML("<h2 style='font-family:verdana;color:#000099'> has posted a photo</h2>"));

			vp.add(north);
			//vp.add(new HTML("<h4 style='color:blue'>"+a+"has uploaded a new photo!<h4>"));
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(5);
			hp.add(image);
			VerticalPanel tagsAndOptions = new VerticalPanel();
			FlowPanel fp = new FlowPanel();
			tagsView.add(fp);
			
			tagsAndOptions.add(new HTML("<i style='color:#C0C0C0'>Here is what friends say about this photo:</i></br>"));	

			ArrayList<String> tags = photo.getTags();
			for(int j = 0; j < tags.size(); j++){
				Button t = new Button();
				t.setStyleName("gwt-button"+j%6);
				t.setText(tags.get(j));
				fp.add(t);
			}

			tagsAndOptions.add(fp);
			tagsAndOptions.setCellHeight(fp, "80px");
			Label addTag = new Label("Do you also want to add a tag?");
			addTag.setStyleName("gwt-addTag");
			TextArea tb = new TextArea();
			tas.add(tb);
			tb.setWidth("300px");
			tagsAndOptions.add(addTag);
			tagsAndOptions.add(tb);
			Button addTagB = new Button("Add Tag");

			addTagB.addClickHandler(new NewTagClickHandler(i));
			tagsAndOptions.add(addTagB);
			tagsAndOptions.setCellHorizontalAlignment(addTagB, HasHorizontalAlignment.ALIGN_RIGHT);
			
			
			hp.add(tagsAndOptions);
			hp.setCellWidth(tagsAndOptions, "340px");
			VerticalPanel ap = new VerticalPanel();
			ap.setStyleName("gwt-score-border");
			ap.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			Label las = new Label("Average Score");
			las.addStyleName("gwt-avgScore");
			ap.add(las);
			HorizontalPanel aap = new HorizontalPanel();
			avgScoreView.add(aap);
			ap.add(aap);
			this.updateRating(i);
			ap.add(new HTML("</br>"));
			org.cobogw.gwt.user.client.ui.Rating starRating = new org.cobogw.gwt.user.client.ui.Rating(0, 5);
			ap.add(starRating);
			ratingsView.add(starRating);
			ap.add(new HTML("<br>"));
			Button rateThisPhoto = new Button("Rate this Photo");
			rateThisPhoto.addClickHandler(new NewRatingClickHandler(i));
			ap.add(rateThisPhoto);
            hp.add(ap);           
		    vp.add(hp);
			newsPanel.add(vp);
		}
		center.add(newsPanel);
	}
	
	public void setData(User user) {
		this.user = user;
		this.refreshLeft();
	}
	
	private void updateRating(int index) {
		Photo photo = photos.get(index);
		ArrayList<Rating> ratings = photo.getRatings();
		float sum = 0;
		for (int j = 0; j < ratings.size(); j++) {
			sum += ratings.get(j).getScore();
		}
		sum /= ratings.size();
		sum *= 10;
		int score = (int) sum;
		int beforeDot = score/10;
		int afterDot = score%10;
		FitImage bimage = new FitImage();
		bimage.setUrl("pics/"+beforeDot+".png");
		FitImage aimage = new FitImage();
		aimage.setUrl("pics/"+afterDot+".png");
		FitImage dimage = new FitImage();
		dimage.setUrl("pics/dot.png");
		avgScoreView.get(index).clear();
		avgScoreView.get(index).add(bimage);
		avgScoreView.get(index).add(dimage);
		avgScoreView.get(index).add(aimage);
	}
	
	private synchronized void updateTag(String tag, int index) {
		FlowPanel fp = tagsView.get(index);
		Button t = new Button();
		t.setStyleName("gwt-button"+photos.get(index).getTags().size()%6);
		t.setText(tag);
		fp.add(t);
	}
	
	private class ImageClickHandler implements ClickHandler {

		private Photo photo;
		
		public ImageClickHandler(Photo photo) {
			this.photo = photo;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			pennphoto.viewImage(photo);
		}
		
	}
	
	private class NewTagClickHandler implements ClickHandler {
		
		public final int counter;
		
		public NewTagClickHandler(int counter) {
		    this.counter = counter;  
		}

		@Override
		public void onClick(ClickEvent event) {
			String input = tas.get(counter).getText().trim();
			if (input.equals("")) {
				return;
			}
			String [] newTags = input.split(",[ ]*");
			ArrayList<String> oldTags = photos.get(counter).getTags();
			int j;
			for (int i = 0; i < newTags.length; i++) {
				for (j = 0; j < oldTags.size(); j++) {
					if (oldTags.get(j).toLowerCase().equals(newTags[i])) {
						break;
					}
				}
				if (j != oldTags.size()) {
					continue;
				}
				oldTags.add(newTags[i]);
				Tag tag = new Tag();
				tag.setPhotoId(photos.get(counter).getPhotoId());
				tag.setTag(newTags[i]);
				dataService.addTag(tag, new CustomTaggingAsyncCallback(newTags[i], counter));
			}
		}
	}
	
	private class NewRatingClickHandler implements ClickHandler {

		private int index;
		
		public NewRatingClickHandler(int index) {
			this.index = index;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			int value = ratingsView.get(index).getValue();
			if (value == 0) {
				return;
			}
			ArrayList<Rating> ratings = photos.get(index).getRatings();
			int i;
			for (i = 0; i < ratings.size(); i++) {
				if (ratings.get(i).getRaterId() == user.getUserId()) {
					break;
				}
			}
			Rating rating = new Rating();
			rating.setPhotoId(photos.get(index).getPhotoId());
			rating.setRaterId(user.getUserId());
			rating.setScore(value);
			if (i == ratings.size()) {
				dataService.addRating(rating, new CustomAddRatingAsyncCallback(index, rating)); 
			} else {
				dataService.updateRating(rating, new CustomUpdateRatingAsyncCallback(index, value, i));
			}
		}
	}
	
	private class CustomTaggingAsyncCallback implements AsyncCallback<Void> {

		private String tag;
		int counter;
		
		public CustomTaggingAsyncCallback(String tag, int counter) {
			this.tag = tag;
			this.counter = counter;
		}
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			updateTag(tag, counter);
		}
	}
	
	private class CustomAddRatingAsyncCallback implements AsyncCallback<Void> {

		private int index;
		private Rating rating;
		
		public CustomAddRatingAsyncCallback(int index, Rating rating) {
			this.index = index;
			this.rating = rating;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			photos.get(index).getRatings().add(rating);
			updateRating(index);
		}
		
	}
	
	private class CustomUpdateRatingAsyncCallback implements AsyncCallback<Void> {

		private int index;
		private int score;
		private int raterIndex;
		
		public CustomUpdateRatingAsyncCallback(int index, int score, int raterIndex) {
			this.index = index;
			this.score = score;
			this.raterIndex = raterIndex;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Void result) {
			photos.get(index).getRatings().get(raterIndex).setScore(score);
			updateRating(index);
		}
		
	}
}
