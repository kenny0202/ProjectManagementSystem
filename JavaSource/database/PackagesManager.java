/** 
 * @author Eric Lyons-Davis
 * */
package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.project.workPackages.Packages;


@Stateless

/* Class contains all actions to manipulate data in the Projects Table */
public class PackagesManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	
	/* find package based on package ID */
	public Packages findPackageID(String packageID, String projectID) 
	{
        try
        {
	        TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.packageID = :packageID and p.projectID = :projectID", Packages.class); 
	    	query.setParameter("packageID", packageID);
	    	query.setParameter("projectID", projectID);
	    	
	    	Packages packages = query.getSingleResult();
	    	return packages;
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
	
	/* find project based on package ID */
	public Packages findChildPackageID(String packageID, String parentPackageID, String projectID) 
	{
        try
        {
	        TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.packageID = :packageID"
	        		+ " and p.parentPackageID = :parentPackageID and p.projectID = :projectID", Packages.class); 
	    	query.setParameter("packageID", packageID);
	    	query.setParameter("parentPackageID", parentPackageID);
	    	query.setParameter("projectID", projectID);
	    	Packages packages = query.getSingleResult();
	    	return packages;
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
	
	public Packages findParent(String parentId, String projectId) {
		
		try {
	        TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.packageID = :packageID"
	        		+ " and p.projectID = :projectID", Packages.class); 
	        query.setParameter("packageID", parentId);
	        query.setParameter("projectID", projectId);
	        
	        Packages parent = query.getSingleResult(); 
	        return parent; 
		
		} catch (NoResultException e) {
			
        	return null;
        }
	}
	
	
	/* merge existing package*/
	public void merge(Packages wp) 
	{
		em.merge(wp); 
	}
	
	// for when a timesheet is approved
	public Packages findWorkpackage(String projectId, String packageId) {

        try
        {
	        TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.packageID = :packageID"
	        		+ " and p.projectID = :projectID", Packages.class); 
	    	query.setParameter("packageID", packageId);
	    	query.setParameter("projectID", projectId);
	    	Packages packages = query.getSingleResult();
	    	return packages;
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
	
	
	
	/* return all packages in a list */
	public ArrayList<Packages> getAll(String projectID, String parentPackageID) 
    {
    	try
    	{
	    	ArrayList<Packages> packageList = new ArrayList<Packages>();
	    	TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.projectID = :projectID and p.parentPackageID = :parentPackageID", Packages.class); 
	    	query.setParameter("projectID", projectID);
	    	query.setParameter("parentPackageID", parentPackageID);
	    	
	        java.util.List<Packages> packages = query.getResultList();
	       
	        for (int i=0; i < packages.size(); i++)
	        {
	        	packageList.add(packages.get(i)); 
	        }
	        
	        return packageList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	
	/* return all packages for a particular project in a list */
	public ArrayList<Packages> getAll(String projectID) 
    {
    	try
    	{
	    	ArrayList<Packages> packageList = new ArrayList<Packages>();
	    	TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.projectID = :projectID", Packages.class); 
	    	query.setParameter("projectID", projectID);
	    	java.util.List<Packages> packages = query.getResultList();
	       
	        for (int i=0; i < packages.size(); i++)
	        {
	        	packageList.add(packages.get(i)); 
	        }
	        
	        return packageList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	
	/* return all child packages in a list */
	public ArrayList<Packages> getAllChildren(String parentPackageID, String projectID) 
    {
    	try
    	{
	    	ArrayList<Packages> packageList = new ArrayList<Packages>();
	    	TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.parentPackageID = :parentPackageID"
	    			+ " and p.projectID = :projectID", Packages.class); 
	    	query.setParameter("parentPackageID", parentPackageID);
	    	query.setParameter("projectID", projectID);
	    	
	        java.util.List<Packages> packages = query.getResultList();
	       
	        for (int i=0; i < packages.size(); i++)
	        {
	        	packageList.add(packages.get(i)); 
	        }
	        
	        return packageList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	
	/* persist a WorkPackage */
	public void persist(Packages packages)
	{
		em.persist(packages);
	}
	
	/* delete a WorkPackage */
	public void remove(Packages packages) 
	{
		packages = findPackageID(packages.getPackageID(), packages.getProjectID());
		em.remove(packages);
	}
	
	
	
	// get the leaves for a specific project, used for timesheet data entry
	public List<String> getLeafPackages(String projectId) {
		
		List<String> packageIds = new ArrayList<String>(); 
		List<Packages> leafs = new ArrayList<Packages>(); 
		
		TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.projectID = :projectID and p.leaf = true and p.status = \'Open\'", Packages.class); 		
		query.setParameter("projectID", projectId); 

		try {
			
			leafs = query.getResultList(); 
			
			for (int i = 0; i < leafs.size(); i++)
				packageIds.add(leafs.get(i).getPackageID()); 
			
			return packageIds; 
			
		} catch (NoResultException e) {
        	return new ArrayList<String>();
        }
	
	}
	
	
	/* delete all packages for a particular project */
	public void deletePackages(String projectID)
	{
		Query query = em.createQuery("delete from Packages p where p.projectID = :projectID"); 
		query.setParameter("projectID", projectID); 
		query.executeUpdate();
	}
	
	
	// remove a package from its leaf postion, no longer a package that can be worked on
	public void removeFromLeaves(Packages wp) {

		TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.packageID = :packageID", Packages.class); 
		query.setParameter("packageID", wp.getPackageID()); 
		Packages toUpdate = query.getSingleResult();  
		
		toUpdate.setLeaf(false);
		em.merge(toUpdate); 
	}
	
	/* update a WorkPackage's total estimate */
	public void updateTotalEstimate(String packageID, String projectID, double newTotalEstimate)
	{
		Packages packages = findPackageID(packageID, projectID);
		packages.setEstimatedCost(newTotalEstimate);
		em.merge(packages);
	}

	
	/* find all immediate child packages for specified package (or project which uses -1 as a package id) */
	public ArrayList<Packages> getChildPackages(String packageID, String projectID) 
	{
		try
    	{
			ArrayList<Packages> childPackagesList = new ArrayList<Packages>();
			TypedQuery<Packages> query = em.createQuery("select p from Packages p where p.parentPackageID = :parentPackageID"
					+ " and p.projectID = :projectID", Packages.class); 
	    	query.setParameter("parentPackageID", packageID);
	    	query.setParameter("projectID", projectID);
	    	
	        java.util.List<Packages> packages = query.getResultList();
	       
	        for (int i=0; i < packages.size(); i++)
	        {
	        	childPackagesList.add(packages.get(i)); 
	        }
	        
	        return childPackagesList;
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
	
}
