package model.project.reports;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-06T22:08:39.515-0700")
@StaticMetamodel(StatusReport.class)
public class StatusReport_ {
	public static volatile SingularAttribute<StatusReport, Integer> id;
	public static volatile SingularAttribute<StatusReport, String> projectID;
	public static volatile SingularAttribute<StatusReport, String> packageID;
	public static volatile SingularAttribute<StatusReport, Double> ssEstimate;
	public static volatile SingularAttribute<StatusReport, Double> dsEstimate;
	public static volatile SingularAttribute<StatusReport, Double> jsEstimate;
	public static volatile SingularAttribute<StatusReport, Double> p1Estimate;
	public static volatile SingularAttribute<StatusReport, Double> p2Estimate;
	public static volatile SingularAttribute<StatusReport, Double> p3Estimate;
	public static volatile SingularAttribute<StatusReport, Double> p4Estimate;
	public static volatile SingularAttribute<StatusReport, Double> p5Estimate;
	public static volatile SingularAttribute<StatusReport, Double> p6Estimate;
	public static volatile SingularAttribute<StatusReport, String> comments;
}
