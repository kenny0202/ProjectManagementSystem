/** 
 * @author Eric Lyons-Davis
 * */

package model.users.labourGrades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/* models default charges for a specific labour grade */
@Entity
@Table(name="labour_grade_charge_rate")
public class LabourGradeChargeRate 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="entry_date")
	@Temporal(TemporalType.DATE)
	private Date entryDate;
	
	@Column(name="ss_charge_rate")
	private double ssChargeRate;
	
	@Column(name="ds_charge_rate")
	private double dsChargeRate;
	
	@Column(name="js_charge_rate")
	private double jsChargeRate;
	
	@Column(name="p1_charge_rate")
	private double p1ChargeRate;
	
	@Column(name="p2_charge_rate")
	private double p2ChargeRate;
	
	@Column(name="p3_charge_rate")
	private double p3ChargeRate;
	
	@Column(name="p4_charge_rate")
	private double p4ChargeRate;
	
	@Column(name="p5_charge_rate")
	private double p5ChargeRate;
	
	@Column(name="p6_charge_rate")
	private double p6ChargeRate;
	
	/* default constructor */
	public LabourGradeChargeRate()
	{
		
	}


	/* constructor w/ all possible input values */
	public LabourGradeChargeRate(int idInput, double ssChargeRateInput, double dsChargeRateInput, double jsChargeRateInput, double p1ChargeRateInput,
			double p2ChargeRateInput, double p3ChargeRateInput, double p4ChargeRateInput, double p5ChargeRateInput, double p6ChargeRateInput) 
	{
		id = idInput;
		ssChargeRate = ssChargeRateInput;
		dsChargeRate = dsChargeRateInput;
		jsChargeRate = jsChargeRateInput;
		p1ChargeRate = p1ChargeRateInput;
		p2ChargeRate = p2ChargeRateInput;
		p3ChargeRate = p3ChargeRateInput;
		p4ChargeRate = p4ChargeRateInput;
		p5ChargeRate = p5ChargeRateInput;
		p6ChargeRate = p6ChargeRateInput;
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


	public Date getEntryDate() 
	{
		return entryDate;
	}


	public void setEntryDate(Date entryDate) 
	{
		this.entryDate = entryDate;
	}
	
}
