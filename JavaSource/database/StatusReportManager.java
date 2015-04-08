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
import model.project.reports.StatusReport;
import model.project.workPackages.Packages;
import model.timesheets.TimesheetLGView;
import model.timesheets.TimesheetRowData;



@Stateless

/* Class contains all actions to manipulate data to gather rows for Monthly Report */
public class StatusReportManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	/* return all valid rows for status report */
	public ArrayList<TimesheetLGView> getAllRows(String projectID, String workPackage) 
    {
    	try
    	{
	    	ArrayList<TimesheetLGView> monthlyReportRowList = new ArrayList<TimesheetLGView>();
	    	TypedQuery<TimesheetLGView> query = em.createQuery("select t from TimesheetLGView t where t.projectID = :projectID and t.workPackage = :workPackage", TimesheetLGView.class); 
	    	query.setParameter("projectID", projectID);
	    	query.setParameter("workPackage", workPackage);
	    	
	        java.util.List<TimesheetLGView> timesheetRow = query.getResultList();
	       
	        for (int i=0; i < timesheetRow.size(); i++)
	        {
	        	monthlyReportRowList.add(timesheetRow.get(i));
	        }
	        
	        return monthlyReportRowList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }

	/* find most recent Status report for a particular package/project combo */
	public StatusReport findMostRecentStatusReport(String projectID, String packageID)
	{
		try
        {
			TypedQuery<StatusReport> query = em.createQuery
					("select s from StatusReport s where s.projectID = :projectID and s.packageID = :packageID and s.id = "
							+ "(SELECT MAX(s.id) FROM StatusReport s where s.projectID = :projectID and s.packageID = :packageID)", StatusReport.class);
			query.setParameter("projectID", projectID);
	    	query.setParameter("packageID", packageID);
	    	StatusReport statusReport = query.getSingleResult();
	    	
			return statusReport;
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
	
	
	
	/* find a Status Report based on it's id */
	public StatusReport findStatusReport(int id)
	{
		try
        {
	        TypedQuery<StatusReport> query = em.createQuery("select s from StatusReport s where s.id = :id", StatusReport.class); 
	    	query.setParameter("id", id);
	    	
	    	StatusReport statusReport = query.getSingleResult();
	    	return statusReport;
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
	
	
	
	
	/* return all valid rows for status report by labour grade id */
	public ArrayList<TimesheetRowData> getAllValidTimesheetRows(String projectID, String packageID) 
    {
    	try
    	{
	    	ArrayList<TimesheetRowData> monthlyReportRowList = new ArrayList<TimesheetRowData>();
	    	TypedQuery<TimesheetRowData> query = em.createQuery("select t from TimesheetRowData t where t.projectID = :projectID and t.workPackage = :packageID" +
	    			" group by t.labourGradeID", TimesheetRowData.class); 
	    	query.setParameter("projectID", projectID);
	    	query.setParameter("packageID", packageID);
	    	
	        java.util.List<TimesheetRowData> timesheetRow = query.getResultList();
	       
	        for (int i=0; i < timesheetRow.size(); i++)
	        {
	        	monthlyReportRowList.add(timesheetRow.get(i));
	        }
	        
	        return monthlyReportRowList;
    	}
    	
    	catch(NoResultException e)
        {
        	return null;
        }
    }
	
	/* adds a default blank status report for newly created package */
	public void persistDefault(Packages packages)
	{
		StatusReport sr = new StatusReport(packages.getProjectID(), packages.getPackageID(), 0, 0, 0, 0, 0, 0, 0, 0, 0, "N/A");
		em.persist(sr);
	}	
	
	
	/* persist a Status Report */
	public void persist(StatusReport statusReport)
	{
		em.persist(statusReport);
	}
	
	/* merge existing Status Report*/
	public void merge(StatusReport statusReport) {
		em.merge(statusReport); 
	}
	
	/* delete a Status Report */
	public void remove(StatusReport statusReport) 
	{
		statusReport = findStatusReport(statusReport.getId());
		em.remove(statusReport);
	}
	
	/* update a Status Report */
	public void update(StatusReport statusReport, StatusReport newData)
	{
		statusReport = findStatusReport(statusReport.getId());
		statusReport.setId(newData.getId());
		statusReport.setProjectID(newData.getProjectID());
		statusReport.setPackageID(newData.getPackageID());
		statusReport.setSsEstimate(newData.getSsEstimate());
		statusReport.setDsEstimate(newData.getDsEstimate());
		statusReport.setJsEstimate(newData.getJsEstimate());
		statusReport.setP1Estimate(newData.getP1Estimate());
		statusReport.setP2Estimate(newData.getP2Estimate());
		statusReport.setP3Estimate(newData.getP3Estimate());
		statusReport.setP4Estimate(newData.getP4Estimate());
		statusReport.setP5Estimate(newData.getP5Estimate());
		statusReport.setP6Estimate(newData.getP6Estimate());
		statusReport.setComments(newData.getComments());
		em.merge(statusReport);
	}
}
