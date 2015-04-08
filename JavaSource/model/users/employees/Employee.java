/** 
 * @author Eric Lyons-Davis
 * */

package model.users.employees;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="employee")
/* represents an employee. Acts as a parent (or interface) for the many types of employees */
public class Employee implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="employee_ID")
	private String employeeID; /* this will be synonymous with userID in Credentials */
	
	@Column(name="Labour_Grade_Id")
	private String labourGradeID;
	
	@Column(name="role")
	private String role;
	
	@Column(name="supervisor_id") /* employee id of the supervisor for this user */
	private String supervisorID;
	
	@Column(name="First_Name")
	private String firstName;
	
	@Column(name="Last_Name")
	private String lastName;
	
	@Column(name="User_Name", unique=true)
	private String userName;
	
	@Column(name="Address")
	private String address;
	
	@Column(name="Province")
	private String province;
	
	@Column(name="Country")
	private String country;
	
	@Column(name="Postal_Code")
	private String postalCode;
	
	@Column(name="Home_Number")
	private String homeNumber;
	
	@Column(name="Cell_Number")
	private String cellNumber;
	
	@Column(name="Email_Address")
	private String emailAddress;
	
	@Column(name="Sick_Leave_Time_Balance")
	private double sickLeaveTimeBalance;
	
	@Column(name="Vacation_Time_Balance")
	private double vacationTimeBalance;
	
	/* default constructor */
	public Employee()
	{
		
	}
	
	
	/* constructor to create a new employee w/ all possibly known details (besides vacation and sick leave balanaces*/
	public Employee(String inputedEmployeeID, String inputedLabourGradeID, String inputedRole, String inputedSupervisorID, String inputedFirstName, String inputedLastName, String inputedUserName,
			String inputedAddress, String inputedProvince, String inputedCountry, String inputedPostalCode, String inputedHomeNumber,
			String inputedCellNumber, String inputedEmailAddress)
	{
		employeeID = inputedEmployeeID;
		labourGradeID = inputedLabourGradeID;
		role = inputedRole;
		supervisorID = inputedSupervisorID;
		firstName = inputedFirstName;
		lastName = inputedLastName;
		userName = inputedUserName;
		address = inputedAddress;
		province = inputedProvince;
		country = inputedCountry;
		postalCode = inputedPostalCode;
		homeNumber = inputedHomeNumber;
		cellNumber = inputedCellNumber;
		emailAddress = inputedEmailAddress;
		sickLeaveTimeBalance = 0;
		vacationTimeBalance = 0;
		
	}
	
	/* constructor to create an employee w/ all possibly known details */
	public Employee(String inputedEmployeeID, String inputedLabourGradeID, String inputedRole, String inputedSupervisorID, String inputedFirstName, String inputedLastName, String inputedUserName,
			String inputedAddress, String inputedProvince, String inputedCountry, String inputedPostalCode, String inputedHomeNumber,
			String inputedCellNumber, String inputedEmailAddress, double inputedSickLeaveTimeBalance, double inputedVacationTimeBalance)
	{
		employeeID = inputedEmployeeID;
		labourGradeID = inputedLabourGradeID;
		role = inputedRole;
		supervisorID = inputedSupervisorID;
		firstName = inputedFirstName;
		lastName = inputedLastName;
		userName = inputedUserName;
		address = inputedAddress;
		province = inputedProvince;
		country = inputedCountry;
		postalCode = inputedPostalCode;
		homeNumber = inputedHomeNumber;
		cellNumber = inputedCellNumber;
		emailAddress = inputedEmailAddress;
		sickLeaveTimeBalance = inputedSickLeaveTimeBalance;
		vacationTimeBalance = inputedVacationTimeBalance;
	}
	
	
	/* constructor to create an employee w/out role (for usage on update employee commands) */
	public Employee(String inputedEmployeeID, String inputedLabourGradeID, String inputedSupervisorID, String inputedFirstName, String inputedLastName, String inputedUserName,
			String inputedAddress, String inputedProvince, String inputedCountry, String inputedPostalCode, String inputedHomeNumber,
			String inputedCellNumber, String inputedEmailAddress, double inputedSickLeaveTimeBalance, double inputedVacationTimeBalance)
	{
		employeeID = inputedEmployeeID;
		labourGradeID = inputedLabourGradeID;
		supervisorID = inputedSupervisorID;
		firstName = inputedFirstName;
		lastName = inputedLastName;
		userName = inputedUserName;
		address = inputedAddress;
		province = inputedProvince;
		country = inputedCountry;
		postalCode = inputedPostalCode;
		homeNumber = inputedHomeNumber;
		cellNumber = inputedCellNumber;
		emailAddress = inputedEmailAddress;
		sickLeaveTimeBalance = inputedSickLeaveTimeBalance;
		vacationTimeBalance = inputedVacationTimeBalance;
	}
	
	public String getEmployeeID() 
	{
		return employeeID;
	}

	public void setEmployeeID(String employeeID) 
	{
		this.employeeID = employeeID;
	}

	public String getFirstName() 
	{
		return firstName;
	}

	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}

	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
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


	public String getUserName()
	{
		return userName;
	}


	public void setUserName(String userName)
	{
		this.userName = userName;
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
