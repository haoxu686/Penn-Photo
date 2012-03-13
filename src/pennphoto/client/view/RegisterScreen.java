package pennphoto.client.view;

import java.util.ArrayList;

import pennphoto.client.model.DataServiceAsync;
import pennphoto.shared.Professor;
import pennphoto.shared.Student;
import pennphoto.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;


public class RegisterScreen extends Screen {
	User user           = new User();
	Student student     = new Student();
	Professor professor = new Professor();
	ArrayList<Professor> profs = new ArrayList<Professor>();
	ArrayList<Student> stds = new ArrayList<Student>();	
	int userID;
	
	boolean isStudent    = false;
	boolean isMale       = false;
	boolean identityEdited = false;
	boolean sexEdited    = false;
    TextBox textBox                 = new TextBox();
    TextBox textBox_1               = new TextBox();
    RadioButton rdbtnStudent        = new RadioButton("identity", "Student");
    RadioButton rdbtnNewRadioButton = new RadioButton("identity", "Professor");
    RadioButton rdbtnFemale         = new RadioButton("gender", "Female");
    RadioButton rdbtnMale           = new RadioButton("gender", "Male");
    TextBox textBox_2               = new TextBox();
    ListBox yearLB                  = new ListBox();
    
    ListBox monthLB         = new ListBox();
    ListBox dayLB           = new ListBox();
   
    DateBox dateBox         = new DateBox();
    TextBox textBox_3       = new TextBox();
    PasswordTextBox ptb     = new PasswordTextBox();
    PasswordTextBox ptb2    = new PasswordTextBox();
    TextBox major           = new TextBox();
    DoubleBox gpa           = new DoubleBox();
    ListBox proLB           = new ListBox();
    ListBox stdLB           = new ListBox();
    IntegerBox yearsAdvised = new IntegerBox();
    Button Ssubmit          = new Button("Submit");
    Button btnCancelButton  = new Button("Cancel");
    TextBox researchArea    = new TextBox();
    TextBox office          = new TextBox();
    Button Psubmit          = new Button("Submit");
	HTML headline = new HTML("<h1>PENN PHOTO USER ACCOUNT REGISTRATION</h1>");

	public RegisterScreen(PennPhoto pennphoto, DataServiceAsync dataService) {
		super(pennphoto, dataService);
	}

	private void onCancelButtonActionPerformed(ClickEvent event) {
		pennphoto.setCurrentScreen(pennphoto.getLoginScreen());
	}
	
