/** @author Eric Lyons-Davis
 */

package model.timesheets;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name="Timesheet_LG_View")
@IdClass(TimesheetLGViewPK.class)
public class TimesheetLGView implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="work_Package", insertable = false, updatable = false)
	private String workPackage;
	
	@Id
	@Column(name="project_ID", insertable = false, updatable = false)
	private String projectID;
	
	@Id
	@Column(name="labour_Grade_ID", insertable = false, updatable = false)
	private String labourGradeID;
	
	@Column(name="mon", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double mon;
	
	@Column(name="tue", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double tue;
	
	@Column(name="wed", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double wed;
	
	@Column(name="thu", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double thu;
	
	@Column(name="fri", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double fri;
	
	@Column(name="sat", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double sat;
	
	@Column(name="sun", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double sun;
	
	@Column(name="total_sum", insertable = false, updatable = false, columnDefinition="decimal", precision=1, scale=25)
	private double totalSum;
	
	
	/* default constructor */
	public TimesheetLGView()
	{
		
	}

	
	/* getters and setters */
	
	public String getWorkPackage() 
	{
		return workPackage;
	}


	public void setWorkPackage(String workPackage) 
	{
		this.workPackage = workPackage;
	}


	public String getProjectID()
	{
		return projectID;
	}


	public void setProjectID(String projectID)
	{
		this.projectID = projectID;
	}


	public String getLabourGradeID() 
	{
		return labourGradeID;
	}


	public void setLabourGradeID(String labourGradeID) 
	{
		this.labourGradeID = labourGradeID;
	}


	public double getMon() 
	{
		return mon;
	}


	public void setMon(double mon)
	{
		this.mon = mon;
	}


	public double getTue()
	{
		return tue;
	}


	public void setTue(double tue)
	{
		this.tue = tue;
	}


	public double getWed()
	{
		return wed;
	}


	public void setWed(double wed)
	{
		this.wed = wed;
	}


	public double getThu() 
	{
		return thu;
	}


	public void setThu(double thu)
	{
		this.thu = thu;
	}


	public double getFri()
	{
		return fri;
	}


	public void setFri(double fri) 
	{
		this.fri = fri;
	}


	public double getSat() 
	{
		return sat;
	}


	public void setSat(double sat) 
	{
		this.sat = sat;
	}


	public double getSun()
	{
		return sun;
	}


	public void setSun(double sun) 
	{
		this.sun = sun;
	}


	public double getTotalSum() 
	{
		return totalSum;
	}


	public void setTotalSum(double totalSum) 
	{
		this.totalSum = totalSum;
	}
	
	
}
