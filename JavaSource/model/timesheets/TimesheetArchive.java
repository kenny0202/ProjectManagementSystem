package model.timesheets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.users.employees.Employee;



/**
 * A class for accessing all existing Timesheets from the database.
 *
 *@author Eric Lyons-Davis, Roger Harvey
 *
 */
public class TimesheetArchive implements TimesheetCollection{
    

	private static final long serialVersionUID = 1L;
	
	/** The timesheet list. */
	private ArrayList<Timesheet> timesheetList = new ArrayList<Timesheet>();
    
    /** The current employee. */
    private Employee currentEmployee;
    
    /** The current timesheet. */
    private Timesheet currentTimesheet;
    
    /** The current timesheet index. */
    private int currentTimesheetIndex;
    
    /**
     * Constructor that generates a bunch of fake data for the timesheetList.
     */
    public TimesheetArchive() {
        
        
    }
    
    
    
    /**
     * Gets the timesheets.
     *
     * @return all of the timesheets.
     */
    @Override
    public List<Timesheet> getTimesheets() {
            
        return timesheetList;
    }
    
    /**
     * Gets the timesheets for a specific Employee.
     *
     * @param e the employee whose timesheets are returned
     * @return all of the timesheets for an employee.
     */
    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        
        return timesheetList;
    }
    
    /**
     *  Get the current timesheet being worked on
     * 
     * @return currentTimesheet being worked on
     */
    public Timesheet getCurrentTimesheet() {
        return currentTimesheet;
    }

    /**
     * Gets the current timesheet.
     *
     * @param e the employee whose current timesheet is returned
     * @return the current timesheet for an employee.
     */
    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        
    	Iterator<Timesheet> iterator = timesheetList.iterator();
		while(iterator.hasNext())
		{
			Timesheet t = iterator.next();
			if(t.getEmployee().getUserName().equals(e.getUserName()))
			{
				return t;
			}
		}
    	return null;
     }
    

    /**
     * Creates a Timesheet object and adds it to the collection.
     *
     * @return a String representing navigation to the newTimesheet page.
     */
    @Override
    public String addTimesheet() {
               
        Timesheet t = new Timesheet();             
        t.setEmployee( currentEmployee );         
        currentTimesheet = t;               
        timesheetList.add(t);
        currentTimesheetIndex = ( timesheetList.size() - 1 );
        
        
        
        return "timesheetCreated";
    }
    
    /**
     * Adds the current timesheet to archive.
     *
     * @return the string
     */
    public String addCurrentTimesheetToArchive() {
        
        timesheetList.add( currentTimesheet );
        currentTimesheetIndex = ( timesheetList.size() - 1 );
        
        return "timesheetAdded";
    }
    
    /**
     * Sets the current employee.
     *
     * @param e current Employee
     */
    public void setCurrentEmployee( Employee e ) {
        currentEmployee = e;
    }
    
    
    /**
     * Gets all current user timesheets.
     *
     * @param emp the Employee object to check with
     * @return all current user timesheets in an ArrayList
     */
    public ArrayList<Timesheet> getAllCurrentUserTimesheets(Employee emp) {
        
    	ArrayList<Timesheet> employeetimesheetList = new ArrayList<Timesheet>();
    	
    	Iterator<Timesheet> iterator = timesheetList.iterator();
		while(iterator.hasNext())
		{
			Timesheet t = iterator.next();
			if(t.getEmployee().getUserName().equals(emp.getUserName()))
			{
				employeetimesheetList.add(t);
			}
		}
    	
    	return employeetimesheetList;
    }
    
    /**
     * Edits the current timesheet in archive.
     *
     * @return a string to be used for navigation
     */
    public String editCurrentTimesheetinArchive() {
        timesheetList.remove( currentTimesheetIndex );
        timesheetList.add( currentTimesheet );
        
        return "timesheetEdited";
    }

}
