/** 
 * @author Eric Lyons-Davis
 * */

package model.timesheets.statusCodes;

/*
 * This class will *possibly* represent status codes for timesheets
 * (for example, REG, SICK, VACATION, OVERTIME, etc). Unsure if required yet.
 */

public class StatusCode 
{
	private int statusCodeID;
	private String statusCodeLongName; /* the full name of the status code */
	private String statusCodeShortName; /* the abbreviation for the status code name */
	
	/* getters and setters */
	public int getStatusCodeID() 
	{
		return statusCodeID;
	}
	public void setStatusCodeID(int statusCodeID) 
	{
		this.statusCodeID = statusCodeID;
	}
	public String getStatusCodeLongName() 
	{
		return statusCodeLongName;
	}
	public void setStatusCodeLongName(String statusCodeLongName) 
	{
		this.statusCodeLongName = statusCodeLongName;
	}
	public String getStatusCodeShortName() 
	{
		return statusCodeShortName;
	}
	public void setStatusCodeShortName(String statusCodeShortName) 
	{
		this.statusCodeShortName = statusCodeShortName;
	}

}
