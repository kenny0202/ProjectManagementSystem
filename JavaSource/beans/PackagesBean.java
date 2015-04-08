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

import model.project.reports.StatusReport;
import model.project.reports.StatusReportRow;
import model.project.workPackages.Packages;
import model.timesheets.TimesheetLGView;
import model.timesheets.TimesheetRowData;
import model.users.employees.Employee;
import database.EmployeeManager;
import database.MonthlyReportManager;
import database.ProjectsManager;
import database.PackagesManager;
import database.StatusReportManager;

@Named("package")
@ConversationScoped
public class PackagesBean implements Serializable 
{
	@Inject Conversation conversation;
	@Inject ProjectsManager projectManager;
	@Inject PackagesManager packagesManager;
	@Inject StatusReportManager statusReportManager;
	@Inject MonthlyReportManager monthlyReportManager;
	@Inject EmployeeManager employeeManager;
	@Inject StorageBean storage; /* provides access to a bean used to store session scoped variables */
	@Inject ProjectsBean project; /* provides access to a bean used to store session scoped variables */
	
	private static final long serialVersionUID = 1L;
	ArrayList<Packages> packageList = new ArrayList<Packages>();
	private String packageID;
	private String projectID; /* the project id of the project that the work package belongs to */
	private String parentPackageID; /* the ID of the parent package */
	private ArrayList<Packages> childPackageList = new ArrayList<Packages>(); /* list of all direct child packages */
	private ArrayList<Employee> employeeList = new ArrayList<Employee>(); /* a list of all employees assigned to the work package */
	private ArrayList<StatusReportRow> statusReportRowList; /* Represents an array of all rows for the package's status report */
	private String engineerUsername;
	private String status;
	private Date startDate;
	private Date endDate;
	private String description;
	
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
	
	/* status report variables */
	private double ssEstimate;
	private double dsEstimate;
	private double jsEstimate;
	private double p1Estimate;
	private double p2Estimate;
	private double p3Estimate;
	private double p4Estimate;
	private double p5Estimate;
	private double p6Estimate;
	private String comments;
	
	/* view control flags */
	private boolean createChildren = true;
	
	List<String> userNmsForResEngineer = new ArrayList<String>(); 
	
	
	public List<String> getUserNmsForResEngineer() {
		
		userNmsForResEngineer = employeeManager.getPossibleUNames(); 
		return userNmsForResEngineer;
	}
	
