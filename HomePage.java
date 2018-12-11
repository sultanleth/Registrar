import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomePage extends Application {
		
	ScrollPane scrollPane;
	ScrollPane scrollPane_stu;
    int pos = 0;
    final int minPos = 0;
    final int maxPos = 100;
	
	static FileInputStream inputFile;
	static FileOutputStream outputFile;
	static PrintWriter pw;

	static ArrayList<Student> studentsList = new ArrayList<>();
	static ArrayList<Course> coursesList = new ArrayList<>();

	static Student user = null;

	public static void main(String[] args) throws IOException {
		
		setup_Courses();
		setup_Students();

		launch(args);
	}
	
	private static Student Authentication(String id, String password) {
		
		if (id.charAt(0) == '2') {
			for (int i = 0; i < studentsList.size(); i++) {
				if (studentsList.get(i).id == Integer.parseInt(id) && studentsList.get(i).checkPassword(password)) {
					return studentsList.get(i);
				}
			}
		}

		return null;
	}

	public static void setup_Students() throws IOException {

		FileInputStream setup = new FileInputStream("src/setup_Students");
		Scanner readfile = new Scanner(setup);
		while(readfile.hasNext()) {
			int id = Integer.parseInt(readfile.next());
			String fName = readfile.next();
			String mName = readfile.next();
			String lName = readfile.next();
			String password = "s"+id;
			String email = "s"+id+"@kfupm.edu.sa";
			String nationality = readfile.next();
			String dateOfBirth = readfile.next();
			String joiningDate = readfile.next();
			String major = readfile.next();
			Student stu = new Student(id,fName,mName,lName,password,email,nationality,dateOfBirth,joiningDate,major);
			studentsList.add(stu);
		}
		
		readfile.close();
	}

	public static void setup_Courses() throws IOException {

		FileInputStream setup = new FileInputStream("src/setup_courses");
		Scanner readfile = new Scanner(setup);
		while(readfile.hasNextLine()) {
			
			String name = readfile.nextLine();
			String code = readfile.nextLine();
			int section = Integer.parseInt(readfile.nextLine());
			int courseId = Integer.parseInt(readfile.nextLine());
			int term = Integer.parseInt(readfile.nextLine());
			String lecturer = readfile.nextLine();
			String time = readfile.nextLine();
			String location = readfile.nextLine();
			String days = readfile.nextLine();
			int maxCapacity = Integer.parseInt(readfile.nextLine());
			int minCapacity = Integer.parseInt(readfile.nextLine()); 
			
			Course c = new Course(name,code,section,courseId,term,lecturer,time,location,days,maxCapacity,minCapacity);
			coursesList.add(c);
			
		}
		
		
		readfile.close();
		
		
		for(int i=0 ;i<8 ; i++) {
			coursesList.get(1+(i*18)).setPrerequisite(coursesList.get(0));
			coursesList.get(2+(i*18)).setPrerequisite(coursesList.get(0),coursesList.get(1));
			coursesList.get(4+(i*18)).setPrerequisite(coursesList.get(3));
			coursesList.get(6+(i*18)).setPrerequisite(coursesList.get(5));
			coursesList.get(10+(i*18)).setPrerequisite(coursesList.get(0),coursesList.get(1),coursesList.get(9));
			coursesList.get(11+(i*18)).setPrerequisite(coursesList.get(0),coursesList.get(1),coursesList.get(9));
			coursesList.get(12+(i*18)).setPrerequisite(coursesList.get(10));
			coursesList.get(13+(i*18)).setPrerequisite(coursesList.get(12));
			coursesList.get(14+(i*18)).setPrerequisite(coursesList.get(9));
			coursesList.get(15+(i*18)).setPrerequisite(coursesList.get(14));
			coursesList.get(16+(i*18)).setPrerequisite(coursesList.get(0),coursesList.get(1),coursesList.get(9));
		}
	}

	public static Course getCourse(int id) {

		for (int i = 0; i < coursesList.size(); i++) {
			if (coursesList.get(i).courseId == id) {
				return coursesList.get(i);
			}
		}
		return null;
	}

	public String getSemester(String num) {
		int term=0;
		try {
		term = Integer.parseInt(num);
	}catch(Exception e) {
		return "Error: Invalid Input, Try again!";
		}
		String str = String.format("%-9S\t%-6S\t\t%-7S\t\t%-7S\t%-4S\t\t%-11S\t\t%-8S",
				"Course","ID","Section","Lecturer","Days","Time","Location" );
		for (int i = 0; i < coursesList.size(); i++) {
			Course temp = coursesList.get(i);
			if (temp.term == term) {
				String course = "\n" + temp.code + "\t\t" + temp.courseId + "\t\t" 
							+ temp.section + "\t\t" + temp.lecturer + "\t\t"
							+ temp.days + "\t\t" + temp.time + "\t\t" 
							+ temp.location;
				
				str = str + course;
			}
		}
		if(str.equals(String.format("%-9S\t%-6S\t\t%-7S\t\t%-7S\t%-4S\t\t%-11S\t\t%-8S",
				"Course","ID","Section","Lecturer","Days","Time","Location" )))
			str = str + "\n\n" + "invalid input, check the number of the term";
		
		return str;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		Image logo = new Image("KFUPM.jpg");
		ImageView viewLogo = new ImageView(logo);
		
		viewLogo.setFitHeight(200);
		viewLogo.setFitWidth(200);
		
		VBox root = new VBox(10);
		root.setAlignment(Pos.CENTER);
		
		Label enterId = new Label("Enter your ID number:");
		Label enterPassword = new Label("Enter your password:");
		Label message = new Label("");
		message.setAlignment(Pos.CENTER);
		
		TextField id = new TextField();
		id.setMaxWidth(200);
		TextField password = new TextField(); 
		password.setMaxWidth(200);
		
		HBox btns = new HBox(20); 
		Button login = new Button("Login");
		Button cancel = new Button("Cancel");
		btns.getChildren().addAll(login,cancel);
		btns.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(viewLogo,enterId,id,enterPassword,password,btns,message);
		
		login.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	if(id.getText().isEmpty() || password.getText().isEmpty()) {
            		message.setText("Enter your username or password");
            	}else {
            	user = Authentication(id.getText(), password.getText());
            	if(user == null) {
            		message.setText("The username or password you typed is incorrect.\n"
            				+ "Please try again. If you still cannot log in, contact your system administrator.");
                	id.setText("");
                	password.setText("");
            	}else {
            		studentPanel();
            		primaryStage.close();
            	}
            	}	
            }
        });
		
		
		cancel.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	primaryStage.close();
            }
        });
		
		Scene scene = new Scene(root,500,500);

		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Registrar System");
		primaryStage.show();
	}
	
	public void studentPanel() {
		
		Image logo = new Image("KFUPM.jpg");
		ImageView viewLogo = new ImageView(logo);
		
		viewLogo.setFitHeight(150);
		viewLogo.setFitWidth(150);
		
		Stage studentStage = new Stage();
		
		
		HBox B1 = new HBox(30);
		B1.setAlignment(Pos.CENTER);
		VBox B12 = new VBox(5);
		B12.setAlignment(Pos.TOP_CENTER);
		HBox B121 = new HBox(2);
		B121.setAlignment(Pos.TOP_CENTER);
		VBox B13 = new VBox(10);
		B13.setAlignment(Pos.TOP_CENTER);
		HBox B132 = new HBox(20);
		B132.setAlignment(Pos.TOP_CENTER);
		VBox B14 = new VBox(5);
		B14.setAlignment(Pos.TOP_CENTER);
		HBox B141 = new HBox(2);
		B141.setAlignment(Pos.TOP_CENTER);
		
		
		B12.setPrefWidth(480);
		B14.setPrefWidth(480);
		
		B1.getChildren().addAll(B12,B13,B14);
		
		//B12
		
		Label header1 = new Label("");
		header1.setPrefHeight(100);
		
		Label labelSemester = new Label("Enter the number of the Semester:");
		TextField NumSemester = new TextField("");
		
		Button getTerm = new Button("Run");
		
		Label semesterCourses = new Label("List of courses available in the semester ");
		Label list_semesterCourses = new Label("");
		list_semesterCourses.setFont(new Font(11));
		
		list_semesterCourses.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                if (event.getDeltaY() > 0)
                    scrollPane.setHvalue(pos == minPos ? minPos : pos--);
                else
                    scrollPane.setHvalue(pos == maxPos ? maxPos : pos++);

            }
        });


        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(list_semesterCourses);
		
        
		B121.getChildren().addAll(NumSemester,getTerm);
		B12.getChildren().addAll(header1,labelSemester,B121,semesterCourses,scrollPane);
		
		//B14
		
		Label header3 = new Label("");
		header3.setPrefHeight(100);
		
		
		Label stuLabelSemester = new Label("Enter the number of the Semester:");
		TextField stuNumSemester = new TextField("");
		
		Button getStuTerm = new Button("Run");
		Button getAllStuCourses = new Button("All Courses");
		
		Label stuSemesterCourses = new Label("List your courses for the semester");
		Label stuList_semesterCourses = new Label("");
		stuList_semesterCourses.setFont(new Font(11));
		
		stuList_semesterCourses.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                if (event.getDeltaY() > 0)
                	scrollPane_stu.setHvalue(pos == minPos ? minPos : pos--);
                else
                	scrollPane_stu.setHvalue(pos == maxPos ? maxPos : pos++);

            }
        });


		scrollPane_stu = new ScrollPane();
		scrollPane_stu.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane_stu.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane_stu.setPannable(true);
        scrollPane_stu.setFitToWidth(true);
        scrollPane_stu.setContent(stuList_semesterCourses);
		
		

		B141.getChildren().addAll(stuNumSemester,getStuTerm,getAllStuCourses);
		B14.getChildren().addAll(header3,stuLabelSemester,B141, stuSemesterCourses,scrollPane_stu);
		
		//B13
		
		B13.setPrefWidth(300);
		
		Label header2 = new Label("");
		header2.setPrefHeight(30);
		
		Label labelID = new Label("Enter the ID number Of the course:");
		TextField ID = new TextField("");
	
		Button course_Info = new Button("   Course\ninformation");
		Button addCourse = new Button("  Add\nCourse");
		Button dropCourse = new Button(" Drop\nCourse");
		
		B132.getChildren().addAll(course_Info,addCourse,dropCourse);
		
		Label message = new Label("");
	
		Button student_Info = new Button("Personal information");
		student_Info.setAlignment(Pos.BOTTOM_CENTER);
		
		B13.getChildren().addAll(header2,viewLogo,labelID,ID,B132,message,student_Info);
		
		//end of B13
		
		student_Info.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	message.setText(user.toString());
            }
        });
		
		course_Info.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {	
            	if(ID.getText().isEmpty()) {
            		message.setFont(new Font(18));
            		message.setText("Enter the ID of the course");
            	}
            	else {
            		try {
            		Course c = getCourse(Integer.parseInt(ID.getText()));
    				if (c != null) {
    					viewLogo.setFitHeight(100);
    					viewLogo.setFitWidth(100);
    					message.setFont(new Font(12));
    					message.setText(c.toString());
    				}else {
    					message.setText("This course does not exist!");	
    					message.setFont(new Font(18));
    				}
            	}catch(Exception e) {
            		message.setFont(new Font(18));
            		message.setText("Error: Invalid input\nEnter the ID of the course");
            	}
            	}
            }
        });
		
		addCourse.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	if(ID.getText().isEmpty()) {
            		message.setText("Enter the ID of the course");
            		message.setFont(new Font(18));
            	}else {
            		try {
            		Course c = getCourse(Integer.parseInt(ID.getText()));
    				if (c != null) {
    					message.setText(user.addCourse(c));
    					message.setFont(new Font(12));
    				}else {
    					message.setText("This course does not exist!");
    					message.setFont(new Font(18));
    					}
            		}catch(Exception e) {
                		message.setFont(new Font(20));
                		message.setText("Error: Invalid input\nEnter the ID of the course");
                	}
            	}
            }
        });

		dropCourse.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	if(ID.getText().isEmpty()) {
            		message.setText("Enter the ID of the course");
            		message.setFont(new Font(20));
            	}else {
            		try {
            		message.setText(user.dropCourse(ID.getText()));
            		message.setFont(new Font(15));
            		user.updata();
            		}catch(Exception e) {
                		message.setFont(new Font(20));
                		message.setText("Error: Invalid input\nEnter the ID of the course");
                	}
            	}	
            }
        });

		getStuTerm.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	if(stuNumSemester.getText().isEmpty()) {
            		stuList_semesterCourses.setText("Enter the number of semester");
            		stuList_semesterCourses.setFont(new Font(18));
            	}else {
            		stuSemesterCourses.setText("List of your courses for the semester " + stuNumSemester.getText());
            		stuList_semesterCourses.setText(user.getSemester(stuNumSemester.getText()));
            		stuList_semesterCourses.setFont(new Font(11));
            	}
            }
        });
		
		getAllStuCourses.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            		stuSemesterCourses.setText("List of your courses");
            		stuList_semesterCourses.setText(user.getAllCourses());
            		stuList_semesterCourses.setFont(new Font(12));
            }
        });
		
		
		getTerm.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
            	if(NumSemester.getText().isEmpty()) {
            		list_semesterCourses.setText("Enter the number of semester");
            		list_semesterCourses.setFont(new Font(18));
            	}else {
            		semesterCourses.setText("List your courses for the semester " + NumSemester.getText());
            		list_semesterCourses.setText(getSemester(NumSemester.getText()));
            		list_semesterCourses.setFont(new Font(11));
            	}
            }
        });
				
		Scene studentScene = new Scene(B1);
		studentStage.setMaximized(true);
		studentStage.setScene(studentScene);
		studentStage.setTitle("Student Panel");
		studentStage.show();
		
	}
}