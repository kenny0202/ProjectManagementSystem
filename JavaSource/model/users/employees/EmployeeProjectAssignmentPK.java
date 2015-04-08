/** @author Eric Lyons-Davis
 * 
 */
package model.users.employees;

import java.io.Serializable;

/* PK class for Packages*/
public class EmployeeProjectAssignmentPK implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private String employeeUserName;
	private String projectID;
	
	/* default constructor */
	public EmployeeProjectAssignmentPK()
	{
		
	}
	
	public int hashCode() 
    {
        return (int) projectID.hashCode();
    }
	

	public boolean equals(Object obj) 
    {
        if (obj == this) 
    		return true;
    	if (!(obj instanceof EmployeeProjectAssignmentPK)) 
    		return false;

    	EmployeeProjectAssignmentPK pk = (EmployeeProjectAssignmentPK) obj;
        return pk.employeeUserName.equals(employeeUserName) && pk.projectID.equals(projectID);  	
    }
	
	
	
	/* getters and setters */

    public String getEmployeeUserName() 
    {
		return employeeUserName;
	}

	public void setEmployeeuserName(String employeeUserName) 
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
