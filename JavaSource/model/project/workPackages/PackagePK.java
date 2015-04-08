package model.project.workPackages;

import java.io.Serializable;

/* PK class for Packages*/
public class PackagePK implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private String projectID;
	private String packageID;
	
	/* getters and setters */
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
	
    public int hashCode() {
        return (int) projectID.hashCode();
    }

    public boolean equals(Object obj) {
        /*if (obj == this) return true;
        if (!(obj instanceof PackagePK)) return false;
        if (obj == null) return false;
        PackagePK pk = (PackagePK) obj;
        return pk.packageID.equals(packageID) && pk.projectID.equals(projectID);*/
    	if (obj == this) 
    		return true;
    	if (!(obj instanceof PackagePK)) 
    		return false;

    	PackagePK pk = (PackagePK) obj;
        return pk.packageID.equals(packageID) && pk.projectID.equals(projectID);  	
    }
	
}
