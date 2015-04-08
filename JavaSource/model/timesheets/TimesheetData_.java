package model.timesheets;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.users.employees.Employee;

@Generated(value="Dali", date="2015-04-06T22:08:39.553-0700")
@StaticMetamodel(TimesheetData.class)
public class TimesheetData_ {
	public static volatile SingularAttribute<TimesheetData, Integer> id;
	public static volatile SingularAttribute<TimesheetData, Employee> employee;
	public static volatile SingularAttribute<TimesheetData, Date> endWeek;
	public static volatile ListAttribute<TimesheetData, TimesheetRowData> timesheetRows;
	public static volatile SingularAttribute<TimesheetData, BigDecimal> overtime;
	public static volatile SingularAttribute<TimesheetData, BigDecimal> flextime;
	public static volatile SingularAttribute<TimesheetData, Boolean> submittedForApproval;
	public static volatile SingularAttribute<TimesheetData, Boolean> approved;
	public static volatile SingularAttribute<TimesheetData, Boolean> denied;
}
