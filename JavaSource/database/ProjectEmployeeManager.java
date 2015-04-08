/** 
 * @author Eric Lyons-Davis
 * */
package database;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.users.employees.Employee;
import model.users.employees.EmployeeProjectAssignment;

@Stateless
/* Class contains all actions to manipulate data in the ProjectEmployeeAsssignment Table */
public class ProjectEmployeeManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	/* return all employeeProjectAssignment objects in a list */
	public ArrayList<EmployeeProjectAssignment> getAll(String projectID) 
    {
    	try
    	{
	    	TypedQuery<EmployeeProjectAssignment> query = em.createQuery("select e from EmployeeProjectAssignment e where e.projectID = :projectID",
	    			EmployeeProjectAssignment.class); 
	    	query.setParameter("projectID", projectID);
	        java.util.List<EmployeeProjectAssignment> employeeProjectList = query.getResultList();
	       
	        return (ArrayList<EmployeeProjectAssignment>) employeeProjectList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    	
    }
	
	/* find a valid EmployeeProjectAssignment */
	public EmployeeProjectAssignment searchForAssignment(String projectID, String employeeUserName) 
	{
		try
		{
			TypedQuery<EmployeeProjectAssignment> query = em.createQuery("select e from EmployeeProjectAssignment e where e.employeeUserName = :employeeUserName "
					+ "and e.projectID = :projectID", EmployeeProjectAssignment.class); 
	    	query.setParameter("employeeUserName", employeeUserName);
	    	query.setParameter("projectID", projectID);
			
	    	EmployeeProjectAssignment employeeProjectAssignment = query.getSingleResult();
	    	return employeeProjectAssignment;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
	
	
	
	
	/* return all employees in a list in a string format for a drop down list */
	public ArrayList<String> getAllForDropDownAssign(String projectID) 
    {
    	try
    	{
	    	TypedQuery<Employee> query = em.createQuery("select e from Employee e where "
	    			+ "e.userName NOT IN (select p.employeeUserName from EmployeeProjectAssignment p where p.projectID = :projectID)",
	    			 Employee.class);
	    	query.setParameter("projectID", projectID);
	        java.util.List<Employee> employees = query.getResultList();
	       
	        ArrayList<String> employeeList = new ArrayList<String>();
	    	
	        for (int i=0; i < employees.size(); i++)
	        {
	        	employeeList.add(employees.get(i).getUserName()); 
	        }
	        
	        return employeeList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
     }
	
	
	
	
	/* return all employees in a list in a string format for a drop down list */
	public ArrayList<String> getAllForDropDownRemove(String projectID) 
    {
		try
    	{
	    	TypedQuery<Employee> query = em.createQuery("select e from Employee e where "
	    			+ "e.userName IN (select a.employeeUserName from EmployeeProjectAssignment a where a.projectID = :projectID)"
	    			+ "and e.userName NOT IN (select p.projectManagerUsername from Projects p where p.projectID = :projectID)",
	    			 Employee.class);
	    	query.setParameter("projectID", projectID);
	        java.util.List<Employee> employees = query.getResultList();
	       
	        ArrayList<String> employeeList = new ArrayList<String>();
	    	
	        for (int i=0; i < employees.size(); i++)
	        {
	        	employeeList.add(employees.get(i).getUserName()); 
	        }
	        
	        return employeeList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
     }
	
	
	/* persist an EmployeeProjectAssignment */
	public void persist(EmployeeProjectAssignment EmployeeProjectAssignment)
	{
		em.persist(EmployeeProjectAssignment);
	}

	
	/* delete a EmployeeProjectAssignment */
	public void remove(EmployeeProjectAssignment employeeProjectAssignment) 
	{
		employeeProjectAssignment = searchForAssignment(employeeProjectAssignment.getProjectID(), employeeProjectAssignment.getEmployeeUserName());
		em.remove(employeeProjectAssignment);
	}
	
	
	public void merge(EmployeeProjectAssignment EmployeeProjectAssignment) 
	{
		
		em.merge(EmployeeProjectAssignment); 
	}
	
}
