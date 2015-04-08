/** 
 * @author Eric Lyons-Davis
 * */
package beans;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/* holds application-wide variables */
@Named("storage")
@ApplicationScoped
public class StorageBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String projectID;
	private String parentPackageID;
	
	/* getters and setters */
	
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
	
	
}
