/** 
 * @author Eric Lyons-Davis
 * */

package model.project.projects;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/* represents a project. Will consist of various Report and Work Package 
 * objects as well as Project specific variables */

@Entity
@Table(name="projects")
public class Projects 
{
	@Id
	@Column(name="project_ID")
	private String projectID;
	
	@Column(name="package_ID")
	private String packageID;
	
	@Column(name="project_manager_username")
	private String projectManagerUsername;
	
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name="end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(name="client_name")
	private String clientName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="markup_rate")
	private double markupRate;

	/* default constructor */
	public Projects()
	{
		
	}
	
	/* constructor to create a new project w/ all possible vars initialized */
	public Projects(String inputedProjectID, String inputedprojectManagerUsername, String inputedDescription, String inputedClientName,
			Date inputedStartDate, Date inputedEndDate, double inputedMarkupRate) 
	{
		projectID = inputedProjectID;
		projectManagerUsername = inputedprojectManagerUsername;
		description = inputedDescription;
		clientName = inputedClientName;
		startDate = inputedStartDate;
		endDate = inputedEndDate;
		markupRate = inputedMarkupRate;
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
	
	public String getProjectID() 
	{
		return projectID;
	}
	
	public void setProjectID(String projectID) 
	{
		this.projectID = projectID;
	}

	public String getPackageID()
	{
		return packageID;
	}

	public void setPackageID(String packageID) 
	{
		this.packageID = packageID;
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
