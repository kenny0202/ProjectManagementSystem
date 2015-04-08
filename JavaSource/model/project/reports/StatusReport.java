/** 
 * @author Eric Lyons-Davis
 * */

package model.project.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "status_report")
public class StatusReport 
{	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="project_ID")
	private String projectID;
	
	@Column(name="package_ID")
	private String packageID;
	
	@Column(name="ss_Estimate")
	private double ssEstimate;
	
	@Column(name="ds_Estimate")
	private double dsEstimate;
	
	@Column(name="js_Estimate")
	private double jsEstimate;
	
	@Column(name="p1_Estimate")
	private double p1Estimate;
	
	@Column(name="p2_Estimate")
	private double p2Estimate;
	
	@Column(name="p3_Estimate")
	private double p3Estimate;
	
	@Column(name="p4_Estimate")
	private double p4Estimate;
	
	@Column(name="p5_Estimate")
	private double p5Estimate;
	
	@Column(name="p6_Estimate")
	private double p6Estimate;
	
	@Column(name="comments")
	private String comments;
	
	
	/* default constructor */
	public StatusReport()
	{
		
		
	}
	
	
	/* constructor w/ all available parameters set */
	public StatusReport(String projectIDInput, String packageIDInput, double ssEstimateInput, double dsEstimateInput, double jsEstimateInput,
			 double p1EstimateInput, double p2EstimateInput, double p3EstimateInput, double p4EstimateInput, double p5EstimateInput,  double p6EstimateInput, 
			 String commentsInput)
	{
		projectID = projectIDInput;
		packageID = packageIDInput;
		ssEstimate = ssEstimateInput;
		dsEstimate = dsEstimateInput;
		dsEstimate = jsEstimateInput;
		p1Estimate = p1EstimateInput;
		p2Estimate = p2EstimateInput;
		p3Estimate = p3EstimateInput;
		p4Estimate = p4EstimateInput;
		p5Estimate = p5EstimateInput;
		p6Estimate = p6EstimateInput;
		comments = commentsInput;
	}

	
	/* lets you use a specific getter based on an index value */
	public double getStatusReportRowLabourGradeByIndex(int i)
	{
		switch(i)
		{
			case 0: 
				return this.getSsEstimate();
			case 1:
				return this.getDsEstimate();
			case 2:
				return this.getJsEstimate();
			case 3:
				return this.getP1Estimate();
			case 4:
				return this.getP2Estimate();
			case 5:
				return this.getP3Estimate();
			case 6:
				return this.getP4Estimate();
			case 7:
				return this.getP5Estimate();
			case 8:
				return this.getP6Estimate();
			default:
				return this.getDsEstimate();
		}
	}


	
	
	
	/* getters and setters */
	
	public int getId()
	{
		return id;
	}


	public void setId(int id) 
	{
		this.id = id;
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


	public double getSsEstimate()
	{
		return ssEstimate;
	}


	public void setSsEstimate(double ssEstimate)
	{
		this.ssEstimate = ssEstimate;
	}


	public double getDsEstimate() 
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
	
}
