/** 
 * @author Eric Lyons-Davis
 * */

package model.users.credentials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* represents a user's credentials; kept in a separate class for security purposes */

@Entity
@Table(name="credentials")
public class Credentials 
{
	@Id
	@Column(name="user_name", unique=true)
	private String userName; 

	@Column(name="password")
	private String password;
	 
	/* constructor that takes in a UserName and a password */
	public Credentials(String inputUserName, String inputPassword)
	{
		userName = inputUserName;
		password = inputPassword;
	}
	
	/* default constructor */
	public Credentials()
	{
		
	}
	
	 
	 /* getters and setters */
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public String getUserName() 
	{
		return userName;
	}

	public void setUserName(String userID) 
	{
		this.userName = userID;
	}	
}
