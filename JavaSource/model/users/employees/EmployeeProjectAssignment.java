/** 
 * @author Eric Lyons-Davis
 * */

package model.users.employees;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


/* represents a user's credentials; kept in a separate class for security purposes */

@Entity
@IdClass(EmployeeProjectAssignmentPK.class)
@Table(name="Employee_Project_Assignment")
public class EmployeeProjectAssignment 
{
	@Id
	@Column(name="employee_username")
	private String employeeUserName; 
	
	@Id
	@Column(name="project_ID")
	private String projectID;
	 
	/* constructor that takes in a UserName and a password */
	public EmployeeProjectAssignment(String inputProjectID, String inputEmployeeUserName)
	{
		projectID = inputProjectID;
		employeeUserName = inputEmployeeUserName;
	}
	
	/* default constructor */
	public EmployeeProjectAssignment()
	{
		
	}

	/* getters and setters */

	public String getEmployeeUserName() 
	{
		return employeeUserName;
	}

	public void setEmployeeUserName(String employeeUserName) 
	{
		this.employeeUserName = employeeUserName;
	}

	public String getProjectID()
	{
		return projectID;
	}

	public void setProjectID(String projectID) 
	{
		this.projectID = projectID;
	}
}
