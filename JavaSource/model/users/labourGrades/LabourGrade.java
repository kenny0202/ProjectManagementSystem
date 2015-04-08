/** 
 * @author Eric Lyons-Davis
 * */

package model.users.labourGrades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="labour_grades")
public class LabourGrade 
{
	@Id
	@Column(name="labour_grade_id")
	private String labourGradeID;
	
	@Column(name="labour_grade_title")
	private String labourGradeTitle; /* the actual name of the labour grade */
	
	/* Default Constructor */
	public LabourGrade()
	{
		
	}
	
	/* getters and setters */
	public String getLaborGradeID()
	{
		return labourGradeID;
	}
	
	public void setLaborGradeID(String laborGradeID) 
	{
		this.labourGradeID = laborGradeID;
	}
	
	public String getLabourGradeTitle() 
	{
		return labourGradeTitle;
	}
	
	public void setLabourGradeTitle(String labourGradeTitle) 
	{
		this.labourGradeTitle = labourGradeTitle;
	}
}
