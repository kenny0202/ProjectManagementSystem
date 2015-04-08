/** 
 * @author Eric Lyons-Davis
 * */
package beans;

/* handles employee management CRUD operations */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import model.users.credentials.Credentials;
import model.users.employees.Employee;
import database.CredentialsManager;
import database.EmployeeManager;
import database.LabourGradeManager;

@Named("employee")
@ConversationScoped

public class EmployeeBean implements Serializable
{
	@Inject private EmployeeManager employeeManager;
	@Inject private CredentialsManager credentialsManager;
	@Inject private LabourGradeManager labourGradeManager;
	@Inject Conversation conversation;
	
	private static final long serialVersionUID = 1L;
	private String employeeID;
	private String labourGradeID;
	private String role;
	private String supervisorID;
	private String firstName;
	private String lastName;
	private String userName;
	private String address;
	private String province;
	private String country;
	private String postalCode;
	private String homeNumber;
	private String cellNumber;
	private String emailAddress;
	private double sickLeaveTimeBalance;
	private double vacationTimeBalance;
	private String password;
	ArrayList<Employee> employeeList = new ArrayList<Employee>();
	
	List<String> supervisorIds = new ArrayList<String>(); 
	// when updating a supervisor, if they are a superviosr, they cant appear in the list of options
	List<String> supervisorIdsWithoutCurr = new ArrayList<String>(); 
	List<String> labourGradeIds = new ArrayList<String>(); 
	
	
	public List<String> getSupervisorIds() {
		
		supervisorIds = employeeManager.getAllSupervisors(); 
		return supervisorIds; 
	}
	
	public List<String> getSupervisorIdsWithoutCurr() {

		supervisorIdsWithoutCurr = employeeManager.getAllSupervisors(); 
		
		for (int i = 0; i < supervisorIdsWithoutCurr.size(); i++ ) {
			
			if (supervisorIdsWithoutCurr.get(i).equals(employeeID)) 
				supervisorIdsWithoutCurr.remove(i); 
		}
		
		return supervisorIdsWithoutCurr; 
	}
	
	public List<String> getLabourGradeIds() {
		
		labourGradeIds = labourGradeManager.getAllLabourGrades(); 
		return labourGradeIds; 
	}
	
	/* grabs the data in the database to display */
	public String displayEmployeeList()
	{
		employeeList = employeeManager.getAll();
		return "Success"; 
	}
	
