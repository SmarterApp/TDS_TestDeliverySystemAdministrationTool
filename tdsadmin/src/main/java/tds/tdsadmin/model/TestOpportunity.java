package tds.tdsadmin.model;

import java.util.Date;

public class TestOpportunity {
	private String altSsid;
	private String name;
	private String testName;
	private String sessionId;
	private String status;
	private Date dateStarted;
	private Date dateExpired;
	private Date dateCompleted;
	private Date datePaused;
	private boolean isSegmented;

	public String getAltSsid() {
		return altSsid;
	}

	public void setAltSsid(String altSsid) {
		this.altSsid = altSsid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public Date getDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(Date dateExpired) {
		this.dateExpired = dateExpired;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public Date getDatePaused() {
		return datePaused;
	}

	public void setDatePaused(Date datePaused) {
		this.datePaused = datePaused;
	}

	public boolean isSegmented() {
		return isSegmented;
	}

	public void setSegmented(boolean isSegmented) {
		this.isSegmented = isSegmented;
	}
}
