/** 
 * @author Eric Lyons-Davis
 * */
package database;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.users.labourGrades.LabourGrade;
import model.users.labourGrades.LabourGradeChargeRate;

@Stateless
/* Class contains all actions to manipulate data in the Labour_Grade Table */
public class LabourGradeManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	/* find a valid Credential for login validation */
	public LabourGrade searchForLabourGrade(String labourGradeID) 
	{
		try
		{
			TypedQuery<LabourGrade> query = em.createQuery("select g from LabourGrade g where g.labourGradeID = :labourGradeID", LabourGrade.class); 
	    	query.setParameter("labourGradeID", labourGradeID);
	    	LabourGrade labourGrade = query.getSingleResult();
	    	return labourGrade;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
	
	
	
	/* returns a list of all labour grades w/ their associates charge rates */
	public ArrayList<LabourGradeChargeRate> getAllChargeRates()
	{
		try
		{
			TypedQuery<LabourGradeChargeRate> query = em.createQuery("select l from LabourGradeChargeRate l", LabourGradeChargeRate.class); 
	    	List<LabourGradeChargeRate> labourGradeResultList = query.getResultList();
	    	return (ArrayList<LabourGradeChargeRate>) labourGradeResultList;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
	
	
	/* persist a LabourGrade */
	public void persist(LabourGrade labourGrade)
	{
		em.persist(labourGrade);
	}

	
	/* delete a LabourGrade */
	public void remove(LabourGrade labourGrade) 
	{
		labourGrade = searchForLabourGrade(labourGrade.getLaborGradeID());
		em.remove(labourGrade);
	}
	
	
	/* update a LabourGrade */
	public void update(LabourGrade labourGrade, LabourGrade newData)
	{
		//Date d = new Date(); 
		
		labourGrade = searchForLabourGrade(labourGrade.getLabourGradeTitle());
		labourGrade.setLaborGradeID(newData.getLaborGradeID());
		labourGrade.setLabourGradeTitle(newData.getLabourGradeTitle());
		//labour
		em.merge(labourGrade);
	}

	
	/* update a LabourGrade */
	public void updateChargeRate(LabourGradeChargeRate oldData, LabourGradeChargeRate newData)
	{
		Calendar cal = Calendar.getInstance();  // for when values were updated
	
		oldData = findChargeRate();
		oldData.setSsChargeRate(newData.getSsChargeRate());
		oldData.setDsChargeRate(newData.getDsChargeRate());
		oldData.setJsChargeRate(newData.getJsChargeRate());
		oldData.setP1ChargeRate(newData.getP1ChargeRate());
		oldData.setP2ChargeRate(newData.getP2ChargeRate());
		oldData.setP3ChargeRate(newData.getP3ChargeRate());
		oldData.setP4ChargeRate(newData.getP4ChargeRate());
		oldData.setP5ChargeRate(newData.getP5ChargeRate());
		oldData.setP6ChargeRate(newData.getP6ChargeRate());
		oldData.setEntryDate(cal.getTime()); 
		em.merge(oldData);
	}

	/* finds a specific charge rate via id */
	public LabourGradeChargeRate findChargeRate() 
	{
		try
		{
			TypedQuery<LabourGradeChargeRate> query = em.createQuery("select l from LabourGradeChargeRate l", LabourGradeChargeRate.class); 
	    	LabourGradeChargeRate labourGradeChargeRate = query.getSingleResult();
	    	return labourGradeChargeRate;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
	
	public List<String> getAllLabourGrades() {
		
		try
		{
			TypedQuery<LabourGrade> query = em.createQuery("select l from LabourGrade l", LabourGrade.class); 
			List<LabourGrade> ids = query.getResultList(); 
			List<String> labourGradeIds = new ArrayList<String>(); 
			
			for (int i = 0; i < ids.size(); i++)
				labourGradeIds.add(ids.get(i).getLaborGradeID()); 
			
			return labourGradeIds;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
}