	public void refresh() {
		center.clear();
        leftRoot.clear();
        yearLB.addItem("");
        monthLB.addItem("");
        dayLB.addItem("");
        for (int i=2012; i>=1900;i--)
            yearLB.addItem(""+i);
        for (int i=1; i<=12; i++)
        	monthLB.addItem(""+i);
        for (int i=1; i<=31; i++)
        	dayLB.addItem(""+i);
        dpContainer.setBorderWidth(0);
        right.clear();

        FlexTable flexTable = new FlexTable();

       
        Label lblName = new Label("Firstname");
        flexTable.setWidget(0, 0, lblName);
       
 
        flexTable.setWidget(0, 1, textBox);
       
        Label lblLastname = new Label("Lastname");
        flexTable.setWidget(1, 0, lblLastname);
       
        
        flexTable.setWidget(1, 1, textBox_1);
       
        Label lblStudentOrProfessor = new Label("Student or Professor?");
        flexTable.setWidget(2, 0, lblStudentOrProfessor);
       
        FlexTable flexTable_1 = new FlexTable();
        flexTable.setWidget(2, 1, flexTable_1);
       

        flexTable_1.setWidget(0, 0, rdbtnStudent);
        rdbtnStudent.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				isStudent = true;
				identityEdited = true;
				
			}
        	
        });
       

        flexTable_1.setWidget(1, 0, rdbtnNewRadioButton);
        rdbtnNewRadioButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				isStudent = false;
				identityEdited = true;				
			}
        	
        });
       

       
        Label lblGender = new Label("Gender");
        flexTable.setWidget(3, 0, lblGender);
       
        FlexTable flexTable_2 = new FlexTable();
        flexTable.setWidget(3, 1, flexTable_2);
       

        flexTable_2.setWidget(0, 0, rdbtnFemale);
        rdbtnFemale.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				sexEdited = true;
				isMale = false;
			}
        	
        });
       

        flexTable_2.setWidget(1, 0, rdbtnMale);
        rdbtnMale.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				sexEdited = true;
				isMale = true;
			}
        	
        });
        
       
        Label lblEmail = new Label("Email");
        flexTable.setWidget(4, 0, lblEmail);
       
 
        flexTable.setWidget(4, 1, textBox_2);
       
        Label lblNewLabel = new Label("Date of Birth");
        flexTable.setWidget(5, 0, lblNewLabel);
       
        HorizontalPanel pickDate = new HorizontalPanel();
        //yearLB.setItemText(1, "1989");
        pickDate.add(yearLB);
        pickDate.add(monthLB);
        pickDate.add(dayLB);
        flexTable.setWidget(5, 1, pickDate);
       
        Label lblAddress = new Label("Address");
        flexTable.setWidget(6, 0, lblAddress);
       

        flexTable.setWidget(6, 1, textBox_3);
        
        Label pw = new Label("Password");
        flexTable.setWidget(7, 0, pw);
        flexTable.setWidget(7, 1, ptb);

        Label pwa = new Label("Password Again");
        flexTable.setWidget(8, 0, pwa);
        flexTable.setWidget(8, 1, ptb2);      
        
        btnCancelButton = new Button("Cancel");
        btnCancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onCancelButtonActionPerformed(event);
			}
        });
        Button btnNewButton = new Button("Next");
        btnNewButton.addClickHandler(new regClickHandler());
        flexTable.setWidget(9, 0, btnCancelButton);
        flexTable.setWidget(9, 1, btnNewButton);
        center.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        center.add(headline);
        center.add(flexTable);
	}
	
    public class regClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			if(textBox.getText().isEmpty()){
				pennphoto.showAlert("The firstname field is empty!");
				return;
			}
			if(textBox_1.getText().isEmpty()){
				pennphoto.showAlert("The lastname field is empty!");
				return;
			}
			if(!identityEdited){
				pennphoto.showAlert("Please select your identity!");
				return;
			}
			if(!sexEdited){
				pennphoto.showAlert("Please select your sexuality!");
				return;
			}
			if(textBox_2.getText().isEmpty()){
				pennphoto.showAlert("The email field is empty!");
				return;
			}
			if(yearLB.getSelectedIndex()==0||monthLB.getSelectedIndex()==0||dayLB.getSelectedIndex()==0){
				pennphoto.showAlert("The are three field of birthday should be selected");
				return;
			}
			if(textBox_3.getText().isEmpty()){
				pennphoto.showAlert("The address field is empty!");
				return;
			}
			if(ptb.getText().isEmpty()){
				pennphoto.showAlert("The password field shouldn't be empty!");
				return;
			}
			if(!ptb.getText().equals(ptb2.getText())){
				pennphoto.showAlert("The two password must be the same!");
				return;
			}
			
			int year = yearLB.getSelectedIndex();
			year = 2013- year;
			int month = monthLB.getSelectedIndex();
			int day = dayLB.getSelectedIndex();
			int total = year*10000+month*100+day;
			String yearString = Integer.valueOf(total/10000).toString();
			String monthString = Integer.valueOf((total%10000)/100).toString();
			String dayString = Integer.valueOf(total%100).toString();
			
			String dob = yearString+"-"+monthString+"-"+dayString;

			
			//user.setUserId(userID+1);
			user.setFirstname(textBox.getText());
			user.setLastName(textBox_1.getText());
			if(isMale) {
				user.setGender("M");
			}
			else {
				user.setGender("F");
			}

			user.setEmail(textBox_2.getText());
			user.setDob(dob);
			user.setAddress(textBox_3.getText());
			user.setPassword(ptb.getText());
			if(isStudent){
				///student.setUserId(userID+1);
				student.setStuId("std"+(userID+1));
				student.setFirstname(textBox.getText());
				student.setLastName(textBox_1.getText());
                user.setType("S");
                student.setType("S");
				if(isMale) {
					student.setGender("M");
				} else {
					student.setGender("F");
				}

				student.setEmail(textBox_2.getText());
				student.setDob(dob);
				student.setAddress(textBox_3.getText());
				student.setPassword(ptb.getText());
			}
			else{
				//professor.setUserId(userID+1);
				professor.setFirstname(textBox.getText());
				professor.setLastName(textBox_1.getText());
				user.setType("P");
				professor.setType("P");
				if(isMale) {
					professor.setGender("M");
				}
				else {
					professor.setGender("F");
				}

				professor.setEmail(textBox_2.getText());
				professor.setDob(dob);
				professor.setAddress(textBox_3.getText());
				professor.setPassword(ptb.getText());
			}

			
			if(isStudent){

				setNextScreenForS();
				
			}
			else{
				setNextScreenForP();
				
			}
		}
		public void setNextScreenForS(){

			dataService.getProfessors(new AsyncCallback<ArrayList<Professor>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<Professor> result) {
					int size = result.size();
					proLB.addItem("");
					for(int i=0; i<size; i++){
						profs.add(result.get(i));
						proLB.addItem(result.get(i).getFirstname()+" "+result.get(i).getLastName());
					}
					
				}
				
			});
			center.clear();
			FlexTable ft = new FlexTable();
			ft.setWidget(0,0, new Label("Major"));
			ft.setWidget(0, 1, major);
			ft.setWidget(1, 0, new Label("GPA"));
			ft.setWidget(1, 1, gpa);
			ft.setWidget(2, 0, new Label("Advisor"));
			ft.setWidget(2, 1, proLB);
			ft.setWidget(3, 0, new Label("Years Advised"));
			ft.setWidget(3, 1, yearsAdvised);
			ft.setWidget(4, 0, btnCancelButton);
			ft.setWidget(4, 1, Ssubmit);
			Ssubmit.addClickHandler(new ssClickHandler());
			center.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			center.add(headline);
			center.add(ft);
			
		}
		public void setNextScreenForP(){

			center.clear();
			FlexTable ft = new FlexTable();
			ft.setWidget(0, 0, new Label("Research Area"));
			ft.setWidget(0, 1, researchArea);
			ft.setWidget(1, 0, new Label("Office"));
			ft.setWidget(1, 1, office);
			ft.setWidget(2, 0, btnCancelButton);
			ft.setWidget(2, 1, Psubmit);
			Psubmit.addClickHandler(new ppClickHandler());
			center.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			center.add(headline);
			center.add(ft);
			
		}
		public class ssClickHandler implements ClickHandler{

			@Override
			public void onClick(ClickEvent event) {
				if(major.getText().isEmpty()){
					pennphoto.showAlert("The major field shouldn't be empty!");
					return;
				}			
				if(gpa.getText().isEmpty()){
					pennphoto.showAlert("The gpa field shouldn't be empty!");
					return;
				}
				if(proLB.getSelectedIndex()==0){
					pennphoto.showAlert("You should at least show one advisor!");
					return;				
				}
				if(yearsAdvised.getText().isEmpty()){
					pennphoto.showAlert("The years advised field shouldn't be empty!");
					return;
				}
				
				student.setMajor(major.getText());
				try {
					student.setGpa(Float.valueOf(gpa.getText()));
				} catch (NumberFormatException e) {
					pennphoto.showAlert("Incorrect format of GPA!");
					return;
				}
				student.setAdvisor(profs.get(proLB.getSelectedIndex()-1));
				try {
					student.setYearsAdvised(Integer.valueOf(yearsAdvised.getText()));
				} catch (NumberFormatException e) {
					pennphoto.showAlert("Incorrect format of Years Advised!");
					return;
				}
				addStudent();

			}
			
		}
		public class ppClickHandler implements ClickHandler{

			@Override
			public void onClick(ClickEvent event) {
				if(researchArea.getText().isEmpty()){
					pennphoto.showAlert("The research field shouldn't be empty!");
					return;
				}
				if(office.getText().isEmpty()){
					pennphoto.showAlert("The office field shouldn't be empty!");
					return;
				}
				professor.setResearchArea(researchArea.getText());
				professor.setOffice(office.getText());
				addProfessor();
			}
			
		}
    	
    }
	public void addStudent() {
		dataService.addStudent(student, new AsyncCallback<Integer>(){
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(Integer result) {
				pennphoto.setCurrentScreen(pennphoto.getLoginScreen());
				pennphoto.showAlert("Your user id is "+ result.intValue()+". Please keep this value at a safe place.");
			}
			
		});
	}
	
	public void addProfessor() {
		dataService.addProfessor(professor, new AsyncCallback<Integer>(){
			@Override
			public void onFailure(Throwable caught) {
				pennphoto.showAlert("The server has failed to allocate a legal user id.");		
			}
			@Override
			public void onSuccess(Integer result) {
				pennphoto.setCurrentScreen(pennphoto.getLoginScreen());
				pennphoto.showAlert("Your user id is "+ result.intValue()+". Please keep this value at a safe place.");
			}
		});
	}
}
