/** 
 * @author Eric Lyons-Davis
 * */

package model.project.reports;

/* represents a row in the status report */

public class StatusReportRow 
{
	private String laborGrade;
	private double progressInPD;
	private double estimateToComplete;
	
	/* default constructor */
	public StatusReportRow()
	{
	
	}
	
	/* parameter constructor */
	public StatusReportRow(String laborGradeInput, double progressInPDInput,double estimateToCompleteInput)
	{
		laborGrade = laborGradeInput;
		progressInPD = progressInPDInput;
		estimateToComplete = estimateToCompleteInput;
	}
	
	/* labour grade id only constructor */
	public StatusReportRow(String laborGradeInput)
	{
		laborGrade = laborGradeInput;
	}
	
	/* getters and setters */
	
	public String getLaborGrade() {
		return laborGrade;
	}

	public void setLaborGrade(String laborGrade) {
		this.laborGrade = laborGrade;
	}

	public double getProgressInPD() {
		return progressInPD;
	}

	public void setProgressInPD(double progressInPD) {
		this.progressInPD = progressInPD;
	}

	public double getEstimateToComplete() {
		return estimateToComplete;
	}

	public void setEstimateToComplete(double estimateToComplete) {
		this.estimateToComplete = estimateToComplete;
	}
		
}
