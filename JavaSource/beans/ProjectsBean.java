/** 
 * @author Eric Lyons-Davis
 * */
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import model.project.projects.Projects;
import model.project.reports.MonthlyReportRow;
import model.project.reports.StatusReport;
import model.project.workPackages.Packages;
import model.timesheets.TimesheetLGView;
import model.users.employees.EmployeeProjectAssignment;
import model.users.labourGrades.LabourGradeChargeRate;
import database.EmployeeManager;
import database.LabourGradeManager;
import database.MonthlyReportManager;
import database.PackagesManager;
import database.ProjectEmployeeManager;
import database.ProjectsManager;
import database.StatusReportManager;

/* bean for projects */

@Named("project")
@ConversationScoped
public class ProjectsBean implements Serializable
{
	@Inject Conversation conversation;
	@Inject ProjectsManager projectManager;
	@Inject PackagesManager packagesManager;
	@Inject ProjectEmployeeManager projectEmployeeManager;
	@Inject MonthlyReportManager monthlyReportManager;
	@Inject StatusReportManager statusReportManager;
	@Inject LabourGradeManager labourGradeManager;
	@Inject EmployeeManager employeeManager;
	@Inject StorageBean storage; /* provides access to a bean used to store session scoped variables */
	@Inject PackagesBean packages;
	@Inject LoginBean login;
	
	private static final long serialVersionUID = 1L;
	private String projectID;
	private String projectManagerUsername;
	private Date startDate;
	private Date endDate;
	private String clientName;
	private String description;
	private double markupRate;
	ArrayList<Projects> projectsList = new ArrayList<Projects>();
	ArrayList<Packages> childPackageList = new ArrayList<Packages>(); /* list of all direct child packages */
	ArrayList<MonthlyReportRow> monthlyReportRowListPD; /* person days */
	ArrayList<MonthlyReportRow> monthlyReportRowListLD; /* labour dollars */
	List<String> userNmsForPm = new ArrayList<String>(); 
	
	/* project budget variables */
	private double ssBudget;
	private double dsBudget;
	private double jsBudget;
	private double p1Budget;
	private double p2Budget;
	private double p3Budget;
	private double p4Budget;
	private double p5Budget;
	private double p6Budget;
	private double budget;
	private double estimatedCost;
	private double currentCost;

	public List<String> getUserNmsForPm() {
	
		userNmsForPm = employeeManager.getPossibleUNames(); 
		return userNmsForPm; 
	}
	
