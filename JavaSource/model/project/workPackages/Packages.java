/** 
 * @author Eric Lyons-Davis
 * */

package model.project.workPackages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/* represent work packages */

@Entity
@IdClass(PackagePK.class)
@Table(name="Packages")
public class Packages implements Serializable
{
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name="package_ID")
	private String packageID;
	
	@Id
	@Column(name="project_ID")
	private String projectID; /* the project id of the project that the work package belongs to */
	
	@Column(name="engineer_username")
	private String engineerUsername;
	
	@Column(name="status")
	private String status;
	
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name="end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(name="estimated_cost")
	private double estimatedCost;
	
	@Column(name="current_cost")
	private double currentCost;
	
	@Column(name="ss_Budget")
	private double ssBudget;
	
	@Column(name="ds_Budget")
	private double dsBudget;
	
	@Column(name="js_Budget")
	private double jsBudget;
	
	@Column(name="p1_Budget")
	private double p1Budget;
	
	@Column(name="p2_Budget")
	private double p2Budget;
	
	@Column(name="p3_Budget")
	private double p3Budget;
	
	@Column(name="p4_Budget")
	private double p4Budget;
	
	@Column(name="p5_Budget")
	private double p5Budget;
	
	@Column(name="p6_Budget")
	private double p6Budget;
	
	@Column(name="description")
	private String description;
	
	@Column(name="parent_package_ID")
	private String parentPackageID; /* the ID of the parent package */
	
	// whether or not a package is chargeable
	@Column
	private boolean leaf; 
	

	@Transient
	@Column(name="child_package_IDs")
	private ArrayList<Packages> childPackageList = new ArrayList<Packages>(); /* the ID's of the child packages */

	/* default constructor */
	public Packages()
	{
		
	}
	
	/* constructor to create a new package w/ all possible vars initialized besides currentCost */
	public Packages(String inputedPackageID, String inputedProjectID, String inputedParentPackageID, String inputedengineerUsername, String inputedDescription,
			Date inputedStartDate, Date inputedEndDate, String inputedStatus, double ssBudgetInput, double dsBudgetInput, double jsBudgetInput,
			double p1BudgetInput, double p2BudgetInput, double p3BudgetInput, double p4BudgetInput, double p5BudgetInput, double p6BudgetInput, boolean isLeaf) 
	{
		packageID = inputedPackageID;
		projectID = inputedProjectID;
		parentPackageID = inputedParentPackageID;
		engineerUsername = inputedengineerUsername;
		description = inputedDescription;
		startDate = inputedStartDate;
		endDate = inputedEndDate;
		ssBudget = ssBudgetInput;
		dsBudget = dsBudgetInput;
		jsBudget = jsBudgetInput;
		p1Budget = p1BudgetInput;
		p2Budget = p2BudgetInput;
		p3Budget = p3BudgetInput;
		p4Budget = p4BudgetInput;
		p5Budget = p5BudgetInput;
		p6Budget = p6BudgetInput;
		status = inputedStatus;
		leaf = isLeaf; 
		currentCost = 0;
		estimatedCost = 0;
	}
	
	/* constructor to create a new package w/ all possible vars initialized besides currentCost */
	public Packages(String inputedPackageID, String inputedProjectID, String inputedParentPackageID, String inputedengineerUsername, String inputedDescription,
			Date inputedStartDate, Date inputedEndDate,double ssBudgetInput, double dsBudgetInput, double jsBudgetInput,
			double p1BudgetInput, double p2BudgetInput, double p3BudgetInput, double p4BudgetInput, double p5BudgetInput, double p6BudgetInput, boolean isLeaf) 
	{
		packageID = inputedPackageID;
		projectID = inputedProjectID;
		parentPackageID = inputedParentPackageID;
		engineerUsername = inputedengineerUsername;
		description = inputedDescription;
		startDate = inputedStartDate;
		endDate = inputedEndDate;
		ssBudget = ssBudgetInput;
		dsBudget = dsBudgetInput;
		jsBudget = jsBudgetInput;
		p1Budget = p1BudgetInput;
		p2Budget = p2BudgetInput;
		p3Budget = p3BudgetInput;
		p4Budget = p4BudgetInput;
		p5Budget = p5BudgetInput;
		p6Budget = p6BudgetInput;
		status = "Not Started";
		leaf = isLeaf; 
		currentCost = 0;
		estimatedCost = 0;
	}
	

	/* obtains the current cost by searching for all of the sub work-package's current costs */ 
	public double obtainCurrentCost()
	{
		double currentCostTotal = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			currentCostTotal += currentCost;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				currentCostTotal += childPackageList.get(i).obtainCurrentCost();
			}
		}
		
		return currentCostTotal;
	}
	
	
	/* obtains the estimated cost by searching for all of the sub work-package's estimated costs */ 
	public double obtainEstimatedCost()
	{
		double estimatedCostTotal = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			estimatedCostTotal += estimatedCost;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				estimatedCostTotal += childPackageList.get(i).obtainEstimatedCost();
			}
		}
		
		return estimatedCostTotal;
	}
	
	
	/* obtains the ss budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainSSBudgetCost()
	{
		double ssBudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			ssBudgetCost += ssBudget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				ssBudgetCost += childPackageList.get(i).obtainSSBudgetCost();
			}
		}
		
		return ssBudgetCost;
	}
	
	/* obtains the ds budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainDSBudgetCost()
	{
		double dsBudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			dsBudgetCost += dsBudget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				dsBudgetCost += childPackageList.get(i).obtainDSBudgetCost();
			}
		}
		
		return dsBudgetCost;
	}
	
	/* obtains the js budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainJSBudgetCost()
	{
		double jsBudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			jsBudgetCost += jsBudget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				jsBudgetCost += childPackageList.get(i).obtainJSBudgetCost();
			}
		}
		
		return jsBudgetCost;
	}
	
	/* obtains the p1 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP1BudgetCost()
	{
		double p1BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p1BudgetCost += p1Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p1BudgetCost += childPackageList.get(i).obtainP1BudgetCost();
			}
		}
		
		return p1BudgetCost;
	}
	
	/* obtains the p2 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP2BudgetCost()
	{
		double p2BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p2BudgetCost += p2Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p2BudgetCost += childPackageList.get(i).obtainP2BudgetCost();
			}
		}
		
		return p2BudgetCost;
	}
	
	/* obtains the p3 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP3BudgetCost()
	{
		double p3BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p3BudgetCost += p3Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p3BudgetCost += childPackageList.get(i).obtainP3BudgetCost();
			}
		}
		
		return p3BudgetCost;
	}
	
	/* obtains the p4 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP4BudgetCost()
	{
		double p4BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p4BudgetCost += p4Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p4BudgetCost += childPackageList.get(i).obtainP4BudgetCost();
			}
		}
		
		return p4BudgetCost;
	}
	
	/* obtains the p5 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP5BudgetCost()
	{
		double p5BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p5BudgetCost += p5Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p5BudgetCost += childPackageList.get(i).obtainP5BudgetCost();
			}
		}
		
		return p5BudgetCost;
	}
	
	/* obtains the p6 budget cost by searching for all of the sub work-package's budget costs */ 
	public double obtainP6BudgetCost()
	{
		double p6BudgetCost = 0;
		if(childPackageList.isEmpty()) // if this package has no children
		{
			p6BudgetCost += p6Budget;
		}
		
		else
		{
			for(int i = 0; i < childPackageList.size(); i++)
			{
				p6BudgetCost += childPackageList.get(i).obtainP6BudgetCost();
			}
		}
		
		return p6BudgetCost;
	}
	

	public ArrayList<Packages> getChildPackageID()
	{
		return childPackageList;
	}


	public void setChildPackageID(ArrayList<Packages> childPackageList) 
	{
		this.childPackageList = childPackageList;
	}
	

	public ArrayList<Packages> getChildPackageList()
	{
		return childPackageList;
	}


	public void setChildPackageList(ArrayList<Packages> childPackageList) 
	{
		this.childPackageList = childPackageList;
	}

	
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
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
	
	public String getStatus() 
	{
		return status;
	}
	
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	public String getPackageID() 
	{
		return packageID;
	}
	
	public void setPackageID(String packageID) 
	{
		this.packageID = packageID;
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


	public String getProjectID()
	{
		return projectID;
	}


	public void setProjectID(String projectID)
	{
		this.projectID = projectID;
	}


	public String getParentPackageID() 
	{
		return parentPackageID;
	}


	public void setParentPackageID(String parentPackageID)
	{
		this.parentPackageID = parentPackageID;
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
	
	public boolean getLeaf() 
	{
		return leaf;
	}

	public void setLeaf(boolean leaf) 
	{
		this.leaf = leaf;
	}

	public String getEngineerUsername() 
	{
		return engineerUsername;
	}

	public void setEngineerUsername(String engineerUsername) 
	{
		this.engineerUsername = engineerUsername;
	}

}
