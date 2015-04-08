package model.project.workPackages;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-06T22:08:39.536-0700")
@StaticMetamodel(Packages.class)
public class Packages_ {
	public static volatile SingularAttribute<Packages, String> packageID;
	public static volatile SingularAttribute<Packages, String> projectID;
	public static volatile SingularAttribute<Packages, String> engineerUsername;
	public static volatile SingularAttribute<Packages, String> status;
	public static volatile SingularAttribute<Packages, Date> startDate;
	public static volatile SingularAttribute<Packages, Date> endDate;
	public static volatile SingularAttribute<Packages, Double> estimatedCost;
	public static volatile SingularAttribute<Packages, Double> currentCost;
	public static volatile SingularAttribute<Packages, Double> ssBudget;
	public static volatile SingularAttribute<Packages, Double> dsBudget;
	public static volatile SingularAttribute<Packages, Double> jsBudget;
	public static volatile SingularAttribute<Packages, Double> p1Budget;
	public static volatile SingularAttribute<Packages, Double> p2Budget;
	public static volatile SingularAttribute<Packages, Double> p3Budget;
	public static volatile SingularAttribute<Packages, Double> p4Budget;
	public static volatile SingularAttribute<Packages, Double> p5Budget;
	public static volatile SingularAttribute<Packages, Double> p6Budget;
	public static volatile SingularAttribute<Packages, String> description;
	public static volatile SingularAttribute<Packages, String> parentPackageID;
	public static volatile SingularAttribute<Packages, Boolean> leaf;
}
