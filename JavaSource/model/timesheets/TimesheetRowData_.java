package model.timesheets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-06T22:08:39.608-0700")
@StaticMetamodel(TimesheetRowData.class)
public class TimesheetRowData_ {
	public static volatile SingularAttribute<TimesheetRowData, Integer> id;
	public static volatile SingularAttribute<TimesheetRowData, TimesheetData> timesheet;
	public static volatile SingularAttribute<TimesheetRowData, String> projectID;
	public static volatile SingularAttribute<TimesheetRowData, String> workPackage;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> sat;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> sun;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> mon;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> tue;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> wed;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> thu;
	public static volatile SingularAttribute<TimesheetRowData, BigDecimal> fri;
	public static volatile SingularAttribute<TimesheetRowData, String> notes;
	public static volatile SingularAttribute<TimesheetRowData, String> labourGradeID;
}
