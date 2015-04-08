package model.project.projects;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-06T22:08:39.379-0700")
@StaticMetamodel(Projects.class)
public class Projects_ {
	public static volatile SingularAttribute<Projects, String> projectID;
	public static volatile SingularAttribute<Projects, String> packageID;
	public static volatile SingularAttribute<Projects, String> projectManagerUsername;
	public static volatile SingularAttribute<Projects, Date> startDate;
	public static volatile SingularAttribute<Projects, Date> endDate;
	public static volatile SingularAttribute<Projects, String> clientName;
	public static volatile SingularAttribute<Projects, String> description;
	public static volatile SingularAttribute<Projects, Double> markupRate;
}
