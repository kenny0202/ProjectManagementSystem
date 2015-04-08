package model.timesheets;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: TimesheetDataDetails.
 * Represents one row in a timesheet in the "timesheetDetails" table.
 *
 * @author Olga
 *
 */
@Entity
@Table(name="timesheetRow") // the table that this entity represents
public class TimesheetRowData implements Serializable {

	/**
	 * The timesheet row id. Is the primary key and is auto generated
	 * and unique for each timesheet row in the table
	 */	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	/**
	 * The time sheet that this row is connected to. There are
	 * many rows that pertain to one timesheet. 
	 */
	@ManyToOne(targetEntity=TimesheetData.class)
	private TimesheetData timesheet;

	/**
	 * The project id. 
	 */
	@Column
	private String projectID;
	
	/**
	 * The work package id. 
	 */
	@Column
	private String workPackage;
	
	/**
	 * Hours worked for 1st day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */
	@Column(precision=3,scale=1)
	private BigDecimal sat;

	/**
	 * Hours worked for 2nd day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)  
	private BigDecimal sun;
	
	/**
	 * Hours worked for 3rd day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)
	private BigDecimal mon;
	
	/**
	 * Hours worked for 4th day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)
	private BigDecimal tue;

	/**
	 * Hours worked for 5th day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)
	private BigDecimal wed;

	/**
	 * Hours worked for 6th day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)
	private BigDecimal thu;

	/**
	 * Hours worked for 7th day of the week. 
	 * Precision when entered can only be "xx.x". 
	 */	
	@Column(precision=3,scale=1)
	private BigDecimal fri;

	/**
	 * Any notes needed to be recorded for a certain day. 
	 */
	@Column
	private String notes;
	
	@Column
	private String labourGradeID; 

	private static final long serialVersionUID = 1L;
	
	public TimesheetRowData() {
		super();
	}   

	/**
	 * Helper constructor to create an entity from a dto and attach it to
	 * a timesheet
	 * @param row the data in this time sheet row
	 * @param timesheet the timesheet this row belongs to
	 */
	public TimesheetRowData(TimesheetRow row, TimesheetData timesheet, String labourGrade) {
		super();
		this.timesheet = timesheet;
		projectID = row.getProjectID();
		workPackage = row.getWorkPackage();
		notes = row.getNotes();
		sat = row.getHour(0);
		sun = row.getHour(1);
		mon = row.getHour(2);
		tue = row.getHour(3);
		wed = row.getHour(4);
		thu = row.getHour(5);
		fri = row.getHour(6);
		labourGradeID = labourGrade; 
	}  

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(String workPackage) {
		this.workPackage = workPackage;
	}

	public BigDecimal getSat() {
		return sat;
	}

	public void setSat(BigDecimal sat) {
		this.sat = sat;
	}

	public BigDecimal getSun() {
		return sun;
	}

	public void setSun(BigDecimal sun) {
		this.sun = sun;
	}

	public BigDecimal getMon() {
		return mon;
	}

	public void setMon(BigDecimal mon) {
		this.mon = mon;
	}

	public BigDecimal getTue() {
		return tue;
	}

	public void setTue(BigDecimal tue) {
		this.tue = tue;
	}

	public BigDecimal getWed() {
		return wed;
	}

	public void setWed(BigDecimal wed) {
		this.wed = wed;
	}

	public BigDecimal getThu() {
		return thu;
	}

	public void setThu(BigDecimal thu) {
		this.thu = thu;
	}

	public BigDecimal getFri() {
		return fri;
	}

	public void setFri(BigDecimal fri) {
		this.fri = fri;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getLabourGradeID() {
		return labourGradeID;
	}

	public void setLabourGradeID(String labourGradeID) {
		this.labourGradeID = labourGradeID;
	}
	
	/**
	 * Helper method to create a dto object from this entity. 
	 * @return a timesheet row dto. 
	 */
	public TimesheetRow getTimesheetRow() {
		BigDecimal[] hours = {sat, sun, mon, tue, wed, thu, fri};
		TimesheetRow r = new TimesheetRow(projectID, workPackage, hours, notes, labourGradeID);
		return r;
	}
	
}
