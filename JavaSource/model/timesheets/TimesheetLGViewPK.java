/** 
 * @author Eric Lyons-Davis
 * */
package model.timesheets;

import java.io.Serializable;

/* PK class for Packages*/
public class TimesheetLGViewPK implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String workPackage;
	private String projectID;
	private String labourGradeID;
	
	 public int hashCode() 
    {
        return (int) projectID.hashCode();
    }
    
	public boolean equals(Object obj) 
	{
        if (obj == this) 
    		return true;
    	if (!(obj instanceof TimesheetLGViewPK)) 
    		return false;

    	TimesheetLGViewPK pk = (TimesheetLGViewPK) obj;
        return pk.workPackage.equals(workPackage) && pk.projectID.equals(projectID) && pk.labourGradeID.equals(labourGradeID);  	
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

}
