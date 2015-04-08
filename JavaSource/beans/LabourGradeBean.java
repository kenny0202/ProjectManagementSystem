/** 
 * @author Eric Lyons-Davis
 * */
package beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import database.LabourGradeManager;
import model.users.labourGrades.LabourGradeChargeRate;


@Named("labourGrade")
@ConversationScoped

public class LabourGradeBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Inject Conversation conversation;
	@Inject LabourGradeManager labourGradeManager;

	private int id;
	private double ssChargeRate;
	private double dsChargeRate;
	private double jsChargeRate;
	private double p1ChargeRate;
	private double p2ChargeRate;
	private double p3ChargeRate;
	private double p4ChargeRate;
	private double p5ChargeRate;
	private double p6ChargeRate;
	ArrayList<LabourGradeChargeRate> labourGradeList = new ArrayList<LabourGradeChargeRate>();
	
	
	/* grabs the data in the database to display */
	public String displayLabourGradeChargeRateList()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
		}
		
		labourGradeList = labourGradeManager.getAllChargeRates();
		ssChargeRate = labourGradeList.get(0).getSsChargeRate();
		dsChargeRate = labourGradeList.get(0).getDsChargeRate();
		jsChargeRate = labourGradeList.get(0).getJsChargeRate();
		p1ChargeRate = labourGradeList.get(0).getP1ChargeRate();
		p2ChargeRate = labourGradeList.get(0).getP2ChargeRate();
		p3ChargeRate = labourGradeList.get(0).getP3ChargeRate();
		p4ChargeRate = labourGradeList.get(0).getP4ChargeRate();
		p5ChargeRate = labourGradeList.get(0).getP5ChargeRate();
		p6ChargeRate = labourGradeList.get(0).getP6ChargeRate();
		return "LabourGrade"; 
	}
	
	
	/* updates charge values for labour grades */
	public String updateLabourGradeValues()
	{
		/* updatedEmployee used as a container to pass new data to EmployeeManager for updating */
		LabourGradeChargeRate newData = new LabourGradeChargeRate(id, ssChargeRate, dsChargeRate, jsChargeRate, p1ChargeRate, p2ChargeRate, p3ChargeRate,
				p4ChargeRate, p5ChargeRate, p6ChargeRate);
		labourGradeManager.updateChargeRate(labourGradeManager.findChargeRate(), newData);
		displayLabourGradeChargeRateList(); /* update charge rate list w/ new values for display */
		return "Success";
	}
	
	/* back method */
	public String back()
	{
		displayLabourGradeChargeRateList(); /* update charge rate list w/ new values for display */
		return "Back";
	}
	
	/* exit labour grade charge rate menu method */
	public String backExit()
	{
		if(!conversation.isTransient())
		{
			conversation.end();
		}
		
		return "Back";
	}



	/* getters and setters */

	public int getId() 
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}


	public double getSsChargeRate() 
	{
		return ssChargeRate;
	}


	public void setSsChargeRate(double ssChargeRate) 
	{
		this.ssChargeRate = ssChargeRate;
	}


	public double getDsChargeRate()
	{
		return dsChargeRate;
	}


	public void setDsChargeRate(double dsChargeRate)
	{
		this.dsChargeRate = dsChargeRate;
	}


	public double getJsChargeRate() 
	{
		return jsChargeRate;
	}


	public void setJsChargeRate(double jsChargeRate)
	{
		this.jsChargeRate = jsChargeRate;
	}


	public double getP1ChargeRate() 
	{
		return p1ChargeRate;
	}


	public void setP1ChargeRate(double p1ChargeRate) 
	{
		this.p1ChargeRate = p1ChargeRate;
	}


	public double getP2ChargeRate() 
	{
		return p2ChargeRate;
	}


	public void setP2ChargeRate(double p2ChargeRate) 
	{
		this.p2ChargeRate = p2ChargeRate;
	}


	public double getP3ChargeRate() 
	{
		return p3ChargeRate;
	}


	public void setP3ChargeRate(double p3ChargeRate) 
	{
		this.p3ChargeRate = p3ChargeRate;
	}


	public double getP4ChargeRate() 
	{
		return p4ChargeRate;
	}


	public void setP4ChargeRate(double p4ChargeRate) 
	{
		this.p4ChargeRate = p4ChargeRate;
	}


	public double getP5ChargeRate() 
	{
		return p5ChargeRate;
	}


	public void setP5ChargeRate(double p5ChargeRate) 
	{
		this.p5ChargeRate = p5ChargeRate;
	}


	public double getP6ChargeRate() 
	{
		return p6ChargeRate;
	}


	public void setP6ChargeRate(double p6ChargeRate) 
	{
		this.p6ChargeRate = p6ChargeRate;
	}


	public ArrayList<LabourGradeChargeRate> getLabourGradeList() 
	{
		return labourGradeList;
	}


	public void setLabourGradeList(ArrayList<LabourGradeChargeRate> labourGradeList) 
	{
		this.labourGradeList = labourGradeList;
	}
	
	
}