	/* status report calculations and navigation rules to view status reports (Status Report) */
	public String viewStatusReport()
	{
		StatusReport mostRecentStatusReport = statusReportManager.findMostRecentStatusReport(projectID, packageID); /* most recent status report (ESTIMATES)*/
		
		ArrayList<TimesheetLGView> timesheetRowList = statusReportManager.getAllRows(projectID, packageID); /* grabs Timesheet rows from DB for specified project/package id combo (ACTUAL) */
		statusReportRowList = new ArrayList<StatusReportRow>(); /* refresh list (null it entirely) */
		
		/* rows used for the report; assigned their labour grade codes */
		StatusReportRow statusReportSSRow = new StatusReportRow("SS");
		StatusReportRow statusReportDSRow = new StatusReportRow("DS");
		StatusReportRow statusReportJSRow = new StatusReportRow("JS");
		StatusReportRow statusReportP1Row = new StatusReportRow("P1");
		StatusReportRow statusReportP2Row = new StatusReportRow("P2");
		StatusReportRow statusReportP3Row = new StatusReportRow("P3");
		StatusReportRow statusReportP4Row = new StatusReportRow("P4");
		StatusReportRow statusReportP5Row = new StatusReportRow("P5");
		StatusReportRow statusReportP6Row = new StatusReportRow("P6");
		
		/* get actuals */
		for(int i = 0; i < timesheetRowList.size(); i++) /* do calculations for each row in the statusReportList and update each row's estimate and actual values*/
		{
			switch(timesheetRowList.get(i).getLabourGradeID())
			{
				case "SS": 
					statusReportSSRow.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "DS":
					statusReportDSRow.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "JS":
					statusReportJSRow.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P1":
					statusReportP1Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P2":
					statusReportP2Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P3":
					statusReportP3Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P4":
					statusReportP4Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P5":
					statusReportP5Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				case "P6":
					statusReportP6Row.setProgressInPD(timesheetRowList.get(i).getTotalSum() / 8);
					break;
				default:
			}
		}
		
		/* add rows to row list */
		statusReportRowList.add(statusReportSSRow);
		statusReportRowList.add(statusReportDSRow);
		statusReportRowList.add(statusReportJSRow);
		statusReportRowList.add(statusReportP1Row);
		statusReportRowList.add(statusReportP2Row);
		statusReportRowList.add(statusReportP3Row);
		statusReportRowList.add(statusReportP4Row);
		statusReportRowList.add(statusReportP5Row);
		statusReportRowList.add(statusReportP6Row);
		
		if(mostRecentStatusReport != null) /* if a status report was found */
		{
			/* get estimates */
			for(int i = 0; i < statusReportRowList.size(); i++) /* do calculations for each row in the statusReportList and update each row's estimate and actual values*/
			{
				statusReportRowList.get(i).setEstimateToComplete(mostRecentStatusReport.getStatusReportRowLabourGradeByIndex(i) / 8);
			}
			
			comments = mostRecentStatusReport.getComments();
		}
		
		
		return "ViewStatusReport";
	}
	
	
	/* calculate actual value of a timesheet row in PD */
	public double calculateRowActualValue(TimesheetRowData row)
	{
		double sum = (row.getMon().doubleValue() + row.getTue().doubleValue() + row.getWed().doubleValue() 
				+ row.getThu().doubleValue() + row.getFri().doubleValue() + row.getSat().doubleValue() + row.getSun().doubleValue()) / 8;
		return sum;
	}
	
	
	/* creates a status report based on the responsible engineer's estimate inputs and comments */
	public String createStatusReport()
	{
		StatusReport statusReport = new StatusReport();
		statusReport.setProjectID(projectID);
		statusReport.setPackageID(packageID);
		statusReport.setSsEstimate(ssEstimate);
		statusReport.setDsEstimate(dsEstimate);
		statusReport.setJsEstimate(jsEstimate);
		statusReport.setP1Estimate(p1Estimate);
		statusReport.setP2Estimate(p2Estimate);
		statusReport.setP3Estimate(p3Estimate);
		statusReport.setP4Estimate(p4Estimate);
		statusReport.setP5Estimate(p5Estimate);
		statusReport.setP6Estimate(p6Estimate);
		statusReport.setComments(comments);
		statusReportManager.persist(statusReport);
		
		/* update package's total estimated cost */
		double newTotalEstimate = ssEstimate + dsEstimate + jsEstimate + p1Estimate + p2Estimate + p3Estimate + p4Estimate + p5Estimate + p6Estimate; /* adds total estimate to update packages total estimate value */
		packagesManager.updateTotalEstimate(packageID, storage.getProjectID(), newTotalEstimate);
		estimatedCost = newTotalEstimate;
		
		return "Success";
	}
	
	
	
	/* grabs the data in the database to display packages */
	public String displayPackageList()
	{
		packageList = packagesManager.getAll(storage.getProjectID(), storage.getParentPackageID());
		return "Success"; 
	}
	
	/* grabs the data in the database to display packages */
	public String displayChildPackageList()
	{
		packageList = packagesManager.getAllChildren(storage.getParentPackageID(), storage.getProjectID());
		return "Success"; 
	}
	
	
	/* navigation rules when you select the Back button; reloads the package table properly */
	public String packageManagementMenu()
	{

		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		storage.setParentPackageID("-1");
		
		displayPackageList(); /* update the package list with entries */
		return "PackageManagement";
	}
	