	/* monthly report calculations and navigation rules (Monthly Report) */
	public String monthlyReport()
	{
		/* our data source lists for most of the calculations in this method */
		ArrayList<TimesheetLGView> timesheetRowList = monthlyReportManager.getAll(projectID); /* grabs Timesheet rows from DB for specified project id */
		ArrayList<Packages> workPackageList = packagesManager.getAll(projectID); /* each valid package for this project will be stored here */
		
		/* initialize both PD and LD monthly report row lists */
		monthlyReportRowListPD = new ArrayList<MonthlyReportRow>();
		monthlyReportRowListLD = new ArrayList<MonthlyReportRow>();
		
		/* initialize markupRate*/
		markupRate = projectManager.findProjectID(storage.getProjectID()).getMarkupRate();
		
		/* grab the current labour grade charge rates */
		LabourGradeChargeRate labourGradeChargeRate = labourGradeManager.getAllChargeRates().get(0);
		
		for(int i = 0; i < workPackageList.size(); i++) /* do calculations for each valid Timesheet Row and then format them into Monthly Report Row format for display*/
		{
			MonthlyReportRow monthlyReportEntryPD = new MonthlyReportRow(); /* person days row */
			MonthlyReportRow monthlyReportEntryLD = new MonthlyReportRow(); /* labour dollars row */
			StatusReport statusReport = statusReportManager.findMostRecentStatusReport(projectID, workPackageList.get(i).getPackageID());
			
			
			/* Package ID Column */
			monthlyReportEntryPD.setPackageID(workPackageList.get(i).getPackageID()); /* taken from timesheet db results */
			monthlyReportEntryLD.setPackageID(workPackageList.get(i).getPackageID()); /* taken from timesheet db results */
			
			/* Budget Column (BCWS) */
			monthlyReportEntryPD.setBudget( 
					(workPackageList.get(i).getSsBudget() 
					+ workPackageList.get(i).getDsBudget() 
					+ workPackageList.get(i).getJsBudget() 
					+ workPackageList.get(i).getP1Budget() 
					+ workPackageList.get(i).getP2Budget() 
					+ workPackageList.get(i).getP3Budget() 
					+ workPackageList.get(i).getP4Budget() 
					+ workPackageList.get(i).getP5Budget() 
					+ workPackageList.get(i).getP6Budget() ) / 8.0);
			
			monthlyReportEntryLD.setBudget( 
					( (workPackageList.get(i).getSsBudget() / 8) * labourGradeChargeRate.getSsChargeRate() ) + 
					( (workPackageList.get(i).getDsBudget() / 8) * labourGradeChargeRate.getDsChargeRate() ) + 
					( (workPackageList.get(i).getJsBudget() / 8) * labourGradeChargeRate.getJsChargeRate() ) + 
					( (workPackageList.get(i).getP1Budget() / 8) * labourGradeChargeRate.getP1ChargeRate() ) + 
					( (workPackageList.get(i).getP2Budget() / 8) * labourGradeChargeRate.getP2ChargeRate() ) + 
					( (workPackageList.get(i).getP3Budget() / 8) * labourGradeChargeRate.getP3ChargeRate() ) + 
					( (workPackageList.get(i).getP4Budget() / 8) * labourGradeChargeRate.getP4ChargeRate() ) + 
					( (workPackageList.get(i).getP5Budget() / 8) * labourGradeChargeRate.getP5ChargeRate() ) + 
					( (workPackageList.get(i).getP6Budget() / 8) * labourGradeChargeRate.getP6ChargeRate() )		);
			
			double acwpPD = 0;
			double acwpLD = 0;
			
			/* ACWP Column */
			for(int j = 0; j < timesheetRowList.size(); j++) /* search timesheet list for a matching package id. if not found, leave value at zero */
			{
				if(timesheetRowList.get(j).getWorkPackage().equals(workPackageList.get(i).getPackageID()))
				{
					acwpPD += timesheetRowList.get(j).getTotalSum() / 8;
					
					switch(timesheetRowList.get(j).getLabourGradeID())
					{
						case "SS": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getSsChargeRate();
							break;
						case "DS": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getDsChargeRate();
							break;
						case "JS": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getJsChargeRate();
							break;
						case "P1": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP1ChargeRate();
							break;
						case "P2": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP2ChargeRate();
							break;
						case "P3": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP3ChargeRate();
							break;
						case "P4": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP4ChargeRate();
							break;
						case "P5": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP5ChargeRate();
							break;
						case "P6": acwpLD += (timesheetRowList.get(j).getTotalSum() / 8) * labourGradeChargeRate.getP6ChargeRate();
							break;
					}
					
					break;
				}
			}
			
			monthlyReportEntryPD.setACWP(acwpPD);
			monthlyReportEntryLD.setACWP(acwpLD * markupRate);
			
			
			/* EAC Column */
			if(workPackageList.get(i).getStatus().equals("Not Started")) /* if the work package hasnt started yet (EAC = Budget)*/
			{
				
			monthlyReportEntryPD.setEAC(
					(workPackageList.get(i).getSsBudget() 
					+ workPackageList.get(i).getDsBudget() 
					+ workPackageList.get(i).getJsBudget() 
					+ workPackageList.get(i).getP1Budget() 
					+ workPackageList.get(i).getP2Budget() 
					+ workPackageList.get(i).getP3Budget() 
					+ workPackageList.get(i).getP4Budget() 
					+ workPackageList.get(i).getP5Budget() 
					+ workPackageList.get(i).getP6Budget() ) / 8.0);
			
			monthlyReportEntryLD.setEAC(
					( (workPackageList.get(i).getSsBudget() / 8) * labourGradeChargeRate.getSsChargeRate() ) + 
					( (workPackageList.get(i).getDsBudget() / 8) * labourGradeChargeRate.getDsChargeRate() ) + 
					( (workPackageList.get(i).getJsBudget() / 8) * labourGradeChargeRate.getJsChargeRate() ) + 
					( (workPackageList.get(i).getP1Budget() / 8) * labourGradeChargeRate.getP1ChargeRate() ) + 
					( (workPackageList.get(i).getP2Budget() / 8) * labourGradeChargeRate.getP2ChargeRate() ) + 
					( (workPackageList.get(i).getP3Budget() / 8) * labourGradeChargeRate.getP3ChargeRate() ) + 
					( (workPackageList.get(i).getP4Budget() / 8) * labourGradeChargeRate.getP4ChargeRate() ) + 
					( (workPackageList.get(i).getP5Budget() / 8) * labourGradeChargeRate.getP5ChargeRate() ) + 
					( (workPackageList.get(i).getP6Budget() / 8) * labourGradeChargeRate.getP6ChargeRate() )		);
			}
			
			else if(workPackageList.get(i).getStatus().equals("Completed")) /* if the work package has been completed (EAC = timesheets) */
			{
				double sumPD = 0;
				double sumLD = 0;
				for(int x = 0; x < timesheetRowList.size(); x++)
				{
					if(timesheetRowList.get(x).getWorkPackage().equals(workPackageList.get(i).getPackageID()))
					{
						sumPD += timesheetRowList.get(x).getTotalSum() / 8;
						
						switch(timesheetRowList.get(x).getLabourGradeID())
						{
							case "SS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getSsChargeRate();
								break;
							case "DS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getDsChargeRate();
								break;
							case "JS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getJsChargeRate();
								break;
							case "P1": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP1ChargeRate();
								break;
							case "P2": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP2ChargeRate();
								break;
							case "P3": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP3ChargeRate();
								break;
							case "P4": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP4ChargeRate();
								break;
							case "P5": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP5ChargeRate();
								break;
							case "P6": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP6ChargeRate();
								break;
						}
					}
				}
				
				monthlyReportEntryPD.setEAC(sumPD);
				monthlyReportEntryLD.setEAC(sumLD * markupRate);
			}
			
			else /* if the work package has been started (open or closed) but NOT completed yet (EAC = timesheets + ETC) */
			{
				double sumPD = 0;
				double sumLD = 0;
				for(int x = 0; x < timesheetRowList.size(); x++)
				{
					if(timesheetRowList.get(x).getWorkPackage().equals(workPackageList.get(i).getPackageID()))
					{
						sumPD += timesheetRowList.get(x).getTotalSum() / 8;
						
						switch(timesheetRowList.get(x).getLabourGradeID())
						{
							case "SS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getSsChargeRate();
								break;
							case "DS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getDsChargeRate();
								break;
							case "JS": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getJsChargeRate();
								break;
							case "P1": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP1ChargeRate();
								break;
							case "P2": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP2ChargeRate();
								break;
							case "P3": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP3ChargeRate();
								break;
							case "P4": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP4ChargeRate();
								break;
							case "P5": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP5ChargeRate();
								break;
							case "P6": sumLD += (timesheetRowList.get(x).getTotalSum() / 8) * labourGradeChargeRate.getP6ChargeRate();
								break;
						}
					}
				}
				
				/* status report estimated ETC */
				sumPD += 
				(( statusReport.getSsEstimate()
				+ statusReport.getDsEstimate()
				+ statusReport.getJsEstimate()
				+ statusReport.getP1Estimate()
				+ statusReport.getP2Estimate()
				+ statusReport.getP3Estimate()
				+ statusReport.getP4Estimate()
				+ statusReport.getP5Estimate()
				+ statusReport.getP6Estimate()	) / 8);
				
				/* status report estimated ETC */
				sumLD += 
				(( (statusReport.getSsEstimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getDsEstimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getJsEstimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP1Estimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP2Estimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP3Estimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP4Estimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP5Estimate() * labourGradeChargeRate.getSsChargeRate())
				+ (statusReport.getP6Estimate() * labourGradeChargeRate.getSsChargeRate())	) / 8);
				
				monthlyReportEntryPD.setEAC(sumPD);
				monthlyReportEntryLD.setEAC(sumLD * markupRate);
			}
				
			/* Variance Column */
			monthlyReportEntryPD.setVariance(calculateVariance(monthlyReportEntryPD.getEAC(), monthlyReportEntryPD.getBudget()));
			monthlyReportEntryLD.setVariance(calculateVariance(monthlyReportEntryLD.getEAC(), monthlyReportEntryLD.getBudget()));
			
			
			/* Comments Column */
			monthlyReportEntryPD.setComments(statusReport.getComments());
			monthlyReportEntryLD.setComments(statusReport.getComments());
			
			monthlyReportRowListPD.add(monthlyReportEntryPD);
			monthlyReportRowListLD.add(monthlyReportEntryLD);
		}
		
		return "MonthlyReport";
	}
	
	
	/* calculates the variance field (Monthly Report) */
	public double calculateVariance(double EAC, double BCWS)
	{
		double variance = (( EAC / BCWS ) - 1 ) * 100;
		return variance;
	}
	
	
	/* grabs the data in the database to display */
	public String displayProjectList()
	{
		projectsList = projectManager.getAll("-1"); /* -1 indicates the parent package of all other packages for the project */		
		return "Success"; 
	}
	
