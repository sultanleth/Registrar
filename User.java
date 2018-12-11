
public class User {

	int id;
	String first_Name;
	String middle_Name;
	String last_Name;
	private String password;
	String email;
	String nationality;
	String dateOfBirth;
	String joiningDate;
	
	
	public User(int id, String first_Name, String middle_Name, String last_Name, String password, String email,
			String nationality, String dateOfBirth, String joiningDate) {

		this.id = id;
		this.first_Name = first_Name;
		this.middle_Name = middle_Name;
		this.last_Name = last_Name;
		this.password = password;
		this.email = email;
		this.nationality = nationality;
		this.dateOfBirth = dateOfBirth;
		this.joiningDate = joiningDate;
	}


	boolean checkPassword(String password) {
				
		if(this.password.equals(password))
			return true;
		else
			return false;
	}
	
}