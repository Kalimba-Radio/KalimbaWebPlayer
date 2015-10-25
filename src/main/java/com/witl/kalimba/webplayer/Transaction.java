package com.witl.kalimba.webplayer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Transaction {
    @Id
	private String tnsId;
	private String pnrId;
	private String ccDapproval;
	private String companyRef;
	
	public String getTnsId() {
		return tnsId;
	}
	public void setTnsId(String tnsId) {
		this.tnsId = tnsId;
	}
	public String getPnrId() {
		return pnrId;
	}
	public void setPnrId(String pnrId) {
		this.pnrId = pnrId;
	}
	public String getCcDapproval() {
		return ccDapproval;
	}
	public void setCcDapproval(String ccDapproval) {
		this.ccDapproval = ccDapproval;
	}
	public String getCompanyRef() {
		return companyRef;
	}
	public void setCompanyRef(String companyRef) {
		this.companyRef = companyRef;
	}
	
	
}
