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

import model.project.projects.Projects;


@Stateless

/* Class contains all actions to manipulate data in the Projects Table */
public class ProjectsManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	/* find project based on project ID */
	public Projects findProjectID(String projectID) 
	{
        try
        {
	        TypedQuery<Projects> query = em.createQuery("select p from Projects p where p.projectID = :projectID", Projects.class); 
	    	query.setParameter("projectID", projectID);
	    	Projects projects = query.getSingleResult();
	    	return projects;
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
	

	/* return all projects in a list */
	public ArrayList<Projects> getAll(String packageID) 
    {
    	try
    	{
	    	ArrayList<Projects> projectsList = new ArrayList<Projects>();
	    	TypedQuery<Projects> query = em.createQuery("select p from Projects p where p.packageID = :packageID", Projects.class); 
	    	query.setParameter("packageID", packageID);
	    	
	        java.util.List<Projects> projects = query.getResultList();
	       
	        for (int i=0; i < projects.size(); i++)
	        {
	        	projectsList.add(projects.get(i)); 
	        }
	        
	        return projectsList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	/* return all projects in a list */
	public ArrayList<String> getAll() 
    {
    	try
    	{
	    	TypedQuery<Projects> query = em.createQuery("select p from Projects p", Projects.class); 
	    	java.util.List<Projects> projects = query.getResultList();
	       
	    	ArrayList<String> projectsList = new ArrayList<String>();
	    	
	    	for (int i=0; i < projects.size(); i++)
	        {
	        	projectsList.add(projects.get(i).getProjectID()); 
	        }
	        
	        return projectsList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	public List<String> getAllTopLevelIds(String userName) {
		
    	//TypedQuery<Projects> query = em.createQuery("SELECT p from Projects p", Projects.class); 

		String stmt = "Select p FROM Projects p, EmployeeProjectAssignment epa where p.projectID = epa.projectID and epa.employeeUserName = :uName";
    	TypedQuery<Projects> query = em.createQuery(stmt, Projects.class); 
    	query.setParameter("uName", userName); 

		List<Projects> list = query.getResultList();
 
    	List<String> projectIds = new ArrayList<String>();
    	
    	for (int i = 0; i < list.size(); i++ )
    		projectIds.add(list.get(i).getProjectID()); 
    		
    	
    	return projectIds; 
		
	}
	
	/* persist a Project */
	public void persist(Projects project)
	{
		em.persist(project);
	}
	
	/* delete a Project */
	public void remove(Projects project) 
	{
		project = findProjectID(project.getProjectID());
		em.remove(project);
	}
	
	/* update a Project */
	public void update(Projects project, Projects newData)
	{
		project = findProjectID(project.getProjectID());
		project.setProjectID(newData.getProjectID());
		project.setProjectManagerUsername(newData.getProjectManagerUsername());
		project.setDescription(newData.getDescription());
		project.setClientName(newData.getClientName());
		project.setStartDate(newData.getStartDate());
		project.setEndDate(newData.getEndDate());
		project.setMarkupRate(newData.getMarkupRate());
		em.merge(project);
	}

}
