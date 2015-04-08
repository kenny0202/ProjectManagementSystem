/** 
 * @author Eric Lyons-Davis
 * */

package model.project.reports;

/* represents a row in the monthly report */

public class MonthlyReportRow 
{
	private String packageID; /* work package ID */
	private double budget; /* planned amount of person days for WP */
	private double ACWP; /* Actual to Date */
	private double EAC; /* Estimate At Completion */
	private double variance; /* in percent */
	private String comments; /* comments taken from last weekly status report */
	
	/* default constructor */
	public MonthlyReportRow()
	{
	
	}
	
	/* parameter constructor */
	public MonthlyReportRow(String packageIDInput, double budgetInput,double AWCPInput, 
			double EACInput, double varianceInput, String commentsInput)
	{
		packageID = packageIDInput;
		budget = budgetInput;
		ACWP = AWCPInput;
		EAC = EACInput;
		variance = varianceInput;
		comments = commentsInput;
	}

	
	/* getters and setters */
	public String getPackageID() 
	{
		return packageID;
	}

	public void setPackageID(String packageID)
	{
		this.packageID = packageID;
	}

	public double getBudget() 
	{
		return budget;
	}

	public void setBudget(double budget)
	{
		this.budget = budget;
	}

	public double getACWP()
	{
		return ACWP;
	}

	public void setACWP(double aCWP)
	{
		ACWP = aCWP;
	}

	public double getEAC()
	{
		return EAC;
	}

	public void setEAC(double eAC)
	{
		EAC = eAC;
	}

	public double getVariance()
	{
		return variance;
	}

	public void setVariance(double variance) 
	{
		this.variance = variance;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments) 
	{
		this.comments = comments;
	}
		
}
