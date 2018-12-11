import java.util.ArrayList;

class Course {

	String courseName;
	String code;
	int section;
	int courseId;
	int term;
	String lecturer;
	String time;
	String location;
	String days;
	int maxCapacity;
	int minCapacity;
	ArrayList<Student> studentList = new ArrayList<>();
	ArrayList<String> prerequisite = new ArrayList<>();
	
	
	public Course(String courseName, String code, int section, int courseId, int term, String lecturer, String time,
			String location, String days, int maxCapacity, int minCapacity) {
		
		this.courseName = courseName;
		this.code = code;
		this.section = section;
		this.courseId = courseId;
		this.term = term;
		this.lecturer = lecturer;
		this.time = time;
		this.location = location;
		this.days = days;
		this.maxCapacity = maxCapacity;
		this.minCapacity = minCapacity;	
		
	}
	
	public void setPrerequisite(Course c1) {
		setPrerequisite(c1,null,null);
	}
	
	public void setPrerequisite(Course c1 , Course c2) {
		setPrerequisite(c1,c2,null);
	}
	
	public void setPrerequisite(Course c1 , Course c2 , Course c3) {
		
		if(c1!=null)
			prerequisite.add(c1.code);
		if(c2!=null)
			prerequisite.add(c2.code);
		if(c3!=null)
			prerequisite.add(c3.code);
				
	}
	
	public ArrayList<String> getPrerequisite(){
		ArrayList<String> pre = new ArrayList<>();
		for(int i=0 ; i<prerequisite.size() ; i++) {
			pre.add(prerequisite.get(i));
		}
		return pre;
	}
	
	public String printPrerequisite() {
		String pre="";
		
		for(int i=0 ; i<prerequisite.size() ; i++) {
			if(i!=0)
				pre = pre + "\n";
			pre = pre + prerequisite.get(i);
		}
		
		if(pre.length()==0)
			pre = "No prerequisite for this course";
		
		return pre;
	}
	
	@Override
	public String toString() {
	
		return 	  "\n-----------------------------------"
				+ "\nCourse Information:"
				+ "\n-----------------------------------"
				+ "\ncourse Name   : " + courseName 
				+ "\nCode          : " + code 
				+ "\nSection       : " + section 
				+ "\nCourse Id     : " + courseId
				+ "\nTerm          : " + term 
				+ "\nLecturer      : " + lecturer 
				+ "\nTime          : " + time
				+ "\nDays          : " + days
				+ "\nLocation      : " + location 
				+ "\nMax. Capacity : " + maxCapacity 
				+ "\nMin. Capacity : " + minCapacity
				+ "\n------------------"
				+ "\nPrerequisite Courses:\n" + printPrerequisite()
				+ "\n-----------------------------------\n";
	}

}
