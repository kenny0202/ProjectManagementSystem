/** 
 * @author Eric Lyons-Davis
 * */
package database;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.users.credentials.Credentials;

@Stateless
/* Class contains all actions to manipulate data in the Credentials Table */
public class CredentialsManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName="COMP4911_Project") EntityManager em;

	/* find a valid Credential for login validation */
	public Credentials searchForCredentials(String userName, String password) 
	{
		try
		{
			TypedQuery<Credentials> query = em.createQuery("select c from Credentials c where c.userName = :userName"
					+ " and c.password = :password", Credentials.class); 
	    	query.setParameter("userName", userName);
	    	query.setParameter("password", password);
			
	    	Credentials credentials = query.getSingleResult();
	    	return credentials;
		}
		
		catch(NoResultException e)
        {
        	return null;
        }
	}
	
	/* find employee based on userName */
	public Credentials findUserName(String userName) 
	{
        try
        {
	        TypedQuery<Credentials> query = em.createQuery("select c from Credentials c where c.userName = :userName", Credentials.class); 
	    	query.setParameter("userName", userName);
	    	
	    	Credentials credentials = query.getSingleResult();
	    	return credentials;
        }
        
        catch(NoResultException e)
        {
        	return null;
        }
        
        catch(Exception e)
        {
        	return null;
        }
    }
	

	/* persist a Credential */
	public void persist(Credentials credentials)
	{
		em.persist(credentials);
	}

	
	/* delete a Credential */
	public void remove(Credentials credentials) 
	{
		credentials = searchForCredentials(credentials.getUserName(), credentials.getPassword());
		em.remove(credentials);
	}
	
	public void merge(Credentials credentials) {
		
		em.merge(credentials); 
	}
	
	/* update a Credential */
	public void update(Credentials credentials, Credentials newData)
	{
		credentials = searchForCredentials(credentials.getUserName(), credentials.getPassword());
		credentials.setUserName(newData.getUserName());
		credentials.setPassword(newData.getPassword());
		em.merge(credentials);
	}
}