	/* create a new project */
	public String createProject()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(employeeManager.findUserName(projectManagerUsername) == null) /* if the inputed project manager's employee id does not exist */
		{
			FacesContext.getCurrentInstance().addMessage("createProjectForm:projectManagerUsername", 
			           new FacesMessage(null, "Selected Project Manager Employee Username not found."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
		
		if(projectManager.findProjectID(projectID) == null) /* if the project ID inputed is unique */
		{
			Projects project = new Projects(projectID, projectManagerUsername, description, clientName, startDate, endDate, markupRate);
			project.setPackageID("-1");
			projectManager.persist(project);
			
			/* add project manager to employee project assignment list*/
			EmployeeProjectAssignment employeeProjectAssignment = new EmployeeProjectAssignment(projectID, projectManagerUsername);
			projectEmployeeManager.persist(employeeProjectAssignment);
			
			displayProjectList(); /* update the project list with the new entry */

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Success";
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("createProjectForm:projectID", 
			           new FacesMessage(null, "Selected Project ID is already in use."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	/* checks to ensure that the selected project to update exists */
	public String validateProject()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(projectManager.findProjectID(projectID) != null)
		{
			
			/* if you are not a project manager, admin or superuser */
			if(!login.getIsAdmin() && !login.getIsSupervisor()  &&
					!projectManager.findProjectID(projectID).getProjectManagerUsername().equals(login.getUserName()) )
			{
				FacesContext.getCurrentInstance().addMessage("selectProjectForm:projectID", 
				           new FacesMessage(null, "You do not have permission to view this project's details."));
				
				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Failure";
			}
			
			
			Projects project = projectManager.findProjectID(projectID);
			childPackageList = packagesManager.getChildPackages("-1", projectID); /* grab project's direct child packages */
			
			storage.setParentPackageID("-1"); /* sets focus for storage bean as to which package/project you are looking at */
			storage.setProjectID(projectID); /* sets focus for storage bean as to which package/project you are looking at */
			
			double calculatedCurrentCost = 0;
			double calculatedEstimatedCost = 0;
			double calculatedSSBudgetCost= 0;
			double calculatedDSBudgetCost= 0;
			double calculatedJSBudgetCost= 0;
			double calculatedP1BudgetCost= 0;
			double calculatedP2BudgetCost= 0;
			double calculatedP3BudgetCost= 0;
			double calculatedP4BudgetCost= 0;
			double calculatedP5BudgetCost= 0;
			double calculatedP6BudgetCost= 0;
			
			for(int i = 0; i < childPackageList.size(); i++) /* load all child package lists */
			{
				packages.loadChildPackageList(childPackageList.get(i));
			}
			
			for(int i = 0; i < childPackageList.size(); i++) /* calculate values from child packages */
			{
				calculatedCurrentCost += childPackageList.get(i).obtainCurrentCost();
				calculatedEstimatedCost += childPackageList.get(i).obtainEstimatedCost();
				calculatedSSBudgetCost += childPackageList.get(i).obtainSSBudgetCost();
				calculatedDSBudgetCost += childPackageList.get(i).obtainDSBudgetCost();
				calculatedJSBudgetCost += childPackageList.get(i).obtainJSBudgetCost();
				calculatedP1BudgetCost += childPackageList.get(i).obtainP1BudgetCost();
				calculatedP2BudgetCost += childPackageList.get(i).obtainP2BudgetCost();
				calculatedP3BudgetCost += childPackageList.get(i).obtainP3BudgetCost();
				calculatedP4BudgetCost += childPackageList.get(i).obtainP4BudgetCost();
				calculatedP5BudgetCost += childPackageList.get(i).obtainP5BudgetCost();
				calculatedP6BudgetCost += childPackageList.get(i).obtainP6BudgetCost();
			}
			
			
			projectID = project.getProjectID();
			projectManagerUsername = project.getProjectManagerUsername();
			description = project.getDescription();
			clientName = project.getClientName();
			startDate = project.getStartDate();
			endDate = project.getEndDate();
			markupRate = project.getMarkupRate();
			ssBudget = calculatedSSBudgetCost;
			dsBudget = calculatedDSBudgetCost;
			jsBudget = calculatedJSBudgetCost;
			p1Budget = calculatedP1BudgetCost;
			p2Budget = calculatedP2BudgetCost;
			p3Budget = calculatedP3BudgetCost;
			p4Budget = calculatedP4BudgetCost;
			p5Budget = calculatedP5BudgetCost;
			p6Budget = calculatedP6BudgetCost;
			estimatedCost = calculatedEstimatedCost; 
			currentCost = calculatedCurrentCost;
			budget = calculatedSSBudgetCost + calculatedDSBudgetCost + calculatedJSBudgetCost + 
					calculatedP1BudgetCost + calculatedP2BudgetCost + calculatedP3BudgetCost + 
					calculatedP4BudgetCost + calculatedP5BudgetCost + calculatedP6BudgetCost;
			return "Success";
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("selectProjectForm:projectID", 
			           new FacesMessage(null, "Selected Project does not exist."));
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	
	/* update a projects's info (with the exception of projectID) */
	public String updateProject()
	{
		if(employeeManager.findUserName(projectManagerUsername) == null) /* if the inputed project manager's employee id does not exist */
		{
			FacesContext.getCurrentInstance().addMessage("updateProjectForm:projectManagerUsername", 
			           new FacesMessage(null, "Selected Project Manager Employee Username not found."));
			return "Failure";
		}
		
		/* update project manager in employee project assignment list */
		EmployeeProjectAssignment employeeProjectAssignment = new EmployeeProjectAssignment(projectID, projectManagerUsername);
		projectEmployeeManager.merge(employeeProjectAssignment);
		
		/* updatedEmployee used as a container to pass new data to EmployeeManager for updating */
		Projects updatedProjects = new Projects(projectID, projectManagerUsername, description, clientName, startDate, endDate, markupRate);
		projectManager.update(projectManager.findProjectID(projectID), updatedProjects);
		displayProjectList(); /* update the project list */
		
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Success";
	}
	
	/* delete an employee based on User Name */
	public String deleteProject()
	{
		try
		{
			if(conversation.isTransient())
			{
				conversation.begin();
			}
			
			Projects project = projectManager.findProjectID(projectID);
			
			if(project != null)
			{
				projectManager.remove(project);
				displayProjectList(); /* update the project list */
				
				/* remove project manager from employee project assignment list */
				EmployeeProjectAssignment employeeProjectAssignment = new EmployeeProjectAssignment(projectID, project.getProjectManagerUsername());
				projectEmployeeManager.remove(employeeProjectAssignment);
				packagesManager.deletePackages(projectID); /* deletes all packages for this project */
				
				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Success";
			}
			
			else
			{
				FacesContext.getCurrentInstance().addMessage("deleteProjectForm:projectID", 
				           new FacesMessage(null, "Selected Project does not exist."));
				
				if(!conversation.isTransient())
				{
					conversation.end();
				}
				
				return "Failure";
			}
		}
		
		catch(Exception e)
		{
			FacesContext.getCurrentInstance().addMessage("deleteEmployeeForm:userName", 
			           new FacesMessage(null, "Selected User Name does not exist."));
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	
	/* navigation rules when you select the Back button; reloads the employee table properly */
	public String projectManagementMenu()
	{
		displayProjectList(); /* update the project list with entries */
		return "ProjectManagement";
	}
	
	/* navigation rules when you select the Back button; reloads the employee table properly */
	public String back()
	{
		displayProjectList(); /* update the project list with the new entry */
		return "Back";
	}
	
	/* navigation rules when you select the Back button and need to end a conversation; reloads the employee table properly */
	public String backErase()
	{
		displayProjectList(); /* update the project list with the new entry */
		
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Back";
	}
	
	/* getters and setters */
	
	public Conversation getConversation()
	{
		return conversation;
	}
	public void setConversation(Conversation conversation) 
	{
		this.conversation = conversation;
	}
	public ProjectsManager getProjectManager()
	{
		return projectManager;
	}
	public void setProjectManager(ProjectsManager projectManager)
	{
		this.projectManager = projectManager;
	}
	public String getProjectID() 
	{
		return projectID;
	}
	public void setProjectID(String projectID)
	{
		this.projectID = projectID;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate) 
	{
		this.startDate = startDate;
	}
	public Date getEndDate() 
	{
		return endDate;
	}
	public void setEndDate(Date endDate) 
	{
		this.endDate = endDate;
	}
	public double getEstimatedCost()
	{
		return estimatedCost;
	}
	public void setEstimatedCost(double estimatedCost)
	{
		this.estimatedCost = estimatedCost;
	}
	public double getCurrentCost()
	{
		return currentCost;
	}
	public void setCurrentCost(double currentCost) 
	{
		this.currentCost = currentCost;
	}
	public String getClientName() 
	{
		return clientName;
	}
	public void setClientName(String clientName) 
	{
		this.clientName = clientName;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public ArrayList<Projects> getProjectsList() 
	{
		return projectsList;
	}
	public void setProjectsList(ArrayList<Projects> projectsList) 
	{
		this.projectsList = projectsList;
	}
	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}

	public StorageBean getStorage() 
	{
		return storage;
	}

	public void setStorage(StorageBean storage)
	{
		this.storage = storage;
	}
	
	
	public double getBudget() 
	{
		return budget;
	}

	public void setBudget(double budget) 
	{
		this.budget = budget;
	}

	public PackagesManager getPackagesManager() 
	{
		return packagesManager;
	}

	public void setPackagesManager(PackagesManager packagesManager) 
	{
		this.packagesManager = packagesManager;
	}

	public double getSsBudget() {
		return ssBudget;
	}

	public void setSsBudget(double ssBudget) 
	{
		this.ssBudget = ssBudget;
	}

	public double getDsBudget() 
	{
		return dsBudget;
	}

	public void setDsBudget(double dsBudget) 
	{
		this.dsBudget = dsBudget;
	}

	public double getJsBudget() 
	{
		return jsBudget;
	}

	public void setJsBudget(double jsBudget) 
	{
		this.jsBudget = jsBudget;
	}

	public double getP1Budget() 
	{
		return p1Budget;
	}

	public void setP1Budget(double p1Budget)
	{
		this.p1Budget = p1Budget;
	}

	public double getP2Budget() 
	{
		return p2Budget;
	}

	public void setP2Budget(double p2Budget) 
	{
		this.p2Budget = p2Budget;
	}

	public double getP3Budget() 
	{
		return p3Budget;
	}

	public void setP3Budget(double p3Budget) 
	{
		this.p3Budget = p3Budget;
	}

	public double getP4Budget()
	{
		return p4Budget;
	}

	public void setP4Budget(double p4Budget)
	{
		this.p4Budget = p4Budget;
	}

	public double getP5Budget() 
	{
		return p5Budget;
	}

	public void setP5Budget(double p5Budget) 
	{
		this.p5Budget = p5Budget;
	}

	public double getP6Budget() 
	{
		return p6Budget;
	}

	public void setP6Budget(double p6Budget) 
	{
		this.p6Budget = p6Budget;
	}


	public ArrayList<MonthlyReportRow> getMonthlyReportRowListPD() 
	{
		return monthlyReportRowListPD;
	}


	public void setMonthlyReportRowListPD(
			ArrayList<MonthlyReportRow> monthlyReportRowListPD) 
	{
		this.monthlyReportRowListPD = monthlyReportRowListPD;
	}


	public ArrayList<MonthlyReportRow> getMonthlyReportRowListLD() 
	{
		return monthlyReportRowListLD;
	}


	public void setMonthlyReportRowListLD(
			ArrayList<MonthlyReportRow> monthlyReportRowListLD) 
	{
		this.monthlyReportRowListLD = monthlyReportRowListLD;
	}


	public String getProjectManagerUsername() 
	{
		return projectManagerUsername;
	}


	public void setProjectManagerUsername(String projectManagerUsername) 
	{
		this.projectManagerUsername = projectManagerUsername;
	}


	public double getMarkupRate()
	{
		return markupRate;
	}


	public void setMarkupRate(double markupRate) 
	{
		this.markupRate = markupRate;
	}

}
