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
import model.timesheets.TimesheetLGView;



@Stateless

/* Class contains all actions to manipulate data to gather rows for Monthly Report */
public class MonthlyReportManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	
	/* return all valid rows for monthly report */
	public ArrayList<TimesheetLGView> getAll(String projectID) 
    {
    	try
    	{
	    	ArrayList<TimesheetLGView> monthlyReportRowList = new ArrayList<TimesheetLGView>();
	    	TypedQuery<TimesheetLGView> query = em.createQuery("select t from TimesheetLGView t where t.projectID = :projectID", TimesheetLGView.class); 
	    	query.setParameter("projectID", projectID);
	    	
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
}
