package database;
/**
 * @author Olga Sabourova
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.timesheets.Timesheet;
import model.timesheets.TimesheetData;
import model.users.employees.Employee;

@Stateless
public class TimesheetManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="COMP4911_Project")
	/**
	 * Used to interact with the persistence context.
	 */	
	EntityManager em;	
	
	private static final String FIND_BY_EMPLOYEE = "SELECT t FROM TimesheetData t where t.employee.id = :employee "
													+ "order by t.endWeek desc";
	 private static final String FIND_BY_SUPERVISOR = "select t from TimesheetData t, Employee e where t.employee.id = e.employeeID and e.supervisorID = :empID"
				+ " and t.submittedForApproval = true and t.approved = false and t.denied = false order by t.endWeek asc";
	
	public TimesheetManager(){}

	// get all the timesheets for an employee
	public List<Timesheet> getTimesheets(Employee e) {
		
		TypedQuery<TimesheetData> query = em.createQuery(FIND_BY_EMPLOYEE, TimesheetData.class)
				.setParameter("employee", e.getEmployeeID());
		
		List<TimesheetData> result = query.getResultList();
		
		return getTimesheets(result);
	}
	
	private List<Timesheet> getTimesheets(List<TimesheetData> result) {
		List<Timesheet> timesheets = new ArrayList<Timesheet>();
		for (TimesheetData timesheetData : result) {
			timesheets.add(timesheetData.getTimesheet());
		}
		return timesheets;
	}
	
	/*Get timesheets that a supervisor needs to approve*/
	public List<Timesheet> getSubmittedTimesheets(String supervisorId) {

		TypedQuery<TimesheetData> query = em.createQuery(FIND_BY_SUPERVISOR, TimesheetData.class)
				.setParameter("empID", supervisorId);		
		List<TimesheetData> result = query.getResultList();

		return getSubmittedTimesheets(result);
	}
	
	/* get timesheets that need to be approved as dtos*/
	private List<Timesheet> getSubmittedTimesheets(List<TimesheetData> result) {
		List<Timesheet> submittedTimesheets = new ArrayList<Timesheet>();
		for (TimesheetData timesheetData : result) {
			submittedTimesheets.add(timesheetData.getTimesheet());
		}
		return submittedTimesheets;
	}
	
	/*Save a timesheet for an employee*/
	public void saveTimeSheet(Timesheet timesheet, Employee forEmployee) {
					
		//  create a data record from a dto object
		TimesheetData newTimesheet = new TimesheetData(timesheet, forEmployee, forEmployee.getLabourGradeID());
		
		if (newTimesheet.getTimesheetRows().size() == 0) {
			return; // no details in the time sheet
		}
		
		TypedQuery<TimesheetData> query = em.createQuery(FIND_BY_EMPLOYEE, TimesheetData.class)
				.setParameter("employee", forEmployee.getEmployeeID());
		
		List<TimesheetData> result = query.getResultList();
		for (TimesheetData timesheetData : result) {
			
			// removes the "old" timesheet with the given end date
			if (timesheetData.getEndWeek().compareTo(timesheet.getEndWeek()) == 0) {
				em.remove(timesheetData);
				break;
			}
		}	
		
		// replace timesheet with old and newly added data
		em.merge(newTimesheet);
	}
}