	/* create a new employee */
	public String createEmployee()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(employeeManager.findUserName(userName) == null) /* if the userName inputed is unique */
		{
			/* ensure that entered labourGradeID is valid */
			if(labourGradeManager.searchForLabourGrade(labourGradeID) == null) 
			{
				FacesContext.getCurrentInstance().addMessage("createEmployeeForm:labourGradeID",
				           new FacesMessage(null, "Invalid Labour Grade ID."));
				return "Failure";
			}
			
			
			/* ensure that entered supervisor id is valid (also ensures that you entered an input for supervisor id; if blank then it's an ok input)*/
			if(!supervisorID.equals("") && employeeManager.findSupervisorUserName(supervisorID) == null) 
			{
				FacesContext.getCurrentInstance().addMessage("createEmployeeForm:supervisorID",
				           new FacesMessage(null, "Invalid Supervisor Employee ID."));

				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Failure";
			}
		
			if(employeeManager.findUserName(employeeID) == null) /* if the employeeID inputed is unique */
			{	
				Employee employee = new Employee(employeeID, labourGradeID, role, supervisorID, firstName, lastName, userName, address, province, country, postalCode, homeNumber, cellNumber, emailAddress);
				employeeManager.persist(employee);
				Credentials credentials = new Credentials(userName, "123"); /* creates a set of credentials for the user (default password is '123') */
				credentialsManager.persist(credentials);
				displayEmployeeList(); /* update the employee list with the new entry */

				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Success";
			}
			
			else
			{
				FacesContext.getCurrentInstance().addMessage("createEmployeeForm:employeeID",
				           new FacesMessage(null, "Selected Employee ID is already in use."));

				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Failure";
			}
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("createEmployeeForm:userName", 
			           new FacesMessage(null, "Selected User Name is already in use."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	
	/* delete an employee based on User Name */
	public String deleteEmployee()
	{
		try
		{
			if(conversation.isTransient())
			{
				conversation.begin();
			}
			
			Employee employee = employeeManager.findUserName(userName);
			Credentials credentials = credentialsManager.findUserName(userName);
			
			if(employee != null)
			{
				employeeManager.remove(employee);
				credentialsManager.remove(credentials);
				displayEmployeeList(); /* update the employee list with the new entry */

				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Success";
			}
			
			else
			{
				FacesContext.getCurrentInstance().addMessage("deleteEmployeeForm:userName", 
				           new FacesMessage(null, "Selected User Name does not exist."));

				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Failure";
			}
		}
		
		catch(Exception e)
		{
			FacesContext.getCurrentInstance().addMessage("deleteEmployeeForm:userName", 
			           new FacesMessage(null, "Selected User Name does not exist."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	/* checks to ensure that the selected employee to update exists */
	public String validateEmployee()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(employeeManager.findUserName(userName) != null)
		{
			Employee employee = employeeManager.findUserName(userName);
			Credentials credentials = credentialsManager.findUserName(userName);
			employeeID = employee.getEmployeeID();
			role = employee.getRole();
			supervisorID = employee.getSupervisorID();
			labourGradeID = employee.getLabourGradeID();
			firstName = employee.getFirstName();
			lastName = employee.getLastName();
			userName = employee.getUserName();
			address = employee.getAddress();
			province = employee.getProvince();
			country = employee.getCountry();
			postalCode = employee.getPostalCode();
			homeNumber = employee.getHomeNumber();
			cellNumber = employee.getCellNumber();
			emailAddress = employee.getEmailAddress();
			sickLeaveTimeBalance = employee.getSickLeaveTimeBalance();
			vacationTimeBalance = employee.getVacationTimeBalance();
			password = credentials.getPassword();
			return "Success";
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("selectEmployeeForm:userName", 
			           new FacesMessage(null, "Selected User Name does not exist."));
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	
	/* update an employee's info (not allowed to change the user name or employee id) */
	public String updateEmployee()
	{
		/* ensure that entered labourGradeID is valid */
		if(labourGradeManager.searchForLabourGrade(labourGradeID) == null) 
		{
			FacesContext.getCurrentInstance().addMessage("updateEmployeeForm:labourGradeID",
			           new FacesMessage(null, "Invalid Labour Grade ID."));
			return "Failure";
		}
		
		/* ensure that entered supervisor id is valid (also ensures that you entered an input for supervisor id; if blank then it's an ok input)*/
		if(!supervisorID.equals("") && employeeManager.findSupervisorUserName(supervisorID) == null) 
		{
			FacesContext.getCurrentInstance().addMessage("updateEmployeeForm:supervisorID",
			           new FacesMessage(null, "Invalid Supervisor Employee ID."));
			return "Failure";
		}
		
		/* updatedEmployee used as a container to pass new data to EmployeeManager for updating */
		Employee updatedEmployee = new Employee(employeeID, labourGradeID, supervisorID, firstName, lastName, userName, address, province, country, 
				postalCode, homeNumber, cellNumber, emailAddress, sickLeaveTimeBalance, vacationTimeBalance);
		
		employeeManager.update(employeeManager.findUserName(userName), updatedEmployee);
		displayEmployeeList(); /* update the employee list with the new entry */
		
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Success";
	}
	
	/* navigation rules when you select the Back button; reloads the employee table properly */
	public String back()
	{
		displayEmployeeList(); /* update the employee list with the new entry */
		return "Back";
	}
	
	/* navigation rules when you select the Back button in pages needing conversation end; reloads the employee table properly */
	public String backErase()
	{

		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		displayEmployeeList(); /* update the employee list with the new entry */
		return "Back";
	}
	
	/* getters and setters */
	public EmployeeManager getEmployeeManager() 
	{
		return employeeManager;
	}
	public void setEmployeeManager(EmployeeManager employeeManager) 
	{
		this.employeeManager = employeeManager;
	}
	public String getEmployeeID() 
	{
		return employeeID;
	}
	public void setEmployeeID(String employeeID) 
	{
		this.employeeID = employeeID;
	}
	public String getLastName() 
	{
		return lastName;
	}
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	public String getFirstName() 
	{
		return firstName;
	}
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	public String getAddress() 
	{
		return address;
	}
	public void setAddress(String address) 
	{
		this.address = address;
	}
	public String getHomeNumber() 
	{
		return homeNumber;
	}
	public void setHomeNumber(String homeNumber) 
	{
		this.homeNumber = homeNumber;
	}
	public String getCellNumber() 
	{
		return cellNumber;
	}
	public void setCellNumber(String cellNumber) 
	{
		this.cellNumber = cellNumber;
	}
	public double getSickLeaveTimeBalance() 
	{
		return sickLeaveTimeBalance;
	}
	public void setSickLeaveTimeBalance(double sickLeaveTimeBalance) 
	{
		this.sickLeaveTimeBalance = sickLeaveTimeBalance;
	}
	public double getVacationTimeBalance() 
	{
		return vacationTimeBalance;
	}
	public void setVacationTimeBalance(double vacationTimeBalance) 
	{
		this.vacationTimeBalance = vacationTimeBalance;
	}
	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}

	public ArrayList<Employee> getEmployeeList() 
	{
		return employeeList;
	}

	public void setEmployeeList(ArrayList<Employee> employeeList) 
	{
		this.employeeList = employeeList;
	}

	
	public String getProvince() 
	{
		return province;
	}


	public void setProvince(String province) 
	{
		this.province = province;
	}


	public String getCountry()
	{
		return country;
	}


	public void setCountry(String country) 
	{
		this.country = country;
	}


	public String getPostalCode() 
	{
		return postalCode;
	}


	public void setPostalCode(String postalCode) 
	{
		this.postalCode = postalCode;
	}


	public String getEmailAddress() 
	{
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) 
	{
		this.emailAddress = emailAddress;
	}


	public String getUserName() 
	{
		return userName;
	}


	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	public CredentialsManager getCredentialsManager() 
	{
		return credentialsManager;
	}

	public void setCredentialsManager(CredentialsManager credentialsManager) 
	{
		this.credentialsManager = credentialsManager;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public String getLabourGradeID() 
	{
		return labourGradeID;
	}

	public void setLabourGradeID(String labourGradeID) 
	{
		this.labourGradeID = labourGradeID;
	}

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}

	public String getSupervisorID() 
	{
		return supervisorID;
	}

	public void setSupervisorID(String supervisorID) 
	{
		this.supervisorID = supervisorID;
	}
	
}
