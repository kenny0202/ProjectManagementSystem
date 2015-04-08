package model.users.employees;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-06T22:08:39.645-0700")
@StaticMetamodel(Employee.class)
public class Employee_ {
	public static volatile SingularAttribute<Employee, String> employeeID;
	public static volatile SingularAttribute<Employee, String> labourGradeID;
	public static volatile SingularAttribute<Employee, String> role;
	public static volatile SingularAttribute<Employee, String> supervisorID;
	public static volatile SingularAttribute<Employee, String> firstName;
	public static volatile SingularAttribute<Employee, String> lastName;
	public static volatile SingularAttribute<Employee, String> userName;
	public static volatile SingularAttribute<Employee, String> address;
	public static volatile SingularAttribute<Employee, String> province;
	public static volatile SingularAttribute<Employee, String> country;
	public static volatile SingularAttribute<Employee, String> postalCode;
	public static volatile SingularAttribute<Employee, String> homeNumber;
	public static volatile SingularAttribute<Employee, String> cellNumber;
	public static volatile SingularAttribute<Employee, String> emailAddress;
	public static volatile SingularAttribute<Employee, Double> sickLeaveTimeBalance;
	public static volatile SingularAttribute<Employee, Double> vacationTimeBalance;
}
