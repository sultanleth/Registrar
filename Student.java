import java.io.*;
import java.util.*;

class Student extends User {

	int currentTerm = 171;
	String major;
	ArrayList<Course> courses = new ArrayList<>();

	public Student(int id, String first_Name, String middle_Name, String last_Name, String password, String email,
			String nationality, String dateOfBirth, String joiningDate, String major) throws IOException {

		super(id, first_Name, middle_Name, last_Name, password, email, nationality, dateOfBirth, joiningDate);
		this.major = major;

		File reg = new File("C:/Registrar System");
			if(!reg.isDirectory())
			reg.mkdir();
		
		
		try {
			FileInputStream input = new FileInputStream(String.format("C:/Registrar System/s%d.txt", id));
			Scanner scanner = new Scanner(input);
			while (scanner.hasNext()) {
				Course temp = HomePage.getCourse(Integer.parseInt(scanner.next()));
				if (temp != null) {
					courses.add(temp);
					if(temp.term>currentTerm)
						currentTerm = temp.term;
				}
			}
			input.close();
			scanner.close();
		} catch (IOException e) {
			FileWriter output = new FileWriter(String.format("C:/Registrar System/s%d.txt", id));
			output.close();
		}
	}

	public String addCourse(Course co) {
		
		currentTerm = 171;
		
		for(int i=0 ; i<courses.size() ; i++) {
			if(courses.get(i).term>currentTerm)
				currentTerm = courses.get(i).term;
		}
		
		if(co.term<currentTerm)
			return "Registration for the semester of this course is over";
		
		if (co.studentList.size() == co.maxCapacity)
			return "The student capacity in the course (" + co.code + ") is full";

		for (int i = 0; i < courses.size(); i++) {
			Course tmp = courses.get(i);

			if (tmp.code.equals(co.code)) {
				return "Pre-recorded course";
			}

			if (tmp.term == co.term && tmp.days.equals(co.days) && tmp.time.equals(co.time)) {
				return "another course (" + tmp.code + ") in the same time";
			}
		}

		boolean found = true;

		ArrayList<String> coursePre = co.getPrerequisite();
		String pre = "";
		for (int i = 0; i < coursePre.size(); i++) {

			if (found == false)
				return    "-----------------------------------------"
						+ "\nThe request is rejected!"
						+ "\nThe course(s) :\n" 
						+ co.printPrerequisite() 
						+ "\nis(are) a prerequisite for this course ("+co.code+")"
						+ "\n-----------------------------------------\n";

			pre = coursePre.get(i);

			found = false;

			for (int j = 0; j < courses.size(); j++) {

				Course temp = courses.get(j);

				if (temp.term < co.term) {

					if (temp.code.equals(pre)) {
						found = true;
						break;
					}
				}

			}
		}

		if (!found)
			return    "-----------------------------------------"
			+ "\nThe request (adding " + co.code + ") is rejected!"
			+ "\nThe course(s) :\n" 
			+ co.printPrerequisite() 
			+ "\nis(are) a prerequisite for this course"
			+ "\n-----------------------------------------\n";

		courses.add(co);
		co.studentList.add(this);
		if(currentTerm<co.term)
		currentTerm = co.term;

		updata();
		
		return    "\n-----------------------------------------"
				+ "\nThe course (" + co.code + ") is successfully added"
				+ co.toString();
	}

	public String dropCourse(String str) {
		
		int id = Integer.parseInt(str);
		
		for(int i=0 ; i<courses.size() ; i++) {
			if(courses.get(i).courseId==id) {
				if(courses.get(i).term>=currentTerm) {
					return    "\n-----------------------------------------"
							+ "\n" + courses.remove(i).code + " is deleted"
							+ "\n-----------------------------------------\n";
				}else
					return "You can't drop this course!\nThe course (" + courses.get(i).code + ") was finished in the term (" + courses.get(i).term + ")";
			}
		}
		
		return "You can't drop this course!\nThis course does not exist in your Courses List";
	}
	
	public String updata() {
		FileWriter output;
		try {
			output = new FileWriter(String.format("C:/Registrar System/s%d.txt", id));
			PrintWriter pw = new PrintWriter(output);

			for (int i = 0; i < courses.size(); i++) {
				if (i != 0)
					pw.println();
				pw.print(courses.get(i).courseId);
			}

			pw.close();
			output.close();
		} catch (IOException e) {
			return "Error: The data update failed";
		}

		return "Data updated successfully";
	}

	@Override
	public String toString() {
		return    "\n-----------------------------------"
				+ "\nStudent Information:"
				+ "\n-----------------------------------"
				+ "\nID number     : " + id 
				+ "\nFirst Name    : " + first_Name 
				+ "\nMiddle Name   : " + middle_Name
				+ "\nLast Name     : " + last_Name 
				+ "\nNationality   : " + nationality 
				+ "\nDate Of Birth : " + dateOfBirth 
				+ "\nThe Major     : " + major 
				+ "\nJoining Date  : " + joiningDate 
				+ "\nKFUPM E-mail  : " + email
				+ "\n-----------------------------------\n";
	}

	public String getSemester(String num) {
		
		
		int term = 0;
		try {
			term = Integer.parseInt(num);
		}catch(Exception e) {
			return "Error: Invalid Input, Try again!";
			}
		String str = String.format("%-9S\t%-6S\t\t%-7S\t\t%-7S\t%-4S\t\t%-11S\t\t%-8S",
				"Course","ID","Section","Lecturer","Days","Time","Location" );
		for (int i = 0; i < courses.size(); i++) {
			Course temp = courses.get(i);
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
			str = str + "\n\n" + "You don't have any course in this term ("+term+")"
					+ "\nor the input is invalid, check the number of the term";
		
		return str;
	}
	
	public String getAllCourses() {
		String str = String.format("%-9S\t%-6S\t\t%-7S\t\t%-7S\t%-4S",
				"Course","ID","Section","Lecturer","Term" );
		for (int i = 0; i < courses.size(); i++) {
			Course temp = courses.get(i);
			String course = "\n" + temp.code + "\t\t" + temp.courseId + "\t\t" 
							+ temp.section + "\t\t" + temp.lecturer + "\t\t"
							+ temp.term;
				
				str = str + course;
		}
		if(str.equals(String.format("%-9S\t%-6S\t\t%-7S\t\t%-7S\t%-4S",
				"Course","ID","Section","Lecturer","Term" )))
			str = str + "\n\n" + "You don't have any course yet !";
		
		return str;
	}

}
