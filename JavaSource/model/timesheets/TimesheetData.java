package model.timesheets;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

//import predefined.Employee;
import model.users.employees.Employee;


/**
 * Entity implementation class for Entity: TimesheetData.
 * Represents a timesheet in the "timesheets" table.
 *
 * @author Olga
 *
 */
@Entity
@Table(name="timesheet") // the table that this entity represents

/**
 * Queries that will be used often for retrieving data from the table that this entity represents.
 */
@NamedQueries({
//@NamedQuery(name = "Timesheets.findAll", query = "SELECT t FROM TimesheetData t order by t.endWeek"),
@NamedQuery(name = "Timesheets.findByEmployee", query = "SELECT t FROM TimesheetData t where t.employee.employeeID = :employee order by t.endWeek"),

@NamedQuery(name = "Timesheets.findAll", query = "SELECT t FROM TimesheetData t order by t.endWeek")})
public class TimesheetData implements Serializable {
	   
	/**
	 * The timesheet id. Is the primary key and is auto generated
	 * and unique for each employee in the table
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	/**
	 * Association many to one because there are many timesheets
	 * that belong to only one employee. 
	 */
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Employee.class)
	@JoinColumn(name="employee_ID")
	private Employee employee;
	
	/**
	 * Annotation needs to be specified for Date. 
	 */
	@Temporal(TemporalType.DATE)
	private Date endWeek;

	/**
	 * Association with timesheet details. Time sheet has many rows. 
	 * cascade={CascadeType.ALL} allows to remove a timesheet and its rows at once.
	 */
	@OneToMany(targetEntity=TimesheetRowData.class, mappedBy="timesheet",cascade={CascadeType.ALL},orphanRemoval=true,fetch=FetchType.EAGER)
	private List<TimesheetRowData> timesheetRows;

	/**
	 * Overtime hours worked. 
	 */
	@Column(precision=5,scale=1)
	private BigDecimal overtime;

	/**
	 * flextime hours worked. 
	 */
	@Column(precision=5,scale=1)
	private BigDecimal flextime;
	
	@Column(name="needsReviewing")
	private boolean submittedForApproval;
		
	@Column
	private boolean approved; 
		
	@Column
	private boolean denied; 
	
	private static final long serialVersionUID = 1L;
	
	public TimesheetData() {
	}   
	
	/**
	 * Helper constructor to prepare the timesheet entity from a timesheet dto to
	 * be stored in the database. This constructor makes sure also that empty rows
	 * don't get stored.
	 * @param ts the dto to be stored as a record in the database
	 * @param forEmployee the employee the timesheet pertains to
	 */
	public TimesheetData(Timesheet ts, Employee forEmployee, String empLabourGrade) {
		super();
		employee = forEmployee;
		endWeek = ts.getEndWeek();
		overtime = ts.getOvertime();
		flextime = ts.getFlextime();
		approved = ts.getApproved(); 
		denied = ts.getDenied(); 
		submittedForApproval = ts.getSubmittedForApproval(); 
		timesheetRows = new ArrayList<TimesheetRowData>();
	
		for (TimesheetRow row : ts.getDetails()) {
						
			boolean dataExists = (!row.getProjectID().equalsIgnoreCase("Select Project"))
					&& (!row.getWorkPackage().equalsIgnoreCase("Select Package"))
					&& (!row.getWorkPackage().equalsIgnoreCase("None Available"))
					&& (row.getSum().doubleValue() > 0.0); 
			
			if ( dataExists ) {

				// change null days of a row to 0.0 
				for (int i = 0; i < row.getHoursForWeek().length; i++ ) {
					
					if (row.getHoursForWeek()[i] == null)
						row.getHoursForWeek()[i] = new BigDecimal(0.0);					
				}
				
				timesheetRows.add(new TimesheetRowData(row, this, empLabourGrade));
			} 
		}
	}
	
	public Date getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(Date endWeek) {
		this.endWeek = endWeek;
	}

	public BigDecimal getOvertime() {
		return overtime;
	}

	public void setOvertime(BigDecimal overtime) {
		this.overtime = overtime;
	}

	public BigDecimal getFlextime() {
		return flextime;
	}

	public void setFlextime(BigDecimal flextime) {
		this.flextime = flextime;
	}
	
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<TimesheetRowData> getTimesheetRows() {
		return timesheetRows;
	}

	public void setTimesheetRows(List<TimesheetRowData> timesheetRows) {
		this.timesheetRows = timesheetRows;
	}
	
	public void setSubmittedForApproval(boolean submittedForApproval) {
		this.submittedForApproval = submittedForApproval; 
	}

	public boolean getSubmittedForApproval() {
		return submittedForApproval; 
	}
		
	public boolean getDenied() {
		return denied; 
	}
	
	public void getDenied(boolean denied) {
		
		this.denied = denied; 
	}
	
	public boolean getApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * Helper method to turn a dto object from this entity.
	 * Gets a timesheet for this employee 
	 * @return The time sheet dto for this employee.
	 */
	public Timesheet getTimesheet() {
		
	/*	Employee e = null;
		if (this.employee != null) {
			e = this.employee.getEmployee();
		}
		*/
			
		List<TimesheetRow> charges = new ArrayList<TimesheetRow>();
				
		if (this.timesheetRows != null) {
			for (TimesheetRowData detail : timesheetRows) {
				charges.add(detail.getTimesheetRow());
			}
		}

		// no employee right now
		Timesheet t = new Timesheet(employee, this.endWeek, charges);
		t.setFlextime(this.flextime);
		t.setOvertime(this.overtime);
		
		t.setApproved(this.approved);
		t.setSubmittedForApproval(this.submittedForApproval);
		t.setDenied(this.denied);
		
		//t.setEmployee(employee);

		return t;
	}
	
}