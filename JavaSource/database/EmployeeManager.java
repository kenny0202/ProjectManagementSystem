/** 
 * @author Eric Lyons-Davis, Olga Sabourova
 * */
package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



import model.users.employees.Employee;

@Stateless
/* Class contains all actions to manipulate data in the Employee Table */
public class EmployeeManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	
	/* find employee based on employeeID */
	public Employee findEmployeeID(String employeeID) 
	{
        return em.find(Employee.class, employeeID);
    }
	
	/* get all employees (as username) that can be possibly be assingned as pms for projects
	 * or as responsible engineers for wps. method used for both cases*/
	public List<String> getPossibleUNames() {
		
		List<String> userNames = new ArrayList<String>(); 
		
		try {
			
			TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.role != \'Human Resources\'", Employee.class); 
			List<Employee> emps = query.getResultList(); 
			
			for (int i = 0; i < emps.size(); i++)
				userNames.add(emps.get(i).getUserName()); 
			
			return userNames; 
			
		} catch (NoResultException e) {
			
			return userNames;
		}
	}
	
	/* find employee based on userName */
	public Employee findUserName(String userName) 
	{
        try
        {
	        TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.userName = :userName", Employee.class); 
	    	query.setParameter("userName", userName);
	    	
	    	Employee employee = query.getSingleResult();
	    	return employee;
        }
        
        catch(NoResultException e)
        {
        	return null;
        }
        
        catch(Exception e)
        {
        	return null;
        }
    }
	

	/* find employee based on userName and if they are a supervisor or not*/
	public Employee findSupervisorUserName(String supervisorID) 
	{
        try
        {
	        TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.employeeID = :supervisorID and e.role = :role", Employee.class); 
	    	query.setParameter("supervisorID", supervisorID);
	    	query.setParameter("role", "Supervisor");
	    	
	    	Employee employee = query.getSingleResult();
	    	return employee;
        }
        
        catch(NoResultException e)
        {
        	return null;
        }
        
        catch(Exception e)
        {
        	return null;
        }
    }
	
	/* return all employees in a list */
	public ArrayList<Employee> getAll() 
    {
    	try
    	{
	    	ArrayList<Employee> employeeList = new ArrayList<Employee>();
	    	TypedQuery<Employee> query = em.createQuery("select e from Employee e",
	    			 Employee.class); 
	        java.util.List<Employee> employees = query.getResultList();
	       
	        for (int i=0; i < employees.size(); i++)
	        {
	        	employeeList.add(employees.get(i)); 
	        }
	        
	        return employeeList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
     }
	
	/* persist an Employee */
	public void persist(Employee employee)
	{
		em.persist(employee);
	}
	
	/* merge existing employee*/
	public void merge(Employee employee) {
		em.merge(employee); 
	}
	
	/* delete an employee */
	public void remove(Employee employee) 
	{
		employee = findEmployeeID(employee.getEmployeeID());
		em.remove(employee);
	}
	
	/* update an employee */
	public void update(Employee employee, Employee newData)
	{
		employee = findEmployeeID(employee.getEmployeeID());
		employee.setEmployeeID(newData.getEmployeeID());
		employee.setLabourGradeID(newData.getLabourGradeID());
		employee.setSupervisorID(newData.getSupervisorID());
		employee.setFirstName(newData.getFirstName());
		employee.setLastName(newData.getLastName());
		employee.setUserName(newData.getUserName());
		employee.setAddress(newData.getAddress());
		employee.setProvince(newData.getProvince());
		employee.setCountry(newData.getCountry());
		employee.setPostalCode(newData.getPostalCode());
		employee.setHomeNumber(newData.getHomeNumber());
		employee.setCellNumber(newData.getCellNumber());
		employee.setEmailAddress(newData.getEmailAddress());
		employee.setSickLeaveTimeBalance(newData.getSickLeaveTimeBalance());
		employee.setVacationTimeBalance(newData.getVacationTimeBalance());
		em.merge(employee);
	}
	
	/* get supervisors - for assigning someboday a supervisor */
	public List<String> getAllSupervisors() {
		
		try {
			
			List<String> supervisorIds = new ArrayList<String>(); 
			TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.role = :role", Employee.class); 
			query.setParameter("role", "Supervisor");
			List<Employee> supervisors = query.getResultList(); 
			
			for (int i = 0; i < supervisors.size(); i++ ) 
				supervisorIds.add(supervisors.get(i).getEmployeeID()); 
			
			return supervisorIds; 
    	
		} catch(NoResultException e) {
			
        	return new ArrayList<String>();
        }
		
	}
	
}
