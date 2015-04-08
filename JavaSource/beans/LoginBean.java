/** 
 * @author Olga Sabourova, Eric Lyons-Davis
 * */
package beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.users.credentials.Credentials;
import model.users.employees.Employee;
import database.CredentialsManager;
import database.EmployeeManager;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable
{
	@Inject private CredentialsManager credentialsManager; /* allows access to SQL calls on the Credentials database */
	@Inject private EmployeeManager employeeManager; 
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private Employee employee;
	
	// for when user wants to change their password
	private String oldPassword; 
	private String newPassword;
	private boolean passChangeValid = true; 

	private Credentials credentials; 
	private boolean loggedIn = false; 
	private boolean isSupervisor; 
	private boolean isHr; 
	private boolean isRegUser; 
	private boolean isAdmin; 
	
	public String validateLogin()
	{
		//conversation.begin();
		credentials = credentialsManager.searchForCredentials(userName, password); 
		
		if( credentials != null)
		{
			//conversation.end();
			employee = employeeManager.findUserName(userName); 
			
			// for disable views based on roles
			isSupervisor = employee.getRole().equals("Supervisor"); 
			isHr = employee.getRole().equals("Human Resources"); 
			isRegUser = employee.getRole().equals("Regular User"); 
			isAdmin = employee.getRole().equals("Admin");
			loggedIn = true; 
			return "SuperUser"; 
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("loginForm:userName", 
			           new FacesMessage(null, "Incorrect username/password"));
			return null;
		}
	}

	public String updateEmployee() {
		
		employeeManager.merge(employee);
		FacesContext.getCurrentInstance().addMessage("empProfile:savedMsg",
				new FacesMessage(null, "Information updated!"));
		return null;
	}
	
	public String toChangePassPage() {
		
		oldPassword = ""; 
		newPassword = ""; 
		return "changePassword"; 
	}
	public String changePassword() {
		
		if (!oldPassword.equals(password)) {
			passChangeValid = false; 

			return null; 
		} else {
			passChangeValid = true;
			credentials.setPassword(newPassword);
			password = newPassword; 
			credentialsManager.merge(credentials);
			
			return "employeeProfileMenu"; 
		}
	}
	
	public String logout() {
		
		loggedIn = false; 
		setUserName("");
		setPassword("");
		return "/pages/index.xhtml";
	}
	
	/* getters and setters */
	public String getUserName() 
	{
		return userName;
	}

	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public Employee getEmployee() {
		return this.employee; 
	}
	
	public boolean getLoggedIn() {
		return loggedIn; 
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public boolean getPassChangeValid() {
		
		return passChangeValid; 
	}
	

	public boolean getIsSupervisor() {

		return isSupervisor;
	}

	public boolean getIsHr() {
		return isHr;
	}

	public boolean getIsRegUser() {
		return isRegUser;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

}
