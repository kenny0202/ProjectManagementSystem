package beans;

/**
 * @author Olga
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.project.projects.Projects;
import model.project.workPackages.Packages;
import model.timesheets.Timesheet;
import model.timesheets.TimesheetRow;
import model.users.employees.Employee;
import database.EmployeeManager;
import database.PackagesManager;
import database.ProjectsManager;
import database.TimesheetManager;

@Named
@ConversationScoped
public class TimesheetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject TimesheetManager timesheetManager;
	@Inject EmployeeManager employeeManager; 
	@Inject PackagesManager packagesManager; 
	@Inject ProjectsManager projectsManager; 
	
	@Inject Conversation conversation;
	
	Timesheet timesheet;
	
	private Employee reviewee; 	

	private List<String> assignedProjects = new ArrayList<String>(); 
	private List<String> leafPackages = new ArrayList<String>(); 
	
	
	public TimesheetBean() {
		if (timesheet == null) {
			timesheet = new Timesheet();
		}
	}
	
	public List<String> getAssignedProjects(String userName) {
				
		assignedProjects = projectsManager.getAllTopLevelIds(userName); 
		assignedProjects.add(0, "Select Project");
		return assignedProjects; 
	}
		
	public List<String> getLeafPackages(String projectID) {
				
		leafPackages = packagesManager.getLeafPackages(projectID); 
		
		if (leafPackages.size() > 0  )
			leafPackages.add(0, "Select Package"); 
		//else
		if (leafPackages.size() == 0 && !projectID.equals("") && !projectID.equals("Select Project"))	
			leafPackages.add(0, "None Available");
		
		return leafPackages; 	
	}
	
	/**
	 * Gets the timesheet. 
	 * @return the time sheet. 
	 */
	public Timesheet getTimeSheet() {	
		return timesheet;
	}
	
	public List<Timesheet> getTimesheets(Employee e) {
		
		List<Timesheet> timesheets = timesheetManager.getTimesheets(e);
		addCurrentWeekIfNotFound(timesheets);
		return timesheets;
	}
	
	public List<Timesheet> getSubmittedTimesheets(String supervisorId) {
		
		List<Timesheet> submittedTimesheets = timesheetManager.getSubmittedTimesheets(supervisorId); 				
		return submittedTimesheets;
	}

	public boolean dataExists() {
		
		boolean dataExists = false; 
		
		for (TimesheetRow row : timesheet.getDetails()) {
			
			dataExists = (row.getProjectID().compareTo("") != 0) 
					|| (row.getWorkPackage() != null && row.getWorkPackage().length() > 0)
					|| (row.getSum().doubleValue() > 0); 
		}
		
		return dataExists; 
	}
	
	public String submissionsExist(String supervisorId) {
				
		List<Timesheet> submittedTimesheets = timesheetManager.getSubmittedTimesheets(supervisorId);
		
		if (submittedTimesheets.size() == 0)
			return "noReviewees.xhtml";
		else
			return "viewNeedApproval.xhtml"; 
	}
	
	private void addCurrentWeekIfNotFound(List<Timesheet> timesheets ) {
		boolean found = false;
		Timesheet currentWeek = new Timesheet();
		for (Timesheet timesheet : timesheets) {
			if (timesheet.getWeekEnding().equals(currentWeek.getWeekEnding())) {
				found = true;
				break;
			}
		}
		if (!found) {
			timesheets.add(currentWeek);
		}
	}
	
	public String addTimesheetRow() {
		if (timesheet != null) {
			timesheet.addRow();
		}
		return null;
	}
	
	public boolean totalHoursValid() {
		
		double flex = timesheet.getFlextime().doubleValue(); 
		double over = timesheet.getOvertime().doubleValue(); 
		double totalHrs = 0.0; //timesheet.getTotalHours().doubleValue(); 
		
		// only want to grab the hours from where a row has a selected
		// project and work package
		for (int i = 0; i < timesheet.getDetails().size(); i++ ) {
			
			TimesheetRow tmp = timesheet.getDetails().get(i); 
			
			boolean rowDataExists = 
					   (!tmp.getProjectID().equals("Select Project")) 
						&& (!tmp.getWorkPackage().equals("Select Package"))	
						&& (!tmp.getWorkPackage().equals("None Available"));		
			
			if ( rowDataExists) {
				
				for (int j = 0; j < tmp.getHoursForWeek().length; j++) {
					
					if (tmp.getHoursForWeek()[j] != null)
						totalHrs += tmp.getHoursForWeek()[j].doubleValue(); 
				}
			}
		}
		
		if ( (totalHrs - (flex + over)) < 40.0 ) 
			return false; 
		
		return true; 
	}
	
	/** Sets the flag that the timesheet needs approval.*/
	public String submitForApproval(Employee emp) {

		if (!totalHoursValid()) {
			
			FacesContext.getCurrentInstance().addMessage("modifyTimesheetForm:inputErrorMsg",
					new FacesMessage(null, "For Submission: Total Hours must equal at least 40 after subtraction of overtime and flextime."));
			return null; 
		}
		
		try {
		
			validateTimesheet();
			timesheet.setSubmittedForApproval(true);
			timesheet.setDenied(false);
			timesheetManager.saveTimeSheet(timesheet, emp);
			return "viewTimesheets"; 
			
		} catch (Exception e) {
	
			FacesContext.getCurrentInstance().addMessage("modifyTimesheetForm:inputErrorMsg",
					new FacesMessage(null, e.getMessage()));
			return null; 
		}
	}

	public String approveTimesheet(Employee forEmployee, String supervisor) {
		
		timesheet.setSubmittedForApproval(false);
		timesheet.setApproved(true);
		updateCurrentCosts(); 
		timesheetManager.saveTimeSheet(timesheet, forEmployee);
		// determine what page to go back to, approvalList, or message saying none
		return submissionsExist(supervisor);
	}	
	
	public String denyTimesheet(Employee forEmployee, String supervisor) {
		
		timesheet.setSubmittedForApproval(false);
		timesheet.setDenied(true);
		timesheetManager.saveTimeSheet(timesheet, forEmployee);
		// determine what page to go back to, approvalList, or message saying none
		return submissionsExist(supervisor);
	}	
	
	public void updateCurrentCosts() {

		List<TimesheetRow> rows = timesheet.getDetails(); 
		Packages currPackage;
		TimesheetRow tmpRow; 

		// loop through timesheet rows
		for ( int i = 0; i < rows.size(); i++) {
			
			tmpRow = rows.get(i); 
			currPackage = packagesManager.findWorkpackage(tmpRow.getProjectID(), tmpRow.getWorkPackage());

			if (currPackage != null) {
				
				BigDecimal[] hoursForWeek = tmpRow.getHoursForWeek();
				BigDecimal totalHours = new BigDecimal(0); 
				
				// add up total hours in the row
				for (int j = 0; j < hoursForWeek.length; j++ ) {
					
					if (hoursForWeek[j] != null) // null check
						totalHours = totalHours.add(hoursForWeek[j]); 
				}
				
				double oldCurrentCost = currPackage.getCurrentCost(); 
				double newCurrentCost = oldCurrentCost + totalHours.doubleValue(); 
				
				currPackage.setCurrentCost(newCurrentCost);
				packagesManager.merge(currPackage);
			}	
		}
	}
	
	/*For saving timesheet changes*/
	public String save(Employee emp) {
		
		try {
			validateTimesheet();
			timesheetManager.saveTimeSheet(timesheet, emp); // save timesheet into database for current user
			FacesContext.getCurrentInstance().addMessage("modifyTimesheetForm:inputErrorMsg",
					new FacesMessage(null, "Changes Saved!"));

			return null; 
			
		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage("modifyTimesheetForm:inputErrorMsg",
					new FacesMessage(null, e.getMessage()));
			
			return null;
		}
	}
	
	public String displayTimesheet(Timesheet ts) {
		
		if (conversation.isTransient()) {
			conversation.begin();
		}
		
		// if the timesheet has less than 5 rows add additional
		// rows up to 5 in total. 
		if (ts.getDetails().size() < 5) {
			int count = ts.getDetails().size();
			for (int i = 0; i < 5 - count; i++) {
				ts.addRow();
			}
		}	
		// current timesheet is the one the user selected. 
		timesheet = ts; 		
		
		return "modifyTimesheet";
	}
	
	public String backToAll() {
		
		if (!conversation.isTransient()) 
			conversation.end();
			
		return "viewTimesheets";
	}
	
	public String displaySubmittedTimesheet(Timesheet ts) {
		
		if (conversation.isTransient()) {
			conversation.begin();
		}
		timesheet = ts; 
		reviewee = ts.getEmployee(); 
				
		return "approveTimesheet";
	}
	
	public Employee getReviewee() {
		return reviewee; 
	}	
	
	/**
	 * Work around to check that the hours entered in each row are 
	 * between 0 and 24. 
	 */
	private void validateTimesheet() {
		for (TimesheetRow row : timesheet.getDetails()) {
			row.setHoursForWeek(row.getHoursForWeek()); // to trigger 0-24 hour range validation
		}
	}
}