	/* loads child package list into arraylist */
	public String childPackageManagementMenu()
	{

		if(!conversation.isTransient())
		{
			conversation.end();
		}

		allowChildPackageCreation(packageID); /* decided if the page should show the option to create children or not */
		displayChildPackageList(); /* update the child package list with entries */
		return "PackageManagement";
	}
	
	
	/* checks to ensure that the selected package exists */
	public String validateProject()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(packagesManager.findChildPackageID(packageID, storage.getParentPackageID(), storage.getProjectID()) != null)
		{
			Packages packages = packagesManager.findChildPackageID(packageID, storage.getParentPackageID(), storage.getProjectID());
			childPackageList = packagesManager.getChildPackages(packageID, storage.getProjectID()); /* grab packages direct child packages */
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
				loadChildPackageList(childPackageList.get(i));
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
		
			projectID = packages.getProjectID();
			packageID = packages.getPackageID();
			parentPackageID = packages.getParentPackageID();
			engineerUsername = packages.getEngineerUsername();
			description = packages.getDescription();
			status = packages.getStatus();
			startDate = packages.getStartDate();
			endDate = packages.getEndDate();
			
			if(calculatedEstimatedCost == 0 && calculatedCurrentCost == 0 && calculatedSSBudgetCost == 0 &&
					calculatedDSBudgetCost == 0 && calculatedJSBudgetCost == 0 && calculatedP1BudgetCost == 0
					 && calculatedP2BudgetCost == 0 && calculatedP3BudgetCost == 0 && calculatedP4BudgetCost == 0
					 && calculatedP5BudgetCost == 0 && calculatedP6BudgetCost == 0) /* if item is a leaf package */
			{
				estimatedCost = packages.getEstimatedCost();
				currentCost = packages.getCurrentCost();
				ssBudget = packages.getSsBudget();
				dsBudget = packages.getDsBudget();
				jsBudget = packages.getJsBudget();
				p1Budget = packages.getP1Budget();
				p2Budget = packages.getP2Budget();
				p3Budget = packages.getP3Budget();
				p4Budget = packages.getP4Budget();
				p5Budget = packages.getP5Budget();
				p6Budget = packages.getP6Budget();
				budget = packages.getSsBudget() + packages.getDsBudget() + packages.getJsBudget() + 
						packages.getP1Budget() + getP2Budget() + getP3Budget() + getP4Budget() + getP5Budget() + getP6Budget();
			}
			
			else
			{
				estimatedCost = calculatedEstimatedCost;
				currentCost = calculatedCurrentCost;
				ssBudget = calculatedSSBudgetCost;
				dsBudget = calculatedDSBudgetCost;
				jsBudget = calculatedJSBudgetCost;
				p1Budget = calculatedP1BudgetCost;
				p2Budget = calculatedP2BudgetCost;
				p3Budget = calculatedP3BudgetCost;
				p4Budget = calculatedP4BudgetCost;
				p5Budget = calculatedP5BudgetCost;
				p6Budget = calculatedP6BudgetCost;
				budget = calculatedSSBudgetCost + calculatedDSBudgetCost + calculatedJSBudgetCost + 
						calculatedP1BudgetCost + calculatedP2BudgetCost + calculatedP3BudgetCost + 
						calculatedP4BudgetCost + calculatedP5BudgetCost + calculatedP6BudgetCost;
			}
			
			allowChildPackageCreation(packageID); /* decided if the page should show the option to create children or not */
			storage.setParentPackageID(packageID); /* sets focus for storage bean as to which package/project you are looking at */
			return "Success";
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("selectPackageForm:packageID", 
			           new FacesMessage(null, "Selected Package does not exist."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	
	

	/* create a new package */
	public String createPackage()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(employeeManager.findUserName(engineerUsername) == null) /* if the inputed project manager's employee id does not exist */
		{
			FacesContext.getCurrentInstance().addMessage("createPackageForm:engineerUsername", 
			           new FacesMessage(null, "Selected Engineer Employee Username not found."));

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
		
		if(packagesManager.findPackageID(packageID, storage.getProjectID()) == null) /* if the package ID inputed is unique */
		{
			projectID = storage.getProjectID();
			parentPackageID = storage.getParentPackageID();
			Packages packages = new Packages(packageID, projectID, parentPackageID, engineerUsername, description, startDate, endDate, ssBudget, dsBudget, jsBudget,
					p1Budget, p2Budget, p3Budget, p4Budget, p5Budget, p6Budget, true);
			packagesManager.persist(packages);
			displayPackageList(); /* update the package list with the new entry */
			statusReportManager.persistDefault(packages); /* create default status report sheet */
			
			// if a new package has been create from a child, the child now is the parent, and no longer a leaf			
			Packages parent = packagesManager.findParent(parentPackageID, projectID);			
			if (parent != null) 
			{
				packagesManager.removeFromLeaves(parent);
			}
			
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Success";
		}
		
		else
		{
			FacesContext.getCurrentInstance().addMessage("createPackageForm:packageID", 
			           new FacesMessage(null, "Selected Package ID is already in use."));
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "Failure";
		}
	}
	

	/* Navigation to child package search */
	public String searchChildPackage()
	{
		return "Search";
	}
	
	
	/* navigation rules when you select the Back button; reloads the package table properly */
	public String back()
	{
		if(!storage.getParentPackageID().equals("-1"))
		{
			allowChildPackageCreation(storage.getParentPackageID());
		}
		
		else 
		{
			createChildren = true;
		}
		
		
		displayPackageList(); /* update the project list with the new entry */
		return "Back";
	}
	
	
	/* decides what info to display if you go select the back button */
	public String backDecision()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		if(!storage.getParentPackageID().equals("-1")) /* if the currently viewed package is a child of another package */
		{
			storage.setParentPackageID(packagesManager.findPackageID(storage.getParentPackageID(), storage.getProjectID()).getParentPackageID()); /* helps to select parent package to view  */

			if(!storage.getParentPackageID().equals("-1"))
			{
				allowChildPackageCreation(storage.getParentPackageID());
			}
			
			else 
			{
				createChildren = true;
			}
			
			displayPackageList(); 
			
			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "PackageManagement";
		}
		
		else /* if the currently viewed package is the top level before the project level */
		{
			project.displayProjectList();

			if(!conversation.isTransient())
			{
				conversation.end();
			}
			
			return "ProjectManagement";
		}
	}
	

	/* updates the view to show "create child package" options based on the new value */
	public String backUpdateChildCreatePermissions()
	{
		allowChildPackageCreation(packageID);
		displayPackageList();
		return "Back";
	}
	
	/* navigation rules when you select the Back button and need to end a conversation; reloads the package table properly */
	public String backErase()
	{
		displayPackageList(); /* update the project list with the new entry */
		
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Back";
	}
	
	
	/* hides the "Create Sub Package" column if the to-be parent package has been charged to (aka if its status is open/closed/completed) */
	public void allowChildPackageCreation(String packageIDInput) 
	{
		Packages currentPackage = packagesManager.findPackageID(packageIDInput, storage.getProjectID());
		
		if(currentPackage.getStatus().equals("Not Started"))
		{
			createChildren = true;
		}
		
		else
		{
			createChildren = false;
		}
	}
	

	/* updates a package's options (ex. package status) */
	public String saveOptionChanges()
	{
		Packages newData = packagesManager.findPackageID(packageID, storage.getProjectID());
		newData.setStatus(status);
		packagesManager.merge(newData);
		findAllChildPackages(newData); /* updates all child packages status to be the same */
		allowChildPackageCreation(packageID); /* saves option to see the create child packages option */
		
		return "Success";
	}
	
	
	/* finds all child packages of a package and calls other methods to do various things to them */
	public void findAllChildPackages(Packages selectedPackage)
	{
		ArrayList<Packages> childPackageMasterList = packagesManager.getChildPackages(selectedPackage.getPackageID(), storage.getProjectID()); /* grab all direct children of inputed package */
		boolean stopFlag = false;
		int i = 0;
		
		while(stopFlag == false)
		{	
			stopFlag = true;
			/* i will be the value that it last was from adding things to the list, this way anything previously checked will no longer be readded to the list (which caused infinite iteration issues) */
			for(;i < childPackageMasterList.size(); i++) 
			{
				ArrayList<Packages> childPackages = packagesManager.getChildPackages(childPackageMasterList.get(i).getPackageID(), storage.getProjectID());
				if(!childPackages.isEmpty()) /* if there are no children */
				{
					for(int j = 0; j < childPackages.size(); j++) /* all newly found children to master child package list */
					{
						childPackageMasterList.add(childPackages.get(j));
					}
					
					stopFlag = false;
				}
			}
		}
		
		/* ensure that we only propogate status changes if they are for closed or complete (to prevent interesting issues) */
		if(selectedPackage.getStatus().equals("Completed") || selectedPackage.getStatus().equals("Closed")) 
		{
			changePackagesStatus(childPackageMasterList, selectedPackage.getStatus()); /* change all found child packages to have the status as the parent */
		}
	}
	
	
	public void changePackagesStatus(ArrayList<Packages> childPackageMasterList, String status) /* change the status of all packages in an arraylist */
	{
		for(int i = 0; i < childPackageMasterList.size(); i++)
		{
			childPackageMasterList.get(i).setStatus(status);
			packagesManager.merge(childPackageMasterList.get(i));
		}
	}
	
	
	/* load selected child package list and all it's sub child package lists */
	public void loadChildPackageList(Packages selectedPackage)
	{
		selectedPackage.setChildPackageList(packagesManager.getChildPackages(selectedPackage.getPackageID(), storage.getProjectID()));
			
		if(selectedPackage.getChildPackageList().isEmpty()) // if this package has no children
		{
			return;
		}
			
		else
		{
			for(int i = 0; i < selectedPackage.getChildPackageList().size(); i++)
			{
				 loadChildPackageList(selectedPackage.getChildPackageList().get(i));
			}
		}
		
		return;
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
	public String getPackageID() 
	{
		return packageID;
	}
	public void setPackageID(String packageID) 
	{
		this.packageID = packageID;
	}
	public ArrayList<Packages> getChildPackageList() 
	{
		return childPackageList;
	}
	public void setChildPackageList(ArrayList<Packages> childPackageList)
	{
		this.childPackageList = childPackageList;
	}
	public ArrayList<Employee> getEmployeeList() 
	{
		return employeeList;
	}
	public void setEmployeeList(ArrayList<Employee> employeeList) 
	{
		this.employeeList = employeeList;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
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
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}
	public String getProjectID() 
	{
		return projectID;
	}
	public void setProjectID(String projectID)
	{
		this.projectID = projectID;
	}
	public PackagesManager getPackageManager() 
	{
		return packagesManager;
	}

	public void setPackageManager(PackagesManager packageManager) 
	{
		this.packagesManager = packageManager;
	}
	public ArrayList<Packages> getPackageList() 
	{
		return packageList;
	}
	public void setPackageList(ArrayList<Packages> packageList) 
	{
		this.packageList = packageList;
	}


	public String getParentPackageID()
	{
		return parentPackageID;
	}


	public void setParentPackageID(String parentPackageID) 
	{
		this.parentPackageID = parentPackageID;
	}

	public StorageBean getStorage() 
	{
		return storage;
	}

	public void setStorage(StorageBean storage) 
	{
		this.storage = storage;
	}

	public ProjectsBean getProject()
	{
		return project;
	}

	public void setProject(ProjectsBean project) 
	{
		this.project = project;
	}

	public double getBudget() 
	{
		return budget;
	}

	public void setBudget(double budget) 
	{
		this.budget = budget;
	}

	public ArrayList<StatusReportRow> getStatusReportRowList() 
	{
		return statusReportRowList;
	}

	public void setStatusReportRowList(ArrayList<StatusReportRow> statusReportRowList)
	{
		this.statusReportRowList = statusReportRowList;
	}

	public double getSsEstimate() 
	{
		return ssEstimate;
	}

	public void setSsEstimate(double ssEstimate) 
	{
		this.ssEstimate = ssEstimate;
	}

	public double setDsBudget() 
	{
		return dsEstimate;
	}

	public void setDsEstimate(double dsEstimate)
	{
		this.dsEstimate = dsEstimate;
	}

	public double getJsEstimate() 
	{
		return jsEstimate;
	}

	public void setJsEstimate(double jsEstimate) 
	{
		this.jsEstimate = jsEstimate;
	}

	public double getP1Estimate()
	{
		return p1Estimate;
	}

	public void setP1Estimate(double p1Estimate) 
	{
		this.p1Estimate = p1Estimate;
	}

	public double getP2Estimate() 
	{
		return p2Estimate;
	}

	public void setP2Estimate(double p2Estimate)
	{
		this.p2Estimate = p2Estimate;
	}

	public double getP3Estimate() 
	{
		return p3Estimate;
	}

	public void setP3Estimate(double p3Estimate) 
	{
		this.p3Estimate = p3Estimate;
	}

	public double getP4Estimate() 
	{
		return p4Estimate;
	}

	public void setP4Estimate(double p4Estimate)
	{
		this.p4Estimate = p4Estimate;
	}

	public double getP5Estimate() 
	{
		return p5Estimate;
	}

	public void setP5Estimate(double p5Estimate) 
	{
		this.p5Estimate = p5Estimate;
	}

	public double getP6Estimate() 
	{
		return p6Estimate;
	}

	public void setP6Estimate(double p6Estimate) 
	{
		this.p6Estimate = p6Estimate;
	}

	public String getComments() 
	{
		return comments;
	}

	public void setComments(String comments) 
	{
		this.comments = comments;
	}


	public double getSsBudget() 
	{
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


	public double getDsEstimate() 
	{
		return dsEstimate;
	}


	public String getEngineerUsername()
	{
		return engineerUsername;
	}


	public void setEngineerUsername(String engineerUsername)
	{
		this.engineerUsername = engineerUsername;
	}


	public boolean isCreateChildren() 
	{
		return createChildren;
	}


	public void setCreateChildren(boolean createChildren)
	{
		this.createChildren = createChildren;
	}
	
	
	
}
