/** 
 * @author Eric Lyons-Davis
 * */
package beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.users.employees.EmployeeProjectAssignment;
import database.EmployeeManager;
import database.ProjectEmployeeManager;
import database.ProjectsManager;

/* for assigning & removing employees to projects */

@Named("projectEmployee")
@ConversationScoped
public class ProjectEmployeeBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject Conversation conversation;
	@Inject ProjectsManager projectManager;
	@Inject EmployeeManager employeeManager;
	@Inject ProjectEmployeeManager projectEmployeeManager;
	@Inject StorageBean storage;
	
	private String employeeUserName;
	//private String projectID;
	ArrayList<EmployeeProjectAssignment> projectEmployeeList = new ArrayList<EmployeeProjectAssignment>();
	
	/* used for drop down menu selections */
	ArrayList<String> employeeUserNameList = new ArrayList<String>(); 
	ArrayList<String> projectIDList = new ArrayList<String>(); 
	ArrayList<String> employeeProjectList = new ArrayList<String>(); 
	
	/* initial table load and navigation to Employee Project Assignment menu */
	public String viewEmployeeAssignment()
	{
		displayProjectEmployeeList(); /* display the project employee list */
		return "EmployeeProject";
	}
	
	
	/* navigation rules when you select the Back button; reloads the employee table properly */
	public String back()
	{
		displayProjectEmployeeList(); /* update the project employee list */
		return "Back";
	}
	
	public String backEnd()
	{
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Back";
	}

	/* populates the employee list for assignment and then handles navigation to the assign employee page */
	public String getEmployeeForAssignment()
	{
		getAllEmployeeUserNamesAssign();
		return "Assign";
	}
	
	/* populates the employee list for removal and then handles navigation to the remove employee page */
	public String getEmployeeForRemoval()
	{
		getAllEmployeeUserNamesRemove();
		return "Remove";
	}
	
	
	
	/* get all user names of employees who are not already on the project */
	public ArrayList<String> getAllEmployeeUserNamesAssign() 
	{
		employeeUserNameList = projectEmployeeManager.getAllForDropDownAssign(storage.getProjectID()); 
		return employeeUserNameList; 
	}
	
	/* get all user names of employees who are on the project */
	public ArrayList<String> getAllEmployeeUserNamesRemove() 
	{
		employeeUserNameList = projectEmployeeManager.getAllForDropDownRemove(storage.getProjectID()); 
		return employeeUserNameList; 
	}
	
	/* get all project ID's */
	public ArrayList<String> getAllProjectIDs() 
	{
		projectIDList = projectManager.getAll();
		return projectIDList; 
	}
	
	/* display list that contains employees assigned to projects */
	public void displayProjectEmployeeList() 
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		projectEmployeeList = projectEmployeeManager.getAll(storage.getProjectID());
		
	}

	/* assign an employee to a project */
	public String assignEmployee()
	{
		EmployeeProjectAssignment employeeProjectAssignment = new EmployeeProjectAssignment(storage.getProjectID(), employeeUserName);
		projectEmployeeManager.persist(employeeProjectAssignment);
		displayProjectEmployeeList() ;
		return "Success";
	}

	/* remove an employee to a project (cannot remove project manager, they have been filtered out of the list) */
	public String removeEmployee()
	{
		EmployeeProjectAssignment employeeProjectAssignment = new EmployeeProjectAssignment(storage.getProjectID(), employeeUserName);
		projectEmployeeManager.remove(employeeProjectAssignment);
		displayProjectEmployeeList() ;
		return "Success";
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
		return storage.getProjectID();
	}

	public ArrayList<EmployeeProjectAssignment> getProjectEmployeeList() 
	{
		return projectEmployeeList;
	}

	public ArrayList<String> getProjectIDList() 
	{
		return projectIDList;
	}

	public void setProjectIDList(ArrayList<String> projectIDList) 
	{
		this.projectIDList = projectIDList;
	}

	public void setProjectEmployeeList(
			ArrayList<EmployeeProjectAssignment> projectEmployeeList) 
	{
		this.projectEmployeeList = projectEmployeeList;
	}

	public ArrayList<String> getEmployeeUserNameList() 
	{
		return employeeUserNameList;
	}

	public void setEmployeeUserNameList(ArrayList<String> employeeUserNameList) 
	{
		this.employeeUserNameList = employeeUserNameList;
	}

	public ArrayList<String> getEmployeeProjectList()
	{
		return employeeProjectList;
	}

	public void setEmployeeProjectList(ArrayList<String> employeeProjectList) 
	{
		this.employeeProjectList = employeeProjectList;
	}

	
}
